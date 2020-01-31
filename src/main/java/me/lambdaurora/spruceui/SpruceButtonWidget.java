/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Represents a button widget.
 *
 * @author LambdAurora
 * @version 1.3.0
 * @since 1.0.0
 */
public class SpruceButtonWidget extends ButtonWidget implements Tooltipable
{
    private Text tooltip;
    private int  tooltipTicks;
    private long lastTick;

    public SpruceButtonWidget(int x, int y, int width, int height, String message, PressAction action)
    {
        super(x, y, width, height, message, action);
    }

    @Override
    public @NotNull Optional<Text> getTooltip()
    {
        return Optional.ofNullable(this.tooltip);
    }

    @Override
    public void setTooltip(@Nullable Text tooltip)
    {
        this.tooltip = tooltip;
    }

    @Override
    public void render(int mouse_x, int mouse_y, float delta)
    {
        super.render(mouse_x, mouse_y, delta);

        if (this.visible && this.tooltip != null) {
            long currentRender = System.currentTimeMillis();
            if (this.lastTick != 0) {
                if (currentRender - this.lastTick >= 20) {
                    this.tooltipTicks++;
                    this.lastTick = currentRender;
                }
            } else this.lastTick = currentRender;

            if (!this.isFocused() && !this.isHovered)
                this.tooltipTicks = 0;

            String tooltipText = this.tooltip.asFormattedString();
            if (!tooltipText.isEmpty() && this.tooltipTicks >= 30) {
                List<String> wrappedTooltipText = MinecraftClient.getInstance().textRenderer.wrapStringToWidthAsList(tooltipText, Math.max(this.width * 2 / 3, 200));
                if (this.isHovered)
                    new Tooltip(mouse_x, mouse_y, wrappedTooltipText).queue();
                else if (this.isFocused())
                    new Tooltip(this.x - 12, this.y + 12 + wrappedTooltipText.size() * 10, wrappedTooltipText).queue();
            }
        }
    }
}
