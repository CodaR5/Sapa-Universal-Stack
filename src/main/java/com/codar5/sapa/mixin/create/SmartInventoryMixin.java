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
     * 注意：1.21.1 中 SyncedTileEntity 已重命名为 SyncedBlockEntity。
     */
    @ModifyArg(
        method = "<init>(ILcom/simibubi/create/foundation/blockEntity/SyncedBlockEntity;)V",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/item/SmartInventory;<init>(ILcom/simibubi/create/foundation/blockEntity/SyncedBlockEntity;IZ)V"),
        index = 2,
        require = 0
    )
    private int sapa$modifyDefaultStackSize(int original) {
        return Config.cachedMaxStackSize;
    }
}
