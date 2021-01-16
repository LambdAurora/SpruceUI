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
import me.lambdaurora.spruceui.option.*;
import me.lambdaurora.spruceui.screen.SpruceScreen;
import me.lambdaurora.spruceui.test.SpruceUITest;
import me.lambdaurora.spruceui.test.TestEnum;
import me.lambdaurora.spruceui.widget.SpruceButtonWidget;
import me.lambdaurora.spruceui.widget.container.SpruceOptionListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a screen to test the different {@link me.lambdaurora.spruceui.option.SpruceOption} classes.
 *
 * @author LambdAurora
 */
public class SpruceOptionScreen extends SpruceScreen {
    private final Screen parent;

    //private ButtonListWidget list;
    private SpruceOptionListWidget list;

    public SpruceOptionScreen(@Nullable Screen parent) {
        super(new LiteralText("SpruceUI Test Option Menu"));
        this.parent = parent;
    }

    private int getTextHeight() {
        return (5 + this.textRenderer.fontHeight) * 3 + 5;
    }

    @Override
    protected void init() {
        super.init();

        // Button list.
        //this.list = new ButtonListWidget(this.client, this.width, this.height, 43, this.height - 29 - this.getTextHeight(), 25);
        this.list = SpruceUITest.get().buildOptionList(Position.of(0, 22), this.width, this.height - 35 - 22);
        SpruceUITest.get().resetConsumer = btn -> {
            // Re-initialize the screen to update all the values.
            this.init(this.client, this.client.getWindow().getScaledWidth(), this.client.getWindow().getScaledHeight());
        };

        this.addChild(this.list);

        // Add reset button. You can add option buttons outside a button list widget. GameOptions instance is required because of Vanilla limitations.
        //this.addButton(this.resetOption.createButton(this.client.options, this.width / 2 - 155, this.height - 29, 150));
        // Add done button.
        this.addButton(new SpruceButtonWidget(Position.of(this, this.width / 2 - 155 + 160, this.height - 29), 150, 20, SpruceTexts.GUI_DONE,
                btn -> this.client.openScreen(this.parent)).asVanilla());
    }

    @Override
    public void renderTitle(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
    }
}
