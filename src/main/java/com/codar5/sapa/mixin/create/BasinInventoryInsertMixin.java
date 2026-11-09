package com.codar5.sapa.mixin.create;

import com.codar5.sapa.Config;
import com.simibubi.create.content.processing.basin.BasinInventory;
import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BasinInventory.class, remap = false)
public abstract class BasinInventoryInsertMixin extends SmartInventory {

    public BasinInventoryInsertMixin(int slots, SyncedBlockEntity te) {
        super(slots, te);
    }

    @Inject(method = "insertItem", at = @At("HEAD"), cancellable = true)
    private void sapa$bypassSingleStackCheck(int slot, ItemStack stack, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
        if (Config.cachedMaxStackSize > 64) {
            cir.setReturnValue(super.insertItem(slot, stack, simulate));
        }
    }
}
