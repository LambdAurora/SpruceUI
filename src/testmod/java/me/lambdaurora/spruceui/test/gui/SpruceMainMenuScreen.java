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
import me.lambdaurora.spruceui.widget.SpruceButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a screen to navigate to the different SpruceUI test screens.
 *
 * @author LambdAurora
 */
public class SpruceMainMenuScreen extends SpruceScreen {
    private final Screen parent;

    public SpruceMainMenuScreen(@Nullable Screen parent) {
        super(new LiteralText("SpruceUI Test Main Menu"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int startY = this.height / 4 + 48;
        this.addButton(new SpruceButtonWidget(Position.of(this, this.width / 2 - 100, startY), 200, 20, new LiteralText("Option Test"),
                btn -> this.client.openScreen(new SpruceOptionScreen(this))).asVanilla());
        this.addButton(new SpruceButtonWidget(Position.of(this, this.width / 2 - 100, startY += 25), 200, 20, new LiteralText("Text Area Test"),
                btn -> this.client.openScreen(new SpruceTextAreaScreen(this))).asVanilla());
        this.addButton(new SpruceButtonWidget(Position.of(this, this.width / 2 - 100, startY += 25), 200, 20, new LiteralText("Tabbed Screen Test"),
                btn -> this.client.openScreen(new SpruceTabbedTestScreen(this))).asVanilla());

        // Add done button.
        this.addButton(new SpruceButtonWidget(Position.of(this, this.width / 2 - 75, this.height - 29), 150, 20, SpruceTexts.GUI_DONE,
                btn -> this.client.openScreen(this.parent)).asVanilla());
    }

    @Override
    public void renderTitle(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
    }
}
