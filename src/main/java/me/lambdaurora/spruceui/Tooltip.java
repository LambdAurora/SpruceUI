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
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * Represents a tooltip.
 *
 * @author LambdAurora
 * @version 1.6.1
 * @since 1.0.0
 */
public class Tooltip extends DrawableHelper implements SprucePositioned
{
    private static final Queue<Tooltip>    TOOLTIPS = Queues.newConcurrentLinkedQueue();
    private final        int               x;
    private final        int               y;
    private final        List<OrderedText> tooltip;

    public Tooltip(int x, int y, @NotNull String tooltip, int parentWidth)
    {
        this(x, y, StringVisitable.plain(tooltip), parentWidth);
    }

    public Tooltip(int x, int y, @NotNull StringVisitable tooltip, int parentWidth)
    {
        this(x, y, MinecraftClient.getInstance().textRenderer.wrapLines(tooltip, Math.max(parentWidth * 2 / 3, 200)));
    }

    public Tooltip(int x, int y, @NotNull List<OrderedText> tooltip)
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
    public void render(MatrixStack matrices)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        Tooltipable.render(client, matrices, this.tooltip, this.x, this.y);
    }

    /**
     * Queues the tooltip to render.
     */
    public void queue()
    {
        TOOLTIPS.add(this);
    }

    /**
     * Queues the tooltip of the widget to render.
     *
     * @param widget The widget.
     * @param mouseX The mouse X coordinate.
     * @param mouseY The mouse Y coordinate.
     * @param <T>    The type of the widget.
     * @since 1.6.0
     */
    public static <T extends Tooltipable & SpruceWidget> void queueFor(@NotNull T widget, int mouseX, int mouseY, int tooltipTicks, @NotNull Consumer<Integer> tooltipTicksSetter, long lastTick, @NotNull Consumer<Long> lastTickSetter)
    {
        if (widget.isVisible()) {
            widget.getTooltip().ifPresent(tooltip -> {
                long currentRender = System.currentTimeMillis();
                if (lastTick != 0) {
                    if (currentRender - lastTick >= 20) {
                        tooltipTicksSetter.accept(tooltipTicks + 1);
                        lastTickSetter.accept(currentRender);
                    }
                } else lastTickSetter.accept(currentRender);

                if (!widget.isFocused() && !widget.isMouseHovered())
                    tooltipTicksSetter.accept(0);

                if (!tooltip.getString().isEmpty() && tooltipTicks >= 30) {
                    List<OrderedText> wrappedTooltipText = MinecraftClient.getInstance().textRenderer.wrapLines(tooltip, Math.max(widget.getWidth() * 2 / 3, 200));
                    if (widget.isMouseHovered())
                        new Tooltip(mouseX, mouseY, wrappedTooltipText).queue();
                    else if (widget.isFocused())
                        new Tooltip(widget.getX() - 12, widget.getY() + 12 + wrappedTooltipText.size() * 10, wrappedTooltipText).queue();
                }
            });
        }
    }

    /**
     * Renders all the tooltips.
     *
     * @param matrices The matrices.
     */
    public static void renderAll(MatrixStack matrices)
    {
        synchronized (TOOLTIPS) {
            Tooltip tooltip;

            while ((tooltip = TOOLTIPS.poll()) != null)
                tooltip.render(matrices);
        }
    }
}
