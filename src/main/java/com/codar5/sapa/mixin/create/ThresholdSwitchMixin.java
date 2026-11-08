package com.codar5.sapa.mixin.create;

import com.codar5.sapa.Config;
import com.simibubi.create.content.redstone.thresholdSwitch.ThresholdSwitchBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ThresholdSwitchBlockEntity.class, remap = false)
public class ThresholdSwitchMixin {
    /**
     * 拦截阈值开关中的 Math.min 调用，使其使用配置的堆叠上限，
     * 而不是硬编码的 64 或默认的 20 组容量。
     */
    @Redirect(
        method = "*",
        at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I")
    )
    private int sapa$redirectMin(int a, int b) {
        // 如果原版逻辑试图用 64 截断，则使用配置的上限
        if (a == 64 || b == 64) {
            return Config.cachedMaxStackSize;
        }
        return Math.min(a, b);
    }
}
