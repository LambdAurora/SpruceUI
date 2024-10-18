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
import net.minecraft.client.renderer.CoreShaders;

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
		RenderSystem.setShader(CoreShaders.POSITION_TEX_COLOR);
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
}
