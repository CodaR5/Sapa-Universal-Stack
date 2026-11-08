package com.codar5.sapa.mixin.create;

import com.simibubi.create.content.logistics.packagerLink.LogisticsManager;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = LogisticsManager.class, remap = false)
public class LogisticsManagerMixin {
    /**
     * 重定向库存汇总时对 ItemStack.getCount() 的调用，
     * 确保返回未被截断的真实数量。
     */
    @Redirect(
        method = "*",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getCount()I")
    )
    private int sapa$redirectGetCount(ItemStack stack) {
        return stack.getCount();
    }
}
