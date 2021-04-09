/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget.container;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.background.Background;
import dev.lambdaurora.spruceui.background.DirtTexturedBackground;
import dev.lambdaurora.spruceui.border.Border;
import dev.lambdaurora.spruceui.border.EmptyBorder;
import dev.lambdaurora.spruceui.navigation.NavigationDirection;
import dev.lambdaurora.spruceui.util.ScissorManager;
import dev.lambdaurora.spruceui.widget.AbstractSpruceWidget;
import dev.lambdaurora.spruceui.widget.WithBackground;
import dev.lambdaurora.spruceui.widget.WithBorder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

/**
 * Represents an entry list.
 *
 * @param <E> the type of entry
 * @author LambdAurora
 * @version 3.0.0
 * @since 2.0.0
 */
public abstract class SpruceEntryListWidget<E extends SpruceEntryListWidget.Entry> extends AbstractSpruceParentWidget<E> implements WithBackground, WithBorder {
    protected final Position anchor = Position.of(this, 0, 0);
    private final List<E> entries = new Entries();
    private final int anchorYOffset;
    private double scrollAmount;
    private Background background = DirtTexturedBackground.DARKENED;
    private boolean renderTransition = true;
    private Border border = EmptyBorder.EMPTY_BORDER;
    private boolean scrolling = false;
    private boolean allowOutsideHorizontalNavigation = false;

    public SpruceEntryListWidget(@NotNull Position position, int width, int height, int anchorYOffset, Class<E> entryClass) {
        super(position, entryClass);
        this.width = width;
        this.height = height;
        this.anchorYOffset = anchorYOffset;
        this.anchor.setRelativeY(anchorYOffset);
    }

    /**
     * Returns the inner width of the list.
     *
     * @return the inner width
     */
    public int getInnerWidth() {
        int width = this.getWidth();
        if (this.getMaxScroll() > 0)
            width -= 6;
        width -= this.getBorder().getThickness() * 2;
        return width;
    }

    @Override
    public @NotNull Background getBackground() {
        return this.background;
    }

    @Override
    public void setBackground(@NotNull Background background) {
        this.background = background;
    }

    /**
     * Returns whether or not the transition borders are rendered.
     *
     * @return {@code true} if the transition should be rendered, else {@code false}
     */
    public boolean shouldRenderTransition() {
        return this.renderTransition;
    }

    /**
     * Sets whether or not the transition borders are rendered.
     *
     * @param render {@code true} if the transition should be rendered, else {@code false}
     */
    public void setRenderTransition(boolean render) {
        this.renderTransition = render;
    }

    @Override
    public @NotNull Border getBorder() {
        return this.border;
    }

    @Override
    public void setBorder(@NotNull Border border) {
        this.border = border;
        this.anchor.setRelativeX(border.getThickness());
        if (this.anchor.getRelativeY() == this.anchorYOffset && this.hasBorder())
            this.anchor.setRelativeY(this.anchorYOffset + border.getThickness());
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
     * It also recompute the visibility of each entries.
     *
     * @param amount the scroll amount
     */
    public void setScrollAmount(double amount) {
        this.scrollAmount = MathHelper.clamp(amount, 0, this.getMaxScroll());
        this.anchor.setRelativeY((int) (this.anchorYOffset + this.getBorder().getThickness() - this.scrollAmount));

        for (E entry : this.entries) {
            entry.setVisibleInList(!(entry.getY() + entry.getHeight() < this.getY() || entry.getY() > this.getY() + this.getHeight()));
        }
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
        return this.getX() + this.getWidth() - 6 - this.getBorder().getThickness();
    }

    @Override
    public List<E> children() {
        return this.entries;
    }

    protected final void clearEntries() {
        this.entries.clear();
    }

    protected void replaceEntries(Collection<E> newEntries) {
        this.entries.clear();
        this.entries.addAll(newEntries);
    }

    protected E getEntry(int index) {
        return this.children().get(index);
    }

    protected int addEntry(E entry) {
        this.entries.add(entry);
        return this.entries.size() - 1;
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

    /* Navigation */

    @Override
    public boolean onNavigation(@NotNull NavigationDirection direction, boolean tab) {
        if (this.requiresCursor()) return false;
        if (direction.isHorizontal() && this.getFocused() != null) {
            boolean result = this.getFocused().onNavigation(direction, tab);
            return !this.allowOutsideHorizontalNavigation || result;
        }
        boolean result = super.onNavigation(direction, tab);
        if (result) this.ensureVisible(this.getFocused());
        return result;
    }

    /* Input */

    @Override
    protected boolean onMouseClick(double mouseX, double mouseY, int button) {
        this.scrolling = button == GLFW.GLFW_MOUSE_BUTTON_1 && mouseX >= this.getScrollbarPositionX() && mouseX < (this.getScrollbarPositionX() + 6);
        return super.onMouseClick(mouseX, mouseY, button) || this.scrolling;
    }

    @Override
    protected boolean onMouseDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (super.onMouseDrag(mouseX, mouseY, button, deltaX, deltaY)) return true;
        else if (button == GLFW.GLFW_MOUSE_BUTTON_1 && this.scrolling) {
            if (mouseY < this.getY()) {
                this.setScrollAmount(0);
            } else if (mouseY > (this.getY() + this.getHeight())) {
                this.setScrollAmount(this.getMaxScroll());
            } else {
                double d = Math.max(1, this.getMaxScroll());
                int height = this.height;
                int j = MathHelper.clamp((int) ((float) (height * height) / (float) this.getMaxPosition()), 32, height - 8);
                double e = Math.max(1, d / (double) (height - j));
                this.setScrollAmount(this.getScrollAmount() + deltaY * e);
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean onMouseScroll(double mouseX, double mouseY, double amount) {
        this.setScrollAmount(this.getScrollAmount() - amount * ((double) this.getMaxPosition() / this.getEntriesCount()) / 2);
        return true;
    }

    /* Render */

    @Override
    protected void renderBackground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.getBackground().render(matrices, this, 0, mouseX, mouseY, delta);
    }

    @Override
    protected void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int scrollbarPositionX = this.getScrollbarPositionX();
        int scrollBarEnd = scrollbarPositionX + 6;
        int left = this.getX();
        int right = left + this.getWidth();
        int top = this.getY();
        int bottom = top + this.getHeight();

        ScissorManager.push(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        this.entries.forEach(e -> e.render(matrices, mouseX, mouseY, delta));
        ScissorManager.pop();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        // Render the transition thingy.
        if (this.shouldRenderTransition()) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
            RenderSystem.disableTexture();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            // TOP
            buffer.vertex(left, top + 4, 0).color(0, 0, 0, 0).next();
            buffer.vertex(right, top + 4, 0).color(0, 0, 0, 0).next();
            buffer.vertex(right, top, 0).color(0, 0, 0, 255).next();
            buffer.vertex(left, top, 0).color(0, 0, 0, 255).next();
            // RIGHT
            buffer.vertex(right - 4, bottom, 0).color(0, 0, 0, 0).next();
            buffer.vertex(right, bottom, 0).color(0, 0, 0, 255).next();
            buffer.vertex(right, top, 0).color(0, 0, 0, 255).next();
            buffer.vertex(right - 4, top, 0).color(0, 0, 0, 0).next();
            // BOTTOM
            buffer.vertex(left, bottom, 0).color(0, 0, 0, 255).next();
            buffer.vertex(right, bottom, 0).color(0, 0, 0, 255).next();
            buffer.vertex(right, bottom - 4, 0).color(0, 0, 0, 0).next();
            buffer.vertex(left, bottom - 4, 0).color(0, 0, 0, 0).next();
            // LEFT
            buffer.vertex(left, bottom, 0).color(0, 0, 0, 255).next();
            buffer.vertex(left + 4, bottom, 0).color(0, 0, 0, 0).next();
            buffer.vertex(left + 4, top, 0).color(0, 0, 0, 0).next();
            buffer.vertex(left, top, 0).color(0, 0, 0, 255).next();
            tessellator.draw();
        }

        // Scrollbar
        int maxScroll = this.getMaxScroll();
        if (maxScroll > 0) {
            RenderSystem.disableTexture();
            int scrollbarHeight = (int) ((float) ((this.getHeight()) * (this.getHeight())) / (float) this.getMaxPosition());
            scrollbarHeight = MathHelper.clamp(scrollbarHeight, 32, this.getHeight() - 8);
            int scrollbarY = (int) this.getScrollAmount() * (this.getHeight() - scrollbarHeight) / maxScroll + this.getY();
            if (scrollbarY < this.getY()) {
                scrollbarY = this.getY();
            }

            this.renderScrollbar(tessellator, buffer, scrollbarPositionX, scrollBarEnd, scrollbarY, scrollbarHeight);
        }

        this.getBorder().render(matrices, this, mouseX, mouseY, delta);

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    protected void renderScrollbar(Tessellator tessellator, BufferBuilder buffer, int scrollbarX, int scrollbarEndX, int scrollbarY, int scrollbarHeight) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(scrollbarX, this.getY() + this.getHeight(), 0.0).color(0, 0, 0, 255).next();
        buffer.vertex(scrollbarEndX, this.getY() + this.getHeight(), 0.0).color(0, 0, 0, 255).next();
        buffer.vertex(scrollbarEndX, this.getY(), 0.0).color(0, 0, 0, 255).next();
        buffer.vertex(scrollbarX, this.getY(), 0.0).color(0, 0, 0, 255).next();
        buffer.vertex(scrollbarX, scrollbarY + scrollbarHeight, 0.0).color(128, 128, 128, 255).next();
        buffer.vertex(scrollbarEndX, scrollbarY + scrollbarHeight, 0.0).color(128, 128, 128, 255).next();
        buffer.vertex(scrollbarEndX, scrollbarY, 0.0).color(128, 128, 128, 255).next();
        buffer.vertex(scrollbarX, scrollbarY, 0.0).color(128, 128, 128, 255).next();
        buffer.vertex(scrollbarX, scrollbarY + scrollbarHeight - 1, 0.0).color(192, 192, 192, 255).next();
        buffer.vertex(scrollbarEndX - 1, scrollbarY + scrollbarHeight - 1, 0.0).color(192, 192, 192, 255).next();
        buffer.vertex(scrollbarEndX - 1, scrollbarY, 0.0).color(192, 192, 192, 255).next();
        buffer.vertex(scrollbarX, scrollbarY, 0.0).color(192, 192, 192, 255).next();
        tessellator.draw();
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
            E entry2 = this.entries.set(i, entry);
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
            E result = this.entries.remove(i);
            this.recomputePositions();
            return result;
        }

        private void recomputePositions() {
            int y = 0;
            for (E entry : this.entries) {
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
