/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui;

import net.minecraft.client.gui.navigation.ScreenPosition;

/**
 * Represents an object that provides a screen position.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 1.4.0
 */
public interface SprucePositioned {
	/**
	 * {@return the X coordinate}
	 */
	default int getX() {
		return 0;
	}

	/**
	 * {@return the Y coordinate}
	 */
	default int getY() {
		return 0;
	}

	default ScreenPosition getScreenPosition() {
		return new ScreenPosition(this.getX(), this.getY());
	}
}
