package com.codar5.sapa.mixin;

import com.codar5.sapa.Config;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Inject(method = "getMaxStackSize()I", at = @At("RETURN"), cancellable = true)
    private void sapa$overrideSlotMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Config.cachedMaxStackSize);
    }
}
