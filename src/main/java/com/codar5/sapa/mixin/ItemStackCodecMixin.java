package com.codar5.sapa.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackCodecMixin {

    @Shadow
    @Final
    @Mutable
    public static Codec<ItemStack> CODEC;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void sapa$overrideCodec(CallbackInfo ci) {
        CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("id").forGetter(ItemStack::getItemHolder),
                        ExtraCodecs.intRange(1, Integer.MAX_VALUE).fieldOf("count").orElse(1).forGetter(ItemStack::getCount),
                        // 关键修复：使用 getComponentsPatch() 替代私有的 components 字段
                        DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY)
                                .forGetter(ItemStack::getComponentsPatch)
                ).apply(instance, ItemStack::new)
        );
    }
}