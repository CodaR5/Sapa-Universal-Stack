package com.codar5.sapa.mixin;

import com.codar5.sapa.Config;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Container.class)
public interface ContainerMixin {

    @Inject(method = "getMaxStackSize(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void sapa$overrideContainerMaxStackSize(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (!stack.isEmpty()) {
            cir.setReturnValue(Config.cachedMaxStackSize);
        }
    }
}