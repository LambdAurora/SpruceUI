/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.border;

import net.minecraft.client.MinecraftClient;

/**
 * Represents an empty border.
 *
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.7.0
 */
public final class EmptyBorder implements Border {
    public static final EmptyBorder EMPTY_BORDER = new EmptyBorder();

    private EmptyBorder() {
    }

    @Override
    public void render(MinecraftClient client, int x, int y, int width, int height) {
    }

    @Override
    public int getThickness() {
        return 0;
    }
}
