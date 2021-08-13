/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.util;

import org.jetbrains.annotations.NotNull;

/**
 * Provides some common utils.
 *
 * @author LambdAurora
 * @version 3.2.1
 * @since 3.2.1
 */
public class SpruceUtil {
    /**
     * Parses an integer from a string. If the value is not a integer it returns {@code 0}.
     *
     * @param value a {@code String} which represents an integer
     * @return the parsed integer, if parsing fails returns {@code 0}.
     */
    public static int parseIntFromString(@NotNull String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
