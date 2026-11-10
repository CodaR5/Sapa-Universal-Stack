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

    @Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    private void sapa$overrideMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack)(Object)this;
        if (stack.has(DataComponents.MAX_DAMAGE)) {
            return;
        }
        int newSize = Config.cachedMaxStackSize;
        if (newSize > 1) {
            cir.setReturnValue(newSize);
        }
    }
}
