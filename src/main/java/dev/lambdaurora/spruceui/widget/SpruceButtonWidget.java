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
import net.minecraft.network.chat.Text;

/**
 * Represents a button widget.
 *
 * @author LambdAurora
 * @version 3.0.0
 * @since 1.0.0
 */
public class SpruceButtonWidget extends AbstractSprucePressableButtonWidget {
	private final PressAction action;

	public SpruceButtonWidget(Position position, int width, int height, Text message, PressAction action) {
		super(position, width, height, message);
		this.action = action;
	}

	@Override
	public void onPress() {
		this.action.onPress(this);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + '{' +
				"position=" + this.getPosition() +
				", width=" + this.getWidth() +
				", height=" + this.getHeight() +
				", visible=" + this.isVisible() +
				", active=" + this.isActive() +
				", message=" + this.getMessage() +
				", focused=" + this.isFocused() +
				", hovered=" + this.isMouseHovered() +
				", wasHovered=" + this.wasHovered +
				", dragging=" + this.dragging +
				", lastDrag=" + this.lastDrag +
				", alpha=" + this.getAlpha() +
				'}';
	}

	public interface PressAction {
		void onPress(SpruceButtonWidget button);
	}
}
