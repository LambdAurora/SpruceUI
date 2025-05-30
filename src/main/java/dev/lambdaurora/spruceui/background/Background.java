/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.background;

import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import dev.lambdaurora.spruceui.widget.SpruceWidget;

/**
 * Represents a background which can be rendered on a widget.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 2.0.0
 */
public interface Background {
	void render(SpruceGuiGraphics graphics, SpruceWidget widget, int vOffset, int mouseX, int mouseY, float delta);
}
