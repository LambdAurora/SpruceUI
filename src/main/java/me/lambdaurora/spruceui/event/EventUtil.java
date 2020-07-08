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

/**
 * Represents a set of utilities for SpruceUI's events.
 *
 * @author LambdAurora
 * @version 1.5.6
 * @since 1.4.0
 */
public final class EventUtil
{
    private EventUtil()
    {
        throw new UnsupportedOperationException("EventUtil is a singleton.");
    }

    static Event<OpenScreenCallback> makeOpenScreenEvent()
    {
        return EventFactory.createArrayBacked(OpenScreenCallback.class, listeners -> (client, screen) -> {
            for (OpenScreenCallback event : listeners) {
                event.apply(client, screen);
            }
        });
    }

    /**
     * Registers a full open screen event.
     *
     * @param pre  Pre open screen callback.
     * @param post Post open screen callback.
     */
    public static void onOpenScreen(OpenScreenCallback pre, OpenScreenCallback post)
    {
        OpenScreenCallback.PRE.register(pre);
        OpenScreenCallback.EVENT.register(post);
    }
}
