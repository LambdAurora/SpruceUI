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
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Represents a cycling option.
 * <p>
 * Works the same as the vanilla one but can provide a tooltip.
 *
 * @author LambdAurora
 * @version 1.3.0
 * @since 1.0.0
 */
public class SpruceCyclingOption extends SpruceOption
{
    private final BiConsumer<GameOptions, Integer>                     setter;
    private final BiFunction<GameOptions, SpruceCyclingOption, String> messageProvider;
    private final Text                                                 tooltip;

    public SpruceCyclingOption(@NotNull String key, @NotNull BiConsumer<GameOptions, Integer> setter, @NotNull BiFunction<GameOptions, SpruceCyclingOption, String> messageProvider, @Nullable Text tooltip)
    {
        super(key);
        this.setter = setter;
        this.messageProvider = messageProvider;
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
        SpruceButtonWidget button = new SpruceButtonWidget(x, y, width, 20, this.getMessage(options), btn -> {
            this.cycle(options, 1);
            btn.setMessage(this.getMessage(options));
        });
        button.setTooltip(this.tooltip);
        return button;
    }

    public @NotNull String getMessage(@NotNull GameOptions options)
    {
        return this.messageProvider.apply(options, this);
    }
}
