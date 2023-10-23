/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.border;

import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.gui.GuiGraphics;

/**
 * Represents a border to draw around a widget.
 *
 * @author LambdAurora
 * @version 5.0.0
 * @since 2.0.0
 */
public interface Border {
	void render(GuiGraphics graphics, SpruceWidget widget, int mouseX, int mouseY, float delta);

	/**
	 * Returns the thickness of the border.
	 *
	 * @return the thickness
	 */
	int getThickness();
}
