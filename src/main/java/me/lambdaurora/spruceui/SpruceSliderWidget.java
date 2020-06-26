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
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Represents a slightly modified slider widget.
 *
 * @author LambdAurora
 * @version 1.5.0
 * @since 1.0.0
 */
public class SpruceSliderWidget extends SliderWidget implements Tooltipable
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

        if (this.visible && this.tooltip != null) {
            long currentRender = System.currentTimeMillis();
            if (this.lastTick != 0) {
                if (currentRender - this.lastTick >= 20) {
                    this.tooltipTicks++;
                    this.lastTick = currentRender;
                }
            } else this.lastTick = currentRender;

            if (!this.isFocused() && !this.hovered)
                this.tooltipTicks = 0;

            if (!this.tooltip.getString().isEmpty() && this.tooltipTicks >= 30) {
                List<? extends StringRenderable> wrappedTooltipText = MinecraftClient.getInstance().textRenderer.wrapLines(this.tooltip, Math.max(this.width * 2 / 3, 200));
                if (this.hovered)
                    new Tooltip(mouseX, mouseY, wrappedTooltipText).queue();
                else if (this.isFocused())
                    new Tooltip(this.x - 12, this.y + 12 + wrappedTooltipText.size() * 10, wrappedTooltipText).queue();
            }
        }
    }
}
