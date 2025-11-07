/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget;

import dev.lambdaurora.spruceui.navigation.NavigationEvent;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.navigation.ScreenAxis;

/**
 * Represents an element with navigation and controller input implementation.
 *
 * @author LambdAurora
 * @version 9.0.0
 * @since 2.0.0
 */
public interface SpruceElement extends GuiEventListener {
	/**
	 * Called when navigating in the menu.
	 *
	 * @param event the navigation event
	 * @return {@code true} if success, else {@code false}
	 */
	default boolean onNavigation(NavigationEvent event) {
		if (this.requiresCursor()) return false;
		if (event.direction().getAxis() == ScreenAxis.VERTICAL) {
			this.setFocused(event.isLookingForward());
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
