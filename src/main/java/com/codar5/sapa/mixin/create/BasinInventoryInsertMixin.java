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

    // 必须提供与父类匹配的构造函数，参数类型必须是 SyncedBlockEntity
    public BasinInventoryInsertMixin(int slots, SyncedBlockEntity te) {
        super(slots, te);
    }

    /**
     * 拦截 BasinInventory 的插入逻辑。
     * 当配置堆叠上限 > 64 时，直接调用父类 SmartInventory 的 insertItem，
     * 绕过 BasinInventory 重写方法中的“同类物品只能占一个槽位”检查。
     */
    @Inject(method = "insertItem", at = @At("HEAD"), cancellable = true)
    private void sapa$bypassSingleStackCheck(int slot, ItemStack stack, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
        if (Config.cachedMaxStackSize > 64) {
            // 直接调用父类的 insertItem，执行标准的 ItemStackHandler 插入逻辑
            ItemStack result = super.insertItem(slot, stack, simulate);
            cir.setReturnValue(result);
        }
    }
}
