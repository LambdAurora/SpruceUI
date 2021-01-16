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
import me.lambdaurora.spruceui.widget.SpruceCheckboxWidget;
import me.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.MinecraftClient;
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
 * @version 2.0.0
 * @since 1.6.0
 */
public class SpruceCheckboxBooleanOption extends SpruceBooleanOption {
    public SpruceCheckboxBooleanOption(@NotNull String key, @NotNull Supplier<Boolean> getter, @NotNull Consumer<Boolean> setter, @Nullable Text tooltip) {
        super(key, getter, setter, tooltip);
    }

    public SpruceCheckboxBooleanOption(@NotNull String key, @NotNull Supplier<Boolean> getter, @NotNull Consumer<Boolean> setter, @Nullable Text tooltip, boolean colored) {
        super(key, getter, setter, tooltip, colored);
    }

    @Override
    public @NotNull SpruceWidget createWidget(@NotNull Position position, int width) {
        SpruceCheckboxWidget button = new SpruceCheckboxWidget(position, width, 20, this.getDisplayText(), (btn, newValue) -> {
            this.set();
            btn.setMessage(this.getDisplayText());
        }, this.get());
        button.setColored(this.isColored());
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

    /**
     * Returns a new SpruceUI Boolean Option from the Vanilla one.
     *
     * @param key the option's key
     * @param vanilla the Vanilla option
     * @param tooltip the tooltip
     * @return the SpruceUI option
     */
    public static @NotNull SpruceCheckboxBooleanOption fromVanilla(@NotNull String key, @NotNull BooleanOption vanilla, @Nullable Text tooltip) {
        GameOptions options = MinecraftClient.getInstance().options;
        return new SpruceCheckboxBooleanOption(key, () -> vanilla.get(options), newValue -> vanilla.set(options, String.valueOf(newValue)), tooltip);
    }
}
