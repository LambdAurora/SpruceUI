/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.test.gui;

import me.lambdaurora.spruceui.*;
import me.lambdaurora.spruceui.screen.SpruceScreen;
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
public class SpruceTextAreaScreen extends SpruceScreen
{
    private final Screen               parent;
    private       SpruceTextAreaWidget textArea;

    public SpruceTextAreaScreen(@Nullable Screen parent)
    {
        super(new LiteralText("SpruceUI Test TextArea Menu"));
        this.parent = parent;
    }

    @Override
    protected void init()
    {
        super.init();

        int textFieldWidth = (int) (this.width * (3.0 / 4.0));

        String text = null;
        if (this.textArea != null) {
            text = this.textArea.getText();
        }

        this.textArea = new SpruceTextAreaWidget(this.textRenderer, this.width / 2 - textFieldWidth / 2, 50, textFieldWidth, this.height - 100,
                new LiteralText("Text Area"));
        if (text != null)
            this.textArea.setText(text);
        else
            this.textArea.setLines(Arrays.asList(
                    "Hello world,",
                    "",
                    "Today I want to present you this text area.",
                    "I hope you like it, spent 2 whole days on this stupid widget.",
                    "",
                    "The underlying implementation was kind of hard to write, especially when the first design had a stupid choice.",
                    "The widget uses a list of strings to store the text, each index of the list represents one row, not one line.",
                    "The first implementation made the error of making it per line, which made rendering very hard and overflowing issues happened",
                    "",
                    "Now it has to convert list of lines to list of rows, it's really not funny to do...",
                    "",
                    "Feature-wise!",
                    " - Arrow keys allows you to move the cursor",
                    " - HOME and END keys work",
                    " - You can select text",
                    " - You can copy/cut/paste text.",
                    " - You can delete a row with CTRL + D",
                    " - CTRL + A selects everything",
                    "",
                    "This widget can be very useful in some cases."));
        // Display as many lines as possible
        this.textArea.setDisplayedLines(this.textArea.getInnerHeight() / this.textRenderer.fontHeight);
        this.addChild(this.textArea);

        // Print to console button, may be useful for debugging.
        this.addButton(new SpruceButtonWidget(this.width / 2 - 155, this.height - 29, 150, 20, new LiteralText("Print to console"),
                btn -> {
                    System.out.println("########################## START TEXT AREA CONTENT ##########################");
                    System.out.println(this.textArea.getText());
                    System.out.println("##########################  END TEXT AREA CONTENT  ##########################");
                }));
        // Add done button.
        this.addButton(new SpruceButtonWidget(this.width / 2 - 155 + 160, this.height - 29, 150, 20, SpruceTexts.GUI_DONE,
                btn -> this.client.openScreen(this.parent)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        this.renderBackground(matrices);
        this.children().stream().filter(child -> child instanceof Drawable).forEach(child -> ((Drawable) child).render(matrices, mouseX, mouseY, delta));
        // Draw the title text.
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
        // Render all the tooltips.
        Tooltip.renderAll(this, matrices);
    }
}
