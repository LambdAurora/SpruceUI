/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.mixin;

import dev.lambdaurora.spruceui.event.OpenScreenCallback;
import dev.lambdaurora.spruceui.event.ResolutionChangeCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Represents the injection point for the {@link OpenScreenCallback} and {@link ResolutionChangeCallback} events.
 *
 * @author LambdAurora
 * @version 3.2.1
 * @since 1.2.0
 */
@Mixin(Minecraft.class)
public class MinecraftClientMixin {
	@Inject(method = "setScreen", at = @At("HEAD"))
	private void onScreenPre(Screen screen, CallbackInfo ci) {
		OpenScreenCallback.PRE.invoker().apply((Minecraft) (Object) this, screen);
	}

	@Inject(method = "setScreen", at = @At("RETURN"))
	private void onScreenChange(Screen screen, CallbackInfo ci) {
		OpenScreenCallback.EVENT.invoker().apply((Minecraft) (Object) this, screen);
	}

	@Inject(method = "resizeDisplay", at = @At("RETURN"))
	private void onResolutionChanged(CallbackInfo ci) {
		ResolutionChangeCallback.EVENT.invoker().apply((Minecraft) (Object) this);
	}
}
