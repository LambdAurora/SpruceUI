/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import com.google.common.collect.Queues;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Queue;

/**
 * Represents a tooltip.
 */
public class Tooltip extends DrawableHelper
{
    private static final Queue<Tooltip> TOOLTIPS = Queues.newConcurrentLinkedQueue();
    private final        int            x;
    private final        int            y;
    private final        List<String>   tooltip;

    public Tooltip(int x, int y, @NotNull Text tooltip, int parent_width)
    {
        this(x, y, tooltip.asFormattedString(), parent_width);
    }

    public Tooltip(int x, int y, @NotNull String tooltip, int parent_width)
    {
        this(x, y, MinecraftClient.getInstance().textRenderer.wrapStringToWidthAsList(tooltip, Math.max(parent_width * 2 / 3, 200)));
    }

    public Tooltip(int x, int y, @NotNull List<String> tooltip)
    {
        this.x = x;
        this.y = y;
        this.tooltip = tooltip;
    }

    public boolean should_render()
    {
        return !this.tooltip.isEmpty();
    }

    /**
     * Renders the tooltip.
     */
    public void render()
    {
        MinecraftClient client = MinecraftClient.getInstance();
        Tooltipable.render(client, this, this.tooltip, this.x, this.y);
    }

    /**
     * Queues the tooltip to render.
     */
    public void queue()
    {
        TOOLTIPS.add(this);
    }

    public static void render_all()
    {
        synchronized (TOOLTIPS) {
            Tooltip tooltip;

            while ((tooltip = TOOLTIPS.poll()) != null)
                tooltip.render();
        }
    }
}
