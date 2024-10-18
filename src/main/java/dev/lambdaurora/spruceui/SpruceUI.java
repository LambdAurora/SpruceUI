/*
 * Copyright © 2024 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui;

import net.minecraft.resources.Identifier;

/**
 * Contains common constants from SpruceUI.
 *
 * @author LambdAurora
 * @version 6.0.0
 * @since 6.0.0
 */
public final class SpruceUI {
	/**
	 * The namespace of SpruceUI, whose value is {@value}.
	 */
	public static final String NAMESPACE = "spruceui";

	/**
	 * {@return a SpruceUI identifier from the given path}
	 *
	 * @param path the path
	 */
	public static Identifier id(String path) {
		return Identifier.of(NAMESPACE, path);
	}

	private SpruceUI() {
		throw new UnsupportedOperationException("SpruceUi only contains static definitions.");
	}
}
