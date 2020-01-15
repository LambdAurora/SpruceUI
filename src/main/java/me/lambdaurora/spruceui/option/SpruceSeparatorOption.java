/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.option;

import me.lambdaurora.spruceui.SpruceSeparatorWidget;
import me.lambdaurora.spruceui.accessor.OptionAccessor;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a separator option.
 *
 * @author LambdAurora
 * @version 1.0.2
 * @since 1.0.1
 */
public class SpruceSeparatorOption extends Option
{
    private final boolean show_title;
    private final Text    tooltip;

    public SpruceSeparatorOption(@NotNull String key, boolean show_title, @Nullable Text tooltip)
    {
        super(key);
        this.show_title = show_title;
        this.tooltip = tooltip;
    }

    @Override
    public AbstractButtonWidget createButton(GameOptions options, int x, int y, int width)
    {
        SpruceSeparatorWidget separator = new SpruceSeparatorWidget(this.show_title ? new TranslatableText(((OptionAccessor) this).spruceui_get_key()) : null, x, y, width);
        separator.set_tooltip(this.tooltip);
        return new SpruceSeparatorWidget.ButtonWrapper(separator, 20);
    }
}
