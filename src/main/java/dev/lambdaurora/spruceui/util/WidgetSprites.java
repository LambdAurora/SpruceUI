/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

/**
 * Represents widget sprites which depend on its state.
 * <p>
 * <b>Important note: this is only present for backporting, this will not be present in future versions!</b>
 */
@Environment(EnvType.CLIENT)
public record WidgetSprites(Identifier enabled, Identifier disabled, Identifier enabledFocused, Identifier disabledFocused) {
	public WidgetSprites(Identifier texture, Identifier focusedTexture) {
		this(texture, texture, focusedTexture, focusedTexture);
	}

	public WidgetSprites(Identifier enabled, Identifier disabled, Identifier enabledFocused) {
		this(enabled, disabled, enabledFocused, disabled);
	}

	/**
	 * {@return the relevant texture for the given state of the widget}
	 *
	 * @param focused {@code true} if the widget is focused, or {@code false} otherwise
	 * @param enabled {@code true} if the widget is enabled, or {@code false} otherwise
	 */
	public Identifier get(boolean enabled, boolean focused) {
		if (enabled) {
			return focused ? this.enabledFocused : this.enabled;
		} else {
			return focused ? this.disabledFocused : this.disabled;
		}
	}
}
