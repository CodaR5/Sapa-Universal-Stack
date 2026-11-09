package com.codar5.sapa.mixin.create;

import com.simibubi.create.content.processing.basin.BasinInventory;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable; // 或使用 NeoForge 的 IItemHandler
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = BasinInventory.class, remap = false)
public abstract class BasinInventoryInsertMixin extends SmartInventory {

    // 使用 @Shadow 获取父类 SmartInventory 中的 inv 字段
    // 该字段类型为 SyncedStackHandler，它实现了 IItemHandlerModifiable
    @Shadow
    private IItemHandlerModifiable inv;

    // 必须提供与父类匹配的构造函数，否则编译会失败
    public BasinInventoryInsertMixin(int slots, net.minecraft.world.level.block.entity.BlockEntity te) {
        super(slots, te);
    }

    /**
     * 完全重写 BasinInventory 的插入逻辑。
     * 丢弃 Create 原本的“仅允许单一物品堆叠”检查，直接执行标准插入。
     */
    @Overwrite
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        // 直接通过 inv（即 SyncedStackHandler）处理插入逻辑
        return inv.insertItem(slot, stack, simulate);
    }
}
