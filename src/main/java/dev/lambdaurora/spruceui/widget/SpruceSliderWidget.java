/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.Tooltipable;
import dev.lambdaurora.spruceui.navigation.NavigationDirection;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Text;
import net.minecraft.resources.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;

/**
 * Represents a slider widget.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 1.0.0
 */
public class SpruceSliderWidget extends AbstractSpruceButtonWidget implements Tooltipable {
	private Text baseMessage;
	protected double value;
	private final Consumer<SpruceSliderWidget> applyConsumer;
	private double multiplier;
	private String sign;
	private boolean inUse = false;

	private static final Identifier SLIDER = Identifier.ofDefault("widget/slider");
	private static final Identifier SLIDER_HANDLE = Identifier.ofDefault("widget/slider_handle");
	private static final Identifier SLIDER_HANDLE_HIGHLIGHTED = Identifier.ofDefault("widget/slider_handle_highlighted");

	public SpruceSliderWidget(Position position, int width, int height, Text message, double value, Consumer<SpruceSliderWidget> applyConsumer, double multiplier, String sign) {
		super(position, width, height, message);
		this.value = value;
		this.baseMessage = message;
		this.applyConsumer = applyConsumer;
		this.multiplier = multiplier;
		this.sign = sign;
		this.updateMessage();
	}

	public SpruceSliderWidget(Position position, int width, int height, Text message, double progress, Consumer<SpruceSliderWidget> applyConsumer) {
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
	public Text getBaseMessage() {
		return this.baseMessage;
	}

	/**
	 * Sets the base message of the slider.
	 *
	 * @param baseMessage the base message of the slider
	 */
	public void setBaseMessage(Text baseMessage) {
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
	public boolean onNavigation(NavigationDirection direction, boolean tab) {
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
		this.inUse = true;
	}

	@Override
	protected void onRelease(double mouseX, double mouseY) {
		if (this.inUse) {
			this.playDownSound();
			this.inUse = false;
		}
	}

	@Override
	protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
		this.setValueFromMouse(mouseX);
		this.inUse = true;
	}

	private void setValueFromMouse(double mouseX) {
		this.setValue((mouseX - (double) (this.getX() + 4)) / (double) (this.getWidth() - 8));
	}

	/* Rendering */

	@Override
	protected Identifier getTexture() {
		return SLIDER;
	}

	@Override
	protected void renderButton(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		RenderSystem.setShaderColor(1.f, 1.f, 1.f, 1.f);

		final Identifier texture = this.isFocusedOrHovered() ? SLIDER_HANDLE_HIGHLIGHTED : SLIDER_HANDLE;
		graphics.drawGuiTexture(texture, this.getX() + (int) (this.value * (double) (this.getWidth() - 8)), this.getY(), 8, 20);

		if (!this.isMouseHovered() && this.inUse) {
			this.inUse = false;
		}

		super.renderButton(graphics, mouseX, mouseY, delta);
	}

	/* Narration */

	@Override
	protected Text getNarrationMessage() {
		return Text.translatable("gui.narrate.slider", this.getMessage());
	}

	@Override
	protected Text getNarrationFocusedUsageMessage() {
		return Text.translatable("narration.slider.usage.focused");
	}

	@Override
	protected Text getNarrationHoveredUsageMessage() {
		return Text.translatable("narration.slider.usage.hovered");
	}
}
