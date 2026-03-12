/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget.container;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.SpruceTextures;
import dev.lambdaurora.spruceui.background.Background;
import dev.lambdaurora.spruceui.background.MenuBackground;
import dev.lambdaurora.spruceui.border.Border;
import dev.lambdaurora.spruceui.border.MenuBorder;
import dev.lambdaurora.spruceui.navigation.NavigationEvent;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import dev.lambdaurora.spruceui.widget.AbstractSpruceWidget;
import dev.lambdaurora.spruceui.widget.SpruceWidgetWithBorder;
import dev.lambdaurora.spruceui.widget.WithBackground;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenAxis;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

/**
 * Represents an entry list.
 *
 * @param <E> the type of entry
 * @author LambdAurora
 * @version 9.0.0
 * @since 2.0.0
 */
public abstract class SpruceEntryListWidget<E extends SpruceEntryListWidget.Entry> extends AbstractSpruceParentWidget<E>
		implements WithBackground, SpruceWidgetWithBorder {
	protected final Position anchor = Position.of(this, 0, 0);
	private final List<E> entries = new Entries();
	private final int anchorYOffset;
	private double scrollAmount;
	private Background background = MenuBackground.MENU_LIST;
	private boolean renderTransition = false;
	private Border border;
	private boolean scrolling = false;
	private boolean allowOutsideHorizontalNavigation = false;

	public SpruceEntryListWidget(Position position, int width, int height, int anchorYOffset, Class<E> entryClass) {
		super(position, entryClass);
		this.width = width;
		this.height = height;
		this.anchorYOffset = anchorYOffset;
		this.anchor.setRelativeY(anchorYOffset);

		this.setBorder(MenuBorder.LIST);
	}

	/**
	 * Returns the inner width of the list.
	 *
	 * @return the inner width
	 */
	public int getInnerWidth() {
		int width = this.getInnerBorderedWidth();
		if (this.getMaxScroll() > 0)
			width -= 6;
		return width;
	}

	@Override
	public Background getBackground() {
		return this.background;
	}

	@Override
	public void setBackground(Background background) {
		this.background = background;
	}

	/**
	 * Returns whether to render the transition borders.
	 *
	 * @return {@code true} if the transition should be rendered, else {@code false}
	 */
	public boolean shouldRenderTransition() {
		return this.renderTransition;
	}

	/**
	 * Sets whether to render the transition borders.
	 *
	 * @param render {@code true} if the transition should be rendered, else {@code false}
	 */
	public void setRenderTransition(boolean render) {
		this.renderTransition = render;
	}

	@Override
	public Border getBorder() {
		return this.border;
	}

	@Override
	public void setBorder(Border border) {
		this.border = border;
		this.anchor.setRelativeX(border.getLeft());
		if (this.anchor.getRelativeY() == this.anchorYOffset && this.getBorder().getTop() != 0)
			this.anchor.setRelativeY(this.anchorYOffset + this.getBorder().getTop());
	}

	public boolean doesAllowOutsideHorizontalNavigation() {
		return this.allowOutsideHorizontalNavigation;
	}

	public void setAllowOutsideHorizontalNavigation(boolean allowOutsideHorizontalNavigation) {
		this.allowOutsideHorizontalNavigation = allowOutsideHorizontalNavigation;
	}

	protected int getLengthUntil(int index) {
		int max = 0;
		for (int i = 0; i <= index; i++) {
			max += this.entries.get(i).getHeight();
		}
		return max;
	}

	public int getMaxPosition() {
		return this.getLengthUntil(this.getEntriesCount() - 1);
	}

	private void scroll(int amount) {
		this.setScrollAmount(this.getScrollAmount() + (double) amount);
	}

	/**
	 * Gets the scroll amount of this list. The amount is clamped between 0 and the maximum scroll ({@link #getMaxScroll()}).
	 *
	 * @return the scroll amount
	 */
	public double getScrollAmount() {
		return this.scrollAmount;
	}

	/**
	 * Sets the scroll amount of this list. The amount is clamped between 0 and the maximum scroll ({@link #getMaxScroll()}).
	 * <p>
	 * It also recomputes the visibility of each entry.
	 *
	 * @param amount the scroll amount
	 */
	public void setScrollAmount(double amount) {
		this.scrollAmount = Mth.clamp(amount, 0, this.getMaxScroll());
		this.anchor.setRelativeY((int) (this.anchorYOffset + this.getBorder().getThickness() - this.scrollAmount));

		for (var entry : this.entries) {
			entry.setVisibleInList(!(entry.getY() + entry.getHeight() < this.getY() || entry.getY() > this.getY() + this.getHeight()));
		}
	}

	/**
	 * Refocuses the scroll of this list.
	 */
	public void refocusScroll() {
		this.setScrollAmount(this.getScrollAmount());
	}

	/**
	 * Returns the max scroll. The scroll amount can't go past this maximum.
	 *
	 * @return the max scroll
	 */
	public int getMaxScroll() {
		return Math.max(0, this.getMaxPosition() - this.getHeight() + 8);
	}

	protected int getScrollbarPositionX() {
		return this.getEndInnerBorderedX() - 6;
	}

	/**
	 * {@return {@code true} if the scrollbar is visible, or {@code false} otherwise}
	 */
	public boolean isScrollbarVisible() {
		return this.getMaxScroll() > 0;
	}

	@Override
	public List<E> children() {
		return this.entries;
	}

	protected final void clearEntries() {
		this.setFocused(null);
		this.entries.clear();
		this.refocusScroll();
	}

	protected void replaceEntries(Collection<E> newEntries) {
		var oldFocused = this.getFocused();
		this.entries.clear();
		this.entries.addAll(newEntries);
		if (!newEntries.contains(oldFocused)) {
			this.setFocused(null);
		}
		this.refocusScroll();
	}

	protected @Nullable E getEntry(int index) {
		return this.children().get(index);
	}

	protected int addEntry(E entry) {
		this.entries.add(entry);
		return this.entries.size() - 1;
	}

	protected boolean removeEntry(E entry) {
		if (this.getFocused() == entry) {
			this.refocusOnRemoval(entry);
		}
		return this.entries.remove(entry);
	}

	protected void refocusOnRemoval(E focused) {
		int i = this.entries.indexOf(focused);
		if (i == -1)
			return;

		int newIndex = i - 1;
		if (newIndex < 0)
			newIndex = i;

		if (this.getEntriesCount() == newIndex - 1)
			this.setFocused(null);
		else
			this.setFocused(this.getEntry(newIndex));
	}

	protected int getEntriesCount() {
		return this.children().size();
	}

	/**
	 * Ensures that the specified entry is visible.
	 *
	 * @param entry the entry which needs to be visible
	 */
	protected void ensureVisible(E entry) {
		int index = this.children().indexOf(entry);
		int rowTop = this.getRowTop(index);
		int j = rowTop - this.getY() - entry.getHeight() - 8;
		if (j < 0) {
			this.scroll(j);
		}

		int nextHeight = 0;
		if (index < this.getEntriesCount() - 1)
			nextHeight = this.children().get(index + 1).getHeight();
		int k = this.getY() + this.getHeight() - rowTop - entry.getHeight() + nextHeight;
		if (k < 0) {
			this.scroll(-k);
		}
	}

	protected int getRowTop(int index) {
		return this.getY() + 4 - (int) this.getScrollAmount() + this.getLengthUntil(index);
	}

	@Override
	protected void setOwnerShip(E entry) {
		entry.getPosition().setAnchor(this.anchor);
		entry.setVisibleInList(!(entry.getY() + entry.getHeight() < this.getY() || entry.getY() > this.getY() + this.getHeight()));
	}

	protected boolean isOverScrollbar(int mouseX, int mouseY) {
		return mouseX >= this.getScrollbarPositionX() && mouseX < (this.getScrollbarPositionX() + 6) && this.isMouseOver(mouseX, mouseY);
	}

	/* Navigation */

	@Override
	public boolean onNavigation(NavigationEvent event) {
		if (this.requiresCursor()) return false;
		if (event.direction().getAxis() == ScreenAxis.HORIZONTAL && this.getFocused() != null) {
			boolean result = this.getFocused().onNavigation(event);
			return !this.allowOutsideHorizontalNavigation || result;
		}
		boolean result = super.onNavigation(event);
		if (result) this.ensureVisible(this.getFocused());
		return result;
	}

	/* Input */

	@Override
	protected boolean onMouseClick(MouseButtonEvent event, boolean doubleClick) {
		this.scrolling = event.button() == GLFW.GLFW_MOUSE_BUTTON_1 && event.x() >= this.getScrollbarPositionX() && event.x() < (this.getScrollbarPositionX() + 6);
		return super.onMouseClick(event, doubleClick) || this.scrolling;
	}

	@Override
	protected boolean onMouseDrag(MouseButtonEvent event, double deltaX, double deltaY) {
		if (super.onMouseDrag(event, deltaX, deltaY)) return true;
		else if (event.button() == GLFW.GLFW_MOUSE_BUTTON_1 && this.scrolling) {
			if (event.y() < this.getY()) {
				this.setScrollAmount(0);
			} else if (event.y() > (this.getY() + this.getHeight())) {
				this.setScrollAmount(this.getMaxScroll());
			} else {
				double d = Math.max(1, this.getMaxScroll());
				int height = this.height;
				int j = Mth.clamp((int) ((float) (height * height) / (float) this.getMaxPosition()), 32, height - 8);
				double e = Math.max(1, d / (double) (height - j));
				this.setScrollAmount(this.getScrollAmount() + deltaY * e);
			}
			return true;
		}
		return false;
	}

	@Override
	protected boolean onMouseScroll(double mouseX, double mouseY, double scrollX, double scrollY) {
		if (super.onMouseScroll(mouseX, mouseY, scrollX, scrollY)) return true;
		this.setScrollAmount(this.getScrollAmount() - scrollY * ((double) this.getMaxPosition() / this.getEntriesCount()) / 2);
		return true;
	}

	/* Rendering */

	@Override
	protected void extractBackground(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
		this.getBackground().extractRenderState(graphics, this, 0, mouseX, mouseY, delta);
	}

	@Override
	protected void extractWidgetRenderState(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
		int left = this.getInnerBorderedX();
		int right = this.getEndInnerBorderedX();
		int top = this.getInnerBorderedY();
		int bottom = this.getEndInnerBorderedY();

		graphics.enableScissor(left, top, right, bottom);
		this.entries.forEach(e -> e.extractRenderState(graphics, mouseX, mouseY, delta));
		graphics.disableScissor();

		// Render the transition thingy.
		if (this.shouldRenderTransition()) {
			final int gradientStart = 0xff000000; // Black
			final int gradientEnd = 0x00000000; // Transparent

			graphics.fillGradient(left, top, right, top + 4, gradientStart, gradientStart, gradientEnd, gradientEnd);
			graphics.fillGradient(right - 4, top, right, bottom, gradientEnd, gradientStart, gradientStart, gradientEnd);
			graphics.fillGradient(left, bottom - 4, right, bottom, gradientEnd, gradientEnd, gradientStart, gradientStart);
			graphics.fillGradient(left, top, left + 4, bottom, gradientStart, gradientEnd, gradientEnd, gradientStart);
		}

		// Scrollbar
		this.extractScrollbar(graphics, mouseX, mouseY);

		this.getBorder().extractRenderState(graphics, this, mouseX, mouseY, delta);
	}

	protected void extractScrollbar(SpruceGuiGraphics graphics, int mouseX, int mouseY) {
		if (this.isScrollbarVisible()) {
			int top = this.getInnerBorderedY();
			int height = this.getInnerBorderedHeight();
			int scrollbarX = this.getScrollbarPositionX();
			int scrollerHeight = (int) ((float) (height * height) / (float) this.getMaxPosition());
			scrollerHeight = Mth.clamp(scrollerHeight, 32, height - 8);
			int scrollbarY = (int) this.getScrollAmount() * (height - scrollerHeight) / this.getMaxScroll() + top;
			if (scrollbarY < top) {
				scrollbarY = top;
			}

			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SpruceTextures.SCROLLER_BACKGROUND,
					scrollbarX, top, 6, this.getInnerBorderedHeight()
			);
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SpruceTextures.SCROLLER,
					scrollbarX, scrollbarY, 6, scrollerHeight
			);

			if (this.isOverScrollbar(mouseX, mouseY)) {
				graphics.requestCursor(this.scrolling ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);
			}
		}
	}

	/* Narration */

	protected void appendPositionNarrations(NarrationElementOutput builder, E entry) {
		var list = this.children();
		if (list.size() > 1) {
			int i = list.indexOf(entry);
			if (i != -1) {
				builder.add(NarratedElementType.POSITION, Component.translatable("narrator.position.list", i + 1, list.size()));
			}
		}
	}

	@Environment(EnvType.CLIENT)
	class Entries extends AbstractList<E> {
		private final List<E> entries;

		private Entries() {
			this.entries = Lists.newArrayList();
		}

		public E get(int i) {
			return this.entries.get(i);
		}

		public int size() {
			return this.entries.size();
		}

		public E set(int i, E entry) {
			var entry2 = this.entries.set(i, entry);
			this.recomputePositions();
			SpruceEntryListWidget.this.setOwnerShip(entry);
			return entry2;
		}

		public void add(int i, E entry) {
			this.entries.add(i, entry);
			this.recomputePositions();
			SpruceEntryListWidget.this.setOwnerShip(entry);
		}

		public E remove(int i) {
			var result = this.entries.remove(i);
			this.recomputePositions();
			return result;
		}

		private void recomputePositions() {
			int y = 0;
			for (var entry : this.entries) {
				entry.getPosition().setRelativeY(y);
				y += entry.getHeight();
			}
		}
	}

	public static abstract class Entry extends AbstractSpruceWidget {
		private boolean visibleInList = false;

		public Entry() {
			super(Position.origin());
		}

		protected void setVisibleInList(boolean visible) {
			this.visibleInList = visible;
		}

		@Override
		public boolean isVisible() {
			return super.isVisible() && this.visibleInList;
		}
	}
}
