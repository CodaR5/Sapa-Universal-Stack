package com.codar5.sapa.mixin.create;

import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FilteringBehaviour.class, remap = false)
public class FilterSlotMixin {

    /**
     * 拦截 FilteringBehaviour 中对物品堆叠上限的查询。
     * 将其强制限制为 64，从而保证 GUI 滑块在 0~64 的正常范围内工作，防止溢出崩溃。
     */
    @Redirect(
            method = "*",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getMaxStackSize()I")
    )
    private int sapa$limitFilterStackSize(ItemStack stack) {
        return 64;
    }
}