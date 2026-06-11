/*
 * Copyright © 2026 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.lambdaurora.spruceui.event.ResolutionChangeCallback;
import dev.lambdaurora.spruceui.event.ScreenEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
	@Shadow
	@Nullable
	private Screen screen;

	@Inject(
			method = "setScreen",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;removed()V", shift = At.Shift.AFTER)
	)
	private void spruceui$onScreenRemove(@Nullable Screen screen, CallbackInfo ci) {
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
}
