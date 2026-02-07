/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.lambdaurora.spruceui.event.ResolutionChangeCallback;
import dev.lambdaurora.spruceui.event.ScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
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
 * @version 8.0.0
 * @since 1.2.0
 */
@Mixin(Minecraft.class)
public class MinecraftClientMixin {
	@Shadow
	@Nullable
	public Screen screen;

	@Unique
	private Screen tickingScreen;

	@Inject(method = "resizeDisplay", at = @At("RETURN"))
	private void onResolutionChanged(CallbackInfo ci) {
		ResolutionChangeCallback.EVENT.invoker().apply((Minecraft) (Object) this);
	}

	@Inject(
			method = "setScreen",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;removed()V", shift = At.Shift.AFTER)
	)
	private void spruceui$onScreenRemove(@Nullable Screen screen, CallbackInfo ci) {
		assert this.screen != null;
		ScreenEvents.REMOVE.forContext(this.screen).invoker().onRemoveScreen(this.screen);
	}

	@Inject(
			method = "destroy",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;removed()V", shift = At.Shift.AFTER)
	)
	private void spruceui$onScreenRemoveBecauseStopping(CallbackInfo ci) {
		assert this.screen != null;
		ScreenEvents.REMOVE.forContext(this.screen).invoker().onRemoveScreen(this.screen);
	}

	@WrapOperation(
			method = "tick",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;tick()V")
	)
	private void spruceui$onScreenTick(Screen instance, Operation<Void> original) {
		ScreenEvents.BEFORE_TICK.forContext(instance).invoker().onBeforeTickScreen(instance);
		original.call(instance);
		ScreenEvents.AFTER_TICK.forContext(instance).invoker().onAfterTickScreen(instance);
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
