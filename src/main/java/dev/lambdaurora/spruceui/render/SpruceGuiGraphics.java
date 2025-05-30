/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.lambdaurora.spruceui.impl.GuiGraphicsAccessor;
import dev.lambdaurora.spruceui.render.state.ColoredRectangleRenderState;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Text;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;

/**
 * Represents a wrapper around {@link GuiGraphics} with extra features.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 8.0.0
 */
public final class SpruceGuiGraphics {
	private final GuiGraphics wrapped;

	public SpruceGuiGraphics(@NotNull GuiGraphics graphics) {
		this.wrapped = graphics;
	}

	public static SpruceGuiGraphics of(@NotNull GuiGraphics graphics) {
		return ((GuiGraphicsAccessor) graphics).spruceui$spruced();
	}

	/**
	 * {@return the wrapped vanilla GUI graphics object}
	 */
	public @NotNull GuiGraphics vanilla() {
		return this.wrapped;
	}

	private GuiGraphicsAccessor accessor() {
		return (GuiGraphicsAccessor) this.wrapped;
	}

	public int guiWidth() {
		return this.wrapped.guiWidth();
	}

	public int guiHeight() {
		return this.wrapped.guiHeight();
	}

	public @NotNull Matrix3x2fStack pose() {
		return this.wrapped.pose();
	}

	/**
	 * Enables the scissor area at the given coordinates.
	 *
	 * @param startX the start X-coordinate of the scissor area
	 * @param startY the start Y-coordinate of the scissor area
	 * @param endX the end X-coordinate of the scissor area
	 * @param endY the end Y-coordinate of the scissor area
	 * @see #disableScissor()
	 * @see #containsPointInScissor(int, int)
	 * @see GuiGraphics#enableScissor(int, int, int, int)
	 */
	public void enableScissor(int startX, int startY, int endX, int endY) {
		this.wrapped.enableScissor(startX, startY, endX, endY);
	}

	/**
	 * Disables the scissor area.
	 *
	 * @see #enableScissor(int, int, int, int)
	 * @see #containsPointInScissor(int, int)
	 * @see GuiGraphics#disableScissor()
	 */
	public void disableScissor() {
		this.wrapped.disableScissor();
	}

	/**
	 * {@return {@code true} if the given point is contained within the scissor area, or {@code false} otherwise}
	 *
	 * @param x the X-coordinate to check
	 * @param y the Y-coordinate to check
	 * @see #enableScissor(int, int, int, int)
	 * @see #disableScissor()
	 * @see GuiGraphics#containsPointInScissor(int, int)
	 */
	public boolean containsPointInScissor(int x, int y) {
		return this.wrapped.containsPointInScissor(x, y);
	}

	/**
	 * Fills a given area with the given color.
	 *
	 * @param startX the start X-coordinate
	 * @param startY the start Y-coordinate
	 * @param endX the end X-coordinate
	 * @param endY the end Y-coordinate
	 * @param color the ARGB color
	 * @see #fill(RenderPipeline, int, int, int, int, int)
	 */
	public void fill(int startX, int startY, int endX, int endY, int color) {
		this.fill(RenderPipelines.GUI, startX, startY, endX, endY, color);
	}

	/**
	 * Fills a given area with the given color.
	 *
	 * @param pipeline the render pipeline to use
	 * @param startX the start X-coordinate
	 * @param startY the start Y-coordinate
	 * @param endX the end X-coordinate
	 * @param endY the end Y-coordinate
	 * @param color the ARGB color
	 * @see #fill(int, int, int, int, int)
	 */
	public void fill(@NotNull RenderPipeline pipeline, int startX, int startY, int endX, int endY, int color) {
		if (startX < endX) {
			int i = startX;
			startX = endX;
			endX = i;
		}

		if (startY < endY) {
			int i = startY;
			startY = endY;
			endY = i;
		}

		this.submitColoredRectangle(
				pipeline, TextureSetup.noTexture(),
				startX, startY, endX, endY,
				color, null, null, null
		);
	}

	public void fillGradient(
			int startX, int startY, int endX, int endY,
			int colorTopLeft, int colorTopRight, int colorBottomRight, int colorBottomLeft
	) {
		this.fillGradient(
				RenderPipelines.GUI,
				startX, startY, endX, endY,
				colorTopLeft, colorTopRight, colorBottomRight, colorBottomLeft
		);
	}

	public void fillGradient(
			@NotNull RenderPipeline pipeline,
			int startX, int startY, int endX, int endY,
			int colorTopLeft, int colorTopRight, int colorBottomRight, int colorBottomLeft
	) {
		if (startX > endX) {
			int i = startX;
			startX = endX;
			endX = i;
		}

		if (startY > endY) {
			int i = startY;
			startY = endY;
			endY = i;
		}

		this.submitColoredRectangle(
				pipeline, TextureSetup.noTexture(),
				startX, startY, endX, endY,
				colorTopLeft, colorTopRight, colorBottomRight, colorBottomLeft
		);
	}

	private void submitColoredRectangle(
			RenderPipeline pipeline, TextureSetup textureSetup, int startX, int startY, int endX, int endY,
			int colorTopLeft, @Nullable Integer colorTopRight, @Nullable Integer colorBottomRight, @Nullable Integer colorBottomLeft
	) {
		this.submitGuiElement(
				new ColoredRectangleRenderState(
						pipeline, textureSetup, new Matrix3x2f(this.pose()),
						startX, startY, endX, endY,
						colorTopLeft, colorTopRight != null ? colorTopRight : colorTopLeft,
						colorBottomRight != null ? colorBottomRight : colorTopLeft,
						colorBottomLeft != null ? colorBottomLeft : colorTopLeft,
						this.accessor().spruceui$getScissorStack().peek()
				)
		);
	}

	public void drawSprite(
			@NotNull RenderPipeline pipeline, @NotNull Identifier sprite, int x, int y, int width, int height
	) {
		this.wrapped.drawSprite(pipeline, sprite, x, y, width, height);
	}

	public void drawSprite(
			@NotNull RenderPipeline pipeline, @NotNull Identifier sprite, int x, int y, int width, int height, int color
	) {
		this.wrapped.drawSprite(pipeline, sprite, x, y, width, height, color);
	}

	public void drawTexture(
			@NotNull RenderPipeline renderPipeline, @NotNull Identifier texture,
			int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight
	) {
		this.wrapped.drawTexture(renderPipeline, texture, x, y, u, v, width, height, width, height, textureWidth, textureHeight);
	}

	public void drawText(
			@NotNull Font font, @NotNull String text, int x, int y, int color, boolean shadow
	) {
		this.wrapped.drawText(font, text, x, y, color, shadow);
	}

	public void drawText(
			@NotNull Font font, @NotNull FormattedCharSequence text, int x, int y, int color, boolean shadow
	) {
		this.wrapped.drawText(font, text, x, y, color, shadow);
	}

	public void drawText(
			@NotNull Font font, @NotNull Text text, int x, int y, int color, boolean shadow
	) {
		this.wrapped.drawText(font, text, x, y, color, shadow);
	}

	public void drawShadowedText(
			@NotNull Font font, @NotNull String text, int x, int y, int color
	) {
		this.drawText(font, text, x, y, color, true);
	}

	public void drawShadowedText(
			@NotNull Font font, @NotNull FormattedCharSequence text, int x, int y, int color
	) {
		this.drawText(font, text, x, y, color, true);
	}

	public void drawShadowedText(
			@NotNull Font font, @NotNull Text text, int x, int y, int color
	) {
		this.drawText(font, text, x, y, color, true);
	}

	public void drawCenteredShadowedText(
			@NotNull Font font, @NotNull FormattedCharSequence text, int centerX, int y, int color
	) {
		this.wrapped.drawCenteredShadowedText(font, text, centerX, y, color);
	}

	public void drawCenteredShadowedText(
			@NotNull Font font, @NotNull Text text, int centerX, int y, int color
	) {
		this.wrapped.drawCenteredShadowedText(font, text, centerX, y, color);
	}

	public void submitGuiElement(@NotNull GuiElementRenderState state) {
		this.accessor().spruceui$getGuiRenderState().submitGuiElement(state);
	}
}
