/*
 * Copyright © 2020-2022 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui;

/**
 * Generic interface representing an object that provides a screen position.
 *
 * @author LambdAurora
 * @version 3.0.0
 * @since 1.4.0
 */
public interface SprucePositioned {
	/**
	 * Returns the X coordinate.
	 *
	 * @return the X coordinate
	 */
	default int getX() {
		return 0;
	}

	/**
	 * Returns the Y coordinate.
	 *
	 * @return the Y coordinate
	 */
	default int getY() {
		return 0;
	}
}
