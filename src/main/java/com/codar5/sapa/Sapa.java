package com.codar5.sapa;

import com.mojang.logging.LogUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Mod(Sapa.MODID)
public class Sapa {
    public static final String MODID = "sapa";
    public static final Logger LOGGER = LogUtils.getLogger();

    // 缓存反射字段，杜绝高频重载时的内存泄漏和 GC 压力
    private static final Field ITEM_PROPERTIES_FIELD;
    private static final Field PROPERTIES_COMPONENTS_FIELD;
    private static final Method COMPONENTS_SET_METHOD;

    static {
        try {
            ITEM_PROPERTIES_FIELD = Item.class.getDeclaredField("properties");
            ITEM_PROPERTIES_FIELD.setAccessible(true);

            Class<?> propsClass = Class.forName("net.minecraft.world.item.Item$Properties");
            PROPERTIES_COMPONENTS_FIELD = propsClass.getDeclaredField("components");
            PROPERTIES_COMPONENTS_FIELD.setAccessible(true);

            Class<?> componentsType = PROPERTIES_COMPONENTS_FIELD.getType();
            COMPONENTS_SET_METHOD = componentsType.getMethod("set", net.minecraft.core.component.DataComponentType.class, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Sapa 无法初始化反射缓存", e);
        }
    }

    public Sapa(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
        modEventBus.addListener(this::modifyDefaultComponents);
        modEventBus.addListener(this::registerPayloads);
        modEventBus.addListener(this::onConfigLoading);
        modEventBus.addListener(this::onConfigReloading);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            com.codar5.sapa.client.ClientEvents.register();
        }
    }

    private void onConfigLoading(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == Config.SPEC) {
            Config.cachedMaxStackSize = Config.maxStackSize.get();
            LOGGER.info("Sapa 配置已加载，堆叠上限缓存: {}", Config.cachedMaxStackSize);
        }
    }

    private void onConfigReloading(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == Config.SPEC) {
            Config.cachedMaxStackSize = Config.maxStackSize.get();
            LOGGER.info("Sapa 配置已重载，新堆叠上限: {}，正在刷新物品...", Config.cachedMaxStackSize);
            reloadAllItems(Config.cachedMaxStackSize);
        }
    }

    private void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        int maxSize = Config.cachedMaxStackSize;
        event.modifyMatching(
            item -> !item.components().has(DataComponents.DAMAGE) && !item.components().has(DataComponents.MAX_DAMAGE),
            builder -> builder.set(DataComponents.MAX_STACK_SIZE, maxSize)
        );
        LOGGER.info("默认组件已修改（跳过有耐久度的物品），堆叠上限: {}", maxSize);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");

        registrar.playToServer(
            StackSizePayload.TYPE,
            StackSizePayload.STREAM_CODEC,
            (payload, context) -> context.enqueueWork(() -> {
                int newSize = payload.newSize();
                Config.maxStackSize.set(newSize);
                Config.SPEC.save();
                Config.cachedMaxStackSize = newSize;
                reloadAllItems(newSize);
                PacketDistributor.sendToAllPlayers(new StackSizeSyncPayload(newSize));
                LOGGER.info("玩家 {} 将堆叠上限修改为: {}", context.player().getName().getString(), newSize);
            })
        );

        registrar.playToClient(
            StackSizeSyncPayload.TYPE,
            StackSizeSyncPayload.STREAM_CODEC,
            (payload, context) -> {
                if (FMLEnvironment.dist == Dist.CLIENT) {
                    context.enqueueWork(() -> {
                        Config.maxStackSize.set(payload.syncedSize());
                        Config.cachedMaxStackSize = payload.syncedSize();
                    });
                }
            }
        );
    }

    public static void reloadAllItems(int newSize) {
        int success = 0, fail = 0, skipped = 0;
        for (Item item : BuiltInRegistries.ITEM) {
            if (item.components().has(DataComponents.DAMAGE) || item.components().has(DataComponents.MAX_DAMAGE)) {
                skipped++;
                continue;
            }
            try {
                // 直接使用缓存的 Field 和 Method，避免内存暴涨
                Object properties = ITEM_PROPERTIES_FIELD.get(item);
                if (properties == null) continue;
                
                Object componentsBuilder = PROPERTIES_COMPONENTS_FIELD.get(properties);
                if (componentsBuilder == null) continue;
                
                COMPONENTS_SET_METHOD.invoke(componentsBuilder, DataComponents.MAX_STACK_SIZE, newSize);
                success++;
            } catch (Exception e) { 
                fail++; 
            }
        }
        LOGGER.info("动态重载堆叠上限完成。成功: {}, 跳过(有耐久度): {}, 失败: {}", success, skipped, fail);
    }

    public record StackSizePayload(int newSize) implements CustomPacketPayload {
        public static final Type<StackSizePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "stack_size_c2s"));
        public static final StreamCodec<FriendlyByteBuf, StackSizePayload> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, StackSizePayload::newSize, StackSizePayload::new);
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record StackSizeSyncPayload(int syncedSize) implements CustomPacketPayload {
        public static final Type<StackSizeSyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "stack_size_s2c"));
        public static final StreamCodec<FriendlyByteBuf, StackSizeSyncPayload> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, StackSizeSyncPayload::syncedSize, StackSizeSyncPayload::new);
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }
}
