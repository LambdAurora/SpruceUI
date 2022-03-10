/*
 * Copyright © 2020-2022 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget.container;

import dev.lambdaurora.spruceui.navigation.NavigationDirection;
import dev.lambdaurora.spruceui.navigation.NavigationUtils;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Represents a parent widget.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 2.0.4
 */
public interface SpruceParentWidget<E extends SpruceWidget> extends SpruceWidget, Iterable<E> {
	/**
	 * Returns the children of this parent widget.
	 *
	 * @return the children
	 */
	List<E> children();

	@Override
	default Iterator<E> iterator() {
		return this.children().iterator();
	}

	/**
	 * Returns the focused element in this widget.
	 *
	 * @return the focused element in this widget, may be {@code null} if none is focused
	 */
	@Nullable E getFocused();

	/**
	 * Sets the focused element in this widget.
	 *
	 * @param focused the focused element in this widget, may be {@code null} to remove focus.
	 */
	void setFocused(@Nullable E focused);

	/**
	 * Returns the potential hovered element at the given mouse coordinates.
	 *
	 * @param mouseX the mouse X-coordinate
	 * @param mouseY the mouse Y-coordinate
	 * @return the hovered element if it exists, may be empty if none is present at the given coordinates
	 */
	default Optional<E> hoveredElement(double mouseX, double mouseY) {
		var it = this.children().iterator();

		E element;
		do {
			if (!it.hasNext()) {
				return Optional.empty();
			}

			element = it.next();
		} while (!element.isMouseOver(mouseX, mouseY));

		return Optional.of(element);
	}

	/* Navigation */

	@Override
	default boolean onNavigation(NavigationDirection direction, boolean tab) {
		if (this.requiresCursor()) return false;
		boolean result = NavigationUtils.tryNavigate(direction, tab, this.children(), this.getFocused(), this::setFocused,
				false);
		if (result)
			this.setFocused(true);
		return result;
	}
}
