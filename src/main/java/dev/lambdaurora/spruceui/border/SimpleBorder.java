/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.border;

import dev.lambdaurora.spruceui.util.ColorUtil;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.gui.GuiGraphics;

/**
 * Represents a simple solid border to draw around a widget.
 *
 * @author LambdAurora
 * @version 6.0.0
 * @since 6.0.0
 */
public final class SimpleBorder implements Border {
	public static final SimpleBorder SIMPLE_BORDER = new SimpleBorder(1, 192, 192, 192, 255);

	private final int thickness;
	private final int color;
	private final int focusedColor;

	public SimpleBorder(int thickness, int color) {
		this(thickness, color, color);
	}

	public SimpleBorder(int thickness, int color, int focusedColor) {
		this.thickness = thickness;
		this.color = color;
		this.focusedColor = color;
	}

	public SimpleBorder(int thickness, int red, int green, int blue, int alpha) {
		this(thickness, red, green, blue, alpha, red, green, blue, alpha);
	}

	public SimpleBorder(int thickness, int red, int green, int blue, int alpha, int focusedRed, int focusedGreen, int focusedBlue, int focusedAlpha) {
		this.thickness = thickness;
		this.color = ColorUtil.packARGBColor(red, green, blue, alpha);
		this.focusedColor = ColorUtil.packARGBColor(focusedRed, focusedGreen, focusedBlue, focusedAlpha);
	}

	@Override
	public void render(GuiGraphics graphics, SpruceWidget widget, int mouseX, int mouseY, float delta) {
		int x = widget.getX();
		int y = widget.getY();
		int right = x + widget.getWidth();
		int bottom = y + widget.getHeight();
		int color = widget.isFocused() ? this.focusedColor : this.color;
		// Top border
		graphics.fill(x, y, right, y + this.thickness, color);
		// Right border
		graphics.fill(right - this.thickness, y, right, bottom, color);
		// Bottom
		graphics.fill(x, bottom, right, bottom - this.thickness, color);
		// Left border
		graphics.fill(x, y, x + this.thickness, bottom, color);
	}

	@Override
	public int getThickness() {
		return this.thickness;
	}

	@Override
	public String toString() {
		return "SimpleBorder{" +
				"thickness=" + this.thickness +
				", color=" + Integer.toHexString(this.color) +
				", focusedColor=" + Integer.toHexString(this.focusedColor) +
				'}';
	}
}
