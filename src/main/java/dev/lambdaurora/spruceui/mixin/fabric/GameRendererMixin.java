/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.mixin.fabric;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.lambdaurora.spruceui.impl.GameRendererPatch;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@WrapOperation(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screens/Screen;renderWithTooltip(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"
			)
	)
	private void spruceui$onRenderScreen$fabric(
			Screen currentScreen, GuiGraphics graphics, int mouseX, int mouseY, float tickDelta, Operation<Void> operation
	) {
		GameRendererPatch.onRender(currentScreen, graphics, mouseX, mouseY, tickDelta, operation);
	}
}
