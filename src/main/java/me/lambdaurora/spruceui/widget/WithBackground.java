/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.widget;

import me.lambdaurora.spruceui.background.Background;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a widget with a background.
 *
 * @author LambdAurora
 * @version 2.0.0
 * @since 2.0.0
 */
public interface WithBackground {
    /**
     * Returns the background of this widget.
     *
     * @return the background
     */
    @NotNull Background getBackground();

    /**
     * Sets the background of this widget.
     *
     * @param background the background
     */
    void setBackground(@NotNull Background background);
}
