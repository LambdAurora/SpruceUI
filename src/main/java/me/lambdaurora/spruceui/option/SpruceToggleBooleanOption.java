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
import me.lambdaurora.spruceui.widget.SpruceToggleSwitch;
import me.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents a boolean option.
 * <p>
 * Works the as {@link SpruceBooleanOption} but uses a toggle switch instead.
 *
 * @author LambdAurora
 * @version 2.0.1
 * @since 2.0.0
 */
public class SpruceToggleBooleanOption extends SpruceBooleanOption {
    public SpruceToggleBooleanOption(@NotNull String key, @NotNull Supplier<Boolean> getter, @NotNull Consumer<Boolean> setter, @Nullable Text tooltip) {
        super(key, getter, setter, tooltip, false);
    }

    @Override
    public @NotNull SpruceWidget createWidget(@NotNull Position position, int width) {
        SpruceToggleSwitch button = new SpruceToggleSwitch(position, width, 20, this.getDisplayText(), (btn, newValue) -> {
            this.set();
            btn.setMessage(this.getDisplayText());
        }, this.get());
        this.getOptionTooltip().ifPresent(button::setTooltip);
        return button;
    }

    @Override
    public @NotNull Text getDisplayText() {
        return this.getPrefix();
    }

    @Override
    public @NotNull Text getDisplayText(@NotNull Text value) {
        return this.getPrefix();
    }
}
