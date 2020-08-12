/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

/**
 * Represents a widget.
 *
 * @author LambdAurora
 * @version 1.6.0
 * @since 1.6.0
 */
public interface SpruceWidget extends SprucePositioned
{
    /**
     * Returns whether the widget is visible or not.
     *
     * @return True if the widget is visible, else false.
     */
    boolean isVisible();

    /**
     * Sets whether the widget is visible or not.
     *
     * @param visible True if the widget is visible, else false.
     */
    void setVisible(boolean visible);

    /**
     * Returns the widget width.
     *
     * @return The width.
     */
    int getWidth();

    /**
     * Returns whether the widget is focused or not.
     *
     * @return True if the widget is focused, else false.
     */
    boolean isFocused();

    /**
     * Returns whether the widget is hovered or not.
     *
     * @return True if the widget is hovered, else false.
     */
    boolean isHovered();
}
