/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.widget.container;

import me.lambdaurora.spruceui.Position;
import me.lambdaurora.spruceui.widget.AbstractSpruceWidget;
import me.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractSpruceParentWidget<E extends SpruceWidget> extends AbstractSpruceWidget implements ParentElement
{
    private final Class<E> childClass;
    private @Nullable E focused;
    private boolean dragging;

    public AbstractSpruceParentWidget(@NotNull Position position, Class<E> childClass)
    {
        super(position);
        this.childClass = childClass;
    }

    @Override
    public @Nullable E getFocused()
    {
        return this.focused;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setFocused(@Nullable Element focused)
    {
        if (focused == null)
            this.focused = null;
        else if (this.childClass.isInstance(focused)) {
            this.focused = (E) focused;
        }
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
}
