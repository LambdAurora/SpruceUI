/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.navigation.NavigationEvent;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import dev.lambdaurora.spruceui.tooltip.Tooltipable;
import net.minecraft.client.gui.navigation.ScreenAxis;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

/**
 * Represents a slider widget.
 *
 * @author LambdAurora
 * @version 9.0.0
 * @since 1.0.0
 */
public class SpruceSliderWidget extends AbstractSpruceButtonWidget implements Tooltipable {
	private Component baseMessage;
	protected double value;
	private final Consumer<SpruceSliderWidget> applyConsumer;
	private double multiplier;
	private String sign;
	private boolean inUse = false;

	private static final Identifier SLIDER = Identifier.withDefaultNamespace("widget/slider");
	private static final Identifier SLIDER_HANDLE = Identifier.withDefaultNamespace("widget/slider_handle");
	private static final Identifier SLIDER_HANDLE_HIGHLIGHTED = Identifier.withDefaultNamespace("widget/slider_handle_highlighted");

	public SpruceSliderWidget(Position position, int width, int height, Component message, double value, Consumer<SpruceSliderWidget> applyConsumer, double multiplier, String sign) {
		super(position, width, height, message);
		this.value = value;
		this.baseMessage = message;
		this.applyConsumer = applyConsumer;
		this.multiplier = multiplier;
		this.sign = sign;
		this.updateMessage();
	}

	public SpruceSliderWidget(Position position, int width, int height, Component message, double progress, Consumer<SpruceSliderWidget> applyConsumer) {
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
		this.value = Mth.clamp(value, 0.0D, 1.0D);
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
	public Component getBaseMessage() {
		return this.baseMessage;
	}

	/**
	 * Sets the base message of the slider.
	 *
	 * @param baseMessage the base message of the slider
	 */
	public void setBaseMessage(Component baseMessage) {
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
	public boolean onNavigation(NavigationEvent event) {
		if (event.direction().getAxis() == ScreenAxis.HORIZONTAL && !event.tab()) {
			if (event.isLookingForward() && this.value < 1 || this.value > 0) {
				this.setValue(this.getValue() + (event.isLookingForward() ? (1 / this.multiplier) : -(1 / this.multiplier)));
				return true;
			}
		}
		return super.onNavigation(event);
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
	protected void renderButton(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
		final Identifier texture = this.isFocusedOrHovered() ? SLIDER_HANDLE_HIGHLIGHTED : SLIDER_HANDLE;
		graphics.drawSprite(
				RenderPipelines.GUI_TEXTURED,
				texture,
				this.getX() + (int) (this.value * (double) (this.getWidth() - 8)), this.getY(),
				8, 20
		);

		if (!this.isMouseHovered() && this.inUse) {
			this.inUse = false;
		}

		super.renderButton(graphics, mouseX, mouseY, delta);
	}

	/* Narration */

	@Override
	protected Component getNarrationMessage() {
		return Component.translatable("gui.narrate.slider", this.getMessage());
	}

	@Override
	protected Component getNarrationFocusedUsageMessage() {
		return Component.translatable("narration.slider.usage.focused");
	}

	@Override
	protected Component getNarrationHoveredUsageMessage() {
		return Component.translatable("narration.slider.usage.hovered");
	}
}
