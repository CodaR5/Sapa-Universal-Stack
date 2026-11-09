package com.codar5.sapa.mixin.create;

import com.simibubi.create.content.processing.basin.BasinInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = BasinInventory.class, remap = false)
public abstract class BasinInventoryInsertMixin {

    // 使用 @Shadow 访问父类（ItemStackHandler）中的槽位列表
    @Shadow
    protected NonNullList<ItemStack> stacks;

    // 使用 @Shadow 访问通知槽位改变的方法
    @Shadow
    protected abstract void onContentsChanged(int slot);

    // 使用 @Shadow 访问获取槽位上限的方法
    @Shadow
    protected abstract int getStackLimit(int slot, ItemStack stack);

    /**
     * 完全重写 BasinInventory 的插入逻辑。
     * 丢弃 Create 原本的“仅允许单一物品堆叠”检查，直接执行标准插入。
     */
    @Overwrite
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack existing = this.stacks.get(slot);
        int limit = getStackLimit(slot, stack);

        if (!existing.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(existing, stack)) {
                return stack;
            }
            limit -= existing.getCount();
        }

        if (limit <= 0) {
            return stack;
        }

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                this.stacks.set(slot, reachedLimit ? stack.copyWithCount(limit) : stack.copy());
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            this.onContentsChanged(slot);
        }

        return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
    }
}
