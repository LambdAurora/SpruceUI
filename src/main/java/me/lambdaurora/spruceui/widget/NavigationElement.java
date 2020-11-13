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
 * Represents an element with navigation implementation.
 *
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.7.0
 */
public interface NavigationElement extends Element
{
    /**
     * Called when navigating in the menu.
     *
     * @param direction direction of navigation
     * @param tab {@code true} if the navigation was triggered by the tab key, else {@code false}
     * @return {@code true} if success, else {@code false}
     */
    default boolean onNavigation(@NotNull NavigationDirection direction, boolean tab)
    {
        if (direction.isVertical()) {
            return this.changeFocus(direction == NavigationDirection.DOWN);
        }
        return false;
    }
}
