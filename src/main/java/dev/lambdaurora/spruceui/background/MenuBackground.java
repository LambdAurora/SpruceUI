/*
 * Copyright © 2024 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.background;

import dev.lambdaurora.spruceui.SpruceTextures;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import dev.lambdaurora.spruceui.widget.WithBorder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.Identifier;

/**
 * Represents a background used for menus.
 *
 * @param texture the texture used for the background
 * @param inWorldTexture the textured used for the background when playing in a world
 * @author LambdAurora
 * @version 6.0.0
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
		int width = widget.getWidth();
		int height = widget.getHeight();

		if (widget instanceof WithBorder withBorder) {
			var border = withBorder.getBorder();

			x += border.getLeft();
			y += border.getTop();

			width -= border.getLeft() + border.getRight();
			height -= border.getTop() + border.getBottom();
		}

		Identifier identifier = CLIENT.level == null ? this.inWorldTexture : this.texture;
		graphics.drawTexture(
				RenderType::guiTextured, identifier,
				x, y,
				0, 0,
				width, height,
				16, 16
		);
	}
}
