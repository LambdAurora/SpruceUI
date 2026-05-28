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
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.SpruceLabelWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

/**
 * Represents a screen to navigate to the different SpruceUI test screens.
 *
 * @author LambdAurora
 */
public class SpruceMainMenuScreen extends SpruceScreen {
	private final Screen parent;

	public SpruceMainMenuScreen(@Nullable Screen parent) {
		super(Component.literal("SpruceUI Test Main Menu"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();

		this.addRenderableOnly(new SpruceLabelWidget(Position.of(0, 8), this.title, this.width, SpruceTextAlignment.CENTER));

		int startY = this.height / 4 + 48;
		this.addRenderableWidget(new SpruceButtonWidget(Position.of(this, this.width / 2 - 100, startY), 200, 20, Component.literal("Option Test"),
				btn -> this.minecraft.setScreenAndShow(new SpruceOptionScreen(this))));
		this.addRenderableWidget(new SpruceButtonWidget(Position.of(this, this.width / 2 - 100, startY += 25), 200, 20, Component.literal("Text Area Test"),
				btn -> this.minecraft.setScreenAndShow(new SpruceTextAreaScreen(this))));
		this.addRenderableWidget(new SpruceButtonWidget(Position.of(this, this.width / 2 - 100, startY += 25), 200, 20, Component.literal("Tabbed Screen Test"),
				btn -> this.minecraft.setScreenAndShow(new SpruceTabbedTestScreen(this))));

		// Add done button.
		this.addRenderableWidget(new SpruceButtonWidget(Position.of(this, this.width / 2 - 75, this.height - 29), 150, 20, SpruceTexts.GUI_DONE,
				btn -> this.minecraft.setScreenAndShow(this.parent)));
	}
}
