/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.tooltip.components;

import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import dev.lambdaurora.spruceui.util.ColorUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a text tooltip component.
 *
 * @param text the text of this tooltip component
 * @author LambdAurora
 * @version 8.0.0
 * @since 8.0.0
 */
public record ClientTextTooltipComponent(FormattedCharSequence text) implements SpruceClientTooltipComponent {
	@Override
	public int getWidth(Font font) {
		return font.width(this.text);
	}

	@Override
	public int getHeight(Font font) {
		return font.lineHeight + 1;
	}

	@Override
	public void renderText(@NotNull SpruceGuiGraphics graphics, @NotNull Font font, int i, int j) {
		graphics.drawText(font, this.text, i, j, ColorUtil.WHITE, true);
	}
}
