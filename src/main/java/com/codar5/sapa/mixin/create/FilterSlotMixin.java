package com.codar5.sapa.mixin.create;

import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FilteringBehaviour.class, remap = false)
public class FilterSlotMixin {
    /**
     * 拦截 FilteringBehaviour 内所有对 ItemStack.getMaxStackSize() 的调用，
     * 在过滤器上下文中强制返回 64，确保 GUI 不会因上限过高而溢出。
     */
    @Redirect(
        method = "*",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getMaxStackSize()I")
    )
    private int sapa$limitFilterStackSize(ItemStack stack) {
        return 64;
    }
}
