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
import dev.lambdaurora.spruceui.Tooltip;
import dev.lambdaurora.spruceui.Tooltipable;
import dev.lambdaurora.spruceui.wrapper.VanillaButtonWrapper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.text.Text;
import net.minecraft.unmapped.C_fajlgudl;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

/**
 * Represents a button-like widget.
 *
 * @author LambdAurora
 * @version 5.0.0
 * @since 2.0.0
 */
public abstract class AbstractSpruceButtonWidget extends AbstractSpruceWidget implements Tooltipable {
	private Text message;
	private Text tooltip;
	private int tooltipTicks;
	private long lastTick;
	protected float alpha = 1.f;

	/**
	 * @see net.minecraft.client.gui.widget.PressableWidget#field_45339
	 */
	protected static final C_fajlgudl BUTTON_TEXTURES = new C_fajlgudl(
			new Identifier("widget/button"), new Identifier("widget/button_disabled"), new Identifier("widget/button_highlighted")
	);

	public AbstractSpruceButtonWidget(Position position, int width, int height, Text message) {
		super(position);
		this.width = width;
		this.height = height;
		this.message = message;
	}

	/**
	 * Gets the message of this button-like widget.
	 *
	 * @return the message of this widget.
	 */
	public Text getMessage() {
		return this.message;
	}

	/**
	 * Sets the message of this button-like widget.
	 *
	 * @param message the message of this widget.
	 */
	public void setMessage(Text message) {
		this.message = message;
	}

	public float getAlpha() {
		return this.alpha;
	}

	public void setAlpha(float value) {
		this.alpha = value;
	}

	@Override
	public Optional<Text> getTooltip() {
		return Optional.ofNullable(this.tooltip);
	}

	@Override
	public void setTooltip(@Nullable Text tooltip) {
		this.tooltip = tooltip;
	}

	public VanillaButtonWrapper asVanilla() {
		return new VanillaButtonWrapper(this);
	}

	/* Input */

	protected boolean isValidClickButton(int button) {
		return button == GLFW.GLFW_MOUSE_BUTTON_1;
	}

	@Override
	protected boolean onMouseClick(double mouseX, double mouseY, int button) {
		if (this.isValidClickButton(button)) {
			this.onClick(mouseX, mouseY);
			return true;
		}
		return false;
	}

	@Override
	public boolean onMouseRelease(double mouseX, double mouseY, int button) {
		if (this.isValidClickButton(button)) {
			this.onRelease(mouseX, mouseY);
			return true;
		}
		return false;
	}

	@Override
	protected boolean onMouseDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (this.isValidClickButton(button)) {
			this.onDrag(mouseX, mouseY, deltaX, deltaY);
			return true;
		}
		return false;
	}

	protected void onClick(double mouseX, double mouseY) {
	}

	protected void onRelease(double mouseX, double mouseY) {
	}

	protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
	}

	/* Rendering */

	protected Identifier getTexture() {
		return BUTTON_TEXTURES.method_52729(this.isActive(), this.isFocusedOrHovered());
	}

	@Override
	protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		this.renderButton(graphics, mouseX, mouseY, delta);
		if (!this.dragging)
			Tooltip.queueFor(this, mouseX, mouseY, this.tooltipTicks,
					i -> this.tooltipTicks = i, this.lastTick, i -> this.lastTick = i);
	}

	protected void renderButton(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		int color = this.active ? 16777215 : 10526880;
		graphics.drawCenteredShadowedText(this.client.textRenderer, this.getMessage(),
				this.getX() + this.getWidth() / 2, this.getY() + (this.getHeight() - 8) / 2,
				color | MathHelper.ceil(this.alpha * 255.0F) << 24);
	}

	@Override
	protected void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		RenderSystem.setShaderColor(1.f, 1.f, 1.f, this.getAlpha());
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		graphics.drawGuiTexture(this.getTexture(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}

	/* Narration */

	@Override
	protected @Nullable Text getNarrationMessage() {
		return Text.translatable("gui.narrate.button", this.getMessage());
	}

	protected Text getNarrationFocusedUsageMessage() {
		return Text.translatable("narration.button.usage.focused");
	}

	protected Text getNarrationHoveredUsageMessage() {
		return Text.translatable("narration.button.usage.hovered");
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
		super.appendNarrations(builder);
		if (this.isActive()) {
			if (this.isFocused()) builder.put(NarrationPart.USAGE, this.getNarrationFocusedUsageMessage());
			else builder.put(NarrationPart.USAGE, this.getNarrationHoveredUsageMessage());
		}
		this.getTooltip().ifPresent(text -> builder.put(NarrationPart.HINT, text));
	}
}
