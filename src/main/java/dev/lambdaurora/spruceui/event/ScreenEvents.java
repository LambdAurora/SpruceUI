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
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public final class ScreenEvents {
	public static final FilteredEvent<Identifier, BeforeInit, Screen> BEFORE_INIT = EventUtil.EVENT_MANAGER.createFiltered(
			BeforeInit.class, Screen.class
	);

	public static final FilteredEvent<Identifier, AfterInit, Screen> AFTER_INIT = EventUtil.EVENT_MANAGER.createFiltered(
			AfterInit.class, Screen.class
	);

	public static final FilteredEvent<Identifier, Remove, Screen> REMOVE = EventUtil.EVENT_MANAGER.createFiltered(
			Remove.class, Screen.class
	);

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
