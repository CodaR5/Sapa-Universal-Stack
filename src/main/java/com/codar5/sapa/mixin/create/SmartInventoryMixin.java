package com.codar5.sapa.mixin.create;

import com.codar5.sapa.Config;
import com.simibubi.create.foundation.item.SmartInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = SmartInventory.class, remap = false)
public class SmartInventoryMixin {

    /**
     * 修改 SmartInventory 默认构造方法中硬编码的 64。
     * 关键：由于注入点在 this() 调用之前，处理方法必须是 static。
     */
    @ModifyArg(
        method = "<init>(ILcom/simibubi/create/foundation/blockEntity/SyncedBlockEntity;)V",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/item/SmartInventory;<init>(ILcom/simibubi/create/foundation/blockEntity/SyncedBlockEntity;IZ)V"),
        index = 2,
        require = 0
    )
    private static int sapa$modifyDefaultStackSize(int original) {
        return Config.cachedMaxStackSize;
    }
}
