/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.option;

import me.lambdaurora.spruceui.SpruceCheckboxWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.GameOptions;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents a boolean option.
 * <p>
 * Works the as {@link SpruceBooleanOption} but uses a checkbox instead.
 *
 * @author LambdAurora
 * @version 1.6.0
 * @since 1.6.0
 */
public class SpruceCheckboxBooleanOption extends SpruceBooleanOption
{
    public SpruceCheckboxBooleanOption(@NotNull String key, @NotNull Supplier<Boolean> getter, @NotNull Consumer<Boolean> setter, @Nullable Text tooltip)
    {
        super(key, getter, setter, tooltip);
    }

    @Override
    public @NotNull AbstractButtonWidget createButton(@NotNull GameOptions options, int x, int y, int width)
    {
        SpruceCheckboxWidget button = new SpruceCheckboxWidget(x, y, width, 20, this.getDisplayText(), this.get(), btn -> {
            this.set();
            btn.setMessage(this.getDisplayText());
        });
        this.getOptionTooltip().ifPresent(button::setTooltip);
        return button;
    }

    @Override
    public @NotNull Text getDisplayText()
    {
        return this.getPrefix();
    }

    @Override
    public @NotNull Text getDisplayText(@NotNull Text value)
    {
        return this.getPrefix();
    }

    /**
     * Returns a new SpruceUI Boolean Option from the Vanilla one.
     *
     * @param key     The option's key.
     * @param vanilla The Vanilla option.
     * @param tooltip The tooltip.
     * @return The SpruceUI option.
     */
    public static @NotNull SpruceCheckboxBooleanOption fromVanilla(@NotNull String key, @NotNull BooleanOption vanilla, @Nullable Text tooltip)
    {
        GameOptions options = MinecraftClient.getInstance().options;
        return new SpruceCheckboxBooleanOption(key, () -> vanilla.get(options), newValue -> vanilla.set(options, String.valueOf(newValue)), tooltip);
    }
}
