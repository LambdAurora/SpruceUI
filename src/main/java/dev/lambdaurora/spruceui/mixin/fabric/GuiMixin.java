/*
 * Copyright © 2026 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.mixin.fabric;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.lambdaurora.spruceui.event.ResolutionChangeCallback;
import dev.lambdaurora.spruceui.event.ScreenEvents;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Represents the injection point for the {@link ScreenEvents} and {@link ResolutionChangeCallback} events.
 *
 * @author LambdAurora
 * @version 11.0.0
 * @since 11.0.0
 */
@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public class GuiMixin {
	@WrapOperation(
			method = "extractRenderState",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screens/Screen;extractRenderStateWithTooltipAndSubtitles(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V"
			)
	)
	private void spruceui$onRenderScreen(
			Screen currentScreen, GuiGraphicsExtractor graphics, int mouseX, int mouseY, float tickDelta, Operation<Void> operation
	) {
		var sprucedGraphics = SpruceGuiGraphics.of(graphics);

		ScreenEvents.BEFORE_EXTRACT.forContext(currentScreen).invoker()
				.onBeforeExtractScreen(currentScreen, sprucedGraphics, mouseX, mouseY, tickDelta);
		operation.call(currentScreen, graphics, mouseX, mouseY, tickDelta);
		ScreenEvents.AFTER_EXTRACT.forContext(currentScreen).invoker()
				.onAfterExtractScreen(currentScreen, sprucedGraphics, mouseX, mouseY, tickDelta);
	}
}
