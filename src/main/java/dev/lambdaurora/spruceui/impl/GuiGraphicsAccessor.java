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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.state.GuiRenderState;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface GuiGraphicsAccessor {
	SpruceGuiGraphics spruceui$spruced();

	GuiGraphics.ScissorStack spruceui$getScissorStack();

	GuiRenderState spruceui$getGuiRenderState();
}
