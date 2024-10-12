/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget;

import dev.lambdaurora.spruceui.Position;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Text;
import net.minecraft.util.math.MathHelper;

public abstract class AbstractSpruceIconButtonWidget extends SpruceButtonWidget {
	public AbstractSpruceIconButtonWidget(Position position, int width, int height, Text message, PressAction action) {
		super(position, width, height, message, action);
	}

	/**
	 * Renders the icon of the button.
	 *
	 * @return the x-offset the icon creates
	 */
	protected abstract int renderIcon(GuiGraphics graphics, int mouseX, int mouseY, float delta);

	@Override
	protected void renderButton(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		int iconWidth = this.renderIcon(graphics, mouseX, mouseY, delta);
		if (!this.getMessage().getString().isEmpty()) {
			int color = this.isActive() ? 16777215 : 10526880;
			graphics.drawCenteredShadowedText(this.client.font, this.getMessage(),
					this.getX() + 8 + iconWidth + (this.getWidth() - 8 - iconWidth - 6) / 2,
					this.getY() + (this.height - 8) / 2, color | MathHelper.ceil(this.getAlpha() * 255.0F) << 24);
		}
	}
}
