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
     * 目标: public SmartInventory(int slots, SyncedTileEntity te) { this(slots, te, 64, false); }
     * index = 2 对应 this() 调用中的 stackSize 参数。
     */
    @ModifyArg(
        method = "<init>(ILcom/simibubi/create/foundation/tileEntity/SyncedTileEntity;)V",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/item/SmartInventory;<init>(ILcom/simibubi/create/foundation/tileEntity/SyncedTileEntity;IZ)V"),
        index = 2,
        require = 0
    )
    private int sapa$modifyDefaultStackSize(int original) {
        return Config.cachedMaxStackSize;
    }
}
