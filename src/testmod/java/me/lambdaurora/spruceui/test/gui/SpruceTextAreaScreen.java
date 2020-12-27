/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.test.gui;

import me.lambdaurora.spruceui.Position;
import me.lambdaurora.spruceui.SpruceTexts;
import me.lambdaurora.spruceui.Tooltip;
import me.lambdaurora.spruceui.screen.SpruceScreen;
import me.lambdaurora.spruceui.test.SpruceUITest;
import me.lambdaurora.spruceui.widget.SpruceButtonWidget;
import me.lambdaurora.spruceui.widget.container.SpruceContainerWidget;
import me.lambdaurora.spruceui.widget.text.SpruceTextAreaWidget;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

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

        SpruceContainerWidget containerWidget = SpruceUITest.buildTextAreaContainer(Position.of(this, 0, 50), this.width, this.height - 50, textArea -> {
            if (this.textArea != null) {
                textArea.setText(this.textArea.getText());
            }
            this.textArea = textArea;
        }, btn -> this.client.openScreen(this.parent));
        this.addChild(containerWidget);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.children().stream().filter(child -> child instanceof Drawable).forEach(child -> ((Drawable) child).render(matrices, mouseX, mouseY, delta));
        // Draw the title text.
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
        // Render all the tooltips.
        Tooltip.renderAll(this, matrices);
    }
}
