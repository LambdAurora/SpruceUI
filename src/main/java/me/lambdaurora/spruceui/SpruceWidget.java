/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a widget.
 *
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.6.0
 */
public interface SpruceWidget extends SprucePositioned, NavigationElement, Drawable, Element
{
    /**
     * Returns the position of the widget.
     *
     * @return the position
     */
    @NotNull Position getPosition();

    @Override
    default int getX()
    {
        return this.getPosition().getX();
    }

    @Override
    default int getY()
    {
        return this.getPosition().getY();
    }

    /**
     * Returns whether the widget is visible or not.
     *
     * @return true if the widget is visible, else false
     */
    boolean isVisible();

    /**
     * Sets whether the widget is visible or not.
     *
     * @param visible true if the widget is visible, else false
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
     * Returns whether the widget is focused or not.
     *
     * @return true if the widget is focused, else false
     */
    boolean isFocused();

    /**
     * Returns whether the widget is hovered or not.
     *
     * @return true if the widget is hovered, else false
     */
    boolean isMouseHovered();

    @Override
    default boolean isMouseOver(double mouseX, double mouseY)
    {
        return this.isVisible() && mouseX >= (double) this.getX() && mouseX < (double) (this.getX() + this.getWidth()) && mouseY >= (double) this.getY() && mouseY < (double) (this.getY() + this.getHeight());
    }
}
