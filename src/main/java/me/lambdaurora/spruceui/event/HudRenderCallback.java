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
 * Represents an event callback which is fired when the client's HUD is rendered.
 * <p>
 * It is a placeholder for the missing HudRenderCallback in the 1.14.4 Fabric API.
 *
 * @author LambdAurora
 * @version 1.3.1-1.14.4
 * @since 1.2.1-1.14.4
 */
@FunctionalInterface
public interface HudRenderCallback
{
    Event<HudRenderCallback> EVENT = EventFactory.createArrayBacked(HudRenderCallback.class, listeners -> tickDelta -> {
        for (HudRenderCallback event : listeners) {
            event.onHudRender(tickDelta);
        }
    });

    void onHudRender(float tickDelta);
}
