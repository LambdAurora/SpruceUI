/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.mixin;

import dev.lambdaurora.spruceui.impl.GuiGraphicsAccessor;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsExtractorMixin implements GuiGraphicsAccessor {
	@Unique
	private final SpruceGuiGraphics spruce$graphics = new SpruceGuiGraphics((GuiGraphicsExtractor) (Object) this);

	@Shadow
	@Final
	public GuiGraphicsExtractor.ScissorStack scissorStack;
	@Shadow
	@Final
	public GuiRenderState guiRenderState;

	@Override
	public SpruceGuiGraphics spruceui$spruced() {
		return this.spruce$graphics;
	}

	@Override
	public GuiGraphicsExtractor.ScissorStack spruceui$getScissorStack() {
		return this.scissorStack;
	}

	@Override
	public GuiRenderState spruceui$getGuiRenderState() {
		return this.guiRenderState;
	}
}
