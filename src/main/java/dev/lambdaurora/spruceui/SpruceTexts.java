/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui;

import net.minecraft.network.chat.Text;

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
	public static final Text CHAT_LINK_OPEN = Text.translatable("chat.link.open");

	/**
	 * Represents the button text to reset a keybinding.
	 *
	 * @see #getNarratorControlsReset(Text)
	 * @since 1.6.0
	 */
	public static final Text CONTROLS_RESET = Text.translatable("controls.reset");

	/**
	 * Represents the text "reset all" which is presents on a button in the controls screen.
	 *
	 * @since 1.6.0
	 */
	public static final Text CONTROLS_RESET_ALL = Text.translatable("controls.resetAll");

	public static final Text GUI_DONE = Text.translatable("gui.done");

	/**
	 * Represents "none" as text.
	 *
	 * @since 2.0.0
	 */
	public static final Text GUI_NONE = Text.translatable("gui.none");

	/**
	 * Represents the unbind action as text.
	 *
	 * @since 2.0.0
	 */
	public static final Text GUI_UNBIND = Text.translatable("spruceui.gui.unbind");

	public static final Text MENU_OPTIONS = Text.translatable("menu.options");

	/**
	 * Represents the text "not bound".
	 *
	 * @since 1.6.0
	 */
	public static final Text NOT_BOUND = Text.translatable("key.keyboard.unknown");

	/**
	 * Represents the option value "default" as text.
	 */
	public static final Text OPTIONS_GENERIC_DEFAULT = Text.translatable("generator.default");

	/**
	 * Represents the option value "fancy" as text.
	 */
	public static final Text OPTIONS_GENERIC_FANCY = Text.translatable("spruceui.options.generic.fancy");

	/**
	 * Represents the option value "fast" as text.
	 */
	public static final Text OPTIONS_GENERIC_FAST = Text.translatable("spruceui.options.generic.fast");

	/**
	 * Represents the option value "fastest" as text.
	 */
	public static final Text OPTIONS_GENERIC_FASTEST = Text.translatable("spruceui.options.generic.fastest");

	/**
	 * Represents the option value "simple" as text.
	 */
	public static final Text OPTIONS_GENERIC_SIMPLE = Text.translatable("spruceui.options.generic.simple");

	/**
	 * Represents the option value "on" as text.
	 */
	public static final Text OPTIONS_ON = Text.translatable("options.on");

	/**
	 * Represents the option value "off" as text.
	 */
	public static final Text OPTIONS_OFF = Text.translatable("options.off");

	/**
	 * Returns the option value whether if the option is ON or OFF.
	 *
	 * @param value {@code true} if the option is ON, else {@code false}
	 * @return the option value text
	 */
	public static Text getToggleText(boolean value) {
		return value ? OPTIONS_ON : OPTIONS_OFF;
	}

	/**
	 * Represents the option value "visible" as text.
	 */
	public static final Text OPTIONS_VISIBLE = Text.translatable("options.visible");

	/**
	 * Represents the option value "hidden" as text.
	 */
	public static final Text OPTIONS_HIDDEN = Text.translatable("options.hidden");

	/**
	 * Represents the "reset" text.
	 */
	public static final Text RESET_TEXT = Text.translatable("spruceui.reset");

	/**
	 * Returns the narrator text to describe the button which resets a keybinding.
	 *
	 * @param bindingName the binding name
	 * @return the text
	 * @see #CONTROLS_RESET
	 * @since 1.6.0
	 */
	public static Text getNarratorControlsReset(Text bindingName) {
		return Text.translatable("narrator.controls.reset", bindingName);
	}
}
