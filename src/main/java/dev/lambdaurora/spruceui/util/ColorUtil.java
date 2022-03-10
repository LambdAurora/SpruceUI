/*
 * Copyright © 2020-2022 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.util;

import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Range;

/**
 * Utilities for color manipulation.
 *
 * @author LambdAurora
 * @version 3.0.0
 * @since 2.0.0
 */
public final class ColorUtil {
	private ColorUtil() {
		throw new UnsupportedOperationException("ColorUtil only contains static definitions.");
	}

	public static final int BLACK = 0xff000000;
	public static final int WHITE = 0xffffffff;
	public static final int TEXT_COLOR = 0xffe0e0e0;
	public static final int UNEDITABLE_COLOR = 0xff707070;

	/**
	 * Returns a color value between {@code 0.0} and {@code 1.0} using the integer value.
	 *
	 * @param colorComponent the color value as int
	 * @return the color value as float
	 */
	public static float floatColor(@Range(from = 0, to = 255) int colorComponent) {
		return colorComponent / 255.f;
	}

	/**
	 * Returns a color value between {@code 0} and {@code 255} using the float value.
	 *
	 * @param colorComponent the color value as float
	 * @return the color value as integer
	 */
	public static @Range(from = 0, to = 255) int intColor(float colorComponent) {
		return MathHelper.clamp((int) (colorComponent * 255.f), 0, 255);
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
		return new int[]{
				argbUnpackRed(color),
				argbUnpackGreen(color),
				argbUnpackBlue(color),
				argbUnpackAlpha(color)
		};
	}

	/**
	 * Extracts and unpacks the red component of the ARGB color.
	 *
	 * @param color the ARGB color
	 * @return the unpacked red component
	 */
	public static int argbUnpackRed(int color) {
		return (color >> 16) & 255;
	}

	/**
	 * Extracts and unpacks the green component of the ARGB color.
	 *
	 * @param color the ARGB color
	 * @return the unpacked green component
	 */
	public static int argbUnpackGreen(int color) {
		return (color >> 8) & 255;
	}

	/**
	 * Extracts and unpacks the blue component of the ARGB color.
	 *
	 * @param color the ARGB color
	 * @return the unpacked blue component
	 */
	public static int argbUnpackBlue(int color) {
		return color & 255;
	}

	/**
	 * Extracts and unpacks the alpha component of the ARGB color.
	 *
	 * @param color the ARGB color
	 * @return the unpacked alpha component
	 */
	public static int argbUnpackAlpha(int color) {
		return (color >> 24) & 255;
	}

	/**
	 * Darkens an ARGB color.
	 *
	 * @param color the ARGB color
	 * @return the darkened ARGB color
	 */
	public static int argbDarken(int color) {
		return argbMultiply(color, .7f);
	}

	/**
	 * Multiplies the RGB values of an ARGB color with a multiplier
	 *
	 * @param color the color
	 * @param multiplier the multiplier
	 * @return the multiplied color with untouched alpha value
	 */
	public static int argbMultiply(int color, float multiplier) {
		return argbMultiply(color, multiplier, argbUnpackAlpha(color));
	}

	/**
	 * Multiplies the RGB values of an ARGB color with a multiplier
	 *
	 * @param color the color
	 * @param multiplier the multiplier
	 * @param alpha the alpha value of the multiplied color
	 * @return the multiplied color
	 */
	public static int argbMultiply(int color, float multiplier, @Range(from = 0, to = 255) int alpha) {
		return packARGBColor(
				MathHelper.clamp((int) (argbUnpackRed(color) * multiplier), 0, 255),
				MathHelper.clamp((int) (argbUnpackGreen(color) * multiplier), 0, 255),
				MathHelper.clamp((int) (argbUnpackBlue(color) * multiplier), 0, 255),
				alpha);
	}

	/**
	 * Multiples two ARGB color.
	 *
	 * @param a an ARGB color
	 * @param b an ARGB color
	 * @return the multiplied color
	 */
	public static int argbMultiply(int a, int b) {
		float aRed = floatColor(argbUnpackRed(a));
		float aGreen = floatColor(argbUnpackGreen(a));
		float aBlue = floatColor(argbUnpackBlue(a));
		float aAlpha = floatColor(argbUnpackAlpha(a));

		float bRed = floatColor(argbUnpackRed(b));
		float bGreen = floatColor(argbUnpackGreen(b));
		float bBlue = floatColor(argbUnpackBlue(b));
		float bAlpha = floatColor(argbUnpackAlpha(b));

		return packARGBColor(
				intColor(aRed * bRed),
				intColor(aGreen * bGreen),
				intColor(aBlue * bBlue),
				intColor(aAlpha * bAlpha)
		);
	}
}
