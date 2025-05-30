/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.render.state;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

/**
 * Represents a render state of a colored rectangle.
 * <p>
 * This is similar to Vanilla's {@link net.minecraft.client.gui.render.state.ColoredRectangleRenderState},
 * but with better control of each corner's colors.
 *
 * @param pipeline the render pipeline
 * @param textureSetup the texture setup
 * @param pose the pose of this rectangle
 * @param startX the start X-coordinate of this rectangle
 * @param startY the start Y-coordinate of this rectangle
 * @param endX the end X-coordinate of this rectangle
 * @param endY the end Y-coordinate of this rectangle
 * @param colorTopLeft the color of the top left corner
 * @param colorTopRight the color of the top right corner
 * @param colorBottomRight the color of the bottom right corner
 * @param colorBottomLeft the color of the bottom left corner
 * @param scissorArea the scissor area
 * @param bounds the bounds of this rectangle
 *
 * @version 8.0.0
 * @since 8.0.0
 * @author LambdAurora
 */
@Environment(EnvType.CLIENT)
public record ColoredRectangleRenderState(
		RenderPipeline pipeline, TextureSetup textureSetup,
		Matrix3x2f pose,
		int startX, int startY,
		int endX, int endY,
		int colorTopLeft, int colorTopRight,
		int colorBottomRight, int colorBottomLeft,
		@Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds
) implements GuiElementRenderState {
	public ColoredRectangleRenderState(
			RenderPipeline pipeline, TextureSetup textureSetup,
			Matrix3x2f pose,
			int startX, int startY,
			int endX, int endY,
			int colorTopLeft, int colorTopRight,
			int colorBottomRight, int colorBottomLeft,
			@Nullable ScreenRectangle scissorArea
	) {
		this(
				pipeline, textureSetup,
				pose,
				startX, startY,
				endX, endY,
				colorTopLeft, colorTopRight,
				colorBottomRight, colorBottomLeft,
				scissorArea, getBounds(startX, startY, endX, endY, pose, scissorArea)
		);
	}

	@Override
	public void buildVertices(VertexConsumer vertexConsumer, float z) {
		vertexConsumer.addVertexWith2DPose(this.pose(), this.startX(), this.startY(), z).color(this.colorTopLeft());
		vertexConsumer.addVertexWith2DPose(this.pose(), this.startX(), this.endY(), z).color(this.colorBottomLeft());
		vertexConsumer.addVertexWith2DPose(this.pose(), this.endX(), this.endY(), z).color(this.colorBottomRight());
		vertexConsumer.addVertexWith2DPose(this.pose(), this.endX(), this.startY(), z).color(this.colorTopRight());
	}

	@Nullable
	private static ScreenRectangle getBounds(
			int startX, int startY, int endX, int endY, Matrix3x2f pose, @Nullable ScreenRectangle scissorArea
	) {
		var defaultBounds = new ScreenRectangle(startX, startY, endX - startX, endY - startY)
				.transformMaxBounds(pose);
		return scissorArea != null ? scissorArea.intersection(defaultBounds) : defaultBounds;
	}
}