/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.event;

import dev.yumi.commons.event.FilteredEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public final class ScreenEvents {
	/**
	 * An event that is called before {@linkplain Screen#init(Minecraft, int, int) a screen is initialized} to its default state.
	 */
	public static final FilteredEvent<Identifier, BeforeInit, Screen> BEFORE_INIT = EventUtil.EVENT_MANAGER.createFiltered(
			BeforeInit.class, Screen.class
	);

	/**
	 * An event that is called after {@linkplain Screen#init(Minecraft, int, int) a screen is initialized} to its default state.
	 */
	public static final FilteredEvent<Identifier, AfterInit, Screen> AFTER_INIT = EventUtil.EVENT_MANAGER.createFiltered(
			AfterInit.class, Screen.class
	);

	/**
	 * An event that is called after {@link Screen#removed()} is called.
	 * This event signifies that the screen is now closed.
	 * <p>
	 * This event is typically used to undo any screen specific state changes or to terminate threads spawned by a screen.
	 *
	 * @implNote This event may precede initialization events {@link ScreenEvents#BEFORE_INIT}
	 * but there is no guarantee that event will be called immediately afterward.
	 */
	public static final FilteredEvent<Identifier, Remove, Screen> REMOVE = EventUtil.EVENT_MANAGER.createFiltered(
			Remove.class, Screen.class
	);

	public static final FilteredEvent<Identifier, BeforeRender, Screen> BEFORE_RENDER
			= EventUtil.EVENT_MANAGER.createFiltered(BeforeRender.class, Screen.class);

	public static final FilteredEvent<Identifier, AfterRender, Screen> AFTER_RENDER
			= EventUtil.EVENT_MANAGER.createFiltered(AfterRender.class, Screen.class);

	@FunctionalInterface
	public interface BeforeInit {
		void beforeInitScreen(
				@NotNull Minecraft client,
				@NotNull Screen screen,
				int scaledWidth,
				int scaledHeight
		);
	}

	@FunctionalInterface
	public interface AfterInit {
		void afterInitScreen(@NotNull ScreenInitContext context);
	}

	@FunctionalInterface
	public interface Remove {
		void onRemoveScreen(@NotNull Screen screen);
	}

	@FunctionalInterface
	public interface BeforeRender {
		void onBeforeRenderScreen(
				@NotNull Screen screen, @NotNull GuiGraphics graphics, int mouseX, int mouseY, float tickDelta
		);
	}

	@FunctionalInterface
	public interface AfterRender {
		void onAfterRenderScreen(
				@NotNull Screen screen, @NotNull GuiGraphics graphics, int mouseX, int mouseY, float tickDelta
		);
	}

	public interface ScreenInitContext {
		@NotNull Minecraft client();

		@NotNull Screen screen();

		int scaledWidth();

		int scaledHeight();

		@NotNull <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(
				@NotNull T widget
		);
	}
}
