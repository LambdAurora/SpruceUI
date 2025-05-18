/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.event;

import dev.lambdaurora.spruceui.SpruceUI;
import dev.yumi.commons.event.EventManager;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a set of utilities for SpruceUI's events.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 1.4.0
 */
@ApiStatus.Internal
final class EventUtil {
	static final EventManager<Identifier> EVENT_MANAGER = new EventManager<>(SpruceUI.id("default"), Identifier::parse);

	private EventUtil() {
		throw new UnsupportedOperationException("EventUtil is a singleton.");
	}
}
