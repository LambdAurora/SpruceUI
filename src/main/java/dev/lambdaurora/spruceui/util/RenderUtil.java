/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferRenderer;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.unmapped.C_fpcijbbg;

public final class RenderUtil {
	private RenderUtil() {
		throw new IllegalStateException("RenderUtil only contains static-definitions.");
	}

	/**
	 * Renders the dirt background texture.
	 *
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param width the width
	 * @param height the height
	 * @param vOffset the v offset
	 * @see #renderBackgroundTexture(int, int, int, int, float, int, int, int, int)
	 */
	public static void renderBackgroundTexture(int x, int y, int width, int height, float vOffset) {
		renderBackgroundTexture(x, y, width, height, vOffset, 64, 64, 64, 255);
	}

	/**
	 * Renders the dirt background texture.
	 *
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param width the width
	 * @param height the height
	 * @param vOffset the v offset
	 * @param red the red-component color value
	 * @param green the green-component color value
	 * @param blue the blue-component color value
	 * @param alpha the alpha-component alpha value
	 */
	public static void renderBackgroundTexture(int x, int y, int width, int height, float vOffset,
	                                           int red, int green, int blue, int alpha) {
		
	}

	/**
	 * Renders a selection box as background.
	 *
	 * @param x the X-coordinate of the selection box
	 * @param y the Y-coordinate of the selection box
	 * @param width the width of the selection box
	 * @param height the height of the selection box
	 * @param red the red-component color value of the outer border
	 * @param green the green-component color value of the outer border
	 * @param blue the blue-component color value of the outer border
	 * @param alpha the alpha-component color value of the outer border
	 */
	public static void renderSelectionBox(int x, int y, int width, int height, int red, int green, int blue, int alpha) {
		var tessellator = Tessellator.getInstance();
		var bufferBuilder = tessellator.method_60827(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

		int top = y + height;
		int right = x + width;

		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		RenderSystem.setShaderColor(red / 255.f, green / 255.f, blue / 255.f, alpha / 255.f);
		bufferBuilder.method_22912(x, top, 0);
		bufferBuilder.method_22912(right, top, 0);
		bufferBuilder.method_22912(right, y, 0);
		bufferBuilder.method_22912(x, y, 0);
		C_fpcijbbg builtBuffer = bufferBuilder.method_60794();
		if (builtBuffer != null) {
			BufferRenderer.drawWithShader(builtBuffer);
		}
		RenderSystem.setShaderColor(0, 0, 0, 1.f);
		bufferBuilder.method_22912(x + 1, top - 1, 0);
		bufferBuilder.method_22912(right - 1, top - 1, 0);
		bufferBuilder.method_22912(right - 1, y + 1, 0);
		bufferBuilder.method_22912(x + 1, y + 1, 0);
		builtBuffer = bufferBuilder.method_60794();
		if (builtBuffer != null) {
			BufferRenderer.drawWithShader(builtBuffer);
		}
		tessellator.method_60828();
	}
}
