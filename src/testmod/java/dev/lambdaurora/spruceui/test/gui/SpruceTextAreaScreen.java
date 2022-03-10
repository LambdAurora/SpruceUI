/*
 * Copyright © 2020-2022 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.test.gui;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.test.SpruceUITest;
import dev.lambdaurora.spruceui.widget.text.SpruceTextAreaWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a screen to test the {@link SpruceTextAreaWidget} widget.
 *
 * @author LambdAurora
 */
public class SpruceTextAreaScreen extends SpruceScreen {
    private final Screen parent;
    private SpruceTextAreaWidget textArea;

    public SpruceTextAreaScreen(@Nullable Screen parent) {
        super(new LiteralText("SpruceUI Test TextArea Menu"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        var containerWidget =
                SpruceUITest.buildTextAreaContainer(Position.of(this, 0, 50), this.width, this.height - 50,
                        textArea -> {
                            if (this.textArea != null) {
                                textArea.setText(this.textArea.getText());
                            }
                            this.textArea = textArea;
                        }, btn -> this.client.setScreen(this.parent));
        this.addDrawableChild(containerWidget);
    }

    @Override
    public void renderTitle(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
    }
}
