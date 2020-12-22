/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.widget;

import me.lambdaurora.spruceui.Position;
import me.lambdaurora.spruceui.SprucePositioned;
import net.minecraft.client.gui.Drawable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a widget.
 *
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.6.0
 */
public interface SpruceWidget extends SprucePositioned, SpruceElement, Drawable {
    /**
     * Returns the position of the widget.
     *
     * @return the position
     */
    @NotNull Position getPosition();

    @Override
    default int getX() {
        return this.getPosition().getX();
    }

    @Override
    default int getY() {
        return this.getPosition().getY();
    }

    /**
     * Returns whether the widget is visible or not.
     *
     * @return {@code true} if the widget is visible, else {@code false}
     */
    boolean isVisible();

    /**
     * Sets whether the widget is visible or not.
     *
     * @param visible {@code true} if the widget is visible, else {@code false}
     */
    void setVisible(boolean visible);

    /**
     * Returns the widget width.
     *
     * @return the width
     */
    int getWidth();

    /**
     * Returns the widget height.
     *
     * @return the height
     */
    int getHeight();

    /**
     * Returns whether this widget is active or not.
     *
     * @return {@code true} if the widget is active, else {@code false}
     */
    default boolean isActive() {
        return true;
    }

    /**
     * Returns whether the widget is focused or not.
     *
     * @return {@code true} if the widget is focused, else {@code false}
     */
    boolean isFocused();

    /**
     * Sets whether the widget is focused or not.
     *
     * @param focused {@code true} if the widget is focused, else {@code false}
     */
    void setFocused(boolean focused);

    /**
     * Returns whether the widget is hovered or not.
     *
     * @return {@code true} if the widget is hovered, else {@code false}
     */
    boolean isMouseHovered();

    /**
     * Returns whether the widget is focused or hovered.
     *
     * @return {@code true} if the widget is focused or hovered, else {@code false}
     */
    default boolean isFocusedOrHovered() {
        return this.isMouseHovered() || this.isFocused();
    }

    @Override
    default boolean isMouseOver(double mouseX, double mouseY) {
        return this.isVisible() && mouseX >= (double) this.getX() && mouseX < (double) (this.getX() + this.getWidth()) && mouseY >= (double) this.getY() && mouseY < (double) (this.getY() + this.getHeight());
    }
}
