package com.codar5.sapa.mixin.create;

import com.codar5.sapa.Config;
import com.simibubi.create.content.logistics.packagerLink.LogisticsManager;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = LogisticsManager.class, remap = false)
public class LogisticsManagerMixin {

    /**
     * 拦截对物品数量的查询，确保物流网络汇总时拿到的是真实数量。
     */
    @Redirect(
            method = "*",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getCount()I")
    )
    private int sapa$redirectGetCount(ItemStack stack) {
        return stack.getCount();
    }

    /**
     * 拦截物流网络计算时对 Math.min 的调用，防止由于硬编码导致的截断。
     */
    @Redirect(
            method = "*",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I")
    )
    private int sapa$redirectMin(int a, int b) {
        // 如果较短的边是原版的 64，则替换为我们配置的上限
        if (a == 64 || b == 64) {
            return Config.cachedMaxStackSize;
        }
        return Math.min(a, b);
    }
}