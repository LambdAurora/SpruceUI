/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.navigation;

import me.lambdaurora.spruceui.widget.SpruceWidget;

import java.util.List;
import java.util.ListIterator;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Utilities for handling navigation.
 *
 * @author LambdAurora
 * @version 2.0.0
 * @since 2.0.0
 */
public final class NavigationUtils {
    private NavigationUtils() {
        throw new UnsupportedOperationException("NavigationUtils only contains static definitions.");
    }

    public static <E extends SpruceWidget> boolean tryNavigate(NavigationDirection direction, boolean tab, List<E> children, E focused, Consumer<E> setFocused, boolean alwaysFocus) {
        if (children.isEmpty())
            return false;
        if (!tab && alwaysFocus && focused != null) {
            int i = children.indexOf(focused);
            if ((!direction.isLookingForward() && i == 0) || (direction.isLookingForward() && i == children.size() - 1)) {
                boolean result = focused.onNavigation(direction, false);
                focused.setFocused(true);
                return result;
            }
        }
        if (focused == null || !focused.onNavigation(direction, tab)) {
            int i = children.indexOf(focused);
            int next;
            if (focused != null && i >= 0) next = i + (direction.isLookingForward() ? 1 : 0);
            else if (direction.isLookingForward()) next = 0;
            else next = children.size();

            ListIterator<E> iterator = children.listIterator(next);
            BooleanSupplier hasNext = direction.isLookingForward() ? iterator::hasNext : iterator::hasPrevious;
            Supplier<E> nextGetter = direction.isLookingForward() ? iterator::next : iterator::previous;

            E nextElement;
            do {
                if (!hasNext.getAsBoolean()) {
                    setFocused.accept(null);
                    return false;
                }

                nextElement = nextGetter.get();
            } while (!nextElement.onNavigation(direction, tab));

            setFocused.accept(nextElement);
        }
        return true;
    }
}
