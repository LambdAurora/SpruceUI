/*
 * Copyright © 2024 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;

/**
 * Contains the identifiers of various useful textures.
 *
 * @author LambdAurora
 * @version 5.1.0
 * @since 5.1.0
 */
public final class SpruceTextures {
	private SpruceTextures() {
		throw new UnsupportedOperationException("SpruceTextures only contain static definitions.");
	}

	/* Backgrounds */

	public static final Identifier MENU_LIST_BACKGROUND = Identifier.ofDefault("textures/gui/menu_list_background.png");
	public static final Identifier INWORLD_MENU_LIST_BACKGROUND = Identifier.ofDefault("textures/gui/inworld_menu_list_background.png");

	/**
	 * The dirt background texture used in pre-1.20.5 versions.
	 */
	public static final Identifier LEGACY_OPTIONS_BACKGROUND = Identifier.of("spruceui", "textures/gui/legacy_options_background.png");

	/* Border */

	public static final Identifier MENU_TOP_BORDER = Screen.HEADER_SEPARATOR;
	public static final Identifier INWORLD_MENU_TOP_BORDER = Screen.INWORLD_HEADER_SEPARATOR;
	public static final Identifier MENU_TOP_RIGHT_BORDER = Identifier.of("spruceui", "textures/gui/top_right_border_separator.png");
	public static final Identifier INWORLD_MENU_TOP_RIGHT_BORDER = Identifier.of("spruceui", "textures/gui/inworld_top_right_border_separator.png");
	public static final Identifier MENU_RIGHT_BORDER = Identifier.of("spruceui", "textures/gui/right_border_separator.png");
	public static final Identifier INWORLD_MENU_RIGHT_BORDER = Identifier.of("spruceui", "textures/gui/inworld_right_border_separator.png");
	public static final Identifier MENU_BOTTOM_RIGHT_BORDER = Identifier.of("spruceui", "textures/gui/bottom_right_border_separator.png");
	public static final Identifier INWORLD_MENU_BOTTOM_RIGHT_BORDER = Identifier.of("spruceui", "textures/gui/inworld_bottom_right_border_separator.png");
	public static final Identifier MENU_BOTTOM_BORDER = Screen.FOOTER_SEPARATOR;
	public static final Identifier INWORLD_MENU_BOTTOM_BORDER = Screen.INWORLD_FOOTER_SEPARATOR;
}
