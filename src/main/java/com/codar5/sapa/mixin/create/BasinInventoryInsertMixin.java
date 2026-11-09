package com.codar5.sapa.mixin.create;

import com.codar5.sapa.Config;
import com.simibubi.create.content.processing.basin.BasinInventory;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BasinInventory.class, remap = false)
public class BasinInventoryInsertMixin {

    /**
     * 拦截 BasinInventory 的插入逻辑。
     * 当配置的堆叠上限大于 64 时，直接强制调用父类 (SmartInventory) 的插入方法，
     * 从而完全跳过 BasinInventory 中“同类物品只能存在一个槽位”的自定义检查。
     */
    @Inject(method = "insertItem", at = @At("HEAD"), cancellable = true)
    private void sapa$bypassSingleStackCheck(int slot, ItemStack stack, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
        if (Config.cachedMaxStackSize > 64) {
            BasinInventory self = (BasinInventory) (Object) this;
            // 直接调用父类 SmartInventory 的 insertItem 逻辑
            cir.setReturnValue(((SmartInventory) self).insertItem(slot, stack, simulate));
        }
    }
}
