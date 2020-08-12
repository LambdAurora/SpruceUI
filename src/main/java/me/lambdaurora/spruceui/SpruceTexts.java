/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a text utility class.
 *
 * @author LambdAurora
 * @version 1.6.0
 * @since 1.5.7
 */
public final class SpruceTexts
{
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

    public static final Text MENU_OPTIONS = new TranslatableText("menu.options");

    /**
     * Represents the text "not bound".
     *
     * @since 1.6.0
     */
    public static final Text NOT_BOUND = new TranslatableText("spruceui.not_bound");

    /**
     * Represents the option value "default" as text.
     */
    public static final Text OPTIONS_GENERIC_DEFAULT = new TranslatableText("spruceui.options.generic.default");

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
     * Represents the option value "none" as text.
     */
    public static final Text OPTIONS_GENERIC_NONE = new TranslatableText("spruceui.options.generic.none");

    /**
     * Represents the option value "simple" as text.
     */
    public static final Text OPTIONS_GENERIC_SIMPLE = new TranslatableText("spruceui.options.generic.simple");

    /**
     * Represents the option value "unbound" as text.
     */
    public static final Text OPTIONS_GENERIC_UNBOUND = new TranslatableText("spruceui.options.generic.unbound");

    /**
     * Represents the option value "on" as text.
     */
    public static final Text OPTIONS_ON = new TranslatableText("options.on");

    /**
     * Represents the option value "off" as text.
     */
    public static final Text OPTIONS_OFF = new TranslatableText("options.off");

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
     * @param bindingName The binding name.
     * @return The text.
     * @see #CONTROLS_RESET
     * @since 1.6.0
     */
    public static Text getNarratorControlsReset(@NotNull Text bindingName)
    {
        return new TranslatableText("narrator.controls.reset", bindingName);
    }
}
