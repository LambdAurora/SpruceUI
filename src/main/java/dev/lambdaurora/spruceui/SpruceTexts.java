/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui;

import net.minecraft.network.chat.Component;

/**
 * Represents a text utility class.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 1.5.7
 */
public final class SpruceTexts {
	/**
	 * Represents the text usually present in tooltips to indicate that the link can be opened.
	 *
	 * @since 1.6.0
	 */
	public static final Component CHAT_LINK_OPEN = Component.translatable("chat.link.open");

	/**
	 * Represents the button text to reset a keybinding.
	 *
	 * @see #getNarratorControlsReset(Component)
	 * @since 1.6.0
	 */
	public static final Component CONTROLS_RESET = Component.translatable("controls.reset");

	/**
	 * Represents the text "reset all" which is presents on a button in the controls screen.
	 *
	 * @since 1.6.0
	 */
	public static final Component CONTROLS_RESET_ALL = Component.translatable("controls.resetAll");

	public static final Component GUI_DONE = Component.translatable("gui.done");

	/**
	 * Represents "none" as text.
	 *
	 * @since 2.0.0
	 */
	public static final Component GUI_NONE = Component.translatable("gui.none");

	/**
	 * Represents the unbind action as text.
	 *
	 * @since 2.0.0
	 */
	public static final Component GUI_UNBIND = Component.translatable("spruceui.gui.unbind");

	public static final Component MENU_OPTIONS = Component.translatable("menu.options");

	/**
	 * Represents the text "not bound".
	 *
	 * @since 1.6.0
	 */
	public static final Component NOT_BOUND = Component.translatable("key.keyboard.unknown");

	/**
	 * Represents the option value "default" as text.
	 */
	public static final Component OPTIONS_GENERIC_DEFAULT = Component.translatable("generator.default");

	/**
	 * Represents the option value "fancy" as text.
	 */
	public static final Component OPTIONS_GENERIC_FANCY = Component.translatable("spruceui.options.generic.fancy");

	/**
	 * Represents the option value "fast" as text.
	 */
	public static final Component OPTIONS_GENERIC_FAST = Component.translatable("spruceui.options.generic.fast");

	/**
	 * Represents the option value "fastest" as text.
	 */
	public static final Component OPTIONS_GENERIC_FASTEST = Component.translatable("spruceui.options.generic.fastest");

	/**
	 * Represents the option value "simple" as text.
	 */
	public static final Component OPTIONS_GENERIC_SIMPLE = Component.translatable("spruceui.options.generic.simple");

	/**
	 * Represents the option value "on" as text.
	 */
	public static final Component OPTIONS_ON = Component.translatable("options.on");

	/**
	 * Represents the option value "off" as text.
	 */
	public static final Component OPTIONS_OFF = Component.translatable("options.off");

	/**
	 * Returns the option value whether if the option is ON or OFF.
	 *
	 * @param value {@code true} if the option is ON, else {@code false}
	 * @return the option value text
	 */
	public static Component getToggleText(boolean value) {
		return value ? OPTIONS_ON : OPTIONS_OFF;
	}

	/**
	 * Represents the option value "visible" as text.
	 */
	public static final Component OPTIONS_VISIBLE = Component.translatable("options.visible");

	/**
	 * Represents the option value "hidden" as text.
	 */
	public static final Component OPTIONS_HIDDEN = Component.translatable("options.hidden");

	/**
	 * Represents the "reset" text.
	 */
	public static final Component RESET_TEXT = Component.translatable("spruceui.reset");

	/**
	 * Returns the narrator text to describe the button which resets a keybinding.
	 *
	 * @param bindingName the binding name
	 * @return the text
	 * @see #CONTROLS_RESET
	 * @since 1.6.0
	 */
	public static Component getNarratorControlsReset(Component bindingName) {
		return Component.translatable("narrator.controls.reset", bindingName);
	}
}
