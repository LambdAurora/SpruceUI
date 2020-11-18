/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.option;

import me.lambdaurora.spruceui.Position;
import me.lambdaurora.spruceui.widget.SpruceButtonWidget;
import me.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.GameOptions;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
 * @version 1.7.0
 * @since 1.0.0
 */
public class SpruceBooleanOption extends SpruceOption
{
    private final Supplier<Boolean> getter;
    private final Consumer<Boolean> setter;
    private final boolean colored;

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
        this.setTooltip(tooltip);
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
     * @return the current value
     */
    public boolean get()
    {
        return this.getter.get();
    }

    /**
     * Returns whether the option value is colored or not.
     *
     * @return {@code true} if the option value is colored, else {@code false}
     */
    public boolean isColored()
    {
        return this.colored;
    }

    @Override
    public @NotNull SpruceWidget createWidget(@NotNull Position position, int width)
    {
        SpruceButtonWidget button = new SpruceButtonWidget(position, width, 20, this.getDisplayText(), btn -> {
            this.set();
            btn.setMessage(this.getDisplayText());
        });
        this.getOptionTooltip().ifPresent(button::setTooltip);
        return button;
    }

    /**
     * Gets the display string.
     *
     * @return the display string
     */
    public @NotNull Text getDisplayText()
    {
        boolean value = this.get();
        Text toggleText = ScreenTexts.getToggleText(value);
        if (this.colored)
            toggleText = toggleText.copy().setStyle(toggleText.getStyle().withColor(value ? Formatting.GREEN : Formatting.RED));
        return this.getDisplayText(toggleText);
    }

    /**
     * Returns a new SpruceUI Boolean Option from the Vanilla one.
     *
     * @param key the option's key
     * @param vanilla the Vanilla option
     * @param tooltip the tooltip
     * @return the SpruceUI option
     */
    public static @NotNull SpruceBooleanOption fromVanilla(@NotNull String key, @NotNull BooleanOption vanilla, @Nullable Text tooltip)
    {
        return fromVanilla(key, vanilla, tooltip, false);
    }

    /**
     * Returns a new SpruceUI Boolean Option from the Vanilla one.
     *
     * @param key the option's key
     * @param vanilla the Vanilla option
     * @param tooltip the tooltip
     * @param colored {@code true} if the option value is colored, else {@code false}
     * @return the SpruceUI option
     */
    public static @NotNull SpruceBooleanOption fromVanilla(@NotNull String key, @NotNull BooleanOption vanilla, @Nullable Text tooltip, boolean colored)
    {
        GameOptions options = MinecraftClient.getInstance().options;
        return new SpruceBooleanOption(key, () -> vanilla.get(options), newValue -> vanilla.set(options, String.valueOf(newValue)), tooltip, colored);
    }
}
