package com.codar5.sapa;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.IntValue maxStackSize;
    public static volatile int cachedMaxStackSize = 64;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("Sapa 万能堆叠模组配置文件").push("general");
        maxStackSize = builder
                .comment("物品的最大堆叠上限 (支持 1 - 1024，有耐久度的物品不受影响)")
                .defineInRange("maxStackSize", 1024, 1, 1024);
        builder.pop();
        SPEC = builder.build();
    }
}