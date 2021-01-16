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
import me.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.aperlambda.lambdacommon.utils.Nameable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents an option.
 *
 * @author LambdAurora
 * @version 2.0.1
 * @since 1.0.3
 */
public abstract class SpruceOption implements Nameable {
    public final String key;
    private Optional<Text> tooltip = Optional.empty();

    public SpruceOption(@NotNull String key) {
        Objects.requireNonNull(key, "Cannot create an option without a key.");
        this.key = key;
    }

    @Override
    public @NotNull String getName() {
        return I18n.translate(this.key);
    }

    public @NotNull Optional<Text> getOptionTooltip() {
        return this.tooltip;
    }

    public void setTooltip(@Nullable Text tooltip) {
        this.tooltip = Optional.ofNullable(tooltip);
    }

    /**
     * Returns the display prefix text.
     *
     * @return the display prefix
     */
    public @NotNull Text getPrefix() {
        return new TranslatableText(this.key);
    }

    /**
     * Returns the display text.
     *
     * @param value the value
     * @return the display text
     */
    public @NotNull Text getDisplayText(@NotNull Text value) {
        return new TranslatableText("spruceui.options.generic", this.getPrefix(), value);
    }

    public abstract @NotNull SpruceWidget createWidget(@NotNull Position position, int width);
}
