/*
 * Copyright © 2024 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.border;

import dev.lambdaurora.spruceui.SpruceTextures;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderType;

/**
 * Represents a textured border to draw around a widget.
 *
 * @author LambdAurora
 * @version 6.0.0
 * @since 6.0.0
 */
public record TexturedBorder(WidgetSprites sprites) implements Border {
	public static final TexturedBorder SIMPLE = new TexturedBorder(new WidgetSprites(
			SpruceTextures.SIMPLE_BORDER_SPRITE,
			SpruceTextures.SIMPLE_HIGHLIGHTED_BORDER_SPRITE
	));

	@Override
	public void render(GuiGraphics graphics, SpruceWidget widget, int mouseX, int mouseY, float delta) {
		graphics.drawSprite(
				RenderType::guiTextured, this.sprites.get(widget.isActive(), widget.isFocusedOrHovered()),
				widget.getX(), widget.getY(),
				widget.getWidth(), widget.getHeight()
		);
	}

	@Override
	public int getThickness() {
		return 1;
	}
}
