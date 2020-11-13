/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import me.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Represents a checkbox widget.
 *
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.0.0
 */
public class SpruceCheckboxWidget extends net.minecraft.client.gui.widget.CheckboxWidget implements SpruceWidget, Tooltipable
{
    private final Position position;
    private final Consumer<SpruceCheckboxWidget> action;
    private Text tooltip;
    private int tooltipTicks;
    private long lastTick;

    public SpruceCheckboxWidget(int x, int y, int width, int height, Text message, boolean checked, Consumer<SpruceCheckboxWidget> action)
    {
        super(x, y, width, height, message, checked);
        this.position = Position.of(x, y);
        this.action = action;
    }

    @Override
    public @NotNull Position getPosition()
    {
        return this.position.move(this.x, this.y);
    }

    @Override
    public void onPress()
    {
        super.onPress();
        this.action.accept(this);
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
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        super.render(matrices, mouseX, mouseY, delta);

        Tooltip.queueFor(this, mouseX, mouseY, this.tooltipTicks, i -> this.tooltipTicks = i, this.lastTick, i -> this.lastTick = i);
    }

    @Override
    public int getX()
    {
        return this.x;
    }

    @Override
    public int getY()
    {
        return this.y;
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
        return super.getWidth();
    }

    @Override
    public int getHeight()
    {
        return super.getHeight();
    }

    @Override
    public boolean isFocused()
    {
        return super.isFocused();
    }

    @Override
    public void setFocused(boolean focused)
    {
        super.setFocused(focused);
    }

    @Override
    public boolean isMouseHovered()
    {
        return this.hovered;
    }
}
