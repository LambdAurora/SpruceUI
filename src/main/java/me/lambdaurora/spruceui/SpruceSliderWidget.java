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
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Represents a slightly modified slider widget.
 */
public class SpruceSliderWidget extends SliderWidget implements Tooltipable
{
    private       String                       base_message;
    private final Consumer<SpruceSliderWidget> apply_consumer;
    private       double                       multiplier;
    private       String                       sign;
    private       Text                         tooltip;
    private       int                          tooltip_ticks;
    private       long                         last_tick;

    protected SpruceSliderWidget(int x, int y, int width, int height, @NotNull String message, double progress, @NotNull Consumer<SpruceSliderWidget> apply_consumer, double multiplier, String sign)
    {
        super(x, y, width, height, progress);
        this.base_message = message;
        this.apply_consumer = apply_consumer;
        this.multiplier = multiplier;
        this.sign = sign;
        this.updateMessage();
    }

    protected SpruceSliderWidget(int x, int y, int width, int height, @NotNull String message, double progress, @NotNull Consumer<SpruceSliderWidget> apply_consumer)
    {
        this(x, y, width, height, message, progress, apply_consumer, 100.0, "%");
    }

    /**
     * Gets the value of the slider.
     *
     * @return The value of the slider.
     */
    public double get_value()
    {
        return this.value;
    }

    /**
     * Returns the value of this slider as an integer .
     *
     * @return The value as an integer.
     */
    public int get_int_value()
    {
        return (int) (this.value * this.multiplier);
    }

    /**
     * Sets the value of this slider.
     *
     * @param value The new value as an integer.
     */
    public void set_int_value(int value)
    {
        this.value = value / this.multiplier;
        this.updateMessage();
    }

    /**
     * Gets the base message of the slider.
     *
     * @return The base message of the slider.
     */
    public @NotNull String get_base_message()
    {
        return base_message;
    }

    /**
     * Sets the base message of the slider.
     *
     * @param base_message The base message of the slider.
     */
    public void set_base_message(@NotNull String base_message)
    {
        this.base_message = base_message;
    }

    @Override
    protected void updateMessage()
    {
        this.setMessage(this.base_message + ": " + this.get_int_value() + sign);
    }

    @Override
    protected void applyValue()
    {
        this.apply_consumer.accept(this);
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
