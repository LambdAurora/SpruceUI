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
    private final MinecraftClient client        = MinecraftClient.getInstance();
    protected     int             x;
    protected     int             y;
    protected     int             width;
    private       Text            title;
    public        boolean         visible       = true;
    protected     boolean         hovered;
    protected     boolean         focused;
    private       boolean         wasHovered;
    protected     long            nextNarration = Long.MAX_VALUE;
    private       Text            tooltip;
    private       int             tooltipTicks;
    private       long            lastTick;

    public SpruceSeparatorWidget(@Nullable Text title, int x, int y, int width)
    {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    /**
     * Gets the width of this separator widget.
     *
     * @return The width of this separator widget.
     */
    public int getWidth()
    {
        return this.width;
    }

    /**
     * Gets the title of this separator widget.
     *
     * @return The title.
     */
    public @NotNull Optional<Text> getTitle()
    {
        return Optional.ofNullable(this.title);
    }

    /**
     * Sets the title of this separator widget.
     *
     * @param title The title.
     */
    public void setTitle(@Nullable Text title)
    {
        if (!Objects.equals(title, this.title)) {
            this.queueNarration(250);
        }
        this.title = title;
    }

    public boolean isHovered()
    {
        return this.hovered || this.focused;
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
    public void render(int mouseX, int mouseY, float delta)
    {
        if (this.visible) {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + 9;

            if (this.title != null) {
                if (this.wasHovered != this.isHovered()) {
                    if (this.isHovered()) {
                        if (this.focused)
                            this.queueNarration(200);
                        else
                            this.queueNarration(750);

                    } else
                        this.nextNarration = Long.MAX_VALUE;
                }

                String title = this.title.asFormattedString();
                int titleWidth = this.client.textRenderer.getStringWidth(title);
                int titleX = this.x + (this.width / 2 - titleWidth / 2);
                if (this.width > titleWidth) {
                    fill(this.x, this.y + 4, titleX - 5, this.y + 6, 0xffe0e0e0);
                    fill(titleX + titleWidth + 5, this.y + 4, this.x + this.width, this.y + 6, 0xffe0e0e0);
                }
                this.drawString(this.client.textRenderer, title, titleX, this.y, 0xffffff);
            } else {
                fill(this.x, this.y + 4, this.x + this.width, this.y + 6, 0xffe0e0e0);
            }

            if (this.tooltip != null) {
                long currentRender = System.currentTimeMillis();
                if (this.lastTick != 0) {
                    if (currentRender - this.lastTick >= 20) {
                        this.tooltipTicks++;
                        this.lastTick = currentRender;
                    }
                } else this.lastTick = currentRender;

                if (!this.focused && !this.hovered)
                    this.tooltipTicks = 0;

                String tooltipText = this.tooltip.asFormattedString();
                if (!tooltipText.isEmpty() && this.tooltipTicks >= 30) {
                    List<String> wrappedTooltipText = MinecraftClient.getInstance().textRenderer.wrapStringToWidthAsList(tooltipText, Math.max(this.width * 2 / 3, 200));
                    if (this.hovered)
                        new Tooltip(mouseX, mouseY, wrappedTooltipText).queue();
                    else if (this.focused)
                        new Tooltip(this.x - 12, this.y + 12 + wrappedTooltipText.size() * 10, wrappedTooltipText).queue();
                }
            }

            this.narrate();
            this.wasHovered = this.isHovered();
        }
    }

    protected void narrate()
    {
        if (this.isHovered() && Util.getMeasuringTimeMs() > this.nextNarration) {
            String string = this.getNarrationMessage();
            if (!string.isEmpty()) {
                NarratorManager.INSTANCE.narrate(string);
                this.nextNarration = Long.MAX_VALUE;
            }
        }
    }

    protected String getNarrationMessage()
    {
        return this.getTitle().map(Text::asFormattedString)
                .filter(title -> !title.isEmpty())
                .map(title -> I18n.translate("spruceui.narrator.separator", title))
                .orElse("");
    }

    public void queueNarration(int ticks)
    {
        this.nextNarration = Util.getMeasuringTimeMs() + (long) ticks;
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
     * @version 1.3.1
     * @since 1.0.1
     */
    public static class ButtonWrapper extends AbstractButtonWidget
    {
        private final SpruceSeparatorWidget widget;

        public ButtonWrapper(@NotNull SpruceSeparatorWidget separator, int height)
        {
            super(separator.x, separator.y, separator.width, height, separator.getTitle().map(Text::asFormattedString).orElse(""));
            this.widget = separator;
        }

        @Override
        public void render(int mouseX, int mouseY, float delta)
        {
            this.widget.y = this.y + this.height / 2 - 9 / 2;
            this.widget.render(mouseX, mouseY, delta);
        }

        @Override
        public boolean changeFocus(boolean down)
        {
            return this.widget.changeFocus(down);
        }
    }
}
