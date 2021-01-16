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
import me.lambdaurora.spruceui.widget.SpruceTexturedButtonWidget;
import me.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.aperlambda.lambdacommon.utils.Nameable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an option with a simple action.
 *
 * @author LambdAurora
 * @version 2.0.0
 * @since 1.0.1
 */
public final class SpruceSimpleActionOption extends SpruceOption implements Nameable {
    private final ButtonFactory buttonFactory;
    private final SpruceButtonWidget.PressAction action;

    public SpruceSimpleActionOption(@NotNull String key, @NotNull ButtonFactory buttonFactory, @NotNull SpruceButtonWidget.PressAction action, @Nullable Text tooltip) {
        super(key);
        this.buttonFactory = buttonFactory;
        this.action = action;
        this.setTooltip(tooltip);
    }

    public SpruceSimpleActionOption(@NotNull String key, @NotNull ButtonFactory buttonFactory, @NotNull SpruceButtonWidget.PressAction action) {
        this(key, buttonFactory, action, null);
    }

    @Override
    public @NotNull SpruceWidget createWidget(@NotNull Position position, int width) {
        SpruceButtonWidget button = this.buttonFactory.build(position, width, new TranslatableText(this.key), this.action);
        this.getOptionTooltip().ifPresent(button::setTooltip);
        return button;
    }

    public static SpruceSimpleActionOption of(@NotNull String key, @NotNull SpruceButtonWidget.PressAction action) {
        return new SpruceSimpleActionOption(key,
                (position, width, message, action1) -> new SpruceButtonWidget(position, width, 20, message, action1),
                action);
    }

    public static SpruceSimpleActionOption of(@NotNull String key, @NotNull SpruceButtonWidget.PressAction action, @Nullable Text tooltip) {
        return new SpruceSimpleActionOption(key,
                (position, width, message, action1) -> new SpruceButtonWidget(position, width, 20, message, action1),
                action, tooltip);
    }

    public static SpruceSimpleActionOption reset(@NotNull SpruceButtonWidget.PressAction action) {
        return reset(action, null);
    }

    public static SpruceSimpleActionOption reset(@NotNull SpruceButtonWidget.PressAction action, @Nullable Text tooltip) {
        return new SpruceSimpleActionOption("spruceui.reset",
                (position, width, message, action1) -> new SpruceButtonWidget(position, width, 20, message, action1),
                action, tooltip);
    }

    public static SpruceSimpleActionOption textured(@NotNull String key, @NotNull SpruceButtonWidget.PressAction action,
                                                    int u, int v, int hoveredVOffset, Identifier texture) {
        return textured(key, action, u, v, hoveredVOffset, texture, null);
    }

    public static SpruceSimpleActionOption textured(@NotNull String key, @NotNull SpruceButtonWidget.PressAction action,
                                                    int u, int v, int hoveredVOffset, Identifier texture, @Nullable Text tooltip) {
        return new SpruceSimpleActionOption(key,
                (position, width, message, action1) -> new SpruceTexturedButtonWidget(position, width, 20, message, action1, u, v, hoveredVOffset, texture),
                action, tooltip);
    }

    public static SpruceSimpleActionOption textured(@NotNull String key, @NotNull SpruceButtonWidget.PressAction action,
                                                    int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight) {
        return textured(key, action, u, v, hoveredVOffset, texture, textureWidth, textureHeight, null);
    }

    public static SpruceSimpleActionOption textured(@NotNull String key, @NotNull SpruceButtonWidget.PressAction action,
                                                    int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight, @Nullable Text tooltip) {
        return new SpruceSimpleActionOption(key,
                (position, width, message, action1) ->
                        new SpruceTexturedButtonWidget(position, width, 20, message, action1, u, v, hoveredVOffset, texture, textureWidth, textureHeight),
                action, tooltip);
    }

    public static SpruceSimpleActionOption texturedWithMessage(@NotNull String key, SpruceButtonWidget.PressAction action,
                                                               int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight) {
        return texturedWithMessage(key, action, u, v, hoveredVOffset, texture, textureWidth, textureHeight, null);
    }

    public static SpruceSimpleActionOption texturedWithMessage(@NotNull String key, SpruceButtonWidget.PressAction action,
                                                               int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight, @Nullable Text tooltip) {
        return new SpruceSimpleActionOption(key,
                (position, width, message, action1) ->
                        new SpruceTexturedButtonWidget(position, width, 20, message, true, action1, u, v, hoveredVOffset, texture, textureWidth, textureHeight),
                action, tooltip);
    }

    /**
     * Represents the button factory.
     *
     * @version 2.0.0
     * @since 2.0.0
     */
    public interface ButtonFactory {
        SpruceButtonWidget build(Position position, int width, Text message, SpruceButtonWidget.PressAction action);
    }
}
