/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an event callback which is fired when an {@link Screen} is opened.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 1.2.0
 */
@FunctionalInterface
public interface OpenScreenCallback {
    Event<OpenScreenCallback> PRE = EventUtil.makeOpenScreenEvent();
    Event<OpenScreenCallback> EVENT = EventUtil.makeOpenScreenEvent();

    void apply(MinecraftClient client, @Nullable Screen screen);
}
