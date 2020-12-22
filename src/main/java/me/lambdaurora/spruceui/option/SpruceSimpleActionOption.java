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
import me.lambdaurora.spruceui.widget.SpruceButtonWidget;
import me.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.aperlambda.lambdacommon.utils.Nameable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Represents an option with a simple action.
 *
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.0.1
 */
public class SpruceSimpleActionOption extends SpruceOption implements Nameable {
    private final Consumer<SpruceButtonWidget> action;

    public SpruceSimpleActionOption(@NotNull String key, @NotNull Consumer<SpruceButtonWidget> action, @Nullable Text tooltip) {
        super(key);
        this.action = action;
        this.setTooltip(tooltip);
    }

    public SpruceSimpleActionOption(@NotNull String key, @NotNull Consumer<SpruceButtonWidget> action) {
        this(key, action, null);
    }

    @Override
    public @NotNull SpruceWidget createWidget(@NotNull Position position, int width) {
        SpruceButtonWidget button = new SpruceButtonWidget(position, width, 20, new TranslatableText(this.key), action::accept);
        this.getOptionTooltip().ifPresent(button::setTooltip);
        return button;
    }
}
