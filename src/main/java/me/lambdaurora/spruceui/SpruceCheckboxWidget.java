/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import java.util.function.Consumer;

/**
 * Represents a checkbox widget.
 *
 * @author LambdAurora
 * @version 1.3.0
 * @since 1.0.0
 */
public class SpruceCheckboxWidget extends net.minecraft.client.gui.widget.CheckboxWidget
{
    private final Consumer<SpruceCheckboxWidget> action;

    public SpruceCheckboxWidget(int x, int y, int width, int height, String message, boolean checked, Consumer<SpruceCheckboxWidget> action)
    {
        super(x, y, width, height, message, checked);
        this.action = action;
    }

    @Override
    public void onPress()
    {
        super.onPress();
        this.action.accept(this);
    }
}
