/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import me.lambdaurora.spruceui.widget.container.SpruceContainerWidget;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Represents a tabbed container widget with the tab selection on one of the side.
 *
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.7.0
 */
public class SpruceSideTabbedWidget extends SpruceContainerWidget
{
    private boolean collapsible;

    public SpruceSideTabbedWidget(Position position)
    {
        super(position);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {

    }
}
