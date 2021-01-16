/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import me.lambdaurora.spruceui.Position;
import me.lambdaurora.spruceui.Tooltipable;
import me.lambdaurora.spruceui.navigation.NavigationDirection;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Represents a slider widget.
 *
 * @author LambdAurora
 * @version 2.0.0
 * @since 1.0.0
 */
public class SpruceSliderWidget extends AbstractSpruceButtonWidget implements Tooltipable {
    private Text baseMessage;
    protected double value;
    private final Consumer<SpruceSliderWidget> applyConsumer;
    private double multiplier;
    private String sign;

    public SpruceSliderWidget(Position position, int width, int height, @NotNull Text message, double value, @NotNull Consumer<SpruceSliderWidget> applyConsumer, double multiplier, String sign) {
        super(position, width, height, message);
        this.value = value;
        this.baseMessage = message;
        this.applyConsumer = applyConsumer;
        this.multiplier = multiplier;
        this.sign = sign;
        this.updateMessage();
    }

    public SpruceSliderWidget(Position position, int width, int height, @NotNull Text message, double progress, @NotNull Consumer<SpruceSliderWidget> applyConsumer) {
        this(position, width, height, message, progress, applyConsumer, 100.0, "%");
    }

    /**
     * Gets the value of the slider.
     *
     * @return the value of the slider
     */
    public double getValue() {
        return this.value;
    }

    /**
     * Sets the value of the slider.
     *
     * @param value the value of the slider
     */
    private void setValue(double value) {
        double oldValue = this.value;
        this.value = MathHelper.clamp(value, 0.0D, 1.0D);
        if (oldValue != this.value) {
            this.applyValue();
        }

        this.updateMessage();
    }


    /**
     * Returns the value of this slider as an integer.
     *
     * @return the value as an integer
     */
    public int getIntValue() {
        return (int) (this.value * this.multiplier);
    }

    /**
     * Sets the value of this slider.
     *
     * @param value the new value as an integer
     */
    public void setIntValue(int value) {
        this.setValue(value / this.multiplier);
    }

    /**
     * Gets the base message of the slider.
     *
     * @return the base message of the slider
     */
    public @NotNull Text getBaseMessage() {
        return this.baseMessage;
    }

    /**
     * Sets the base message of the slider.
     *
     * @param baseMessage the base message of the slider
     */
    public void setBaseMessage(@NotNull Text baseMessage) {
        this.baseMessage = baseMessage;
    }

    protected void updateMessage() {
        this.setMessage(this.baseMessage.copy().append(": " + this.getIntValue() + sign));
    }

    protected void applyValue() {
        this.applyConsumer.accept(this);
    }

    /* Navigation */

    @Override
    public boolean onNavigation(@NotNull NavigationDirection direction, boolean tab) {
        if (direction.isHorizontal() && !tab) {
            if (direction.isLookingForward() && this.value < 1 || this.value > 0) {
                this.setValue(this.getValue() + (direction.isLookingForward() ? (1 / this.multiplier) : -(1 / this.multiplier)));
                return true;
            }
        }
        return super.onNavigation(direction, tab);
    }

    /* Input */

    @Override
    protected void onClick(double mouseX, double mouseY) {
        this.setValueFromMouse(mouseX);
    }

    @Override
    protected void onRelease(double mouseX, double mouseY) {
        this.playDownSound();
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        this.setValueFromMouse(mouseX);
    }

    private void setValueFromMouse(double mouseX) {
        this.setValue((mouseX - (double) (this.getX() + 4)) / (double) (this.getWidth() - 8));
    }

    /* Rendering */

    @Override
    protected int getVOffset() {
        return 0;
    }

    @Override
    protected void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.client.getTextureManager().bindTexture(AbstractButtonWidget.WIDGETS_LOCATION);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int vOffset = (this.isFocusedOrHovered() ? 2 : 1) * 20;
        this.drawTexture(matrices, this.getX() + (int) (this.value * (double) (this.getWidth() - 8)), this.getY(), 0, 46 + vOffset, 4, 20);
        this.drawTexture(matrices, this.getX() + (int) (this.value * (double) (this.getWidth() - 8)) + 4, this.getY(), 196, 46 + vOffset, 4, 20);

        super.renderButton(matrices, mouseX, mouseY, delta);
    }

    /* Narration */

    @Override
    protected @NotNull Optional<Text> getNarrationMessage() {
        return Optional.of(new TranslatableText("gui.narrate.slider", this.getMessage()));
    }
}
