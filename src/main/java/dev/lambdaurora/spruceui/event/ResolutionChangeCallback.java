/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.event;

import dev.yumi.commons.event.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

/**
 * Represents an event callback which is fired when the Minecraft's resolution is changed.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 1.2.0
 */
@FunctionalInterface
public interface ResolutionChangeCallback {
	Event<Identifier, ResolutionChangeCallback> EVENT = EventUtil.EVENT_MANAGER.create(ResolutionChangeCallback.class);

	void apply(Minecraft client);
}
