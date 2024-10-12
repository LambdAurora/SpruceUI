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
import com.mojang.blaze3d.vertex.*;
import dev.lambdaurora.spruceui.SpruceTextures;
import net.minecraft.client.renderer.GameRenderer;

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
		var tessellator = Tessellator.getInstance();
		var bufferBuilder = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		RenderSystem.setShaderColor(1.f, 1.f, 1.f, 1.f);
		RenderSystem.setShaderTexture(0, SpruceTextures.LEGACY_OPTIONS_BACKGROUND);

		int right = x + width;
		int bottom = y + height;

		bufferBuilder.addVertex(x, bottom, 0)
				.uv(0, bottom / 32.f + vOffset)
				.color(red, green, blue, alpha);
		bufferBuilder.addVertex(right, bottom, 0)
				.uv(right / 32.f, bottom / 32.f + vOffset)
				.color(red, green, blue, alpha);
		bufferBuilder.addVertex(right, y, 0)
				.uv(right / 32.f, y / 32.f + vOffset)
				.color(red, green, blue, alpha);
		bufferBuilder.addVertex(x, y, 0)
				.uv(0, y / 32.f + vOffset)
				.color(red, green, blue, alpha);
		MeshData builtBuffer = bufferBuilder.build();
		if (builtBuffer != null) {
			BufferUploader.drawWithShader(builtBuffer);
		}
		tessellator.clear();
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
		var bufferBuilder = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

		int top = y + height;
		int right = x + width;

		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		RenderSystem.setShaderColor(red / 255.f, green / 255.f, blue / 255.f, alpha / 255.f);
		bufferBuilder.addVertex(x, top, 0);
		bufferBuilder.addVertex(right, top, 0);
		bufferBuilder.addVertex(right, y, 0);
		bufferBuilder.addVertex(x, y, 0);
		MeshData builtBuffer = bufferBuilder.build();
		if (builtBuffer != null) {
			BufferUploader.drawWithShader(builtBuffer);
		}
		RenderSystem.setShaderColor(0, 0, 0, 1.f);
		bufferBuilder.addVertex(x + 1, top - 1, 0);
		bufferBuilder.addVertex(right - 1, top - 1, 0);
		bufferBuilder.addVertex(right - 1, y + 1, 0);
		bufferBuilder.addVertex(x + 1, y + 1, 0);
		builtBuffer = bufferBuilder.build();
		if (builtBuffer != null) {
			BufferUploader.drawWithShader(builtBuffer);
		}
		tessellator.clear();
	}
}
