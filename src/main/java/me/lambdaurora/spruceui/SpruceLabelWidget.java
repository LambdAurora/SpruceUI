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
 * @version 1.7.0
 * @since 1.0.0
 */
public class SpruceLabelWidget extends AbstractSpruceWidget implements Tooltipable
{
    public static final Consumer<SpruceLabelWidget> DEFAULT_ACTION = label -> {
    };

    private final MinecraftClient client = MinecraftClient.getInstance();
    private final Consumer<SpruceLabelWidget> action;
    private final int maxWidth;
    //private final int                         maxHeight;
    private Text text;
    private Text tooltip;
    private boolean centered;
    protected boolean hovered;
    protected boolean focused;

    public SpruceLabelWidget(Position position, @NotNull Text text, int maxWidth, @NotNull Consumer<SpruceLabelWidget> action, boolean centered)
    {
        super(position);
        this.maxWidth = maxWidth;
        this.action = action;
        this.centered = centered;
        this.setText(text);
    }

    public SpruceLabelWidget(Position position, @NotNull Text text, int maxWidth, @NotNull Consumer<SpruceLabelWidget> action)
    {
        this(position, text, maxWidth, action, false);
    }

    public SpruceLabelWidget(Position position, @NotNull Text text, int maxWidth, boolean centered)
    {
        this(position, text, maxWidth, DEFAULT_ACTION, centered);
    }

    public SpruceLabelWidget(Position position, @NotNull Text text, int maxWidth)
    {
        this(position, text, maxWidth, DEFAULT_ACTION);
    }

    @Deprecated
    public SpruceLabelWidget(int x, int y, @NotNull Text text, int maxWidth, @NotNull Consumer<SpruceLabelWidget> action, boolean centered)
    {
        this(Position.of(x, y), text, maxWidth, action, centered);
    }

    @Deprecated
    public SpruceLabelWidget(int x, int y, @NotNull Text text, int maxWidth, @NotNull Consumer<SpruceLabelWidget> action)
    {
        this(Position.of(x, y), text, maxWidth, action);
    }

    @Deprecated
    public SpruceLabelWidget(int x, int y, @NotNull Text text, int maxWidth, boolean centered)
    {
        this(Position.of(x, y), text, maxWidth, centered);
    }

    @Deprecated
    public SpruceLabelWidget(int x, int y, @NotNull Text text, int maxWidth)
    {
        this(Position.of(x, y), text, maxWidth);
    }

    @Override
    public @NotNull Position getPosition()
    {
        return this.position;
    }

    /**
     * Sets the text of this label.
     *
     * @param text the text to set
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
    public boolean isFocused()
    {
        return this.focused;
    }

    @Override
    public boolean isMouseHovered()
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
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        int x = this.centered ? this.getX() - this.client.textRenderer.getWidth(this.text) / 2 : this.getX();
        this.client.textRenderer.drawTrimmed(this.text, x, this.getY(), this.maxWidth, 10526880);

        if (this.tooltip != null) {
            if (!this.tooltip.getString().isEmpty()) {
                List<OrderedText> wrappedTooltipText = this.client.textRenderer.wrapLines(this.tooltip, Math.max(this.width / 2, 200));
                if (this.hovered)
                    new Tooltip(mouseX, mouseY, wrappedTooltipText).queue();
                else if (this.focused)
                    new Tooltip(this.getX() - 12, this.getY(), wrappedTooltipText).queue();
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (this.isVisible() && button == GLFW.GLFW_MOUSE_BUTTON_1) {
            if (this.hovered) {
                this.onPress();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onNavigation(@NotNull NavigationDirection direction)
    {
        if (this.action == DEFAULT_ACTION)
            return false;
        return super.onNavigation(direction);
    }
}
