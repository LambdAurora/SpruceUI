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
import dev.lambdaurora.spruceui.SpruceTexts;
import dev.lambdaurora.spruceui.option.SpruceOption;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.test.SpruceUITest;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Text;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a screen to test the different {@link SpruceOption} classes.
 *
 * @author LambdAurora
 */
public class SpruceOptionScreen extends SpruceScreen {
	private final Screen parent;

	//private ButtonListWidget list;
	private SpruceOptionListWidget list;

	public SpruceOptionScreen(@Nullable Screen parent) {
		super(Text.literal("SpruceUI Test Option Menu"));
		this.parent = parent;
	}

	private int getTextHeight() {
		return (5 + this.font.lineHeight) * 3 + 5;
	}

	@Override
	protected void init() {
		super.init();

		// Button list.
		//this.list = new ButtonListWidget(this.client, this.width, this.height, 43, this.height - 29 - this.getTextHeight(), 25);
		this.list = SpruceUITest.get().buildOptionList(Position.of(0, 22), this.width, this.height - 35 - 22);
		SpruceUITest.get().resetConsumer = btn -> {
			// Re-initialize the screen to update all the values.
			this.init(this.client, this.client.getWindow().getGuiScaledWidth(), this.client.getWindow().getGuiScaledHeight());
		};

		this.addRenderableWidget(this.list);

		// Add reset button. You can add option buttons outside a button list widget. GameOptions instance is required because of Vanilla limitations.
		//this.addButton(this.resetOption.createButton(this.client.options, this.width / 2 - 155, this.height - 29, 150));
		// Add done button.
		this.addRenderableWidget(new SpruceButtonWidget(Position.of(this, this.width / 2 - 155 + 160, this.height - 29), 150, 20, SpruceTexts.GUI_DONE,
				btn -> this.client.setScreen(this.parent)).asVanilla());
	}

	@Override
	public void renderTitle(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		guiGraphics.drawCenteredShadowedText(this.font, this.title, this.width / 2, 8, 16777215);
	}
}
