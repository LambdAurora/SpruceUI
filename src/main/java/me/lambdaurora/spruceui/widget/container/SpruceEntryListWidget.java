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
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.lambdaurora.spruceui.Position;
import me.lambdaurora.spruceui.navigation.NavigationDirection;
import me.lambdaurora.spruceui.widget.AbstractSpruceWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
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
 * @version 1.7.0
 * @since 1.7.0
 */
public abstract class SpruceEntryListWidget<E extends SpruceEntryListWidget.Entry> extends AbstractSpruceParentWidget<E> implements ParentElement
{
    protected final Position anchor = Position.of(this, 0, 4);
    private final List<E> entries = new Entries();
    private double scrollAmount;
    private boolean renderBackground = true;
    private boolean renderTransition = true;
    private boolean scrolling = false;

    public SpruceEntryListWidget(@NotNull Position position, int width, int height, Class<E> entryClass)
    {
        super(position, entryClass);
        this.width = width;
        this.height = height;
    }

    /**
     * Returns whether or not the background should be rendered.
     *
     * @return {@code true} if the background should be rendered, else {@code false}
     */
    public boolean shouldRenderBackground()
    {
        return this.renderBackground;
    }

    /**
     * Sets whether or not the background should be rendered.
     *
     * @param render {@code true} if the background should be rendered, else {@code false}
     */
    public void setRenderBackground(boolean render)
    {
        this.renderBackground = render;
    }

    /**
     * Returns whether or not the transition borders are rendered.
     *
     * @return {@code true} if the transition should be rendered, else {@code false}
     */
    public boolean shouldRenderTransition()
    {
        return this.renderTransition;
    }

    /**
     * Sets whether or not the transition borders are rendered.
     *
     * @param render {@code true} if the transition should be rendered, else {@code false}
     */
    public void setRenderTransition(boolean render)
    {
        this.renderTransition = render;
    }

    protected int getLengthUntil(int index)
    {
        int max = 0;
        for (int i = 0; i <= index; i++) {
            max += this.entries.get(i).getHeight();
            if (i != this.entries.size() - 1)
                max += 4;
        }
        return max;
    }

    public int getMaxPosition()
    {
        return this.getLengthUntil(this.getEntriesCount() - 1);
    }

    private void scroll(int amount)
    {
        this.setScrollAmount(this.getScrollAmount() + (double) amount);
    }

    /**
     * Gets the scroll amount of this list. The amount is clamped between 0 and the maximum scroll ({@link #getMaxScroll()}).
     *
     * @return the scroll amount
     */
    public double getScrollAmount()
    {
        return this.scrollAmount;
    }

    /**
     * Sets the scroll amount of this list. The amount is clamped between 0 and the maximum scroll ({@link #getMaxScroll()}).
     * <p>
     * It also recompute the visibility of each entries.
     *
     * @param amount the scroll amount
     */
    public void setScrollAmount(double amount)
    {
        this.scrollAmount = MathHelper.clamp(amount, 0, this.getMaxScroll());
        this.anchor.setRelativeY((int) (4 - this.scrollAmount));

        for (E entry : this.entries) {
            entry.setVisibleInList(!(entry.getY() + entry.getHeight() < this.getY() || entry.getY() > this.getY() + this.getHeight()));
        }
    }

    /**
     * Returns the max scroll. The scroll amount can't go past this maximum.
     *
     * @return the max scroll
     */
    public int getMaxScroll()
    {
        return Math.max(0, this.getMaxPosition() - this.getHeight() + 8);
    }

    protected int getScrollbarPositionX()
    {
        return this.getWidth() - 6;
    }

    @Override
    public List<E> children()
    {
        return this.entries;
    }

    protected final void clearEntries()
    {
        this.entries.clear();
    }

    protected void replaceEntries(Collection<E> newEntries)
    {
        this.entries.clear();
        this.entries.addAll(newEntries);
    }

    protected E getEntry(int index)
    {
        return this.children().get(index);
    }

    protected int addEntry(E entry)
    {
        this.entries.add(entry);
        return this.entries.size() - 1;
    }

    protected int getEntriesCount()
    {
        return this.children().size();
    }

    /**
     * Ensures that the specified entry is visible.
     *
     * @param entry the entry which needs to be visible
     */
    protected void ensureVisible(E entry)
    {
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

    protected int getRowTop(int index)
    {
        return this.getY() + 4 - (int) this.getScrollAmount() + this.getLengthUntil(index);
    }

    private void setOwnerShip(E entry)
    {
        entry.getPosition().setAnchor(this.anchor);
        entry.setVisibleInList(!(entry.getY() + entry.getHeight() < this.getY() || entry.getY() > this.getY() + this.getHeight()));
    }

    /* Navigation */

    @Override
    public boolean onNavigation(@NotNull NavigationDirection direction, boolean tab)
    {
        if (this.requiresCursor()) return false;
        if (direction.isHorizontal() && this.getFocused() != null) {
            this.getFocused().onNavigation(direction, tab);
            return true;
        }
        boolean result = super.onNavigation(direction, tab);
        if (result) this.ensureVisible(this.getFocused());
        return result;
    }

    /* Input */

    @Override
    protected boolean onMouseClick(double mouseX, double mouseY, int button)
    {
        this.scrolling = button == GLFW.GLFW_MOUSE_BUTTON_1 && mouseX >= this.getScrollbarPositionX() && mouseX < (this.getScrollbarPositionX() + 6);
        return super.onMouseClick(mouseX, mouseY, button) || this.scrolling;
    }

    @Override
    protected boolean onMouseDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY)
    {
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
    protected boolean onMouseScroll(double mouseX, double mouseY, double amount)
    {
        this.setScrollAmount(this.getScrollAmount() - amount * ((double) this.getMaxPosition() / this.getEntriesCount()) / 2);
        return true;
    }

    /* Render */

    @Override
    protected void renderBackground(MatrixStack matrices, int mouseX, int mouseY)
    {
        if (!this.shouldRenderBackground())
            return;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        this.client.getTextureManager().bindTexture(DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
        RenderSystem.color4f(1.f, 1.f, 1.f, 1.f);
        int left = this.getX();
        int right = left + this.getWidth();
        int top = this.getY();
        int bottom = top + this.getHeight();
        buffer.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
        buffer.vertex(left, bottom, 0).texture((float) left / 32.f, (float) (bottom + (int) this.getScrollAmount()) / 32.f).color(32, 32, 32, 255).next();
        buffer.vertex(right, bottom, 0).texture((float) right / 32.f, (float) (bottom + (int) this.getScrollAmount()) / 32.f).color(32, 32, 32, 255).next();
        buffer.vertex(right, top, 0).texture((float) right / 32.f, (float) (top + (int) this.getScrollAmount()) / 32.f).color(32, 32, 32, 255).next();
        buffer.vertex(left, top, 0).texture((float) left / 32.f, (float) (top + (int) this.getScrollAmount()) / 32.f).color(32, 32, 32, 255).next();
        tessellator.draw();
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        int scrollbarPositionX = this.getScrollbarPositionX();
        int scrollBarEnd = scrollbarPositionX + 6;
        int left = this.getX();
        int right = left + this.getWidth();
        int top = this.getY();
        int bottom = top + this.getHeight();

        {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            double scaleFactor = this.client.getWindow().getScaleFactor();
            GL11.glScissor(this.getX(), (int) (scaleFactor * (this.client.getWindow().getScaledHeight() - this.getY() - this.getHeight())),
                    (int) (scaleFactor * this.getWidth()), (int) (scaleFactor * this.getHeight()));
        }
        this.entries.forEach(e -> e.render(matrices, mouseX, mouseY, delta));
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        // Render the transition thingy.
        if (this.shouldRenderTransition()) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
            RenderSystem.disableAlphaTest();
            RenderSystem.shadeModel(7425);
            RenderSystem.disableTexture();
            buffer.begin(7, VertexFormats.POSITION_COLOR);
            // TOP
            buffer.vertex(left, top + 4, 0).texture(0.f, 1.f).color(0, 0, 0, 0).next();
            buffer.vertex(right, top + 4, 0).texture(1.f, 1.f).color(0, 0, 0, 0).next();
            buffer.vertex(right, top, 0).texture(1.f, 0.f).color(0, 0, 0, 255).next();
            buffer.vertex(left, top, 0).texture(0.f, 0.f).color(0, 0, 0, 255).next();
            // RIGHT
            buffer.vertex(right - 4, bottom, 0).texture(0.f, 1.f).color(0, 0, 0, 0).next();
            buffer.vertex(right, bottom, 0).texture(1.f, 1.f).color(0, 0, 0, 255).next();
            buffer.vertex(right, top, 0).texture(1.f, 0.f).color(0, 0, 0, 255).next();
            buffer.vertex(right - 4, top, 0).texture(0.f, 0.f).color(0, 0, 0, 0).next();
            // BOTTOM
            buffer.vertex(left, bottom, 0).texture(0.f, 1.f).color(0, 0, 0, 255).next();
            buffer.vertex(right, bottom, 0).texture(1.f, 1.f).color(0, 0, 0, 255).next();
            buffer.vertex(right, bottom - 4, 0).texture(1.f, 0.f).color(0, 0, 0, 0).next();
            buffer.vertex(left, bottom - 4, 0).texture(0.f, 0.f).color(0, 0, 0, 0).next();
            // RIGHT
            buffer.vertex(left, bottom, 0).texture(0.f, 1.f).color(0, 0, 0, 255).next();
            buffer.vertex(left + 4, bottom, 0).texture(1.f, 1.f).color(0, 0, 0, 0).next();
            buffer.vertex(left + 4, top, 0).texture(1.f, 0.f).color(0, 0, 0, 0).next();
            buffer.vertex(left, top, 0).texture(0.f, 0.f).color(0, 0, 0, 255).next();
            tessellator.draw();
        }

        // Scrollbar
        int maxScroll = this.getMaxScroll();
        if (maxScroll > 0) {
            RenderSystem.disableTexture();
            int p = (int) ((float) ((this.getHeight()) * (this.getHeight())) / (float) this.getMaxPosition());
            p = MathHelper.clamp(p, 32, this.getHeight() - 8);
            int q = (int) this.getScrollAmount() * (this.getHeight() - p) / maxScroll + this.getY();
            if (q < this.getY()) {
                q = this.getY();
            }

            buffer.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
            buffer.vertex(scrollbarPositionX, this.getY() + this.getHeight(), 0.0).texture(0.f, 1.f).color(0, 0, 0, 255).next();
            buffer.vertex(scrollBarEnd, this.getY() + this.getHeight(), 0.0).texture(1.f, 1.f).color(0, 0, 0, 255).next();
            buffer.vertex(scrollBarEnd, this.getY(), 0.0).texture(1.f, 0.f).color(0, 0, 0, 255).next();
            buffer.vertex(scrollbarPositionX, this.getY(), 0.0).texture(0.f, 0.f).color(0, 0, 0, 255).next();
            buffer.vertex(scrollbarPositionX, q + p, 0.0).texture(0.f, 1.f).color(128, 128, 128, 255).next();
            buffer.vertex(scrollBarEnd, q + p, 0.0).texture(1.f, 1.f).color(128, 128, 128, 255).next();
            buffer.vertex(scrollBarEnd, q, 0.0).texture(1.f, 0.f).color(128, 128, 128, 255).next();
            buffer.vertex(scrollbarPositionX, q, 0.0).texture(0.f, 0.f).color(128, 128, 128, 255).next();
            buffer.vertex(scrollbarPositionX, q + p - 1, 0.0).texture(0.f, 1.f).color(192, 192, 192, 255).next();
            buffer.vertex(scrollBarEnd - 1, q + p - 1, 0.0).texture(1.f, 1.f).color(192, 192, 192, 255).next();
            buffer.vertex(scrollBarEnd - 1, q, 0.0).texture(1.f, 0.f).color(192, 192, 192, 255).next();
            buffer.vertex(scrollbarPositionX, q, 0.0).texture(0.f, 0.f).color(192, 192, 192, 255).next();
            tessellator.draw();
        }

        RenderSystem.enableTexture();
        RenderSystem.shadeModel(7424);
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
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
            this.recomputePositions();
            SpruceEntryListWidget.this.setOwnerShip(entry);
            return entry2;
        }

        public void add(int i, E entry)
        {
            this.entries.add(i, entry);
            this.recomputePositions();
            SpruceEntryListWidget.this.setOwnerShip(entry);
        }

        public E remove(int i)
        {
            E result = this.entries.remove(i);
            this.recomputePositions();
            return result;
        }

        private void recomputePositions()
        {
            int y = 0;
            for (E entry : this.entries) {
                entry.getPosition().setRelativeY(y);
                y += entry.getHeight() + 4;
            }
        }
    }

    public static abstract class Entry extends AbstractSpruceWidget
    {
        private boolean visibleInList = false;

        public Entry()
        {
            super(Position.origin());
        }

        public void setVisibleInList(boolean visible)
        {
            this.visibleInList = visible;
        }

        @Override
        public boolean isVisible()
        {
            return super.isVisible() && this.visibleInList;
        }
    }
}
