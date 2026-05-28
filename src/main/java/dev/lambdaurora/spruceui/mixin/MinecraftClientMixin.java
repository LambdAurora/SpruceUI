/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.lambdaurora.spruceui.event.ResolutionChangeCallback;
import dev.lambdaurora.spruceui.event.ScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Represents the injection point for the {@link ScreenEvents} and {@link ResolutionChangeCallback} events.
 *
 * @author LambdAurora
 * @version 11.0.0
 * @since 1.2.0
 */
@Mixin(Minecraft.class)
public class MinecraftClientMixin {
	@Shadow
	@Final
	public Gui gui;
	@Unique
	private Screen tickingScreen;

	@Inject(method = "resizeGui", at = @At("RETURN"))
	private void onResolutionChanged(CallbackInfo ci) {
		ResolutionChangeCallback.EVENT.invoker().apply((Minecraft) (Object) this);
	}

	@Inject(
			method = "exitWorldAndClose",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;removed()V", shift = At.Shift.AFTER)
	)
	private void spruceui$onScreenRemoveBecauseStopping(CallbackInfo ci) {
		var screen = this.gui.screen();
		assert screen != null;
		ScreenEvents.REMOVE.forContext(screen).invoker().onRemoveScreen(screen);
	}

	// For some reason LevelLoadingScreen isn't ticked by the main tick loop,
	// so for proper handling it needs to be handled separately.
	@Inject(
			method = "doWorldLoad",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/LevelLoadingScreen;tick()V")
	)
	private void spruceui$onBeforeLoadingScreenTick(CallbackInfo ci, @Local LevelLoadingScreen screen) {
		// Since the injection spans two invocations the current screen is stored in a variable in case
		// the current running tick changes the current screen, which would make the screen in the after tick invalid.
		this.tickingScreen = screen;
		ScreenEvents.BEFORE_TICK.forContext(this.tickingScreen).invoker().onBeforeTickScreen(this.tickingScreen);
	}

	@Inject(
			method = "doWorldLoad",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/LevelLoadingScreen;tick()V", shift = At.Shift.AFTER)
	)
	private void spruceui$onAfterLoadingScreenTick(CallbackInfo ci) {
		ScreenEvents.AFTER_TICK.forContext(this.tickingScreen).invoker().onAfterTickScreen(this.tickingScreen);
		this.tickingScreen = null;
	}
}
