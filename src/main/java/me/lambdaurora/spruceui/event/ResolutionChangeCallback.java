/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event callback which is fired when the Minecraft's resolution is changed.
 *
 * @author LambdAurora
 * @version 1.4.0
 * @since 1.2.0
 */
@FunctionalInterface
public interface ResolutionChangeCallback {
    Event<ResolutionChangeCallback> EVENT = EventFactory.createArrayBacked(ResolutionChangeCallback.class, listeners -> client -> {
        for (ResolutionChangeCallback event : listeners) {
            event.apply(client);
        }
    });

    void apply(@NotNull MinecraftClient client);
}
