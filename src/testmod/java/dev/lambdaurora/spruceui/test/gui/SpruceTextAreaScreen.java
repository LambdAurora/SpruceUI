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
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.test.SpruceUITest;
import dev.lambdaurora.spruceui.widget.text.SpruceTextAreaWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a screen to test the {@link SpruceTextAreaWidget} widget.
 *
 * @author LambdAurora
 */
public class SpruceTextAreaScreen extends SpruceScreen {
	private final Screen parent;
	private SpruceTextAreaWidget textArea;

	public SpruceTextAreaScreen(@Nullable Screen parent) {
		super(Text.literal("SpruceUI Test TextArea Menu"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();

		var containerWidget =
				SpruceUITest.buildTextAreaContainer(Position.of(this, 0, 50), this.width, this.height - 50,
						textArea -> {
							if (this.textArea != null) {
								textArea.setText(this.textArea.getText());
							}
							this.textArea = textArea;
						}, btn -> this.client.setScreen(this.parent));
		this.addDrawableChild(containerWidget);
	}

	@Override
	public void renderTitle(DrawContext context, int mouseX, int mouseY, float delta) {
		context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 8, 16777215);
	}
}
