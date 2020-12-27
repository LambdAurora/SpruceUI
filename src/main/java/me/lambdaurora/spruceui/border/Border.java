/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.border;

import me.lambdaurora.spruceui.SprucePositioned;
import net.minecraft.client.MinecraftClient;

/**
 * Represents a border to draw around a widget.
 *
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.7.0
 */
public interface Border {
    default void render(MinecraftClient client, SprucePositioned positioned, int width, int height) {
        this.render(client, positioned.getX(), positioned.getY(), width, height);
    }

    void render(MinecraftClient client, int x, int y, int width, int height);

    int getThickness();
}
