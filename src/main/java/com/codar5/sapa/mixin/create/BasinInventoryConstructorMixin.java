package com.codar5.sapa.mixin.create;

import com.codar5.sapa.Config;
import com.simibubi.create.content.processing.basin.BasinInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = BasinInventory.class, remap = false)
public class BasinInventoryConstructorMixin {

    /**
     * 拦截 BasinInventory 构造，将硬编码的 16 替换为配置值。
     * 关键：由于注入点在 super() 调用之前，处理方法必须是 static。
     */
    @ModifyArg(
        method = "<init>(ILcom/simibubi/create/content/processing/basin/BasinBlockEntity;)V",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/item/SmartInventory;<init>(ILcom/simibubi/create/foundation/blockEntity/SyncedBlockEntity;IZ)V"),
        index = 2,
        require = 0
    )
    private static int sapa$modifyBasinCapacity(int original) {
        return Config.cachedMaxStackSize;
    }
}
