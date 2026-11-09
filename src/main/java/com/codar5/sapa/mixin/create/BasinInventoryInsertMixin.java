package com.codar5.sapa.mixin.create;

import com.codar5.sapa.Config;
import com.simibubi.create.content.processing.basin.BasinInventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BasinInventory.class, remap = false)
public abstract class BasinInventoryInsertMixin {

    // 使用 @Shadow 获取 SmartInventory 内部的底层处理器
    // 注意：NeoForge 1.21.1 中包名是 net.neoforged.neoforge.items.IItemHandlerModifiable
    @Shadow
    protected IItemHandlerModifiable inv;

    /**
     * 拦截 BasinInventory 的插入逻辑。
     * 当配置的堆叠上限大于 64 时，直接让底层的处理器执行插入，绕过 BasinInventory 的“单组”检查。
     */
    @Inject(method = "insertItem", at = @At("HEAD"), cancellable = true)
    private void sapa$bypassSingleStackCheck(int slot, ItemStack stack, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
        if (Config.cachedMaxStackSize > 64) {
            // 直接调用底层 ItemStackHandler 的 insertItem 方法，不走 BasinInventory 的重写逻辑
            cir.setReturnValue(this.inv.insertItem(slot, stack, simulate));
        }
    }
}
