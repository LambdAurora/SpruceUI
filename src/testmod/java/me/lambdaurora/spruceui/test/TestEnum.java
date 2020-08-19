/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.test;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.aperlambda.lambdacommon.utils.Nameable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a dummy enum.
 *
 * @author LambdAurora
 */
public enum TestEnum implements Nameable
{
    FIRST,
    SECOND,
    THIRD,
    ANOTHER_VALUE;

    private final Text text;

    TestEnum()
    {
        this.text = new LiteralText(this.getName());
    }

    /**
     * Returns the next enum value available.
     *
     * @return The next available enum value.
     */
    public @NotNull TestEnum next()
    {
        TestEnum[] v = values();
        if (v.length == this.ordinal() + 1)
            return v[0];
        return v[this.ordinal() + 1];
    }

    /**
     * Gets the text of this enum value.
     *
     * @return The text of this enum value.
     */
    public @NotNull Text getText()
    {
        return this.text;
    }

    @Override
    public @NotNull String getName()
    {
        return this.name().toLowerCase();
    }

}
