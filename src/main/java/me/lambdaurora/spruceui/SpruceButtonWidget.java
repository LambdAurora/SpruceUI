/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import me.lambdaurora.spruceui.widget.AbstractSprucePressableButtonWidget;
import me.lambdaurora.spruceui.wrapper.VanillaButtonWrapper;
import net.minecraft.text.Text;

/**
 * Represents a button widget.
 *
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.0.0
 */
public class SpruceButtonWidget extends AbstractSprucePressableButtonWidget implements Tooltipable
{
    private PressAction action;

    public SpruceButtonWidget(Position position, int width, int height, Text message, PressAction action)
    {
        super(position, width, height, message);
        this.action = action;
    }

    @Override
    public void onPress()
    {
        this.action.onPress(this);
    }

    public interface PressAction
    {
        void onPress(SpruceButtonWidget button);
    }
}
