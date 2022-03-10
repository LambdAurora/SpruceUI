/*
 * Copyright © 2020-2022 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.navigation;

import org.lwjgl.glfw.GLFW;

import java.util.Optional;

/**
 * Represents navigation direction.
 *
 * @author LambdAurora
 * @version 3.0.0
 * @since 2.0.0
 */
public enum NavigationDirection {
	LEFT,
	RIGHT,
	UP,
	DOWN;

	/**
	 * Returns whether or not this navigation direction is horizontal.
	 *
	 * @return true if this direction is horizontal, else false
	 */
	public boolean isHorizontal() {
		return this == LEFT || this == RIGHT;
	}

	/**
	 * Returns whether or not this navigation direction is vertical.
	 *
	 * @return true if this direction is vertical, else false
	 */
	public boolean isVertical() {
		return this == UP || this == DOWN;
	}

	public boolean isLookingForward() {
		return this == DOWN || this == RIGHT;
	}

	/**
	 * Returns a navigation direction from a key.
	 *
	 * @param keyCode the key
	 * @param shift true if the shift key is pressed, else false
	 * @return the direction if associated to the specified key, else empty
	 */
	public static Optional<NavigationDirection> fromKey(int keyCode, boolean shift) {
		if (shift && keyCode != GLFW.GLFW_KEY_TAB)
			return Optional.empty();
		switch (keyCode) {
			case GLFW.GLFW_KEY_LEFT:
				return Optional.of(LEFT);
			case GLFW.GLFW_KEY_RIGHT:
				return Optional.of(RIGHT);
			case GLFW.GLFW_KEY_UP:
				return Optional.of(UP);
			case GLFW.GLFW_KEY_TAB:
				if (shift)
					return Optional.of(UP);
			case GLFW.GLFW_KEY_DOWN:
				return Optional.of(DOWN);
			default:
				return Optional.empty();
		}
	}
}
