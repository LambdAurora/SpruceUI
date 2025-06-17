/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.lambdaurora.spruceui.event.ScreenEvents;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@SuppressWarnings({"MixinAnnotationTarget"})
	@Dynamic
	@WrapOperation(
			method = "render",
			at = {
					@At(
							value = "INVOKE",
							target = "Lnet/minecraft/client/gui/screens/Screen;renderWithTooltip(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"
					),
					@At(
							value = "INVOKE",
							target = "Lnet/neoforged/neoforge/client/ClientHooks;drawScreen(Lnet/minecraft/client/gui/screens/Screen;Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
							remap = false
					),
			},
			require = 1,
			allow = 1
	)
	private void spruceui$onRenderScreen(
			Screen currentScreen, GuiGraphics graphics, int mouseX, int mouseY, float tickDelta, Operation<Void> operation
	) {
		var sprucedGraphics = SpruceGuiGraphics.of(graphics);

		ScreenEvents.BEFORE_RENDER.forContext(currentScreen).invoker()
				.onBeforeRenderScreen(currentScreen, sprucedGraphics, mouseX, mouseY, tickDelta);
		operation.call(currentScreen, graphics, mouseX, mouseY, tickDelta);
		ScreenEvents.AFTER_RENDER.forContext(currentScreen).invoker()
				.onAfterRenderScreen(currentScreen, sprucedGraphics, mouseX, mouseY, tickDelta);
	}
}
