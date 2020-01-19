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
 * @version 1.2.0
 * @since 1.2.0
 */
@FunctionalInterface
public interface ResolutionChangedCallback
{
    Event<ResolutionChangedCallback> EVENT = EventFactory.createArrayBacked(ResolutionChangedCallback.class, listeners -> client -> {
        for (ResolutionChangedCallback event : listeners) {
            event.apply(client);
        }
    });

    void apply(@NotNull MinecraftClient client);
}
