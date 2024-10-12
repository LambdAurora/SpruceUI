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
import dev.lambdaurora.spruceui.SprucePositioned;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarratableEntry;

/**
 * Represents a widget.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 1.6.0
 */
public interface SpruceWidget extends SprucePositioned, SpruceElement, NarratableEntry, Renderable {
	/**
	 * Returns the position of the widget.
	 *
	 * @return the position
	 */
	Position getPosition();

	@Override
	default int getX() {
		return this.getPosition().getX();
	}

	@Override
	default int getY() {
		return this.getPosition().getY();
	}

	/**
	 * {@return the X coordinate of the end of this widget}
	 */
	default int getEndX() {
		return this.getX() + this.getWidth();
	}

	/**
	 * {@return the Y coordinate of the end of this widget}
	 */
	default int getEndY() {
		return this.getY() + this.getHeight();
	}

	/**
	 * Returns the widget width.
	 *
	 * @return the width
	 */
	int getWidth();

	/**
	 * Returns the widget height.
	 *
	 * @return the height
	 */
	int getHeight();

	/**
	 * Returns whether the widget is visible or not.
	 *
	 * @return {@code true} if the widget is visible, else {@code false}
	 */
	boolean isVisible();

	/**
	 * Sets whether the widget is visible or not.
	 *
	 * @param visible {@code true} if the widget is visible, else {@code false}
	 */
	void setVisible(boolean visible);

	/**
	 * Returns whether this widget is active or not.
	 *
	 * @return {@code true} if the widget is active, else {@code false}
	 */
	boolean isActive();

	/**
	 * Sets whether this widget is active or not.
	 *
	 * @param active {@code true} if the widget is active, else {@code false}
	 */
	void setActive(boolean active);

	/**
	 * Returns whether the widget is focused or not.
	 *
	 * @return {@code true} if the widget is focused, else {@code false}
	 */
	boolean isFocused();

	/**
	 * Sets whether the widget is focused or not.
	 *
	 * @param focused {@code true} if the widget is focused, else {@code false}
	 */
	void setFocused(boolean focused);

	/**
	 * Returns whether the widget is hovered or not.
	 *
	 * @return {@code true} if the widget is hovered, else {@code false}
	 */
	default boolean isMouseHovered() {
		return this.narrationPriority() == NarrationPriority.HOVERED;
	}

	/**
	 * Returns whether the widget is focused or hovered.
	 *
	 * @return {@code true} if the widget is focused or hovered, else {@code false}
	 */
	default boolean isFocusedOrHovered() {
		return this.isMouseHovered() || this.isFocused();
	}

	@Override
	default boolean isMouseOver(double mouseX, double mouseY) {
		return this.isVisible() && mouseX >= (double) this.getX() && mouseX < (double) (this.getX() + this.getWidth()) && mouseY >= (double) this.getY() && mouseY < (double) (this.getY() + this.getHeight());
	}

	boolean isDragging();

	void setDragging(boolean dragging);
}
