package com.codar5.sapa.mixin.create;

import com.codar5.sapa.Config;
import com.simibubi.create.foundation.item.SmartInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = SmartInventory.class, remap = false)
public class SmartInventoryMixin {

    /**
     * 拦截 SmartInventory 构造函数，将硬编码的 stackSize (64) 替换为配置值。
     * 目标：public SmartInventory(int slots, SyncedTileEntity te, int stackSize, boolean stackNonStackables)
     * index = 2 对应 stackSize 参数。
     */
    @ModifyArg(
        method = "<init>(ILcom/simibubi/create/foundation/tileEntity/SyncedTileEntity;IZ)V",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/item/SmartInventory;<init>(Lnet/minecraftforge/items/wrapper/RecipeWrapper;)V"),
        index = 2,
        require = 0
    )
    private int sapa$modifySmartInventoryStackSize(int original) {
        return Config.cachedMaxStackSize;
    }
}
