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
import net.minecraft.client.util.TextFormat;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents a boolean option.
 * <p>
 * Works the same as the vanilla one but can provide a tooltip.
 *
 * @author LambdAurora
 * @version 1.3.2
 * @since 1.0.0
 */
public class SpruceBooleanOption extends SpruceOption
{
    private final Supplier<Boolean> getter;
    private final Consumer<Boolean> setter;
    private final boolean           colored;
    private final Text              tooltip;

    public SpruceBooleanOption(@NotNull String key, @NotNull Supplier<Boolean> getter, @NotNull Consumer<Boolean> setter, @Nullable Text tooltip)
    {
        this(key, getter, setter, tooltip, false);
    }

    public SpruceBooleanOption(@NotNull String key, @NotNull Supplier<Boolean> getter, @NotNull Consumer<Boolean> setter, @Nullable Text tooltip, boolean colored)
    {
        super(key);
        this.getter = getter;
        this.setter = setter;
        this.colored = colored;
        this.tooltip = tooltip;
    }

    public void set(@NotNull String value)
    {
        this.set("true".equals(value));
    }

    public void set()
    {
        this.set(!this.get());
    }

    private void set(boolean value)
    {
        this.setter.accept(value);
    }

    /**
     * Gets the current value.
     *
     * @return The current value.
     */
    public boolean get()
    {
        return this.getter.get();
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
        SpruceButtonWidget button = new SpruceButtonWidget(x, y, width, 20, this.getDisplayString(), btn -> {
            this.set();
            btn.setMessage(this.getDisplayString());
        });
        button.setTooltip(this.tooltip);
        return button;
    }

    /**
     * Gets the display string.
     *
     * @return The display string.
     */
    public @NotNull String getDisplayString()
    {
        boolean value = this.get();
        return this.getDisplayPrefix() + (this.colored ? (value ? TextFormat.GREEN : TextFormat.RED) : "") + I18n.translate(value ? "options.on" : "options.off");
    }
}
