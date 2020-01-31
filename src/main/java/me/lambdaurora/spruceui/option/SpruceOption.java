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
 * @version 1.3.0
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

    public @NotNull String getName()
    {
        return I18n.translate(this.key);
    }
}
