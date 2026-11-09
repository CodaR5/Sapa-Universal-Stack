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

    // 必须提供与父类匹配的构造函数
    public BasinInventoryInsertMixin(int slots, SyncedBlockEntity te) {
        super(slots, te);
    }

    /**
     * 拦截 BasinInventory 的插入逻辑，绕过“单槽位”检查。
     */
    @Inject(method = "insertItem", at = @At("HEAD"), cancellable = true)
    private void sapa$bypassSingleStackCheck(int slot, ItemStack stack, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
        if (Config.cachedMaxStackSize > 64) {
            cir.setReturnValue(super.insertItem(slot, stack, simulate));
        }
    }

    /**
     * 核心修复：重写槽位容量限制，避免每个槽位最多只能放 64 个（导致总共只能放 576 个）。
     */
    @Inject(method = "getSlotLimit", at = @At("HEAD"), cancellable = true)
    private void sapa$modifySlotLimit(int slot, CallbackInfoReturnable<Integer> cir) {
        if (Config.cachedMaxStackSize > 64) {
            cir.setReturnValue(Config.cachedMaxStackSize);
        }
    }
}
