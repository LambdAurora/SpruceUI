/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.border;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;

/**
 * Represents a simple solid border to draw around a widget.
 *
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.7.0
 */
public class SimpleBorder implements Border {
    public static final SimpleBorder SIMPLE_BORDER = new SimpleBorder(1, 192, 192, 192, 255);

    private final int thickness;
    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    public SimpleBorder(int thickness, int red, int green, int blue, int alpha) {
        this.thickness = thickness;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    @Override
    public void render(MinecraftClient client, int x, int y, int width, int height) {
        RenderSystem.disableTexture();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, VertexFormats.POSITION_COLOR);
        int right = x + width;
        int bottom = y + height;
        // Top border
        this.vertex(buffer, x, y + this.thickness);
        this.vertex(buffer, right, y + this.thickness);
        this.vertex(buffer, right, y);
        this.vertex(buffer, x, y);
        // Right border
        this.vertex(buffer, right - this.thickness, bottom);
        this.vertex(buffer, right, bottom);
        this.vertex(buffer, right, y);
        this.vertex(buffer, right - this.thickness, y);
        // Bottom
        this.vertex(buffer, x, bottom);
        this.vertex(buffer, right, bottom);
        this.vertex(buffer, right, bottom - this.thickness);
        this.vertex(buffer, x, bottom - this.thickness);
        // Left border
        this.vertex(buffer, x, bottom);
        this.vertex(buffer, x + this.thickness, bottom);
        this.vertex(buffer, x + this.thickness, y);
        this.vertex(buffer, x, y);
        tessellator.draw();

        RenderSystem.enableTexture();
    }

    private void vertex(BufferBuilder buffer, int x, int y) {
        buffer.vertex(x, y, 0).color(this.red, this.green, this.blue, this.alpha).next();
    }

    @Override
    public int getThickness() {
        return this.thickness;
    }
}
