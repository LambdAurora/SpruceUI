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
 * Represents navigation direction.
 *
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.7.0
 */
public enum NavigationDirection
{
    LEFT,
    RIGHT,
    UP,
    DOWN;

    public boolean isHorizontal()
    {
        return this == LEFT || this == RIGHT;
    }

    public boolean isVertical()
    {
        return this == UP || this == DOWN;
    }
}
