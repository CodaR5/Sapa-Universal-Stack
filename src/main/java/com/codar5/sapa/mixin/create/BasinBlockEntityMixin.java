package com.codar5.sapa.mixin.create;

import com.codar5.sapa.Config;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BasinBlockEntity.class, remap = false)
public class BasinBlockEntityMixin {

    /**
     * 拦截工作盆内部库存对最大堆叠数的查询，返回你的配置上限。
     * 方法名 "getMaxStackSize" 为通用推断，若失效需反编译确认 BasinBlockEntity 的库存初始化逻辑。
     */
    @Redirect(
        method = "*",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getMaxStackSize()I"),
        require = 0
    )
    private int sapa$overrideBasinStackSize(ItemStack stack) {
        return Config.cachedMaxStackSize;
    }
}
