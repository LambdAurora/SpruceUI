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
 * @version 1.4.0
 * @since 1.4.0
 */
public interface SprucePositioned
{
    default int getX() {
        return 0;
    }

    default int getY() {
        return 0;
    }
}
