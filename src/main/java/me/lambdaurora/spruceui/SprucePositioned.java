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
 * Generic interface representing an object that provides a screen position.
 *
 * @author LambdAurora
 * @version 1.5.6
 * @since 1.4.0
 */
public interface SprucePositioned
{
    /**
     * Returns the X coordinate.
     *
     * @return The X coordinate.
     */
    default int getX()
    {
        return 0;
    }

    /**
     * Returns the Y coordinate.
     *
     * @return The Y coordinate.
     */
    default int getY()
    {
        return 0;
    }
}
