/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget.container.tabbed;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.background.Background;
import dev.lambdaurora.spruceui.background.EmptyBackground;
import dev.lambdaurora.spruceui.border.MenuBorder;
import dev.lambdaurora.spruceui.navigation.NavigationEvent;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import dev.lambdaurora.spruceui.util.ColorUtil;
import dev.lambdaurora.spruceui.widget.AbstractSpruceWidget;
import dev.lambdaurora.spruceui.widget.SpruceSeparatorWidget;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import dev.lambdaurora.spruceui.widget.WithBackground;
import dev.lambdaurora.spruceui.widget.container.AbstractSpruceParentWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceEntryListWidget;
import net.minecraft.client.gui.navigation.ScreenAxis;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Represents a container widget with tabs.
 *
 * @author LambdAurora
 * @version 9.0.0
 * @since 2.0.0
 */
public class SpruceTabbedWidget extends AbstractSpruceParentWidget<SpruceWidget> {
	private final @Nullable List<FormattedCharSequence> title;
	private final SideTabList list;
	private final Position anchor;
	private boolean isLeft = false;

	public SpruceTabbedWidget(Position position, int width, int height, @Nullable Component title) {
		this(position, width, height, title, Math.max(100, width / 8));
	}

	public SpruceTabbedWidget(Position position, int width, int height, @Nullable Component title, int sideWidth) {
		super(position, SpruceWidget.class);
		this.width = width;
		this.height = height;
		this.title = title != null ? this.client.font.split(title, sideWidth - 8) : null;
		int sideTopOffset = title == null ? 0 : 6 + (this.title.size() * this.client.font.lineHeight + 4);
		this.list = new SideTabList(
				Position.of(position, 0, sideTopOffset),
				sideWidth,
				height - sideTopOffset
		);
		this.anchor = Position.of(this, this.list.getWidth(), 0);
	}

	public SpruceTabbedWidget(Position position, int width, int height, @Nullable Component title, int sideWidth,
			int sideTopOffset) {
		super(position, SpruceWidget.class);
		this.width = width;
		this.height = height;
		this.title = title != null ? this.client.font.split(title, sideWidth - 8) : null;
		this.list = new SideTabList(
				Position.of(position, 0, sideTopOffset),
				sideWidth,
				height - sideTopOffset
		);
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

	public void addTabEntry(Component title, @Nullable Component description, ContainerFactory factory) {
		this.addTabEntry(title, description, factory.build(this.getWidth() - this.list.getWidth(), this.getHeight()));
	}

	public void addTabEntry(Component title, @Nullable Component description, AbstractSpruceWidget container) {
		var entry = this.list.addTabEntry(title, description, container);
		entry.container.getPosition().setAnchor(this.anchor);
	}

	public void addSeparatorEntry(Component title) {
		this.list.addSeparatorEntry(title);
	}

	/**
	 * Removes a tab entry by title.
	 *
	 * @param title the title of the tab entry to remove
	 * @return {@code true} if the tab entry has been removed, {@code false} otherwise
	 */
	public boolean removeTabEntry(Component title) {
		return this.list.removeTabEntry(title);
	}

	/**
	 * Removes a separator entry by title.
	 *
	 * @param title the title of the separator entry to remove
	 * @return {@code true} if the separator entry has been removed, {@code false} otherwise
	 */
	public boolean removeSeparatorEntry(Component title) {
		return this.list.removeSeparatorEntry(title);
	}

	@Override
	public void setFocused(@Nullable SpruceWidget focused) {
		super.setFocused(focused);
	}

	@Override
	public List<SpruceWidget> children() {
		if (this.list.getCurrentTab() == null)
			return List.of(this.list);
		return List.of(this.list, this.list.getCurrentTab().container);
	}

	/* Navigation */

	@Override
	public boolean onNavigation(NavigationEvent event) {
		if (this.requiresCursor()) return false;

		if (this.list.getCurrentTab() == null)
			return super.onNavigation(event);

		if (event.tab()) {
			boolean result = this.list.getCurrentTab().container.onNavigation(event);
			this.setFocused(this.list.getCurrentTab().container.isFocused() ? this.list.getCurrentTab().container : null);
			return result;
		}

		if (event.direction().getAxis() == ScreenAxis.HORIZONTAL) {
			if (event.isLookingForward()) {
				if (this.list.getCurrentTab().container.onNavigation(event))
					this.setFocused(this.list.getCurrentTab().container);
			} else if (this.getFocused() != this.list) {
				boolean result = this.list.getCurrentTab().container.onNavigation(event);
				if (!result)
					this.setFocused(this.list);
			}
			return true;
		} else {
			if (!this.isFocused()) {
				this.setFocused(true);
				this.setFocused(this.isLeft ? this.list : this.list.getCurrentTab().container);
			} else {
				this.isLeft = this.getFocused() == this.list;
			}

			if (this.getFocused() == null) {
				this.setFocused(this.isLeft ? this.list : this.list.getCurrentTab().container);
			}

			return this.getFocused().onNavigation(event);
		}
	}

	/* Render */

	@Override
	protected void extractWidgetRenderState(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
		if (this.title != null) {
			int y = this.getY() + 6;
			for (var it = this.title.iterator(); it.hasNext(); y += 9) {
				var line = it.next();
				graphics.centeredShadowedText(
						this.client.font, line, this.getX() + this.list.getWidth() / 2, y, ColorUtil.WHITE
				);
			}
		}
		this.list.extractRenderState(graphics, mouseX, mouseY, delta);
		if (this.list.getCurrentTab() != null)
			this.list.getCurrentTab().container.extractRenderState(graphics, mouseX, mouseY, delta);
	}

	public static abstract class Entry extends SpruceEntryListWidget.Entry implements WithBackground {
		protected final SideTabList parent;
		private final Component title;
		private Background background = EmptyBackground.EMPTY_BACKGROUND;

		protected Entry(SideTabList parent, Component title) {
			this.parent = parent;
			this.title = title;
		}

		@Override
		public int getWidth() {
			return this.parent.getInnerWidth();
		}

		/**
		 * Returns the title of this entry.
		 *
		 * @return the title
		 */
		public Component getTitle() {
			return this.title;
		}

		@Override
		public Background getBackground() {
			return this.background;
		}

		@Override
		public void setBackground(Background background) {
			this.background = background;
		}

		/* Rendering */

		@Override
		protected void extractBackground(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
			this.getBackground().extractRenderState(graphics, this, 0, mouseX, mouseY, delta);
		}
	}

	public static class TabEntry extends Entry {
		private final List<FormattedCharSequence> title;
		private final @Nullable List<FormattedCharSequence> description;
		private final AbstractSpruceWidget container;
		private boolean selected;

		protected TabEntry(SideTabList parent, Component title, @Nullable Component description, AbstractSpruceWidget container) {
			super(parent, title);
			this.title = this.client.font.split(title, this.parent.getWidth() - 18);
			if (description == null) this.description = null;
			else this.description = this.client.font.split(description, this.parent.getWidth() - 18);
			this.container = container;

			if (container instanceof SpruceEntryListWidget<?> listWidget) {
				listWidget.setAllowOutsideHorizontalNavigation(true);
			}
		}

		@Override
		public int getHeight() {
			return 4 + (this.title.size() * this.client.font.lineHeight + 4)
					+ (this.description == null ? 0 : this.description.size() * this.client.font.lineHeight + 4) + 4;
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
		protected boolean onMouseClick(MouseButtonEvent event, boolean doubleClick) {
			if (event.button() == 0) {
				this.playDownSound();
				this.parent.setSelected(this);
				return true;
			}
			return false;
		}

		/* Render */

		@Override
		protected void extractWidgetRenderState(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
			int y = this.getY() + 4;
			for (var it = this.title.iterator(); it.hasNext(); y += 9) {
				var line = it.next();
				graphics.text(this.client.font, line, this.getX() + 4, y, ColorUtil.WHITE, false);
			}
			if (this.description != null) {
				y += 4;
				for (var it = this.description.iterator(); it.hasNext(); y += 9) {
					var line = it.next();
					graphics.text(this.client.font, line, this.getX() + 8, y, ColorUtil.WHITE, false);
				}
			}

			if (this.isMouseHovered()) {
				graphics.requestCursor(CursorTypes.POINTING_HAND);
			}
		}

		@Override
		protected void extractBackground(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
			super.extractBackground(graphics, mouseX, mouseY, delta);
			if (this.isFocused() && this.parent.isFocused())
				graphics.fill(this.getX(), this.getY(),
						this.getX() + this.getWidth(),
						this.getY() + this.getHeight() - 4,
						0x2fffffff);
			else if (this.selected || this.isMouseHovered())
				graphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(),
						this.getY() + this.getHeight() - 4,
						0x1affffff);
		}

		@Override
		public String toString() {
			return "SpruceTabbedWidget$TabEntry{" +
					"title=" + this.getTitle() +
					", description=" + this.description +
					", position=" + this.getPosition() +
					", width=" + this.getWidth() +
					", height=" + this.getHeight() +
					", container=" + this.container +
					", selected=" + this.selected +
					", background=" + this.getBackground() +
					'}';
		}
	}

	public static class SeparatorEntry extends Entry {
		private final SpruceSeparatorWidget separatorWidget;

		protected SeparatorEntry(SideTabList parent, Component title) {
			super(parent, title);
			this.separatorWidget = new SpruceSeparatorWidget(Position.of(this, 0, 2), this.getWidth(), title) {
				@Override
				public int getWidth() {
					return SeparatorEntry.this.getWidth();
				}
			};
		}

		public SpruceSeparatorWidget getSeparatorWidget() {
			return this.separatorWidget;
		}

		@Override
		public int getHeight() {
			return this.separatorWidget.getHeight() + 6;
		}

		/* Navigation */

		@Override
		public boolean onNavigation(NavigationEvent event) {
			return this.separatorWidget.onNavigation(event);
		}

		/* Rendering */

		@Override
		protected void extractWidgetRenderState(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
			this.separatorWidget.extractRenderState(graphics, mouseX, mouseY, delta);
		}

		@Override
		public String toString() {
			return "SpruceTabbedWidget$SeparatorEntry{" +
					"title=" + this.getTitle() +
					", position=" + this.getPosition() +
					", width=" + this.getWidth() +
					", height=" + this.getHeight() +
					", background=" + this.getBackground() +
					'}';
		}
	}

	public static class SideTabList extends SpruceEntryListWidget<dev.lambdaurora.spruceui.widget.container.tabbed.SpruceTabbedWidget.Entry> {
		private @Nullable TabEntry currentTab = null;

		protected SideTabList(Position position, int width, int height) {
			super(position, width, height, 0, SpruceTabbedWidget.Entry.class);
			this.setBorder(MenuBorder.TAB_LIST);
			this.setRenderTransition(false);
		}

		public @Nullable TabEntry getCurrentTab() {
			return this.currentTab;
		}

		@Override
		public void setFocused(boolean focused) {
			super.setFocused(focused);
			if (!focused)
				this.setSelected(this.currentTab);
		}

		public void setSelected(@Nullable TabEntry tab) {
			if (this.currentTab != null)
				this.currentTab.selected = false;
			if (tab != null)
				tab.setFocused(true);
			this.setFocused(tab);
			this.currentTab = tab;
		}

		public TabEntry addTabEntry(Component title, @Nullable Component description, AbstractSpruceWidget container) {
			var entry = new TabEntry(this, title, description, container);
			this.addEntry(entry);
			if (this.getCurrentTab() == null)
				this.setSelected(entry);
			return entry;
		}

		public SeparatorEntry addSeparatorEntry(Component title) {
			var entry = new SeparatorEntry(this, title);
			this.addEntry(entry);
			return entry;
		}

		@Override
		public boolean removeEntry(SpruceTabbedWidget.Entry entry) {
			if (this.getCurrentTab() == entry) {
				this.refocusTabOnRemoval(entry);
			}
			return super.removeEntry(entry);
		}

		public boolean removeTabEntry(Component title) {
			for (var entry : this) {
				if (entry instanceof TabEntry && entry.getTitle().equals(title)) {
					return this.removeEntry(entry);
				}
			}
			return false;
		}

		public boolean removeSeparatorEntry(Component title) {
			for (var entry : this) {
				if (entry instanceof SeparatorEntry && entry.getTitle().equals(title)) {
					return this.removeEntry(entry);
				}
			}
			return false;
		}

		protected void refocusTabOnRemoval(SpruceTabbedWidget.Entry focused) {
			int currentIndex = this.children().indexOf(focused);

			for (int index = currentIndex - 1; index >= 0; index--) {
				var entry = this.getEntry(index);
				if (entry instanceof TabEntry tabEntry) {
					this.setSelected(tabEntry);
					return;
				}
			}

			for (int index = currentIndex + 1; index < this.children().size(); index++) {
				var entry = this.getEntry(index);
				if (entry instanceof TabEntry tabEntry) {
					this.setSelected(tabEntry);
					return;
				}
			}

			this.setSelected(null);
		}

		/* Navigation */

		@Override
		public boolean onNavigation(NavigationEvent event) {
			if (this.requiresCursor()) return false;
			var old = this.getFocused();
			boolean result = super.onNavigation(event);
			var focused = this.getFocused();
			if (result && old != focused && focused instanceof TabEntry tabEntry) {
				this.setSelected(tabEntry);
			}
			return result;
		}
	}

	public interface ContainerFactory {
		AbstractSpruceWidget build(int width, int height);
	}
}
