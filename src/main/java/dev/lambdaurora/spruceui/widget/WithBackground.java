/*
 * Copyright © 2020-2022 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget;

import dev.lambdaurora.spruceui.background.Background;

/**
 * Represents a widget with a background.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 2.0.0
 */
public interface WithBackground {
	/**
	 * Returns the background of this widget.
	 *
	 * @return the background
	 */
	Background getBackground();

	/**
	 * Sets the background of this widget.
	 *
	 * @param background the background
	 */
	void setBackground(Background background);
}
