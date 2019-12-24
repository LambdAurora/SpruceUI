/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
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
 */
public class SpruceButtonWidget extends ButtonWidget implements Tooltipable
{
    private Text tooltip;
    private int  tooltip_ticks;
    private long last_tick;

    public SpruceButtonWidget(int x, int y, int width, int height, String message, PressAction on_press)
    {
        super(x, y, width, height, message, on_press);
    }

    @Override
    public @NotNull Optional<Text> get_tooltip()
    {
        return Optional.ofNullable(this.tooltip);
    }

    @Override
    public void set_tooltip(@Nullable Text tooltip)
    {
        this.tooltip = tooltip;
    }

    @Override
    public void render(int mouse_x, int mouse_y, float delta)
    {
        super.render(mouse_x, mouse_y, delta);

        if (this.visible && this.tooltip != null) {
            long current_render = System.currentTimeMillis();
            if (this.last_tick != 0) {
                if (current_render - this.last_tick >= 20) {
                    this.tooltip_ticks++;
                    this.last_tick = current_render;
                }
            } else this.last_tick = current_render;

            if (!this.isFocused() && !this.isHovered)
                this.tooltip_ticks = 0;

            String tooltip_text = this.tooltip.asFormattedString();
            if (!tooltip_text.isEmpty() && this.tooltip_ticks >= 30) {
                List<String> wrapped_tooltip_text = MinecraftClient.getInstance().textRenderer.wrapStringToWidthAsList(tooltip_text, Math.max(this.width * 2 / 3, 200));
                if (this.isHovered)
                    new Tooltip(mouse_x, mouse_y, wrapped_tooltip_text).queue();
                else if (this.isFocused())
                    new Tooltip(this.x - 12, this.y + 12 + wrapped_tooltip_text.size() * 10, wrapped_tooltip_text).queue();
            }
        }
    }


}
