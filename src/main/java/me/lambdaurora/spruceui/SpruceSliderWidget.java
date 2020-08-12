/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Represents a slightly modified slider widget.
 *
 * @author LambdAurora
 * @version 1.6.1
 * @since 1.0.0
 */
public class SpruceSliderWidget extends SliderWidget implements SpruceWidget, Tooltipable
{
    private       Text                         baseMessage;
    private final Consumer<SpruceSliderWidget> applyConsumer;
    private       double                       multiplier;
    private       String                       sign;
    private       Text                         tooltip;
    private       int                          tooltipTicks;
    private       long                         lastTick;

    protected SpruceSliderWidget(int x, int y, int width, int height, @NotNull Text message, double progress, @NotNull Consumer<SpruceSliderWidget> applyConsumer, double multiplier, String sign)
    {
        super(x, y, width, height, message, progress);
        this.baseMessage = message;
        this.applyConsumer = applyConsumer;
        this.multiplier = multiplier;
        this.sign = sign;
        this.updateMessage();
    }

    protected SpruceSliderWidget(int x, int y, int width, int height, @NotNull Text message, double progress, @NotNull Consumer<SpruceSliderWidget> applyConsumer)
    {
        this(x, y, width, height, message, progress, applyConsumer, 100.0, "%");
    }

    /**
     * Gets the value of the slider.
     *
     * @return The value of the slider.
     */
    public double getValue()
    {
        return this.value;
    }

    /**
     * Returns the value of this slider as an integer .
     *
     * @return The value as an integer.
     */
    public int getIntValue()
    {
        return (int) (this.value * this.multiplier);
    }

    /**
     * Sets the value of this slider.
     *
     * @param value The new value as an integer.
     */
    public void setIntValue(int value)
    {
        this.value = value / this.multiplier;
        this.updateMessage();
    }

    /**
     * Gets the base message of the slider.
     *
     * @return The base message of the slider.
     */
    public @NotNull Text getBaseMessage()
    {
        return baseMessage;
    }

    /**
     * Sets the base message of the slider.
     *
     * @param baseMessage The base message of the slider.
     */
    public void setBaseMessage(@NotNull Text baseMessage)
    {
        this.baseMessage = baseMessage;
    }

    @Override
    protected void updateMessage()
    {
        this.setMessage(this.baseMessage.copy().append(": " + this.getIntValue() + sign));
    }

    @Override
    protected void applyValue()
    {
        this.applyConsumer.accept(this);
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
    public boolean isFocused()
    {
        return super.isFocused();
    }

    @Override
    public boolean isMouseHovered()
    {
        return this.hovered;
    }
}
