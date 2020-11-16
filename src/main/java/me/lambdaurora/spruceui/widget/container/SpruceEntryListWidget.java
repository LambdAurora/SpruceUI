/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.widget.container;

import com.google.common.collect.Lists;
import me.lambdaurora.spruceui.Position;
import me.lambdaurora.spruceui.widget.AbstractSpruceWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.List;

public abstract class SpruceEntryListWidget<E extends SpruceEntryListWidget.Entry<E>> extends AbstractSpruceParentWidget<E> implements ParentElement
{
    protected final Position anchor = Position.of(this, 0, 0);
    private final List<E> entries = new Entries();

    public SpruceEntryListWidget(@NotNull Position position, Class<E> entryClass)
    {
        super(position, entryClass);
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {

    }

    @Override
    public List<E> children()
    {
        return this.entries;
    }

    @Override
    public void setFocused(Element focused)
    {

    }

    private void setOwnerShip(E entry)
    {
        entry.getPosition().setAnchor(this.anchor);
    }

    @Environment(EnvType.CLIENT)
    class Entries extends AbstractList<E>
    {
        private final List<E> entries;

        private Entries()
        {
            this.entries = Lists.newArrayList();
        }

        public E get(int i)
        {
            return this.entries.get(i);
        }

        public int size()
        {
            return this.entries.size();
        }

        public E set(int i, E entry)
        {
            E entry2 = this.entries.set(i, entry);
            SpruceEntryListWidget.this.setOwnerShip(entry);
            return entry2;
        }

        public void add(int i, E entry)
        {
            this.entries.add(i, entry);
            SpruceEntryListWidget.this.setOwnerShip(entry);
        }

        public E remove(int i)
        {
            return this.entries.remove(i);
        }
    }

    public static abstract class Entry<E extends Entry<E>> extends AbstractSpruceWidget
    {
        public Entry()
        {
            super(Position.origin());
        }
    }
}
