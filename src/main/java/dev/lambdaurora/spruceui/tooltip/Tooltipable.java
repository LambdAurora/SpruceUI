/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.tooltip;

import net.minecraft.network.chat.Text;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an object which can show a tooltip.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 1.0.0
 */
public interface Tooltipable {
	/**
	 * Gets the tooltip components of this object.
	 * <p>
	 * An empty list of tooltip components is equivalent to there not being any tooltips.
	 *
	 * @return the tooltip components
	 */
	@NotNull TooltipData getTooltip();

	/**
	 * Sets the tooltip.
	 *
	 * @param tooltip the tooltip to show
	 */
	void setTooltip(@NotNull TooltipData tooltip);

	/**
	 * Sets the tooltip.
	 *
	 * @param tooltip the tooltip to show
	 */
	default void setTooltip(@NotNull Text tooltip) {
		this.setTooltip(new TooltipData(new TooltipData.TextEntry(tooltip)));
	}
}
