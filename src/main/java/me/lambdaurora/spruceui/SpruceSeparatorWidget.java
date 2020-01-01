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
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a separator element.
 *
 * @author LambdAurora
 * @version 1.0.1
 * @since 1.0.1
 */
public class SpruceSeparatorWidget extends DrawableHelper implements Element, Drawable, Tooltipable
{
    private final MinecraftClient client         = MinecraftClient.getInstance();
    protected     int             x;
    protected     int             y;
    protected     int             width;
    private       Text            title;
    public        boolean         visible        = true;
    protected     boolean         hovered;
    protected     boolean         focused;
    private       boolean         was_hovered;
    protected     long            next_narration = Long.MAX_VALUE;
    private       Text            tooltip;
    private       int             tooltip_ticks;
    private       long            last_tick;

    public SpruceSeparatorWidget(@Nullable Text title, int x, int y, int width)
    {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public int get_x()
    {
        return this.x;
    }

    public int get_y()
    {
        return this.y;
    }

    /**
     * Gets the width of this separator widget.
     *
     * @return The width of this separator widget.
     */
    public int get_width()
    {
        return this.width;
    }

    /**
     * Gets the title of this separator widget.
     *
     * @return The title.
     */
    public @NotNull Optional<Text> get_title()
    {
        return Optional.ofNullable(this.title);
    }

    /**
     * Sets the title of this separator widget.
     *
     * @param title The title.
     */
    public void set_title(@Nullable Text title)
    {
        if (!Objects.equals(title, this.title)) {
            this.queue_narration(250);
        }
        this.title = title;
    }

    public boolean is_hovered()
    {
        return this.hovered || this.focused;
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
        if (this.visible) {
            this.hovered = mouse_x >= this.x && mouse_y >= this.y && mouse_x < this.x + this.width && mouse_y < this.y + 9;

            if (this.title != null) {
                if (this.was_hovered != this.is_hovered()) {
                    if (this.is_hovered()) {
                        if (this.focused)
                            this.queue_narration(200);
                        else
                            this.queue_narration(750);

                    } else
                        this.next_narration = Long.MAX_VALUE;
                }

                String title = this.title.asFormattedString();
                int title_width = this.client.textRenderer.getStringWidth(title);
                int title_x = this.x + (this.width / 2 - title_width / 2);
                if (this.width > title_width) {
                    fill(this.x, this.y + 4, title_x - 5, this.y + 6, 0xffe0e0e0);
                    fill(title_x + title_width + 5, this.y + 4, this.x + this.width, this.y + 6, 0xffe0e0e0);
                }
                this.drawString(this.client.textRenderer, title, title_x, this.y, 0xffffff);
            } else {
                fill(this.x, this.y + 4, this.x + this.width, this.y + 6, 0xffe0e0e0);
            }

            if (this.tooltip != null) {
                long current_render = System.currentTimeMillis();
                if (this.last_tick != 0) {
                    if (current_render - this.last_tick >= 20) {
                        this.tooltip_ticks++;
                        this.last_tick = current_render;
                    }
                } else this.last_tick = current_render;

                if (!this.focused && !this.hovered)
                    this.tooltip_ticks = 0;

                String tooltip_text = this.tooltip.asFormattedString();
                if (!tooltip_text.isEmpty() && this.tooltip_ticks >= 30) {
                    List<String> wrapped_tooltip_text = MinecraftClient.getInstance().textRenderer.wrapStringToWidthAsList(tooltip_text, Math.max(this.width * 2 / 3, 200));
                    if (this.hovered)
                        new Tooltip(mouse_x, mouse_y, wrapped_tooltip_text).queue();
                    else if (this.focused)
                        new Tooltip(this.x - 12, this.y + 12 + wrapped_tooltip_text.size() * 10, wrapped_tooltip_text).queue();
                }
            }

            this.narrate();
            this.was_hovered = this.is_hovered();
        }
    }

    protected void narrate()
    {
        if (this.is_hovered() && Util.getMeasuringTimeMs() > this.next_narration) {
            String string = this.get_narration_message();
            if (!string.isEmpty()) {
                NarratorManager.INSTANCE.narrate(string);
                this.next_narration = Long.MAX_VALUE;
            }
        }
    }

    protected String get_narration_message()
    {
        return this.get_title().map(Text::asFormattedString)
                .filter(title -> !title.isEmpty())
                .map(title -> I18n.translate("spruceui.narrator.separator", title))
                .orElse("");
    }

    public void queue_narration(int ticks)
    {
        this.next_narration = Util.getMeasuringTimeMs() + (long) ticks;
    }

    @Override
    public boolean changeFocus(boolean down)
    {
        if (this.visible) {
            this.focused = !this.focused;
            return this.focused;
        } else {
            return false;
        }
    }

    /**
     * Represents a button wrapper for the option.
     *
     * @author LambdAurora
     * @version 1.0.1
     * @since 1.0.1
     */
    public static class ButtonWrapper extends AbstractButtonWidget
    {
        private final SpruceSeparatorWidget widget;

        public ButtonWrapper(@NotNull SpruceSeparatorWidget separator, int height)
        {
            super(separator.x, separator.y, separator.width, height, separator.get_title().map(Text::asFormattedString).orElse(""));
            this.widget = separator;
        }

        @Override
        public void render(int mouse_x, int mouse_y, float delta)
        {
            this.widget.y = this.y + this.height / 2 - 9 / 2;
            this.widget.render(mouse_x, mouse_y, delta);
        }

        @Override
        public boolean changeFocus(boolean down)
        {
            return this.widget.changeFocus(down);
        }
    }
}
