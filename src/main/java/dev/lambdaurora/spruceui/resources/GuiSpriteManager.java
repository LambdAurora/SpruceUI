/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.resources;

import com.mojang.logging.LogUtils;
import dev.lambdaurora.spruceui.SpruceUI;
import dev.lambdaurora.spruceui.util.ColorUtil;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.io.ResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.Profiler;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Represents the SpruceUI GUI sprite manager.
 * <p>
 * <b>Important Note:</b> this exists to handle missing functionality in 1.20.1,
 * which is now integrated within Minecraft in newer versions.
 * You should not rely on this class for future versions and instead directly use {@link GuiGraphics}.
 */
public final class GuiSpriteManager implements IdentifiableResourceReloadListener {
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final GuiSpriteManager INSTANCE = new GuiSpriteManager();

	private final Map<Identifier, GuiSpriteScaling> metadata = new HashMap<>();
	private ResourceManager resourceManager;

	public static GuiSpriteManager get() {
		return INSTANCE;
	}

	public GuiSpriteScaling getGuiSpriteScaling(Identifier id) {
		var existing = this.metadata.get(id);

		if (existing == null) {
			existing = this.loadGuiSpriteScaling(id);
		}

		return existing;
	}

	private GuiSpriteScaling loadGuiSpriteScaling(Identifier id) {
		var result = this.resourceManager.getResource(id)
				.map(resource -> {
					try {
						return resource.metadata();
					} catch (IOException e) {
						LOGGER.info("Failed to read metadata of {}.", id, e);
						return null;
					}
				})
				.flatMap(metadata -> metadata.getSection(GuiMetadataSection.TYPE))
				.map(GuiMetadataSection::scaling)
				.orElse(GuiSpriteScaling.DEFAULT);

		this.metadata.put(id, result);
		return result;
	}

	public void drawSprite(GuiGraphics graphics, Identifier id, int x, int y, int width, int height) {
		this.drawSprite(graphics, id, x, y, width, height, ColorUtil.WHITE);
	}

	public void drawSprite(GuiGraphics graphics, Identifier id, int x, int y, int width, int height, int color) {
		var guiSpriteScaling = this.getGuiSpriteScaling(id);

		if (guiSpriteScaling instanceof GuiSpriteScaling.Stretch) {
			if (width != 0 && height != 0) {
				this.drawTexturedQuad(graphics, id,
						x, x + width,
						y, y + height,
						0.f, 1.f,
						0.f, 1.f,
						color
				);
			}
		} else if (guiSpriteScaling instanceof GuiSpriteScaling.Tile tile) {
			this.drawTiledSprite(graphics, id,
					x, y,
					width, height,
					0, 0,
					tile.width(), tile.height(),
					tile.width(), tile.height(),
					color
			);
		} else if (guiSpriteScaling instanceof GuiSpriteScaling.NineSlice nineSlice) {
			this.drawNineSlicedSprite(graphics, id, nineSlice, x, y, width, height, color);
		}
	}

	private void drawTiledSprite(
			GuiGraphics graphics,
			Identifier id,
			int x,
			int y,
			int width,
			int height,
			int u,
			int v,
			int tileWidth,
			int tileHeight,
			int textureWidth,
			int textureHeight,
			int color
	) {
		if (width > 0 && height > 0) {
			if (tileWidth > 0 && tileHeight > 0) {
				for (int tileX = 0; tileX < width; tileX += tileWidth) {
					int currentTileWidth = Math.min(tileWidth, width - tileX);

					for (int tileY = 0; tileY < height; tileY += tileHeight) {
						int currentTileHeight = Math.min(tileHeight, height - tileY);
						this.drawSpriteDirect(graphics, id,
								textureWidth, textureHeight,
								u, v,
								x + tileX, y + tileY,
								currentTileWidth, currentTileHeight,
								color
						);
					}
				}
			} else {
				throw new IllegalArgumentException("Tiled sprite texture size must be positive, got " + tileWidth + "x" + tileHeight);
			}
		}
	}

	private void drawNineSlicedSprite(
			GuiGraphics graphics, Identifier id, GuiSpriteScaling.NineSlice data, int x, int y, int width, int height, int color
	) {
		var border = data.border();
		int borderLeft = Math.min(border.left(), width / 2);
		int borderRight = Math.min(border.right(), width / 2);
		int borderTop = Math.min(border.top(), height / 2);
		int borderBottom = Math.min(border.bottom(), height / 2);

		if (width == data.width() && height == data.height()) {
			this.drawSpriteDirect(graphics, id, data.width(), data.height(), 0, 0, x, y, width, height, color);
		} else if (height == data.height()) {
			this.drawSpriteDirect(graphics, id, data.width(), data.height(), 0, 0, x, y, borderLeft, height, color);
			this.drawNineSliceInnerSegment(graphics, data, id,
					x + borderLeft, y,
					width - borderRight - borderLeft, height,
					borderLeft, 0,
					data.width() - borderRight - borderLeft, data.height(),
					data.width(), data.height(),
					color
			);
			this.drawSpriteDirect(graphics, id,
					data.width(), data.height(),
					data.width() - borderRight, 0,
					x + width - borderRight, y,
					borderRight, height,
					color
			);
		} else if (width == data.width()) {
			this.drawSpriteDirect(graphics, id, data.width(), data.height(), 0, 0, x, y, width, borderTop, color);
			this.drawNineSliceInnerSegment(graphics, data, id,
					x, y + borderTop,
					width, height - borderBottom - borderTop,
					0, borderTop,
					data.width(), data.height() - borderBottom - borderTop,
					data.width(), data.height(),
					color
			);
			this.drawSpriteDirect(graphics, id,
					data.width(), data.height(),
					0, data.height() - borderBottom,
					x, y + height - borderBottom,
					width, borderBottom,
					color
			);
		} else {
			this.drawSpriteDirect(graphics, id, data.width(), data.height(), 0, 0, x, y, borderLeft, borderTop, color);
			this.drawNineSliceInnerSegment(graphics, data, id,
					x + borderLeft, y,
					width - borderRight - borderLeft, borderTop,
					borderLeft, 0,
					data.width() - borderRight - borderLeft, borderTop,
					data.width(), data.height(),
					color
			);
			this.drawSpriteDirect(graphics, id,
					data.width(), data.height(),
					data.width() - borderRight, 0,
					x + width - borderRight, y,
					borderRight, borderTop,
					color
			);
			this.drawSpriteDirect(graphics, id,
					data.width(), data.height(),
					0, data.height() - borderBottom,
					x, y + height - borderBottom,
					borderLeft, borderBottom,
					color
			);
			this.drawNineSliceInnerSegment(graphics, data, id,
					x + borderLeft, y + height - borderBottom,
					width - borderRight - borderLeft, borderBottom,
					borderLeft, data.height() - borderBottom,
					data.width() - borderRight - borderLeft, borderBottom,
					data.width(), data.height(),
					color
			);
			this.drawSpriteDirect(graphics, id,
					data.width(), data.height(),
					data.width() - borderRight, data.height() - borderBottom,
					x + width - borderRight, y + height - borderBottom,
					borderRight, borderBottom,
					color
			);
			this.drawNineSliceInnerSegment(
					graphics, data, id,
					x, y + borderTop,
					borderLeft, height - borderBottom - borderTop,
					0, borderTop,
					borderLeft, data.height() - borderBottom - borderTop,
					data.width(), data.height(),
					color
			);
			this.drawNineSliceInnerSegment(
					graphics, data, id,
					x + borderLeft,
					y + borderTop,
					width - borderRight - borderLeft,
					height - borderBottom - borderTop,
					borderLeft,
					borderTop,
					data.width() - borderRight - borderLeft,
					data.height() - borderBottom - borderTop,
					data.width(), data.height(),
					color
			);
			this.drawNineSliceInnerSegment(
					graphics, data, id,
					x + width - borderRight, y + borderTop,
					borderRight, height - borderBottom - borderTop,
					data.width() - borderRight, borderTop,
					borderRight, data.height() - borderBottom - borderTop,
					data.width(), data.height(),
					color
			);
		}
	}

	private void drawNineSliceInnerSegment(
			GuiGraphics graphics,
			GuiSpriteScaling.NineSlice data,
			Identifier id,
			int x,
			int y,
			int width,
			int height,
			int u,
			int v,
			int sliceWidth,
			int sliceHeight,
			int textureWidth,
			int textureHeight,
			int color
	) {
		if (width > 0 && height > 0) {
			if (data.stretchInner()) {
				this.drawTexturedQuad(graphics,
						id,
						x, x + width,
						y, y + height,
						(float) u / (float) textureWidth, (float) (u + sliceWidth) / (float) textureWidth,
						(float) v / (float) textureHeight, (float) (v + sliceHeight) / (float) textureHeight,
						color
				);
			} else {
				this.drawTiledSprite(
						graphics, id, x, y, width, height, u, v, sliceWidth, sliceHeight, textureWidth, textureHeight, color
				);
			}
		}
	}

	private void drawSpriteDirect(
			GuiGraphics graphics, Identifier id,
			int textureWidth, int textureHeight,
			int u, int v,
			int x, int y, int width, int height,
			int color
	) {
		if (width != 0 && height != 0) {
			this.drawTexturedQuad(graphics,
					id,
					x, x + width,
					y, y + height,
					(float) u / (float) textureWidth, (float) (u + width) / (float) textureWidth,
					(float) v / (float) textureHeight, (float) (v + height) / (float) textureHeight,
					color
			);
		}
	}

	private void drawTexturedQuad(
			GuiGraphics graphics, Identifier id,
			int startX, int endX,
			int startY, int endY,
			float u1, float u2,
			float v1, float v2,
			int color
	) {
		graphics.drawTexturedQuad(
				id,
				startX, endX,
				startY, endY,
				0,
				u1, u2,
				v1, v2,
				ColorUtil.argbUnpackRed(color) / 255.f,
				ColorUtil.argbUnpackGreen(color) / 255.f,
				ColorUtil.argbUnpackBlue(color) / 255.f,
				ColorUtil.argbUnpackAlpha(color) / 255.f
		);
	}

	@Override
	public Identifier getFabricId() {
		return SpruceUI.id("gui_sprite_manager");
	}

	@Override
	public CompletableFuture<Void> reload(
			Synchronizer synchronizer,
			ResourceManager resourceManager,
			Profiler prepareProfiler, Profiler applyProfiler,
			Executor prepareExecutor, Executor applyExecutor
	) {
		this.resourceManager = resourceManager;
		return synchronizer.whenPrepared(Unit.INSTANCE).thenRunAsync(this.metadata::clear);
	}
}
