package com.codar5.sapa.client;

import com.codar5.sapa.Config;
import com.codar5.sapa.Sapa;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;
import net.neoforged.neoforge.network.PacketDistributor;

public class StackConfigScreen extends Screen {
    private ExtendedSlider slider;
    private EditBox inputField;

    public StackConfigScreen() {
        super(Component.translatable("gui.sapa.stack_config.title"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;

        this.inputField = new EditBox(this.font, centerX - 100, 95, 200, 20,
            Component.translatable("gui.sapa.stack_config.input"));
        this.inputField.setValue(String.valueOf(Config.maxStackSize.get()));
        this.inputField.setFilter(s -> s.matches("\\d*")); // 只允许输入数字
        this.addRenderableWidget(this.inputField);

        this.slider = new ExtendedSlider(
            centerX - 100, 60, 200, 20,
            Component.translatable("gui.sapa.stack_config.slider"),
            Component.empty(),
            1, 1024,
            Config.maxStackSize.get(),
            true
        ) {
            @Override
            protected void applyValue() {
                super.applyValue();
                if (inputField != null) {
                    inputField.setValue(String.valueOf((int) this.getValue())); // 滑块和输入框同步
                }
            }
        };
        this.addRenderableWidget(this.slider);

        this.addRenderableWidget(new Button.Builder(Component.translatable("gui.sapa.stack_config.apply"), btn -> applyAndSend())
            .pos(centerX - 100, 130).size(95, 20).build());

        this.addRenderableWidget(new Button.Builder(Component.translatable("gui.sapa.stack_config.cancel"), btn -> this.onClose())
            .pos(centerX + 5, 130).size(95, 20).build());
    }

    private void applyAndSend() {
        int newValue;
        try {
            newValue = Integer.parseInt(this.inputField.getValue().trim());
        } catch (NumberFormatException e) {
            newValue = (int) this.slider.getValue();
        }
        newValue = Math.max(1, Math.min(1024, newValue));

        this.inputField.setValue(String.valueOf(newValue));
        this.slider.setValue(newValue);

        Config.maxStackSize.set(newValue);
        PacketDistributor.sendToServer(new Sapa.StackSizePayload(newValue));
        this.onClose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 30, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
