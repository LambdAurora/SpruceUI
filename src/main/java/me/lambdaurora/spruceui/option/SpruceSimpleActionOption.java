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
import net.minecraft.text.TranslatableText;
import org.aperlambda.lambdacommon.utils.Nameable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Represents an option with a simple action.
 *
 * @author LambdAurora
 * @version 1.5.0
 * @since 1.0.1
 */
public class SpruceSimpleActionOption extends SpruceOption implements Nameable
{
    private final Consumer<AbstractButtonWidget> action;
    private final TranslatableText               tooltip;

    public SpruceSimpleActionOption(@NotNull String key, @NotNull Consumer<AbstractButtonWidget> action, @Nullable TranslatableText tooltip)
    {
        super(key);
        this.action = action;
        this.tooltip = tooltip;
    }

    public SpruceSimpleActionOption(@NotNull String key, @NotNull Consumer<AbstractButtonWidget> action)
    {
        this(key, action, null);
    }

    @Override
    public AbstractButtonWidget createButton(GameOptions options, int x, int y, int width)
    {
        SpruceButtonWidget button = new SpruceButtonWidget(x, y, width, 20, new TranslatableText(this.key), action::accept);
        button.setTooltip(this.tooltip);
        return button;
    }
}
