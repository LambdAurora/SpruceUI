/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.navigation;

import net.minecraft.client.gui.navigation.ScreenDirection;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

/**
 * Represents a navigation event.
 *
 * @param direction the direction of navigation
 * @param tab {@code true} if the navigation was triggered by the tab key, or {@code false} otherwise
 * @param hasShiftDown {@code true} if the shift key is down, or {@code false} otherwise
 * @version 9.0.0
 * @since 9.0.0
 */
public record NavigationEvent(
		@NotNull ScreenDirection direction,
		boolean tab,
		boolean hasShiftDown
) {
	public boolean isLookingForward() {
		return this.direction.isPositive();
	}

	public static Optional<NavigationEvent> fromKey(int keyCode, boolean shift) {
		return directionFromKey(keyCode, shift)
				.map(direction -> new NavigationEvent(direction, keyCode == GLFW.GLFW_KEY_TAB, shift));
	}

	/**
	 * Returns a navigation direction from a key.
	 *
	 * @param keyCode the key
	 * @param shift true if the shift key is pressed, else false
	 * @return the direction if associated to the specified key, else empty
	 */
	private static Optional<ScreenDirection> directionFromKey(int keyCode, boolean shift) {
		if (shift && keyCode != GLFW.GLFW_KEY_TAB)
			return Optional.empty();
		switch (keyCode) {
			case GLFW.GLFW_KEY_LEFT:
				return Optional.of(ScreenDirection.LEFT);
			case GLFW.GLFW_KEY_RIGHT:
				return Optional.of(ScreenDirection.RIGHT);
			case GLFW.GLFW_KEY_UP:
				return Optional.of(ScreenDirection.UP);
			case GLFW.GLFW_KEY_TAB:
				if (shift)
					return Optional.of(ScreenDirection.UP);
			case GLFW.GLFW_KEY_DOWN:
				return Optional.of(ScreenDirection.DOWN);
			default:
				return Optional.empty();
		}
	}
}
