/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.event;

import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import dev.yumi.commons.event.FilteredEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Holds events related to {@linkplain Screen screens}.
 * <p>
 * Most, if not all, events are {@linkplain FilteredEvent filtered events},
 * this mean they can match a specific instance of a screen,
 * to do so the main entrypoint is going to be when a screen is being initialized ({@link #BEFORE_INIT} or {@link #AFTER_INIT}),
 * and other events can be registered within the callback of the initialization events
 * using {@link FilteredEvent#forContext(Object)}.
 */
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

	/**
	 * An event that is called before a screen is rendered.
	 *
	 * @see #AFTER_RENDER
	 */
	public static final FilteredEvent<Identifier, BeforeRender, Screen> BEFORE_RENDER
			= EventUtil.EVENT_MANAGER.createFiltered(BeforeRender.class, Screen.class);

	/**
	 * An event that is called after a screen is rendered.
	 *
	 * @see #BEFORE_RENDER
	 */
	public static final FilteredEvent<Identifier, AfterRender, Screen> AFTER_RENDER
			= EventUtil.EVENT_MANAGER.createFiltered(AfterRender.class, Screen.class);

	/**
	 * An event that is called before a screen is ticked.
	 *
	 * @see #AFTER_TICK
	 */
	public static final FilteredEvent<Identifier, BeforeTick, Screen> BEFORE_TICK
			= EventUtil.EVENT_MANAGER.createFiltered(BeforeTick.class, Screen.class);

	/**
	 * An event that is called after a screen is ticked.
	 *
	 * @see #BEFORE_TICK
	 */
	public static final FilteredEvent<Identifier, AfterTick, Screen> AFTER_TICK
			= EventUtil.EVENT_MANAGER.createFiltered(AfterTick.class, Screen.class);

	/**
	 * Represents the callback interface of the {@linkplain ScreenEvents#BEFORE_INIT before screen initialization event}.
	 */
	@FunctionalInterface
	public interface BeforeInit {
		/**
		 * Called before the given screen has initialized.
		 *
		 * @param client the Minecraft client instance
		 * @param screen the screen which is being initialized
		 * @param scaledWidth the scaled width of the screen
		 * @param scaledHeight the scaled height of the screen
		 */
		void beforeInitScreen(
				@NotNull Minecraft client,
				@NotNull Screen screen,
				int scaledWidth,
				int scaledHeight
		);
	}

	/**
	 * Represents the callback interface of the {@linkplain ScreenEvents#AFTER_INIT after screen initialization event}.
	 */
	@FunctionalInterface
	public interface AfterInit {
		/**
		 * Called after the given screen has initialized.
		 *
		 * @param context the initialization context
		 */
		void afterInitScreen(@NotNull ScreenInitContext context);
	}

	/**
	 * Represents the callback interface of the {@linkplain ScreenEvents#REMOVE screen removal event}.
	 */
	@FunctionalInterface
	public interface Remove {
		/**
		 * Called when the given screen has been removed.
		 *
		 * @param screen the screen which has been removed
		 */
		void onRemoveScreen(@NotNull Screen screen);
	}

	/**
	 * Represents the callback interface of the {@linkplain ScreenEvents#BEFORE_RENDER before screen render event}.
	 */
	@FunctionalInterface
	public interface BeforeRender {
		/**
		 * Called before the given screen has rendered.
		 *
		 * @param screen the screen which is rendering
		 * @param graphics the graphics
		 * @param mouseX the mouse X-coordinate
		 * @param mouseY the mouse Y-coordinate
		 * @param tickDelta the tick delta
		 */
		void onBeforeRenderScreen(
				@NotNull Screen screen, @NotNull SpruceGuiGraphics graphics, int mouseX, int mouseY, float tickDelta
		);
	}

	/**
	 * Represents the callback interface of the {@linkplain ScreenEvents#AFTER_RENDER after screen render event}.
	 */
	@FunctionalInterface
	public interface AfterRender {
		/**
		 * Called after the given screen has rendered.
		 *
		 * @param screen the screen which rendered
		 * @param graphics the graphics
		 * @param mouseX the mouse X-coordinate
		 * @param mouseY the mouse Y-coordinate
		 * @param tickDelta the tick delta
		 */
		void onAfterRenderScreen(
				@NotNull Screen screen, @NotNull SpruceGuiGraphics graphics, int mouseX, int mouseY, float tickDelta
		);
	}

	/**
	 * Represents the callback interface of the {@linkplain ScreenEvents#BEFORE_TICK before screen tick event}.
	 */
	@FunctionalInterface
	public interface BeforeTick {
		/**
		 * Called before the given screen has ticked.
		 *
		 * @param screen the screen which is ticking
		 */
		void onBeforeTickScreen(@NotNull Screen screen);
	}

	/**
	 * Represents the callback interface of the {@linkplain ScreenEvents#AFTER_TICK after screen tick event}.
	 */
	@FunctionalInterface
	public interface AfterTick {
		/**
		 * Called after the given screen has ticked.
		 *
		 * @param screen the screen which ticked
		 */
		void onAfterTickScreen(@NotNull Screen screen);
	}

	/**
	 * Represents the context of a screen that is being initialized.
	 */
	public interface ScreenInitContext {
		/**
		 * {@return the Minecraft client instance}
		 */
		@NotNull Minecraft client();

		/**
		 * {@return the screen that's being initialized}
		 */
		@NotNull Screen screen();

		/**
		 * {@return the scaled width of the screen}
		 */
		int scaledWidth();

		/**
		 * {@return the scaled height of the screen}
		 */
		int scaledHeight();

		/**
		 * Adds the given renderable widget to the screen.
		 *
		 * @param widget the widget to add
		 * @return the widget that has been added
		 * @param <E> the type of the widget
		 */
		@NotNull <E extends GuiEventListener & Renderable & NarratableEntry> E addRenderableWidget(
				@NotNull E widget
		);
	}
}
