package com.codar5.sapa.mixin.create;

import com.codar5.sapa.Config;
import com.simibubi.create.content.processing.basin.BasinInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = BasinInventory.class, remap = false)
public class BasinInventoryMixin {

    /**
     * 拦截 BasinInventory 构造函数，将传给父类的 16 替换为配置值。
     * 目标：public BasinInventory(int slots, BasinTileEntity te) { super(slots, te, 16, true); }
     * index = 2 对应父类的 stackSize 参数。
     */
    @ModifyArg(
        method = "<init>(ILcom/simibubi/create/content/processing/basin/BasinBlockEntity;)V",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/item/SmartInventory;<init>(ILcom/simibubi/create/foundation/tileEntity/SyncedTileEntity;IZ)V"),
        index = 2,
        require = 0
    )
    private int sapa$modifyBasinInventoryStackSize(int original) {
        return Config.cachedMaxStackSize;
    }
}
