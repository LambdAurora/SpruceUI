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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.state.GuiRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin implements GuiGraphicsAccessor {
	@Unique
	private final SpruceGuiGraphics spruce$graphics = new SpruceGuiGraphics((GuiGraphics) (Object) this);

	@Shadow
	@Final
	private GuiGraphics.ScissorStack scissorStack;
	@Shadow
	@Final
	private GuiRenderState guiRenderState;

	@Override
	public SpruceGuiGraphics spruceui$spruced() {
		return this.spruce$graphics;
	}

	@Override
	public GuiGraphics.ScissorStack spruceui$getScissorStack() {
		return this.scissorStack;
	}

	@Override
	public GuiRenderState spruceui$getGuiRenderState() {
		return this.guiRenderState;
	}
}
