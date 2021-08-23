/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget;

import dev.lambdaurora.spruceui.border.Border;
import dev.lambdaurora.spruceui.border.EmptyBorder;

/**
 * Represents a widget with a border.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 2.0.0
 */
public interface WithBorder {
    /**
     * Returns whether this widget has a border or not.
     *
     * @return {@code true} if this widget has a border, else {@code false}.
     */
    default boolean hasBorder() {
        return this.getBorder().getThickness() != 0 && this.getBorder() != EmptyBorder.EMPTY_BORDER;
    }

    /**
     * Gets the border of this widget.
     *
     * @return the border
     */
    Border getBorder();

    /**
     * Sets the border of this widget.
     *
     * @param border the border
     */
    void setBorder(Border border);
}
