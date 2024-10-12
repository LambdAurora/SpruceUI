/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui;

import net.minecraft.network.chat.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Represents an object which can show a tooltip.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 1.0.0
 */
public interface Tooltipable {
	/**
	 * Gets the tooltip.
	 *
	 * @return the tooltip to show
	 */
	Optional<Text> getTooltip();

	/**
	 * Sets the tooltip.
	 *
	 * @param tooltip the tooltip to show
	 */
	void setTooltip(@Nullable Text tooltip);
}
