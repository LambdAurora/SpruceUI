/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

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
	public static final Text CHAT_LINK_OPEN = new TranslatableText("chat.link.open");

	/**
	 * Represents the button text to reset a keybinding.
	 *
	 * @see #getNarratorControlsReset(Text)
	 * @since 1.6.0
	 */
	public static final Text CONTROLS_RESET = new TranslatableText("controls.reset");

	/**
	 * Represents the text "reset all" which is presents on a button in the controls screen.
	 *
	 * @since 1.6.0
	 */
	public static final Text CONTROLS_RESET_ALL = new TranslatableText("controls.resetAll");

	public static final Text GUI_DONE = new TranslatableText("gui.done");

	/**
	 * Represents "none" as text.
	 *
	 * @since 2.0.0
	 */
	public static final Text GUI_NONE = new TranslatableText("gui.none");

	/**
	 * Represents the unbind action as text.
	 *
	 * @since 2.0.0
	 */
	public static final Text GUI_UNBIND = new TranslatableText("spruceui.gui.unbind");

	public static final Text MENU_OPTIONS = new TranslatableText("menu.options");

	/**
	 * Represents the text "not bound".
	 *
	 * @since 1.6.0
	 */
	public static final Text NOT_BOUND = new TranslatableText("key.keyboard.unknown");

	/**
	 * Represents the option value "default" as text.
	 */
	public static final Text OPTIONS_GENERIC_DEFAULT = new TranslatableText("generator.default");

	/**
	 * Represents the option value "fancy" as text.
	 */
	public static final Text OPTIONS_GENERIC_FANCY = new TranslatableText("spruceui.options.generic.fancy");

	/**
	 * Represents the option value "fast" as text.
	 */
	public static final Text OPTIONS_GENERIC_FAST = new TranslatableText("spruceui.options.generic.fast");

	/**
	 * Represents the option value "fastest" as text.
	 */
	public static final Text OPTIONS_GENERIC_FASTEST = new TranslatableText("spruceui.options.generic.fastest");

	/**
	 * Represents the option value "simple" as text.
	 */
	public static final Text OPTIONS_GENERIC_SIMPLE = new TranslatableText("spruceui.options.generic.simple");

	/**
	 * Represents the option value "on" as text.
	 */
	public static final Text OPTIONS_ON = new TranslatableText("options.on");

	/**
	 * Represents the option value "off" as text.
	 */
	public static final Text OPTIONS_OFF = new TranslatableText("options.off");

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
	public static final Text OPTIONS_VISIBLE = new TranslatableText("options.visible");

	/**
	 * Represents the option value "hidden" as text.
	 */
	public static final Text OPTIONS_HIDDEN = new TranslatableText("options.hidden");

	/**
	 * Represents the "reset" text.
	 */
	public static final Text RESET_TEXT = new TranslatableText("spruceui.reset");

	/**
	 * Returns the narrator text to describe the button which resets a keybinding.
	 *
	 * @param bindingName the binding name
	 * @return the text
	 * @see #CONTROLS_RESET
	 * @since 1.6.0
	 */
	public static Text getNarratorControlsReset(Text bindingName) {
		return new TranslatableText("narrator.controls.reset", bindingName);
	}
}
