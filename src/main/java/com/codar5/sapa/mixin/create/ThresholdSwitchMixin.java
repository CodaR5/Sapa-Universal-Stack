package com.codar5.sapa.mixin.create;

import com.codar5.sapa.Config;
import com.simibubi.create.content.redstone.thresholdSwitch.ThresholdSwitchBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ThresholdSwitchBlockEntity.class, remap = false)
public class ThresholdSwitchMixin {

    /**
     * 拦截阈值开关计算容量时的 Math.min，使其正确识别超过 64 的堆叠上限。
     */
    @Redirect(
            method = "*",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I")
    )
    private int sapa$redirectMin(int itemMax, int slotLimit) {
        // 如果原版逻辑试图用 64 截断，我们则使用配置的上限
        return Math.min(itemMax, Config.cachedMaxStackSize);
    }
}