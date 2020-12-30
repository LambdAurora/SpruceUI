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
import me.lambdaurora.spruceui.background.Background;
import me.lambdaurora.spruceui.background.EmptyBackground;
import me.lambdaurora.spruceui.background.SimpleColorBackground;
import me.lambdaurora.spruceui.navigation.NavigationDirection;
import me.lambdaurora.spruceui.widget.SpruceWidget;
import me.lambdaurora.spruceui.widget.WithBackground;
import me.lambdaurora.spruceui.widget.container.AbstractSpruceParentWidget;
import me.lambdaurora.spruceui.widget.container.SpruceEntryListWidget;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
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
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.7.0
 */
public class SpruceTabbedWidget extends AbstractSpruceParentWidget<SpruceWidget> {
    private final SideTabList list;
    private final Position anchor;
    private boolean isLeft = false;

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

    /**
     * Returns the side tab list.
     *
     * @return the side tab list widget
     */
    public SideTabList getList() {
        return this.list;
    }

    public void addEntry(Text title, @Nullable Text description, ContainerFactory factory) {
        this.addEntry(title, description, factory.build(this.getWidth() - this.list.getWidth(), this.getHeight()));
    }

    public void addEntry(Text title, @Nullable Text description, SpruceWidget container) {
        Entry entry = this.list.addEntry(title, description, container);
        entry.container.getPosition().setAnchor(this.anchor);
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        super.setFocused(focused);
    }

    @Override
    public List<SpruceWidget> children() {
        if (this.list.getCurrentEntry() == null)
            return Collections.singletonList(this.list);
        return Arrays.asList(this.list, this.list.getCurrentEntry().container);
    }

    /* Navigation */

    @Override
    public boolean onNavigation(@NotNull NavigationDirection direction, boolean tab) {
        if (this.requiresCursor()) return false;

        if (tab) {
            boolean result = this.list.getCurrentEntry().container.onNavigation(direction, tab);
            this.setFocused(this.list.getCurrentEntry().container.isFocused() ? this.list.getCurrentEntry().container : null);
            return result;
        }

        if (direction.isHorizontal()) {
            if (direction == NavigationDirection.RIGHT) {
                if (this.list.getCurrentEntry().container.onNavigation(direction, tab))
                    this.setFocused(this.list.getCurrentEntry().container);
            } else {
                boolean result = this.list.getCurrentEntry().container.onNavigation(direction, tab);
                if (!result)
                    this.setFocused(this.list);
            }
            return true;
        } else {
            if (!this.isFocused()) {
                this.setFocused(true);
                this.setFocused(this.isLeft ? this.list : this.list.getCurrentEntry().container);
            } else {
                this.isLeft = this.getFocused() == this.list;
            }

            if (this.getFocused() == null) {
                this.setFocused(this.isLeft ? this.list : this.list.getCurrentEntry().container);
            }

            return this.getFocused().onNavigation(direction, tab);
        }
    }

    /* Render */

    @Override
    protected void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.list.render(matrices, mouseX, mouseY, delta);
        if (this.list.getCurrentEntry() != null)
            this.list.getCurrentEntry().container.render(matrices, mouseX, mouseY, delta);
    }

    public static class Entry extends SpruceEntryListWidget.Entry implements WithBackground {
        private final SideTabList parent;
        private final Text title;
        private final List<OrderedText> description;
        private final SpruceWidget container;
        private Background background = EmptyBackground.EMPTY_BACKGROUND;
        private boolean selected;

        protected Entry(SideTabList parent, Text title, @Nullable Text description, SpruceWidget container) {
            this.parent = parent;
            this.title = title;
            if (description == null) this.description = null;
            else this.description = this.client.textRenderer.wrapLines(description, this.parent.getWidth() - 18);
            this.container = container;

            if (container instanceof SpruceEntryListWidget<?>) {
                ((SpruceEntryListWidget<?>) container).setAllowOutsideHorizontalNavigation(true);
            }
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
        public @NotNull Background getBackground() {
            return this.background;
        }

        @Override
        public void setBackground(@NotNull Background background) {
            this.background = background;
        }

        public boolean isSelected() {
            return this.selected;
        }

        @Override
        public void setFocused(boolean focused) {
            super.setFocused(focused);
            if (focused)
                this.selected = true;
        }

        /* Input */

        @Override
        protected boolean onMouseClick(double mouseX, double mouseY, int button) {
            if (button == 0) {
                this.playDownSound();
                this.parent.setSelected(this);
                return true;
            }
            return false;
        }

        /* Render */

        @Override
        protected void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            DrawableHelper.drawTextWithShadow(matrices, this.client.textRenderer, this.title, this.getX() + 4, this.getY() + 5, 0xffffff);
            if (this.description != null) {
                int y = this.getY() + 8 + this.client.textRenderer.fontHeight;
                for (Iterator<OrderedText> it = this.description.iterator(); it.hasNext(); y += 9) {
                    OrderedText line = it.next();
                    this.client.textRenderer.draw(matrices, line, this.getX() + 8, y, 0xffffff);
                }
            }
        }

        @Override
        protected void renderBackground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            this.getBackground().render(matrices, this, 0, mouseX, mouseY, delta);
            if (this.selected || this.isMouseHovered())
                fill(matrices, this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight() - 4, 0x1affffff);
        }

        @Override
        public String toString() {
            return "SpruceTabbedWidget$Entry{" +
                    "title=" + this.title +
                    ", description=" + this.description +
                    ", position=" + this.getPosition() +
                    ", width=" + this.getWidth() +
                    ", height=" + this.getHeight() +
                    ", container=" + this.container +
                    ", selected=" + this.selected +
                    ", background=" + this.background +
                    '}';
        }
    }

    public static class SideTabList extends SpruceEntryListWidget<Entry> {
        private SpruceTabbedWidget.Entry currentEntry = null;

        protected SideTabList(@NotNull Position position, int width, int height) {
            super(position, width, height, 0, SpruceTabbedWidget.Entry.class);
            this.setRenderTransition(false);
        }

        public SpruceTabbedWidget.Entry getCurrentEntry() {
            return this.currentEntry;
        }

        @Override
        public void setFocused(boolean focused) {
            super.setFocused(focused);
            if (!focused)
                this.setSelected(this.currentEntry);
        }

        public void setSelected(SpruceTabbedWidget.Entry entry) {
            if (this.currentEntry != null)
                this.currentEntry.selected = false;
            entry.setFocused(true);
            this.setFocused(entry);
            this.currentEntry = entry;
        }

        public SpruceTabbedWidget.Entry addEntry(Text title, @Nullable Text description, SpruceWidget container) {
            SpruceTabbedWidget.Entry entry = new SpruceTabbedWidget.Entry(this, title, description, container);
            this.addEntry(entry);
            if (this.getCurrentEntry() == null)
                this.setSelected(entry);
            return entry;
        }

        /* Navigation */

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
