/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget;

import dev.lambdaurora.spruceui.navigation.NavigationDirection;
import net.minecraft.client.gui.components.events.GuiEventListener;

/**
 * Represents an element with navigation and controller input implementation.
 *
 * @author LambdAurora
 * @version 3.0.0
 * @since 2.0.0
 */
public interface SpruceElement extends GuiEventListener {
	/**
	 * Called when navigating in the menu.
	 *
	 * @param direction direction of navigation
	 * @param tab {@code true} if the navigation was triggered by the tab key, else {@code false}
	 * @return {@code true} if success, else {@code false}
	 */
	default boolean onNavigation(NavigationDirection direction, boolean tab) {
		if (this.requiresCursor()) return false;
		if (direction.isVertical()) {
			this.setFocused(direction == NavigationDirection.DOWN);
			return true;
		}
		return false;
	}

	/**
	 * Returns whether this is element requires a cursor to be used.
	 *
	 * @return {@code true} if a cursor is required, else {@code false}
	 */
	default boolean requiresCursor() {
		return false;
	}
}
