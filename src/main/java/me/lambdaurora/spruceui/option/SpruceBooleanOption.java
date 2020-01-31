/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.option;

import me.lambdaurora.spruceui.SpruceButtonWidget;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Represents a boolean option.
 * <p>
 * Works the same as the vanilla one but can provide a tooltip.
 *
 * @author LambdAurora
 * @version 1.3.1
 * @since 1.0.0
 */
public class SpruceBooleanOption extends SpruceOption
{
    private final Predicate<GameOptions>           getter;
    private final BiConsumer<GameOptions, Boolean> setter;
    private final boolean                          colored;
    private final Text                             tooltip;

    public SpruceBooleanOption(@NotNull String key, @NotNull Predicate<GameOptions> getter, @NotNull BiConsumer<GameOptions, Boolean> setter, @Nullable Text tooltip)
    {
        this(key, getter, setter, tooltip, false);
    }

    public SpruceBooleanOption(@NotNull String key, @NotNull Predicate<GameOptions> getter, @NotNull BiConsumer<GameOptions, Boolean> setter, @Nullable Text tooltip, boolean colored)
    {
        super(key);
        this.getter = getter;
        this.setter = setter;
        this.colored = colored;
        this.tooltip = tooltip;
    }

    public void set(@NotNull GameOptions options, @NotNull String value)
    {
        this.set(options, "true".equals(value));
    }

    public void set(@NotNull GameOptions options)
    {
        this.set(options, !this.get(options));
        options.write();
    }

    private void set(@NotNull GameOptions options, boolean value)
    {
        this.setter.accept(options, value);
    }

    public boolean get(@NotNull GameOptions options)
    {
        return this.getter.test(options);
    }

    /**
     * Returns whether the option value is colored or not.
     *
     * @return True if the option value is colored, else false.
     */
    public boolean isColored()
    {
        return this.colored;
    }

    @Override
    public @NotNull AbstractButtonWidget createButton(@NotNull GameOptions options, int x, int y, int width)
    {
        SpruceButtonWidget button = new SpruceButtonWidget(x, y, width, 20, this.getDisplayString(options), btn -> {
            this.set(options);
            btn.setMessage(this.getDisplayString(options));
        });
        button.setTooltip(this.tooltip);
        return button;
    }

    public @NotNull String getDisplayString(@NotNull GameOptions options)
    {
        boolean value = this.get(options);
        return this.getDisplayPrefix() + (this.colored ? (value ? Formatting.GREEN : Formatting.RED) : "") + I18n.translate(value ? "options.on" : "options.off");
    }
}
