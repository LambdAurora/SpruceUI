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
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import net.minecraft.network.chat.Component;

public class SpruceIconButtonWidget extends AbstractSpruceIconButtonWidget {
	public SpruceIconButtonWidget(Position position, int width, int height, Component message, PressAction action) {
		super(position, width, height, message, action);
	}

	/**
	 * Renders the icon of the button.
	 *
	 * @return the x-offset the icon creates
	 */
	protected int renderIcon(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
		return 0;
	}
}
