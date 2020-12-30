/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.widget;

import me.lambdaurora.spruceui.Position;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class SpruceIconButtonWidget extends AbstractSpruceIconButtonWidget {
    public SpruceIconButtonWidget(Position position, int width, int height, Text message, PressAction action) {
        super(position, width, height, message, action);
    }

    /**
     * Renders the icon of the button.
     *
     * @return the x-offset the icon creates
     */
    protected int renderIcon(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        return 0;
    }
}
