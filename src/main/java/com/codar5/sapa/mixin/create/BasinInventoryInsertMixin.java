package com.codar5.sapa.mixin.create;

import com.simibubi.create.content.processing.basin.BasinInventory;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BasinInventory.class, remap = false)
public class BasinInventoryInsertMixin {

    /**
     * 重定向对 super.insertItem 的调用，但关键在于绕过前置的循环检查。
     * 这里采用 @Redirect 直接返回父类的插入结果，从而跳过 BasinInventory 的 "单组" 检查逻辑。
     */
    @Redirect(
        method = "insertItem",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/item/SmartInventory;insertItem(ILnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;"),
        require = 0
    )
    private ItemStack sapa$bypassSingleStackCheck(SmartInventory inventory, int slot, ItemStack stack, boolean simulate) {
        // 直接调用父类的 insertItem，跳过 BasinInventory 重写逻辑中的前置检查
        return inventory.insertItem(slot, stack, simulate);
    }
}
