/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Represents a label widget.
 *
 * @author LambdAurora
 * @version 1.6.0
 * @since 1.0.0
 */
public class SpruceLabelWidget extends DrawableHelper implements Element, Drawable, SpruceWidget, Tooltipable
{
    public static final Consumer<SpruceLabelWidget> DEFAULT_ACTION = label -> {
    };

    private final MinecraftClient             client = MinecraftClient.getInstance();
    private final Consumer<SpruceLabelWidget> action;
    private final int                         x;
    private final int                         y;
    private final int                         maxWidth;
    //private final int                         maxHeight;
    private       Text                        text;
    private       Text                        tooltip;
    public        boolean                     visible;
    private       int                         width;
    private       int                         height;
    private       boolean                     centered;
    protected     boolean                     hovered;
    protected     boolean                     focused;

    public SpruceLabelWidget(int x, int y, @NotNull Text text, int maxWidth, @NotNull Consumer<SpruceLabelWidget> action, boolean centered)
    {
        this.visible = true;
        this.x = x;
        this.y = y;
        this.maxWidth = maxWidth;
        this.action = action;
        this.centered = centered;
        this.setText(text);
    }

    public SpruceLabelWidget(int x, int y, @NotNull Text text, int maxWidth, @NotNull Consumer<SpruceLabelWidget> action)
    {
        this(x, y, text, maxWidth, action, false);
    }

    public SpruceLabelWidget(int x, int y, @NotNull Text text, int maxWidth, boolean centered)
    {
        this(x, y, text, maxWidth, DEFAULT_ACTION, centered);
    }

    public SpruceLabelWidget(int x, int y, @NotNull Text text, int maxWidth)
    {
        this(x, y, text, maxWidth, DEFAULT_ACTION);
    }

    /**
     * Sets the text of this label.
     *
     * @param text The text to set.
     */
    public void setText(@NotNull Text text)
    {
        int width = this.client.textRenderer.getWidth(text);
        if (width > this.maxWidth) {
            width = this.maxWidth;
        }

        this.text = text;
        this.width = width;
        this.height = this.client.textRenderer.fontHeight;
    }

    @Override
    public @NotNull Optional<Text> getTooltip()
    {
        return Optional.ofNullable(this.tooltip);
    }

    @Override
    public void setTooltip(@Nullable Text tooltip)
    {
        this.tooltip = tooltip;
    }

    @Override
    public boolean isVisible()
    {
        return this.visible;
    }

    @Override
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    @Override
    public int getWidth()
    {
        return this.width;
    }

    /**
     * Gets the height of this label widget.
     *
     * @return The height of this label widget.
     */
    public int getHeight()
    {
        return this.height;
    }

    @Override
    public boolean isFocused()
    {
        return this.focused;
    }

    @Override
    public boolean isHovered()
    {
        return this.hovered;
    }

    /**
     * Fires the press event on this label widget.
     */
    public void onPress()
    {
        this.action.accept(this);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        if (this.visible) {
            int x = this.centered ? this.x - this.client.textRenderer.getWidth(this.text) / 2 : this.x;
            this.hovered = mouseX >= x && mouseY >= this.y && mouseX < x + this.width && mouseY < this.y + this.height;
            this.client.textRenderer.drawTrimmed(this.text, x, this.y, this.maxWidth, 10526880);

            if (this.tooltip != null) {
                if (!this.tooltip.getString().isEmpty()) {
                    List<OrderedText> wrappedTooltipText = this.client.textRenderer.wrapLines(this.tooltip, Math.max(this.width / 2, 200));
                    if (this.hovered)
                        new Tooltip(mouseX, mouseY, wrappedTooltipText).queue();
                    else if (this.focused)
                        new Tooltip(this.x - 12, this.y, wrappedTooltipText).queue();
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (this.visible && button == GLFW.GLFW_MOUSE_BUTTON_1) {
            if (this.hovered) {
                this.onPress();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean changeFocus(boolean down)
    {
        if (this.visible) {
            this.focused = !this.focused;
            return this.focused;
        } else {
            return false;
        }
    }
}
