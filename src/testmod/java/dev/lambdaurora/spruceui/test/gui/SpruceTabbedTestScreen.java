/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.test.gui;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.SpruceTextAlignment;
import dev.lambdaurora.spruceui.SpruceTexts;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.test.SpruceUITest;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.SpruceLabelWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceContainerWidget;
import dev.lambdaurora.spruceui.widget.container.tabbed.SpruceTabbedWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public class SpruceTabbedTestScreen extends SpruceScreen {
	private final Screen parent;

	private SpruceTabbedWidget tabbedWidget;

	protected SpruceTabbedTestScreen(@Nullable Screen parent) {
		super(Component.literal("Tabbed Screen Test"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();
		this.tabbedWidget = new SpruceTabbedWidget(Position.of(this, 0, 4), this.width, this.height - 35 - 4, this.title);
		this.tabbedWidget.addTabEntry(Component.literal("Hello World"), null, (width, height) -> {
			var container = new SpruceContainerWidget(Position.origin(), width, height);
			container.addChildren((containerWidth, containerHeight, widgetAdder) -> {
				widgetAdder.accept(new SpruceLabelWidget(Position.of(0, 16),
						Component.literal("Hello World!").withStyle(ChatFormatting.WHITE),
						containerWidth, SpruceTextAlignment.CENTER)
				);
				widgetAdder.accept(new SpruceLabelWidget(Position.of(0, 48),
						Component.literal("This is a tabbed widget. You can switch tabs by using the list on the left.\n" +
										"It also allows quite a good controller support and arrow key navigation.")
								.withStyle(ChatFormatting.WHITE),
						containerWidth, SpruceTextAlignment.CENTER)
				);
				widgetAdder.accept(new SpruceLabelWidget(Position.of(0, 92),
						Component.literal("Right-aligned text").withStyle(ChatFormatting.WHITE),
						containerWidth, SpruceTextAlignment.RIGHT
				));
			});
			return container;
		});
		this.tabbedWidget.addSeparatorEntry(Component.literal("Separator"));
		this.tabbedWidget.addTabEntry(Component.literal("Option Test"), Component.literal("useful for config stuff.").withStyle(ChatFormatting.GRAY),
				(width, height) -> SpruceUITest.get().buildOptionList(Position.origin(), width, height));
		this.tabbedWidget.addTabEntry(Component.literal("Text Area"), Component.literal("to edit stuff on multiple lines.").withStyle(ChatFormatting.GRAY),
				(width, height) -> SpruceUITest.buildTextAreaContainer(Position.origin(), width, height,
						textArea -> {
						}, null));
		this.addRenderableWidget(this.tabbedWidget);

		// Add done button.
		this.addRenderableWidget(new SpruceButtonWidget(Position.of(this, this.width / 2 - 75, this.height - 29), 150, 20, SpruceTexts.GUI_DONE,
				btn -> this.minecraft.setScreen(this.parent)).asVanilla());
	}
}
