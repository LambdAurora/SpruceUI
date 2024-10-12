/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;

/**
 * Represents an event callback which is fired when the Minecraft's resolution is changed.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 1.2.0
 */
@FunctionalInterface
public interface ResolutionChangeCallback {
	Event<ResolutionChangeCallback> EVENT = EventFactory.createArrayBacked(ResolutionChangeCallback.class, listeners -> client -> {
		for (var event : listeners) {
			event.apply(client);
		}
	});

	void apply(Minecraft client);
}
