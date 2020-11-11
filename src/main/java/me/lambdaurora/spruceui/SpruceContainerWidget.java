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
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public class SpruceContainerWidget extends AbstractSpruceWidget
        implements ParentElement, NavigationElement
{
    private final List<SpruceWidget> children = new ArrayList<>();

    public SpruceContainerWidget(Position position)
    {
        super(position);
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        this.children.forEach(child -> child.render(matrices, mouseX, mouseY, delta));
    }

    @Override
    public List<? extends Element> children()
    {
        return this.children;
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
}
