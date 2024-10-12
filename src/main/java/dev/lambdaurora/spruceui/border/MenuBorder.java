/*
 * Copyright © 2024 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.border;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.lambdaurora.spruceui.SpruceTextures;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;

/**
 * Represents a typical menu border.
 *
 * @param top {@code true} if a top border is present, or {@code false} otherwise
 * @param right {@code true} if a right border is present, or {@code false} otherwise
 * @param bottom {@code true} if a bottom border is present, or {@code false} otherwise
 * @param left {@code true} if a left border is present, or {@code false} otherwise
 * @author LambdAurora
 * @version 5.1.0
 * @since 5.1.0
 */
public record MenuBorder(boolean top, boolean right, boolean bottom, boolean left) implements Border {
	private static final Minecraft CLIENT = Minecraft.getInstance();
	private static final int THICKNESS = 2;

	public static final MenuBorder LIST = new MenuBorder(true, false, true, false);
	public static final MenuBorder TAB_LIST = new MenuBorder(true, true, true, false);

	@Override
	public void render(GuiGraphics graphics, SpruceWidget widget, int mouseX, int mouseY, float delta) {
		RenderSystem.enableBlend();

		if (this.top) {
			Identifier topTexture = CLIENT.level == null ? SpruceTextures.MENU_TOP_BORDER : SpruceTextures.INWORLD_MENU_TOP_BORDER;

			int width = widget.getWidth();

			if (this.right) {
				width -= THICKNESS;
			}

			graphics.drawTexture(topTexture,
					widget.getX(), widget.getY(), 0, 0, width, THICKNESS, 32, THICKNESS
			);
		}

		if (this.top && this.right) {
			Identifier cornerTexture = CLIENT.level == null ? SpruceTextures.MENU_TOP_RIGHT_BORDER : SpruceTextures.INWORLD_MENU_TOP_RIGHT_BORDER;
			graphics.drawTexture(cornerTexture,
					widget.getEndX() - THICKNESS, widget.getY(), 0, 0, THICKNESS, THICKNESS, THICKNESS, THICKNESS
			);
		}

		if (this.right) {
			Identifier rightTexture = CLIENT.level == null ? SpruceTextures.MENU_RIGHT_BORDER : SpruceTextures.INWORLD_MENU_RIGHT_BORDER;

			int y = widget.getY();
			int height = widget.getHeight();

			if (this.top) {
				y += THICKNESS;
				height -= THICKNESS;
			}

			if (this.bottom) {
				height -= THICKNESS;
			}

			graphics.drawTexture(rightTexture,
					widget.getEndX() - THICKNESS, y, 0, 0, THICKNESS, height, THICKNESS, 32
			);
		}

		if (this.bottom && this.right) {
			Identifier cornerTexture = CLIENT.level == null
					? SpruceTextures.MENU_BOTTOM_RIGHT_BORDER : SpruceTextures.INWORLD_MENU_BOTTOM_RIGHT_BORDER;
			graphics.drawTexture(cornerTexture,
					widget.getEndX() - THICKNESS, widget.getEndY() - THICKNESS, 0, 0, THICKNESS, THICKNESS, THICKNESS, THICKNESS
			);
		}

		if (this.bottom) {
			Identifier bottomTexture = CLIENT.level == null ? SpruceTextures.MENU_BOTTOM_BORDER : SpruceTextures.INWORLD_MENU_BOTTOM_BORDER;

			int width = widget.getWidth();

			if (this.right) {
				width -= THICKNESS;
			}

			graphics.drawTexture(bottomTexture,
					widget.getX(), widget.getEndY() - THICKNESS, 0, 0, width, THICKNESS, 32, THICKNESS
			);
		}

		RenderSystem.disableBlend();
	}

	@Override
	public int getTop() {
		return this.top ? THICKNESS : 0;
	}

	@Override
	public int getRight() {
		return this.right ? THICKNESS : 0;
	}

	@Override
	public int getBottom() {
		return this.bottom ? THICKNESS : 0;
	}

	@Override
	public int getLeft() {
		return 0;
	}

	@Override
	public int getThickness() {
		return THICKNESS;
	}
}
