package com.codar5.sapa.mixin.neoforge;

import com.codar5.sapa.Config;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStackHandler.class)
public class ItemStackHandlerMixin {

    /**
     * 强制覆盖所有继承 ItemStackHandler 的槽位限制（原版默认可能是 64 或 99）。
     */
    @Inject(method = "getSlotLimit", at = @At("HEAD"), cancellable = true)
    private void sapa$overrideSlotLimit(int slot, CallbackInfoReturnable<Integer> cir) {
        if (Config.cachedMaxStackSize > 64) {
            cir.setReturnValue(Config.cachedMaxStackSize);
        }
    }
}
