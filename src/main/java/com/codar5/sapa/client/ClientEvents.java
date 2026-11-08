package com.codar5.sapa.client;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ClientEvents {
    public static void register() {
        NeoForge.EVENT_BUS.addListener(ClientEvents::onClientTick);
    }

    private static void onClientTick(ClientTickEvent.Post event) {
        while (KeyBindings.OPEN_CONFIG.consumeClick()) {
            // 防止重复实例化 Screen 导致界面层级堆叠（彻底杜绝内存泄漏）
            if (!(Minecraft.getInstance().screen instanceof StackConfigScreen)) {
                Minecraft.getInstance().setScreen(new StackConfigScreen());
            }
        }
    }
}
