/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.widget.container.tabbed;

import me.lambdaurora.spruceui.Position;
import me.lambdaurora.spruceui.navigation.NavigationDirection;
import me.lambdaurora.spruceui.widget.SpruceWidget;
import me.lambdaurora.spruceui.widget.container.AbstractSpruceParentWidget;
import me.lambdaurora.spruceui.widget.container.SpruceEntryListWidget;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a container widget with tabs.
 *
 * @version 1.7.0
 * @since 1.7.0
 */
public class SpruceTabbedWidget extends AbstractSpruceParentWidget<SpruceWidget> {
    private final SideTabList list;
    private final Position anchor;
    private Entry currentEntry = null;

    public SpruceTabbedWidget(@NotNull Position position, int width, int height) {
        this(position, width, height, Math.max(100, width / 8));
    }

    public SpruceTabbedWidget(@NotNull Position position, int width, int height, int sideWidth) {
        super(position, SpruceWidget.class);
        this.width = width;
        this.height = height;
        this.list = new SideTabList(Position.of(position, 0, 0), sideWidth, height);
        this.anchor = Position.of(this, this.list.getWidth(), 0);
    }

    public void addEntry(Text title, @Nullable Text description, ContainerFactory factory) {
        this.addEntry(title, description, factory.build(this.getWidth() - this.list.getWidth(), this.getHeight()));
    }

    public void addEntry(Text title, @Nullable Text description, SpruceWidget container) {
        Entry entry = this.list.addEntry(title, description, container);
        entry.container.getPosition().setAnchor(this.anchor);
        if (this.currentEntry == null)
            this.list.setSelected(entry);
    }

    @Override
    public List<SpruceWidget> children() {
        if (this.currentEntry == null)
            return Collections.singletonList(this.list);
        return Arrays.asList(this.list, this.currentEntry.container);
    }

    @Override
    public boolean onNavigation(@NotNull NavigationDirection direction, boolean tab) {
        if (this.requiresCursor()) return false;

        if (tab) {
            boolean result = this.currentEntry.container.onNavigation(direction, tab);
            this.setFocused(this.currentEntry.container.isFocused() ? this.currentEntry.container : null);
            return result;
        }

        if (direction.isHorizontal()) {
            if (this.getFocused() == null) {
                if (direction == NavigationDirection.LEFT) this.setFocused(this.list);
                else this.setFocused(this.currentEntry.container);
                return this.getFocused() != null;
            } else if (this.getFocused() == this.list) {
                if (direction == NavigationDirection.LEFT) return false;
                else {
                    this.setFocused(this.currentEntry.container);
                    return true;
                }
            } else {
                if (direction == NavigationDirection.RIGHT) return false;
                else {
                    this.setFocused(this.list);
                    this.currentEntry.setFocused(true);
                    return true;
                }
            }
        } else {
            if (this.getFocused() == null) {
                this.setFocused(this.list);
            }

            return this.getFocused().onNavigation(direction, tab);
        }
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.list.render(matrices, mouseX, mouseY, delta);
        if (this.currentEntry != null)
            this.currentEntry.container.render(matrices, mouseX, mouseY, delta);
    }

    public static class Entry extends SpruceEntryListWidget.Entry {
        private final SideTabList parent;
        private final Text title;
        private final List<OrderedText> description;
        private final SpruceWidget container;

        protected Entry(SideTabList parent, Text title, @Nullable Text description, SpruceWidget container) {
            this.parent = parent;
            this.title = title;
            if (description == null) this.description = null;
            else this.description = this.client.textRenderer.wrapLines(description, this.parent.getWidth() - 18);
            this.container = container;
        }

        @Override
        public int getWidth() {
            return this.parent.getInnerWidth();
        }

        @Override
        public int getHeight() {
            return 8 + this.client.textRenderer.fontHeight
                    + (this.description == null ? 0 : this.description.size() * this.client.textRenderer.fontHeight + 4) + 4;
        }

        @Override
        protected boolean onMouseClick(double mouseX, double mouseY, int button) {
            if (button == 0) {
                this.playDownSound();
                this.parent.setSelected(this);
                return true;
            }
            return false;
        }

        @Override
        public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            DrawableHelper.drawTextWithShadow(matrices, this.client.textRenderer, this.title, this.getX() + 4, this.getY() + 4, 0xffffff);
            if (this.description != null) {
                int y = this.getY() + 8 + this.client.textRenderer.fontHeight;
                for (Iterator<OrderedText> it = this.description.iterator(); it.hasNext(); y += 9) {
                    OrderedText line = it.next();
                    this.client.textRenderer.draw(matrices, line, this.getX() + 8, y, 0xffffff);
                }
            }
        }

        @Override
        protected void renderBackground(MatrixStack matrices, int mouseX, int mouseY) {
            if (this.isFocusedOrHovered())
                fill(matrices, this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight() - 4, 0x1affffff);
        }
    }

    public class SideTabList extends SpruceEntryListWidget<Entry> {
        protected SideTabList(@NotNull Position position, int width, int height) {
            super(position, width, height, 0, SpruceTabbedWidget.Entry.class);
            this.setRenderTransition(false);
        }

        @Override
        public void setFocused(boolean focused) {
            super.setFocused(focused);
            if (!focused)
                this.setSelected(SpruceTabbedWidget.this.currentEntry);
        }

        public void setSelected(SpruceTabbedWidget.Entry entry) {
            entry.setFocused(true);
            this.setFocused(entry);
            SpruceTabbedWidget.this.currentEntry = entry;
        }

        public SpruceTabbedWidget.Entry addEntry(Text title, @Nullable Text description, SpruceWidget container) {
            SpruceTabbedWidget.Entry entry = new SpruceTabbedWidget.Entry(this, title, description, container);
            this.addEntry(entry);
            return entry;
        }

        @Override
        public boolean onNavigation(@NotNull NavigationDirection direction, boolean tab) {
            if (this.requiresCursor()) return false;
            SpruceTabbedWidget.Entry entry = this.getFocused();
            boolean result = super.onNavigation(direction, tab);
            if (result && entry != this.getFocused() && this.getFocused() != null) {
                this.setSelected(this.getFocused());
            }
            return result;
        }
    }

    public interface ContainerFactory {
        SpruceWidget build(int width, int height);
    }
}
