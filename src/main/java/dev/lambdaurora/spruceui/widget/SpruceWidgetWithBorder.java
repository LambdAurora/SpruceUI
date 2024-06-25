/*
 * Copyright © 2024 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget;

/**
 * Represents a widget that has a border.
 *
 * @author LambdAurora
 * @version 5.1.0
 * @since 5.1.0
 */
public interface SpruceWidgetWithBorder extends SpruceWidget, WithBorder {
	/**
	 * {@return the inner X-coordinate of the widget depending on its border}
	 */
	default int getInnerBorderedX() {
		return this.getX() + this.getBorder().getLeft();
	}

	/**
	 * {@return the inner Y-coordinate of the widget depending on its border}
	 */
	default int getInnerBorderedY() {
		return this.getY() + this.getBorder().getTop();
	}

	/**
	 * {@return the inner end X-coordinate of the widget depending on its border}
	 */
	default int getEndInnerBorderedX() {
		return this.getEndX() - this.getBorder().getRight();
	}

	/**
	 * {@return the inner end Y-coordinate of the widget depending on its border}
	 */
	default int getEndInnerBorderedY() {
		return this.getEndY() - this.getBorder().getBottom();
	}

	/**
	 * {@return the inner width of the widget depending on its border}
	 */
	default int getInnerBorderedWidth() {
		return this.getWidth() - (this.getBorder().getLeft() + this.getBorder().getRight());
	}

	/**
	 * {@return the inner height of the widget depending on its border}
	 */
	default int getInnerBorderedHeight() {
		return this.getHeight() - (this.getBorder().getTop() + this.getBorder().getBottom());
	}
}
