package com.codar5.sapa.mixin.create;

import com.codar5.sapa.Config;
import com.simibubi.create.content.processing.basin.BasinInventory;
import com.simibubi.create.foundation.tileEntity.SyncedTileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = BasinInventory.class, remap = false)
public class BasinInventoryConstructorMixin {

    /**
     * 拦截 BasinInventory 构造，将硬编码的 16 替换为配置值。
     * 目标方法: public BasinInventory(int slots, BasinTileEntity te) { super(slots, te, 16, true); }
     * index = 2 对应父类 SmartInventory 构造函数的 stackSize 参数。
     */
    @ModifyArg(
        method = "<init>(ILcom/simibubi/create/content/processing/basin/BasinBlockEntity;)V",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/item/SmartInventory;<init>(ILcom/simibubi/create/foundation/tileEntity/SyncedTileEntity;IZ)V"),
        index = 2,
        require = 0
    )
    private int sapa$modifyBasinCapacity(int original) {
        return Config.cachedMaxStackSize;
    }
}
