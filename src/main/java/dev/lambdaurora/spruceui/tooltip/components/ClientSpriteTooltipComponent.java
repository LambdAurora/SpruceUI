/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.tooltip.components;

import dev.lambdaurora.spruceui.SpruceTextAlignment;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

/**
 * Represents a sprite tooltip component.
 *
 * @param spriteId the sprite to display
 * @param width the width of the sprite
 * @param height the height of the sprite
 * @param alignment the alignement of the sprite if the tooltip is wider than the given width
 * @author LambdAurora
 * @version 8.0.0
 * @since 8.0.0
 */
public record ClientSpriteTooltipComponent(
		Identifier spriteId,
		int width, int height,
		SpruceTextAlignment alignment
) implements SpruceClientTooltipComponent {
	public ClientSpriteTooltipComponent(
			Identifier spriteId,
			int width, int height
	) {
		this(spriteId, width, height, SpruceTextAlignment.LEFT);
	}

	@Override
	public int getWidth(Font font) {
		return this.width;
	}

	@Override
	public int getHeight(Font font) {
		return this.height;
	}

	@Override
	public void renderImage(Font font, int x, int y, int width, int height, SpruceGuiGraphics graphics) {
		int actualX = switch (this.alignment) {
			case LEFT -> x;
			case CENTER -> x + (width / 2 - this.width / 2);
			case RIGHT -> x + width - this.width;
		};

		graphics.drawSprite(RenderPipelines.GUI_TEXTURED, this.spriteId, actualX, y, this.width, this.height);
	}
}
