/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
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
 */
public class SpruceCheckboxWidget extends net.minecraft.client.gui.widget.CheckboxWidget
{
    private final Consumer<SpruceCheckboxWidget> on_press;

    public SpruceCheckboxWidget(int x, int y, int width, int height, String message, boolean checked, Consumer<SpruceCheckboxWidget> on_press)
    {
        super(x, y, width, height, message, checked);
        this.on_press = on_press;
    }

    @Override
    public void onPress()
    {
        super.onPress();
        this.on_press.accept(this);
    }
}
