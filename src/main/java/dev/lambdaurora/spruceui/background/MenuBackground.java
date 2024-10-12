/*
 * Copyright © 2024 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.background;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.lambdaurora.spruceui.SpruceTextures;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import dev.lambdaurora.spruceui.widget.WithBorder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;

/**
 * Represents a background used for menus.
 *
 * @param texture the texture used for the background
 * @param inWorldTexture the textured used for the background when playing in a world
 * @author LambdAurora
 * @version 5.1.0
 * @since 5.1.0
 */
public record MenuBackground(Identifier texture, Identifier inWorldTexture) implements Background {
	private static final Minecraft CLIENT = Minecraft.getInstance();

	public static final MenuBackground MENU_LIST = new MenuBackground(
			SpruceTextures.MENU_LIST_BACKGROUND,
			SpruceTextures.INWORLD_MENU_LIST_BACKGROUND
	);

	@Override
	public void render(GuiGraphics graphics, SpruceWidget widget, int vOffset, int mouseX, int mouseY, float delta) {
		int x = widget.getX();
		int y = widget.getY();
		int endX = widget.getEndX();
		int endY = widget.getEndY();
		int width = widget.getWidth();
		int height = widget.getHeight();

		if (widget instanceof WithBorder withBorder) {
			var border = withBorder.getBorder();

			x += border.getLeft();
			y += border.getTop();
			endX -= border.getRight();
			endY -= border.getBottom();

			width -= border.getLeft() + border.getRight();
			height -= border.getTop() + border.getBottom();
		}

		RenderSystem.enableBlend();
		Identifier identifier = CLIENT.level == null ? this.inWorldTexture : this.texture;
		graphics.drawTexture(
				identifier,
				x, y,
				endX, endY,
				width, height,
				32, 32
		);
		RenderSystem.disableBlend();
	}
}
