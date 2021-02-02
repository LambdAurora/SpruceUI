/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.option;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.widget.SpruceCheckboxWidget;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
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
 * @version 3.0.0
 * @since 1.6.0
 */
public class SpruceCheckboxBooleanOption extends SpruceBooleanOption {
    public SpruceCheckboxBooleanOption(String key, Supplier<Boolean> getter, Consumer<Boolean> setter, @Nullable Text tooltip) {
        super(key, getter, setter, tooltip);
    }

    public SpruceCheckboxBooleanOption(String key, Supplier<Boolean> getter, Consumer<Boolean> setter, @Nullable Text tooltip, boolean colored) {
        super(key, getter, setter, tooltip, colored);
    }

    @Override
    public SpruceWidget createWidget(Position position, int width) {
        SpruceCheckboxWidget button = new SpruceCheckboxWidget(position, width, 20, this.getDisplayText(), (btn, newValue) -> {
            this.set();
            btn.setMessage(this.getDisplayText());
        }, this.get());
        button.setColored(this.isColored());
        this.getOptionTooltip().ifPresent(button::setTooltip);
        return button;
    }

    @Override
    public Text getDisplayText() {
        return this.getPrefix();
    }

    @Override
    public Text getDisplayText(@NotNull Text value) {
        return this.getPrefix();
    }
}
