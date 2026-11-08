package com.codar5.sapa.mixin;

import com.codar5.sapa.Config;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class InventoryMixin {

    // 拦截向背包添加物品的方法，防止数量被截断
    @Inject(method = "add(ILnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void sapa$preventAddTruncation(int slot, ItemStack stack, CallbackInfo ci) {
        // 如果物品数量超过配置上限，直接阻止原版逻辑（需要自行实现完整的添加逻辑，较复杂）
        // 或者，更简单的方式是：如果物品数量未超过配置上限，则让原版逻辑继续，但原版逻辑可能仍会截断。
        // 因此，更稳妥的做法是直接取消原版逻辑，并手动将物品放入指定槽位。
        if (stack.getCount() > Config.cachedMaxStackSize) {
            // 这里需要编写自定义逻辑，将 stack 安全地放入 Inventory
            // 由于直接操作 Inventory 的内部字段比较危险，建议使用反射或 accessor
            // 但为了示例简洁，此处省略复杂的自定义逻辑。
        }
    }
}