/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.widget;

import me.lambdaurora.spruceui.navigation.NavigationDirection;
import net.minecraft.client.gui.Element;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an element with navigation and controller input implementation.
 *
 * @author LambdAurora
 * @version 2.0.0
 * @since 2.0.0
 */
public interface SpruceElement extends Element {
    /**
     * Called when navigating in the menu.
     *
     * @param direction direction of navigation
     * @param tab {@code true} if the navigation was triggered by the tab key, else {@code false}
     * @return {@code true} if success, else {@code false}
     */
    default boolean onNavigation(@NotNull NavigationDirection direction, boolean tab) {
        if (this.requiresCursor()) return false;
        if (direction.isVertical()) {
            return this.changeFocus(direction == NavigationDirection.DOWN);
        }
        return false;
    }

    /**
     * Returns whether this is element requires a cursor to be used.
     *
     * @return {@code true} if a cursor is required, else {@code false}
     */
    default boolean requiresCursor() {
        return false;
    }
}
