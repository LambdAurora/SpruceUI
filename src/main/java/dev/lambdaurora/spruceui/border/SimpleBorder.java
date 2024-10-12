/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.border;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.lambdaurora.spruceui.util.ColorUtil;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;

import java.util.Arrays;

/**
 * Represents a simple solid border to draw around a widget.
 *
 * @author LambdAurora
 * @version 5.0.0
 * @since 2.0.0
 */
public final class SimpleBorder implements Border {
	public static final SimpleBorder SIMPLE_BORDER = new SimpleBorder(1, 192, 192, 192, 255);

	private final int thickness;
	private final int[] color;
	private final int[] focusedColor;

	public SimpleBorder(int thickness, int color) {
		this(thickness, color, color);
	}

	public SimpleBorder(int thickness, int color, int focusedColor) {
		this.thickness = thickness;
		this.color = ColorUtil.unpackARGBColor(color);
		this.focusedColor = ColorUtil.unpackARGBColor(focusedColor);
	}

	public SimpleBorder(int thickness, int red, int green, int blue, int alpha) {
		this(thickness, red, green, blue, alpha, red, green, blue, alpha);
	}

	public SimpleBorder(int thickness, int red, int green, int blue, int alpha, int focusedRed, int focusedGreen, int focusedBlue, int focusedAlpha) {
		this.thickness = thickness;
		this.color = new int[]{red, green, blue, alpha};
		this.focusedColor = new int[]{focusedRed, focusedGreen, focusedBlue, focusedAlpha};
	}

	@Override
	public void render(GuiGraphics graphics, SpruceWidget widget, int mouseX, int mouseY, float delta) {
		var tessellator = Tessellator.getInstance();
		var buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		int x = widget.getX();
		int y = widget.getY();
		int right = x + widget.getWidth();
		int bottom = y + widget.getHeight();
		boolean focused = widget.isFocused();
		// Top border
		this.vertex(buffer, x, y + this.thickness, focused);
		this.vertex(buffer, right, y + this.thickness, focused);
		this.vertex(buffer, right, y, focused);
		this.vertex(buffer, x, y, focused);
		// Right border
		this.vertex(buffer, right - this.thickness, bottom, focused);
		this.vertex(buffer, right, bottom, focused);
		this.vertex(buffer, right, y, focused);
		this.vertex(buffer, right - this.thickness, y, focused);
		// Bottom
		this.vertex(buffer, x, bottom, focused);
		this.vertex(buffer, right, bottom, focused);
		this.vertex(buffer, right, bottom - this.thickness, focused);
		this.vertex(buffer, x, bottom - this.thickness, focused);
		// Left border
		this.vertex(buffer, x, bottom, focused);
		this.vertex(buffer, x + this.thickness, bottom, focused);
		this.vertex(buffer, x + this.thickness, y, focused);
		this.vertex(buffer, x, y, focused);
		MeshData builtBuffer = buffer.build();
		if (builtBuffer != null) {
			BufferUploader.drawWithShader(builtBuffer);
		}
		tessellator.clear();
	}

	private void vertex(BufferBuilder buffer, int x, int y, boolean focused) {
		int[] color = focused ? this.focusedColor : this.color;
		buffer.addVertex(x, y, 0).color(color[0], color[1], color[2], color[3]);
	}

	@Override
	public int getThickness() {
		return this.thickness;
	}

	@Override
	public String toString() {
		return "SimpleBorder{" +
				"thickness=" + this.thickness +
				", color=" + Arrays.toString(this.color) +
				", focusedColor=" + Arrays.toString(this.focusedColor) +
				'}';
	}
}
