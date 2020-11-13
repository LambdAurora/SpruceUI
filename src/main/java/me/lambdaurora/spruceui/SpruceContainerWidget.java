/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import me.lambdaurora.spruceui.navigation.NavigationDirection;
import me.lambdaurora.spruceui.widget.AbstractSpruceWidget;
import me.lambdaurora.spruceui.widget.NavigationElement;
import me.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class SpruceContainerWidget extends AbstractSpruceWidget
        implements ParentElement, NavigationElement
{
    private final List<SpruceWidget> children = new ArrayList<>();
    private @Nullable Element focused;
    private boolean dragging;

    public SpruceContainerWidget(Position position)
    {
        super(position);
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        this.children.forEach(child -> child.render(matrices, mouseX, mouseY, delta));
    }

    public void addChild(@NotNull SpruceWidget child)
    {
        this.children.add(child);
    }

    @Deprecated
    @Override
    public List<? extends Element> children()
    {
        return this.children;
    }

    public List<SpruceWidget> getChildren()
    {
        return this.children;
    }

    @Override
    public boolean isDragging()
    {
        return this.dragging;
    }

    @Override
    public void setDragging(boolean dragging)
    {
        this.dragging = dragging;
    }

    @Override
    public @Nullable Element getFocused()
    {
        return this.focused;
    }

    @Override
    public void setFocused(@Nullable Element focused)
    {
        this.focused = focused;
    }

    @Override
    public boolean onNavigation(@NotNull NavigationDirection direction, boolean tab)
    {
        SpruceWidget focused = (SpruceWidget) this.getFocused();
        boolean isNonNull = focused != null;
        if (!isNonNull || !focused.onNavigation(direction, tab)) {
            List<SpruceWidget> list = this.getChildren();
            int i = list.indexOf(focused);
            int next;
            if (isNonNull && i >= 0) next = i + (direction.isLookingForward() ? 1 : 0);
            else if (direction.isLookingForward()) next = 0;
            else next = list.size();

            ListIterator<SpruceWidget> iterator = list.listIterator(next);
            BooleanSupplier hasNext = direction.isLookingForward() ? iterator::hasNext : iterator::hasPrevious;
            Supplier<SpruceWidget> nextGetter = direction.isLookingForward() ? iterator::next : iterator::previous;

            SpruceWidget nextElement;
            do {
                if (!hasNext.getAsBoolean()) {
                    this.setFocused(null);
                    return false;
                }

                nextElement = nextGetter.get();
            } while (!nextElement.onNavigation(direction, tab));

            this.setFocused(nextElement);
        }
        return true;
    }
}
