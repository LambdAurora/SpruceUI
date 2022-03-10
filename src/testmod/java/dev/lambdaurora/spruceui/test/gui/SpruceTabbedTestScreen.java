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
import dev.lambdaurora.spruceui.SpruceTexts;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.test.SpruceUITest;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.SpruceLabelWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceContainerWidget;
import dev.lambdaurora.spruceui.widget.container.tabbed.SpruceTabbedWidget;
import net.minecraft.client.gui.screen.Screen;
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
        super.init();
        this.tabbedWidget = new SpruceTabbedWidget(Position.of(this, 0, 4), this.width, this.height - 35 - 4, this.title);
        this.tabbedWidget.addTabEntry(new LiteralText("Hello World"), null, (width, height) -> {
            var container = new SpruceContainerWidget(Position.origin(), width, height);
            container.addChildren((containerWidth, containerHeight, widgetAdder) -> {
                widgetAdder.accept(new SpruceLabelWidget(Position.of(0, 16),
                        new LiteralText("Hello World!").formatted(Formatting.WHITE),
                        containerWidth, true));
                widgetAdder.accept(new SpruceLabelWidget(Position.of(0, 48),
                        new LiteralText("This is a tabbed widget. You can switch tabs by using the list on the left.\n" +
                                "It also allows quite a good controller support and arrow key navigation.")
                                .formatted(Formatting.WHITE),
                        containerWidth, true));
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
        this.addDrawableChild(this.tabbedWidget);

        // Add done button.
        this.addDrawableChild(new SpruceButtonWidget(Position.of(this, this.width / 2 - 75, this.height - 29), 150, 20, SpruceTexts.GUI_DONE,
                btn -> this.client.setScreen(this.parent)).asVanilla());
    }
}
