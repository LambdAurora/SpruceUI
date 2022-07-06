/*
 * Copyright © 2020-2022 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.test.gui;

import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.SpruceTexts;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a screen to navigate to the different SpruceUI test screens.
 *
 * @author LambdAurora
 */
public class SpruceMainMenuScreen extends SpruceScreen {
    private final Screen parent;

    public SpruceMainMenuScreen(@Nullable Screen parent) {
        super(Text.literal("SpruceUI Test Main Menu"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int startY = this.height / 4 + 48;
        this.addDrawableChild(new SpruceButtonWidget(Position.of(this, this.width / 2 - 100, startY), 200, 20, Text.literal("Option Test"),
                btn -> this.client.setScreen(new SpruceOptionScreen(this))));
        this.addDrawableChild(new SpruceButtonWidget(Position.of(this, this.width / 2 - 100, startY += 25), 200, 20, Text.literal("Text Area Test"),
                btn -> this.client.setScreen(new SpruceTextAreaScreen(this))));
        this.addDrawableChild(new SpruceButtonWidget(Position.of(this, this.width / 2 - 100, startY += 25), 200, 20, Text.literal("Tabbed Screen Test"),
                btn -> this.client.setScreen(new SpruceTabbedTestScreen(this))));

        // Add done button.
        this.addDrawableChild(new SpruceButtonWidget(Position.of(this, this.width / 2 - 75, this.height - 29), 150, 20, SpruceTexts.GUI_DONE,
                btn -> this.client.setScreen(this.parent)));
    }

    @Override
    public void renderTitle(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
    }
}
