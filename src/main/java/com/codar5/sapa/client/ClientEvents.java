package com.codar5.sapa.client;

import com.codar5.sapa.Sapa;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ClientEvents {

    public static void register() {
        NeoForge.EVENT_BUS.addListener(ClientEvents::onClientTick);
    }

    private static void onClientTick(ClientTickEvent.Post event) {
        while (KeyBindings.OPEN_CONFIG.consumeClick()) {
            Minecraft.getInstance().setScreen(new StackConfigScreen());
        }
    }
}