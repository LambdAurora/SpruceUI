/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.impl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.lambdaurora.spruceui.event.ScreenEvents;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a mod-loader-agnostic patch to {@link net.minecraft.client.renderer.GameRenderer}.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 8.0.0
 */
@ApiStatus.Internal
public final class GameRendererPatch {
	private GameRendererPatch() {
		throw new UnsupportedOperationException("GameRendererPatch only contains static definitions.");
	}

	public static void onRender(Screen currentScreen, GuiGraphics graphics, int mouseX, int mouseY, float tickDelta, Operation<Void> operation) {
		var sprucedGraphics = SpruceGuiGraphics.of(graphics);

		ScreenEvents.BEFORE_RENDER.forContext(currentScreen).invoker()
				.onBeforeRenderScreen(currentScreen, sprucedGraphics, mouseX, mouseY, tickDelta);
		operation.call(currentScreen, graphics, mouseX, mouseY, tickDelta);
		ScreenEvents.AFTER_RENDER.forContext(currentScreen).invoker()
				.onAfterRenderScreen(currentScreen, sprucedGraphics, mouseX, mouseY, tickDelta);
	}
}
