/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import dev.lambdaurora.spruceui.tooltip.Tooltip;
import dev.lambdaurora.spruceui.tooltip.TooltipData;
import dev.lambdaurora.spruceui.tooltip.Tooltipable;
import dev.lambdaurora.spruceui.wrapper.VanillaButtonWrapper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

/**
 * Represents a button-like widget.
 *
 * @author LambdAurora
 * @version 9.0.0
 * @since 2.0.0
 */
public abstract class AbstractSpruceButtonWidget extends AbstractSpruceWidget implements Tooltipable {
	private Component message;
	private TooltipData tooltip = TooltipData.EMPTY;
	private int tooltipTicks;
	private long lastTick;
	protected float alpha = 1.f;

	/**
	 * @see net.minecraft.client.gui.components.AbstractButton#SPRITES
	 */
	protected static final WidgetSprites BUTTON_TEXTURES = new WidgetSprites(
			Identifier.withDefaultNamespace("widget/button"),
			Identifier.withDefaultNamespace("widget/button_disabled"),
			Identifier.withDefaultNamespace("widget/button_highlighted")
	);

	public AbstractSpruceButtonWidget(Position position, int width, int height, Component message) {
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
	public Component getMessage() {
		return this.message;
	}

	/**
	 * Sets the message of this button-like widget.
	 *
	 * @param message the message of this widget.
	 */
	public void setMessage(Component message) {
		this.message = message;
	}

	public float getAlpha() {
		return this.alpha;
	}

	public void setAlpha(float value) {
		this.alpha = value;
	}

	@Override
	public TooltipData getTooltip() {
		return this.tooltip;
	}

	@Override
	public void setTooltip(TooltipData tooltip) {
		Objects.requireNonNull(
				tooltip,
				"Tooltip cannot be null, the absence of a tooltip is represented by TooltipData.EMPTY."
		);
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
	protected boolean onMouseClick(MouseButtonEvent event, boolean doubleClick) {
		if (this.isValidClickButton(event.button())) {
			this.onClick(event.x(), event.y());
			return true;
		}
		return false;
	}

	@Override
	public boolean onMouseRelease(MouseButtonEvent event) {
		if (this.isValidClickButton(event.button())) {
			this.onRelease(event.x(), event.y());
			return true;
		}
		return false;
	}

	@Override
	protected boolean onMouseDrag(MouseButtonEvent event, double deltaX, double deltaY) {
		if (this.isValidClickButton(event.button())) {
			this.onDrag(event.x(), event.y(), deltaX, deltaY);
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
		return BUTTON_TEXTURES.get(this.isActive(), this.isFocusedOrHovered());
	}

	@Override
	protected void renderWidget(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
		this.renderButton(graphics, mouseX, mouseY, delta);

		if (this.isMouseHovered()) {
			graphics.requestCursor(this.isActive() ? CursorTypes.POINTING_HAND : CursorTypes.NOT_ALLOWED);
		}

		if (!this.dragging)
			Tooltip.queueFor(this, mouseX, mouseY, this.tooltipTicks,
					i -> this.tooltipTicks = i, this.lastTick, i -> this.lastTick = i);
	}

	protected void renderButton(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
		int color = this.active ? 16777215 : 10526880;
		this.renderText(graphics, color | Mth.ceil(this.alpha * 255.0F) << 24);
	}

	protected void renderText(SpruceGuiGraphics graphics, int color) {
		int margin = 2;
		int startX = this.getX() + margin;
		int endX = this.getX() + this.getWidth() - margin;
		var collector = graphics.textRenderer(this.alpha, GuiGraphics.HoveredTextEffects.NONE);
		collector.acceptScrolling(this.getMessage(),
				this.getX() + this.getWidth() / 2, startX, endX,
				this.getY(), this.getY() + this.getHeight()
		);
	}

	@Override
	protected void renderBackground(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
		graphics.drawSprite(RenderPipelines.GUI_TEXTURED, this.getTexture(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}

	/* Narration */

	@Override
	protected @Nullable Component getNarrationMessage() {
		return Component.translatable("gui.narrate.button", this.getMessage());
	}

	protected Component getNarrationFocusedUsageMessage() {
		return Component.translatable("narration.button.usage.focused");
	}

	protected Component getNarrationHoveredUsageMessage() {
		return Component.translatable("narration.button.usage.hovered");
	}

	@Override
	public void updateNarration(NarrationElementOutput builder) {
		super.updateNarration(builder);
		if (this.isActive()) {
			if (this.isFocused()) builder.add(NarratedElementType.USAGE, this.getNarrationFocusedUsageMessage());
			else builder.add(NarratedElementType.USAGE, this.getNarrationHoveredUsageMessage());
		}
		this.tooltip.updateNarration(builder);
	}
}
