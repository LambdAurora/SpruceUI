/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a tabbed container widget with the tab selection on one of the side.
 *
 * @author LambdAurora
 * @version 1.6.5
 * @since 1.6.5
 */
public class SpruceSideTabbedWidget extends SpruceContainerWidget
{
    private boolean collapsible;


    @Override
    public List<? extends Element> children()
    {
        return null;
    }

    @Override
    public boolean isDragging()
    {
        return false;
    }

    @Override
    public void setDragging(boolean dragging)
    {

    }

    @Override
    public Element getFocused()
    {
        return null;
    }

    @Override
    public void setFocused(Element focused)
    {

    }

    @Override
    public boolean onNavigation(@NotNull NavigationDirection direction)
    {
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {

    }
}
