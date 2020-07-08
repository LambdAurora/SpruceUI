/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.option;

import net.minecraft.client.options.Option;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.aperlambda.lambdacommon.utils.Nameable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents an option.
 *
 * @author LambdAurora
 * @version 1.5.6
 * @since 1.0.3
 */
public abstract class SpruceOption extends Option implements Nameable
{
    public final String key;

    public SpruceOption(@NotNull String key)
    {
        super(key);
        Objects.requireNonNull(key, "Cannot create an option without a key.");
        this.key = key;
    }

    @Override
    public @NotNull String getName()
    {
        return I18n.translate(this.key);
    }

    /**
     * Returns the display prefix text.
     *
     * @return The display prefix.
     */
    public @NotNull Text getPrefix()
    {
        return new TranslatableText(this.key);
    }

    /**
     * Returns the display text.
     *
     * @param value The value.
     * @return The display text.
     */
    public @NotNull Text getDisplayText(@NotNull Text value)
    {
        return new TranslatableText("spruceui.options.generic", this.getPrefix(), value);
    }
}
