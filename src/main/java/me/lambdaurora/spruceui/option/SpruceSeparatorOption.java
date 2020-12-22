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
import me.lambdaurora.spruceui.widget.SpruceSeparatorWidget;
import me.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a separator option.
 *
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.0.1
 */
public class SpruceSeparatorOption extends SpruceOption {
    private final boolean showTitle;

    public SpruceSeparatorOption(@NotNull String key, boolean showTitle, @Nullable Text tooltip) {
        super(key);
        this.showTitle = showTitle;
        this.setTooltip(tooltip);
    }

    @Override
    public @NotNull SpruceWidget createWidget(@NotNull Position position, int width) {
        SpruceSeparatorWidget separator = new SpruceSeparatorWidget(position, width, this.showTitle ? new TranslatableText(this.key) : null);
        this.getOptionTooltip().ifPresent(separator::setTooltip);
        return separator;
    }
}
