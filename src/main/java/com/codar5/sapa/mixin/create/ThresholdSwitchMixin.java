package com.codar5.sapa.mixin.create;

import com.codar5.sapa.Config;
import com.simibubi.create.content.redstone.thresholdSwitch.ThresholdSwitchBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = ThresholdSwitchBlockEntity.class, remap = false)
public class ThresholdSwitchMixin {

    /**
     * 拦截阈值开关中的硬编码 64，替换为配置值。
     * require = 0 保证如果找不到也不会崩溃。
     */
    @ModifyConstant(
        method = "*",
        constant = @Constant(intValue = 64),
        require = 0
    )
    private int sapa$modifyHardcodedStackSize(int original) {
        return Config.cachedMaxStackSize;
    }
}
