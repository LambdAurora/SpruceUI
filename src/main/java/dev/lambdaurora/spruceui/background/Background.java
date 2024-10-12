/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.background;

import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.gui.GuiGraphics;

/**
 * Represents a background which can be rendered on a widget.
 *
 * @author LambdAurora
 * @version 5.0.0
 * @since 2.0.0
 */
public interface Background {
	void render(GuiGraphics graphics, SpruceWidget widget, int vOffset, int mouseX, int mouseY, float delta);
}
