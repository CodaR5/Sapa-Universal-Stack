package com.codar5.sapa.mixin.create;

import com.codar5.sapa.Config;
import com.simibubi.create.foundation.item.SmartInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SmartInventory.class, remap = false)
public abstract class SmartInventoryLimitMixin {

    /**
     * 强制所有 SmartInventory 子类（包括 BasinInventory）的槽位上限为配置值。
     * 这会绕过 SyncedStackHandler 中针对 stackNonStackables 写死的 64 限制。
     */
    @Inject(method = "getSlotLimit", at = @At("HEAD"), cancellable = true)
    private void sapa$overrideSlotLimit(int slot, CallbackInfoReturnable<Integer> cir) {
        if (Config.cachedMaxStackSize > 64) {
            cir.setReturnValue(Config.cachedMaxStackSize);
        }
    }
}
