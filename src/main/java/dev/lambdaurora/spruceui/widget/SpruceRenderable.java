/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget;

import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;

/**
 * Represents something that can be rendered.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 8.0.0
 */
public interface SpruceRenderable extends Renderable {
	/**
	 * Renders the graphical user interface (GUI) element.
	 *
	 * @param graphics the graphics object used for rendering
	 * @param mouseX the X-coordinate of the mouse cursor
	 * @param mouseY the Y-coordinate of the mouse cursor
	 * @param tickDelta the partial tick time
	 */
	void render(SpruceGuiGraphics graphics, int mouseX, int mouseY, float tickDelta);

	@Override
	default void render(GuiGraphics graphics, int mouseX, int mouseY, float tickDelta) {
		this.render(SpruceGuiGraphics.of(graphics), mouseX, mouseY, tickDelta);
	}
}
