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
import org.aperlambda.lambdacommon.utils.Nameable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents an option.
 *
 * @author LambdAurora
 * @version 1.0.3
 * @since 1.0.3
 */
public abstract class SpruceOption extends Option implements Nameable
{
    protected final String key;

    public SpruceOption(@NotNull String key)
    {
        super(key);
        Objects.requireNonNull(key, "Cannot create an option without a key.");
        this.key = key;
    }

    /**
     * Returns the option key.
     *
     * @return The option key.
     */
    public @NotNull String get_key()
    {
        return this.key;
    }

    public @NotNull String get_name()
    {
        return I18n.translate(this.key);
    }
}
