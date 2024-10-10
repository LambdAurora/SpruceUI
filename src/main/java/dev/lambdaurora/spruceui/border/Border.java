/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.border;

import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.gui.DrawContext;

/**
 * Represents a border to draw around a widget.
 *
 * @author LambdAurora
 * @version 5.1.0
 * @since 2.0.0
 */
public interface Border {
	void render(DrawContext context, SpruceWidget widget, int mouseX, int mouseY, float delta);

	/**
	 * Returns the thickness of the top border.
	 *
	 * @return the thickness
	 * @since 5.1.0
	 */
	default int getTop() {
		return this.getThickness();
	}

	/**
	 * Returns the thickness of the right border.
	 *
	 * @return the thickness
	 * @since 5.1.0
	 */
	default int getRight() {
		return this.getThickness();
	}

	/**
	 * Returns the thickness of the bottom border.
	 *
	 * @return the thickness
	 * @since 5.1.0
	 */
	default int getBottom() {
		return this.getThickness();
	}

	/**
	 * Returns the thickness of the left border.
	 *
	 * @return the thickness
	 * @since 5.1.0
	 */
	default int getLeft() {
		return this.getThickness();
	}

	/**
	 * Returns the thickness of the border.
	 *
	 * @return the thickness
	 */
	int getThickness();
}
