/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.option;

import me.lambdaurora.spruceui.SpruceOptionSliderWidget;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a double option.
 * <p>
 * Works the same as the vanilla one but can provide a tooltip.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
public class SpruceDoubleOption extends Option
{
    protected final float                                               step;
    protected final double                                              min;
    protected       double                                              max;
    private final   Function<GameOptions, Double>                       getter;
    private final   BiConsumer<GameOptions, Double>                     setter;
    private final   BiFunction<GameOptions, SpruceDoubleOption, String> displayStringGetter;
    private final   Text                                                tooltip;

    public SpruceDoubleOption(@NotNull String key, double min, double max, float step, @NotNull Function<GameOptions, Double> getter, @NotNull BiConsumer<GameOptions, Double> setter, @NotNull BiFunction<GameOptions, SpruceDoubleOption, String> displayStringGetter, @Nullable Text tooltip)
    {
        super(key);
        this.min = min;
        this.max = max;
        this.step = step;
        this.getter = getter;
        this.setter = setter;
        this.displayStringGetter = displayStringGetter;
        this.tooltip = tooltip;
    }

    @Override
    public @NotNull AbstractButtonWidget createButton(@NotNull GameOptions options, int x, int y, int width)
    {
        SpruceOptionSliderWidget slider = new SpruceOptionSliderWidget(options, x, y, width, 20, this);
        slider.set_tooltip(this.tooltip);
        return slider;
    }

    public double get_ratio(double value)
    {
        return MathHelper.clamp((this.adjust(value) - this.min) / (this.max - this.min), 0.0D, 1.0D);
    }

    public double get_value(double ratio)
    {
        return this.adjust(MathHelper.lerp(MathHelper.clamp(ratio, 0.0D, 1.0D), this.min, this.max));
    }

    private double adjust(double value)
    {
        if (this.step > 0.0F) {
            value = this.step * (float) Math.round(value / (double) this.step);
        }

        return MathHelper.clamp(value, this.min, this.max);
    }

    public double get_min()
    {
        return this.min;
    }

    public double get_max()
    {
        return this.max;
    }

    public void set_max(float max)
    {
        this.max = max;
    }

    public void set(@NotNull GameOptions options, double value)
    {
        this.setter.accept(options, value);
    }

    public double get(@NotNull GameOptions options)
    {
        return this.getter.apply(options);
    }

    public @NotNull String get_display_string(@NotNull GameOptions options)
    {
        return this.displayStringGetter.apply(options, this);
    }
}
