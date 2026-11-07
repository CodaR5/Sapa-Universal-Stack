package com.codar5.sapa.client;

import com.codar5.sapa.Sapa;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Sapa.MODID, value = Dist.CLIENT)
public class KeyBindings {

    public static final String CATEGORY = "key.categories.sapa";

    public static final KeyMapping OPEN_CONFIG = new KeyMapping(
            "key.sapa.open_config",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            CATEGORY
    );

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(OPEN_CONFIG);
    }
}