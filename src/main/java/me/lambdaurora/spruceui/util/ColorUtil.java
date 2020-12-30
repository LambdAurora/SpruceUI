/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.util;

import org.jetbrains.annotations.Range;

/**
 * Utilities for color manipulation.
 */
public final class ColorUtil {
    private ColorUtil() {
        throw new UnsupportedOperationException("ColorUtil only contains static definitions.");
    }

    /**
     * Packs the given color into an ARGB integer.
     *
     * @param red the red color value
     * @param green the green color value
     * @param blue the blue color value
     * @param alpha the alpha value
     * @return the packed ARGB color
     */
    public static int packARGBColor(@Range(from = 0, to = 255) int red, @Range(from = 0, to = 255) int green, @Range(from = 0, to = 255) int blue, @Range(from = 0, to = 255) int alpha) {
        return ((alpha & 255) << 24) + ((red & 255) << 16) + ((green & 255) << 8) + ((blue) & 255);
    }

    /**
     * Unpacks the given ARGB color into an array of 4 integers in the following format: {@code {red, green, blue, alpha}}.
     *
     * @param color the ARGB color
     * @return the 4 color components as a RGBA array
     */
    public static int[] unpackARGBColor(int color) {
        return new int[]{(color >> 16) & 255, (color >> 8) & 255, color & 255, (color >> 24) & 255};
    }
}
