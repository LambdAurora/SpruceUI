/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
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
import net.minecraft.client.options.Option;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Represents a cycling option.
 * <p>
 * Works the same as the vanilla one but can provide a tooltip.
 */
public class SpruceCyclingOption extends Option
{
    private final BiConsumer<GameOptions, Integer>                     setter;
    private final BiFunction<GameOptions, SpruceCyclingOption, String> message_provider;
    private final Text                                                 tooltip;

    public SpruceCyclingOption(@NotNull String key, @NotNull BiConsumer<GameOptions, Integer> setter, @NotNull BiFunction<GameOptions, SpruceCyclingOption, String> message_provider, @Nullable Text tooltip)
    {
        super(key);
        this.setter = setter;
        this.message_provider = message_provider;
        this.tooltip = tooltip;
    }

    public void cycle(@NotNull GameOptions options, int amount)
    {
        this.setter.accept(options, amount);
        options.write();
    }

    @Override
    public @NotNull AbstractButtonWidget createButton(@NotNull GameOptions options, int x, int y, int width)
    {
        SpruceButtonWidget button = new SpruceButtonWidget(x, y, width, 20, this.get_message(options), btn -> {
            this.cycle(options, 1);
            btn.setMessage(this.get_message(options));
        });
        button.set_tooltip(this.tooltip);
        return button;
    }

    public @NotNull String get_message(@NotNull GameOptions options)
    {
        return this.message_provider.apply(options, this);
    }
}
