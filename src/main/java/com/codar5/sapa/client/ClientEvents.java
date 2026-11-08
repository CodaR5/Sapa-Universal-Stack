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
            // 避免在已经打开该界面的情况下重复创建实例
            if (!(Minecraft.getInstance().screen instanceof StackConfigScreen)) {
                Minecraft.getInstance().setScreen(new StackConfigScreen());
            }
        }
    }
}
