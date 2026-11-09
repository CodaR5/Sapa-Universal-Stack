package com.codar5.sapa.mixin.create;

import com.codar5.sapa.Config;
import com.simibubi.create.content.processing.basin.BasinInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = BasinInventory.class, remap = false)
public class BasinInventoryConstructorMixin {

    @ModifyArg(
        method = "<init>(ILcom/simibubi/create/content/processing/basin/BasinBlockEntity;)V",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/item/SmartInventory;<init>(ILcom/simibubi/create/foundation/blockEntity/SyncedBlockEntity;IZ)V"),
        index = 2,
        require = 0
    )
    private static int sapa$modifyStackSize(int original) {
        return Config.cachedMaxStackSize;
    }

    @ModifyArg(
        method = "<init>(ILcom/simibubi/create/content/processing/basin/BasinBlockEntity;)V",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/item/SmartInventory;<init>(ILcom/simibubi/create/foundation/blockEntity/SyncedBlockEntity;IZ)V"),
        index = 3,
        require = 0
    )
    private static boolean sapa$modifyStackNonStackables(boolean original) {
        return false; 
    }
}
