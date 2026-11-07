package com.codar5.sapa.mixin;

import com.codar5.sapa.Config;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    // 1.21.1 中正确的方法是 getMaxCount
    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    private void sapa$overrideMaxCount(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack)(Object)this;
        // 有耐久度的物品（工具、武器）保持原版 1 的堆叠上限
        if (stack.has(DataComponents.MAX_DAMAGE)) {
            return;
        }
        int newSize = Config.cachedMaxStackSize;
        if (newSize > 1) {
            cir.setReturnValue(newSize);
        }
    }
}
