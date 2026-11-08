package com.codar5.sapa.mixin.create;

import com.codar5.sapa.Config;
import com.simibubi.create.content.logistics.packagerLink.LogisticsManager;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = LogisticsManager.class, remap = false)
public class LogisticsManagerMixin {

    @Redirect(
        method = "*",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getCount()I"),
        require = 0
    )
    private int sapa$redirectGetCount(ItemStack stack) {
        return stack.getCount();
    }

    @Redirect(
        method = "*",
        at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"),
        require = 0
    )
    private int sapa$redirectMin(int a, int b) {
        if (a == 64 || b == 64) {
            return Config.cachedMaxStackSize;
        }
        return Math.min(a, b);
    }
}
