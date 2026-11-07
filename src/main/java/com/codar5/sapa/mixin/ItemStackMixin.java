package com.codar5.sapa.mixin;

import com.codar5.sapa.Config;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    private void sapa$overrideMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack)(Object)this;
        // 检查物品是否拥有耐久度组件，如果有，则不改动（保持原版1）
        if (stack.has(net.minecraft.core.component.DataComponents.MAX_DAMAGE)) {
            return;
        }
        // 否则，返回我们在配置中缓存的最大堆叠数
        int newSize = Config.cachedMaxStackSize;
        if (newSize > 1) {
            cir.setReturnValue(newSize);
        }
    }
}