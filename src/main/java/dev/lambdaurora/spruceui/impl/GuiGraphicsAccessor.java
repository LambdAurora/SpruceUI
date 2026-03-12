/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.impl;

import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface GuiGraphicsAccessor {
	SpruceGuiGraphics spruceui$spruced();

	GuiGraphicsExtractor.ScissorStack spruceui$getScissorStack();

	GuiRenderState spruceui$getGuiRenderState();
}
