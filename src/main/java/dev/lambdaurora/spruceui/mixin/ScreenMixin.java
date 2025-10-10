/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.mixin;

import dev.lambdaurora.spruceui.event.ScreenEvents;
import dev.yumi.commons.event.ContextualizedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {
	@Unique
	private final ContextualizedEvent<Identifier, ScreenEvents.BeforeInit, Screen> spruceui$beforeInitEvent
			= ScreenEvents.BEFORE_INIT.forContext(this.$self());
	@Unique
	private final ContextualizedEvent<Identifier, ScreenEvents.AfterInit, Screen> spruceui$afterInitEvent
			= ScreenEvents.AFTER_INIT.forContext(this.$self());
	@Unique
	@SuppressWarnings("unused") // The reference MUST be kept alive until the Screen dies.
	private ContextualizedEvent<Identifier, ScreenEvents.Remove, Screen> spruceui$removeEvent
			= ScreenEvents.REMOVE.forContext(this.$self());
	@Unique
	@SuppressWarnings("unused") // The reference MUST be kept alive until the Screen dies.
	private ContextualizedEvent<Identifier, ScreenEvents.BeforeRender, Screen> spruceui$beforeRenderEvent
			= ScreenEvents.BEFORE_RENDER.forContext(this.$self());
	@Unique
	@SuppressWarnings("unused") // The reference MUST be kept alive until the Screen dies.
	private ContextualizedEvent<Identifier, ScreenEvents.AfterRender, Screen> spruceui$afterRenderEvent
			= ScreenEvents.AFTER_RENDER.forContext(this.$self());
	@Unique
	@SuppressWarnings("unused") // The reference MUST be kept alive until the Screen dies.
	private ContextualizedEvent<Identifier, ScreenEvents.BeforeTick, Screen> spruceui$beforeTickEvent
			= ScreenEvents.BEFORE_TICK.forContext(this.$self());
	@Unique
	@SuppressWarnings("unused") // The reference MUST be kept alive until the Screen dies.
	private ContextualizedEvent<Identifier, ScreenEvents.AfterTick, Screen> spruceui$afterTickEvent
			= ScreenEvents.AFTER_TICK.forContext(this.$self());

	@Shadow
	protected abstract <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget);

	@Shadow
	@Final
	protected Minecraft client;

	public ScreenMixin() {
	}

	@Inject(
			method = "init(II)V",
			at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/Screen;initialized:Z", ordinal = 0)
	)
	public void spruceui$beforeInit(int width, int height, CallbackInfo ci) {
		this.spruceui$handleBeforeInit(this.client, width, height);
	}

	@Inject(method = "init(II)V", at = @At("TAIL"))
	public void spruceui$afterInit(int width, int height, CallbackInfo ci) {
		this.spruceui$afterInitEvent.invoker().afterInitScreen(this.spruceui$createInitContext(this.client, width, height));
	}

	@Inject(
			method = "resize",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;repositionElements()V")
	)
	private void spruceui$beforeResizeScreen(int width, int height, CallbackInfo ci) {
		this.spruceui$handleBeforeInit(this.client, width, height);
	}

	@Inject(method = "resize", at = @At("TAIL"))
	private void spruceui$afterResizeScreen(int width, int height, CallbackInfo ci) {
		this.spruceui$afterInitEvent.invoker().afterInitScreen(this.spruceui$createInitContext(this.client, width, height));
	}

	@Unique
	private void spruceui$handleBeforeInit(Minecraft client, int width, int height) {
		this.spruceui$removeEvent = ScreenEvents.REMOVE.forContext(this.$self(), true);
		this.spruceui$beforeRenderEvent = ScreenEvents.BEFORE_RENDER.forContext(this.$self(), true);
		this.spruceui$afterRenderEvent = ScreenEvents.AFTER_RENDER.forContext(this.$self(), true);
		this.spruceui$beforeTickEvent = ScreenEvents.BEFORE_TICK.forContext(this.$self(), true);
		this.spruceui$afterTickEvent = ScreenEvents.AFTER_TICK.forContext(this.$self(), true);

		this.spruceui$beforeInitEvent.invoker().beforeInitScreen(client, this.$self(), width, height);
	}

	@Unique
	private ScreenEvents.ScreenInitContext spruceui$createInitContext(Minecraft client, int width, int height) {
		return new ScreenEvents.ScreenInitContext() {
			@Override
			public @NotNull Minecraft client() {
				return client;
			}

			@Override
			public @NotNull Screen screen() {
				return $self();
			}

			@Override
			public int scaledWidth() {
				return width;
			}

			@Override
			public int scaledHeight() {
				return height;
			}

			@Override
			public <T extends GuiEventListener & Renderable & NarratableEntry> @NotNull T addRenderableWidget(@NotNull T widget) {
				return ScreenMixin.this.addRenderableWidget(widget);
			}
		};
	}

	@Unique
	private Screen $self() {
		return (Screen) (Object) this;
	}
}
