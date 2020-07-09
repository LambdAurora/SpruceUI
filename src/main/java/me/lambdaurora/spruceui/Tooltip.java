/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
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
 *
 * @author LambdAurora
 * @version 1.4.1
 * @since 1.0.0
 */
public class Tooltip extends DrawableHelper implements SprucePositioned
{
    private static final Queue<Tooltip> TOOLTIPS = Queues.newConcurrentLinkedQueue();
    private final        int            x;
    private final        int            y;
    private final        List<String>   tooltip;

    public Tooltip(int x, int y, @NotNull String tooltip, int parentWidth)
    {
        this(x, y, MinecraftClient.getInstance().textRenderer.wrapStringToWidthAsList(tooltip, Math.max(parentWidth * 2 / 3, 200)));
    }

    public Tooltip(int x, int y, @NotNull Text tooltip, int parentWidth)
    {
        this(x, y, tooltip.asFormattedString(), parentWidth);
    }

    public Tooltip(int x, int y, @NotNull List<String> tooltip)
    {
        this.x = x;
        this.y = y;
        this.tooltip = tooltip;
    }

    @Override
    public int getX()
    {
        return this.x;
    }

    @Override
    public int getY()
    {
        return this.y;
    }

    /**
     * Returns whether the tooltip should render or not.
     *
     * @return True if the tooltip should render, else false.
     */
    public boolean shouldRender()
    {
        return !this.tooltip.isEmpty();
    }

    /**
     * Renders the tooltip.
     */
    public void render(DrawableHelper helper)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        Tooltipable.render(client, helper, this.tooltip, this.x, this.y);
    }

    /**
     * Queues the tooltip to render.
     */
    public void queue()
    {
        TOOLTIPS.add(this);
    }

    /**
     * Renders all the tooltips.
     */
    public static void renderAll(DrawableHelper helper)
    {
        synchronized (TOOLTIPS) {
            Tooltip tooltip;

            while ((tooltip = TOOLTIPS.poll()) != null)
                tooltip.render(helper);
        }
    }
}
