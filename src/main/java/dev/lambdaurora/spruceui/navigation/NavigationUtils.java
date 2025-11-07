/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.navigation;

import dev.lambdaurora.spruceui.widget.SpruceWidget;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Utilities for handling navigation.
 *
 * @author LambdAurora
 * @version 9.0.0
 * @since 2.0.0
 */
public final class NavigationUtils {
	private NavigationUtils() {
		throw new UnsupportedOperationException("NavigationUtils only contains static definitions.");
	}

	public static <E extends SpruceWidget> boolean tryNavigate(
			NavigationEvent event, List<E> children, @Nullable E focused, Consumer<@Nullable E> setFocused, boolean alwaysFocus
	) {
		if (children.isEmpty())
			return false;
		if (!event.tab() && alwaysFocus && focused != null) {
			int i = children.indexOf(focused);
			if ((!event.isLookingForward() && i == 0) || (event.isLookingForward() && i == children.size() - 1)) {
				boolean result = focused.onNavigation(event);
				focused.setFocused(true);
				return result;
			}
		}
		if (focused == null || !focused.onNavigation(event)) {
			int i = children.indexOf(focused);
			int next;
			if (focused != null && i >= 0) next = i + (event.isLookingForward() ? 1 : 0);
			else if (event.isLookingForward()) next = 0;
			else next = children.size();

			var iterator = children.listIterator(next);
			BooleanSupplier hasNext = event.isLookingForward() ? iterator::hasNext : iterator::hasPrevious;
			Supplier<E> nextGetter = event.isLookingForward() ? iterator::next : iterator::previous;

			E nextElement;
			do {
				if (!hasNext.getAsBoolean()) {
					setFocused.accept(null);
					return false;
				}

				nextElement = nextGetter.get();
			} while (!nextElement.onNavigation(event));

			setFocused.accept(nextElement);
		}
		return true;
	}
}
