/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.border;

import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Represents a border to draw around a widget.
 *
 * @author LambdAurora
 * @version 3.0.0
 * @since 2.0.0
 */
public abstract class Border {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public abstract void render(MatrixStack matrices, SpruceWidget widget, int mouseX, int mouseY, float delta);

    /**
     * Returns the thickness of the border.
     *
     * @return the thickness
     */
    public abstract int getThickness();
}
