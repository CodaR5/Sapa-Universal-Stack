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

        // 1. 输入框
        this.inputField = new EditBox(this.font, centerX - 100, 95, 200, 20,
                Component.translatable("gui.sapa.stack_config.input"));
        this.inputField.setValue(String.valueOf(Config.maxStackSize.get()));
        // 限制只能输入数字，防止输入非数字字符导致解析报错
        this.inputField.setFilter(s -> s.matches("\\d*"));
        this.addRenderableWidget(this.inputField);

        // 2. 滑块 (关键修复：重写 applyValue 同步数值到输入框)
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
                    // 将滑块的值实时同步到输入框
                    inputField.setValue(String.valueOf((int) this.getValue()));
                }
            }
        };
        this.addRenderableWidget(this.slider);

        // 3. 应用按钮
        this.addRenderableWidget(new Button.Builder(
                Component.translatable("gui.sapa.stack_config.apply"),
                btn -> applyAndSend()
        ).pos(centerX - 100, 130).size(95, 20).build());

        // 4. 取消按钮
        this.addRenderableWidget(new Button.Builder(
                Component.translatable("gui.sapa.stack_config.cancel"),
                btn -> this.onClose()
        ).pos(centerX + 5, 130).size(95, 20).build());
    }

    private void applyAndSend() {
        int newValue;
        try {
            newValue = Integer.parseInt(this.inputField.getValue().trim());
        } catch (NumberFormatException e) {
            // 如果输入框为空或非法，则回退使用滑块的值
            newValue = (int) this.slider.getValue();
        }
        // 确保数值在 1-1024 范围内
        newValue = Math.max(1, Math.min(1024, newValue));

        // 同步更新 UI 上的显示
        this.inputField.setValue(String.valueOf(newValue));
        this.slider.setValue(newValue);

        // 保存配置并发送网络包到服务端
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