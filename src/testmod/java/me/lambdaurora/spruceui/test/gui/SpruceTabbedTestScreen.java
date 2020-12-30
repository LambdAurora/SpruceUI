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
import me.lambdaurora.spruceui.widget.SpruceLabelWidget;
import me.lambdaurora.spruceui.widget.container.SpruceContainerWidget;
import me.lambdaurora.spruceui.widget.container.tabbed.SpruceTabbedWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

public class SpruceTabbedTestScreen extends SpruceScreen {
    private final Screen parent;

    private SpruceTabbedWidget tabbedWidget;

    protected SpruceTabbedTestScreen(@Nullable Screen parent) {
        super(new LiteralText("Tabbed Screen Test"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.tabbedWidget = new SpruceTabbedWidget(Position.of(this, 0, 4), this.width, this.height - 35 - 4, this.title);
        this.tabbedWidget.addTabEntry(new LiteralText("Hello World"), null, (width, height) -> {
            SpruceContainerWidget container = new SpruceContainerWidget(Position.origin(), width, height);
            container.addChildren((containerWidth, containerHeight, widgetAdder) -> {
                widgetAdder.accept(new SpruceLabelWidget(Position.center(containerWidth, 16), new LiteralText("Hello World!").formatted(Formatting.WHITE), containerWidth, true));
                widgetAdder.accept(new SpruceLabelWidget(Position.center(containerWidth, 48),
                        new LiteralText("This is a tabbed widget. You can switch tabs by using the list on the left.\n" +
                                "It also allows quite a good controller support and arrow key navigation.")
                                .formatted(Formatting.WHITE), containerWidth, true));
            });
            return container;
        });
        this.tabbedWidget.addSeparatorEntry(new LiteralText("Separator"));
        this.tabbedWidget.addTabEntry(new LiteralText("Option Test"), new LiteralText("useful for config stuff.").formatted(Formatting.GRAY),
                (width, height) -> SpruceUITest.get().buildOptionList(Position.origin(), width, height));
        this.tabbedWidget.addTabEntry(new LiteralText("Text Area"), new LiteralText("to edit stuff on multiple lines.").formatted(Formatting.GRAY),
                (width, height) -> SpruceUITest.buildTextAreaContainer(Position.origin(), width, height,
                        textArea -> {
                        }, null));
        this.addChild(this.tabbedWidget);

        // Add done button.
        this.addButton(new SpruceButtonWidget(Position.of(this, this.width / 2 - 75, this.height - 29), 150, 20, SpruceTexts.GUI_DONE,
                btn -> this.client.openScreen(this.parent)).asVanilla());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.tabbedWidget.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
        // Draw the title text.
        //drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
        // Render all the tooltips.
        Tooltip.renderAll(this, matrices);
    }
}
