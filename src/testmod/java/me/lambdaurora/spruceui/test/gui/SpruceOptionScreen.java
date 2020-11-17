/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.test.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.lambdaurora.spruceui.Position;
import me.lambdaurora.spruceui.SpruceButtonWidget;
import me.lambdaurora.spruceui.SpruceTexts;
import me.lambdaurora.spruceui.Tooltip;
import me.lambdaurora.spruceui.option.*;
import me.lambdaurora.spruceui.screen.SpruceScreen;
import me.lambdaurora.spruceui.test.TestEnum;
import me.lambdaurora.spruceui.widget.container.SpruceOptionListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a screen to test the different {@link me.lambdaurora.spruceui.option.SpruceOption} classes.
 *
 * @author LambdAurora
 */
public class SpruceOptionScreen extends SpruceScreen
{
    private final Screen parent;
    private final SpruceOption booleanOption;
    private final SpruceOption checkboxOption;
    private final SpruceOption toggleSwitchOption;
    private final SpruceOption separatorOption;
    private final SpruceOption doubleOption;
    private final SpruceOption cyclingOption;
    private final SpruceOption actionOption;
    private final SpruceOption resetOption;
    private boolean aBoolean;
    private boolean checkboxBoolean;
    private boolean toggleBoolean;
    private double aDouble;
    private TestEnum cyclingValue = TestEnum.FIRST;

    //private ButtonListWidget list;
    private SpruceOptionListWidget list;

    public SpruceOptionScreen(@Nullable Screen parent)
    {
        super(new LiteralText("SpruceUI Test Option Menu"));
        this.parent = parent;

        this.booleanOption = new SpruceBooleanOption("spruceui_test.option.boolean",
                () -> this.aBoolean,
                newValue -> this.aBoolean = newValue,
                new LiteralText("Represents a boolean option, can either be true or false.\n" +
                        "The option value can be colored."),
                true);
        this.checkboxOption = new SpruceCheckboxBooleanOption("spruceui_test.option.checkbox",
                () -> this.checkboxBoolean,
                newValue -> this.checkboxBoolean = newValue,
                new LiteralText("Represents a boolean option as a checkbox, can either be true or false.\n"
                        + "It's another implementation of `SpruceBooleanOption` internally."),
                true);
        this.toggleSwitchOption = new SpruceToggleBooleanOption("spruceui_test.option.toggle_switch",
                () -> this.toggleBoolean,
                newValue -> this.toggleBoolean = newValue,
                new LiteralText("Represents a boolean option as a toggle switch, can either be true or false.\n"
                        + "It's another implementation of `SpruceBooleanOption` internally."));

        this.separatorOption = new SpruceSeparatorOption("spruceui_test.option.separator", true, null);

        this.doubleOption = new SpruceDoubleOption("spruceui_test.option.double",
                0.0, 50.0, 1.f,
                () -> this.aDouble,
                newValue -> this.aDouble = newValue,
                option -> option.getDisplayText(new LiteralText(String.valueOf(this.aDouble))),
                new LiteralText("Represents an option with a floating point value.\n"
                        + "There is a minimum, a maximum and a step.\n"
                        + "There is also a lambda for the display text as you can integrate a suffix/prefix like \"%\" or anything else."));

        this.cyclingOption = new SpruceCyclingOption("spruceui_test.option.cycling",
                amount -> this.cyclingValue = this.cyclingValue.next(),
                option -> option.getDisplayText(this.cyclingValue.getText()),
                new LiteralText("Represents a cycling option.\n"
                        + "Each press will cycle the value between some pre-defined values."));

        // Choose whatever action this option should do.
        this.actionOption = new SpruceSimpleActionOption("spruceui_test.option.action",
                btn -> {
                    SystemToast toast = SystemToast.create(this.client, SystemToast.Type.TUTORIAL_HINT,
                            new LiteralText("Action button pressed!"), new LiteralText("I'm a result of the action"));
                    this.client.getToastManager().add(toast);
                },
                new LiteralText("Represents an option with a simple action.\n"
                        + "It's used like a normal button and a press callback."));

        // Reset option to reset values.
        this.resetOption = new SpruceResetOption(btn -> {
            this.aBoolean = false;
            this.checkboxBoolean = false;
            this.aDouble = 0.0;
            this.cyclingValue = TestEnum.FIRST;

            // Re-initialize the screen to update all the values.
            MinecraftClient client = MinecraftClient.getInstance();
            this.init(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());
        }, new LiteralText("Represents a reset option.\n"
                + "The option title is already defined and translated in several languages.\n"
                + "You have to manage screen re-initialization and reset logic yourself."));
    }

    private int getTextHeight()
    {
        return (5 + this.textRenderer.fontHeight) * 3 + 5;
    }

    @Override
    protected void init()
    {
        super.init();

        // Button list.
        //this.list = new ButtonListWidget(this.client, this.width, this.height, 43, this.height - 29 - this.getTextHeight(), 25);
        this.list = new SpruceOptionListWidget(Position.of(0,  22), this.width, this.height - 35 - 22);

        this.list.addOptionEntry(this.booleanOption, this.checkboxOption);
        this.list.addOptionEntry(this.toggleSwitchOption, null);
        this.list.addOptionEntry(this.toggleSwitchOption, null);
        this.list.addOptionEntry(this.toggleSwitchOption, null);
        this.list.addOptionEntry(this.toggleSwitchOption, null);
        this.list.addOptionEntry(this.toggleSwitchOption, null);
        this.list.addOptionEntry(this.toggleSwitchOption, null);
        this.list.addOptionEntry(this.toggleSwitchOption, null);
        this.list.addOptionEntry(this.toggleSwitchOption, null);
        this.list.addSingleOptionEntry(this.separatorOption);
        this.list.addSingleOptionEntry(this.doubleOption);
        this.list.addOptionEntry(this.actionOption, this.cyclingOption);

        this.addChild(this.list);

        // Add reset button. You can add option buttons outside a button list widget. GameOptions instance is required because of Vanilla limitations.
        //this.addButton(this.resetOption.createButton(this.client.options, this.width / 2 - 155, this.height - 29, 150));
        // Add done button.
        this.addButton(new SpruceButtonWidget(Position.of(this, this.width / 2 - 155 + 160, this.height - 29), 150, 20, SpruceTexts.GUI_DONE,
                btn -> this.client.openScreen(this.parent)).asVanilla());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        this.renderBackground(matrices);
        this.list.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
        // Draw the title text.
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
        // Render all the tooltips.
        Tooltip.renderAll(this, matrices);
    }
}
