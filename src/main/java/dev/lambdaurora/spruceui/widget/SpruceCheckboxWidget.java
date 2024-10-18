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
import dev.lambdaurora.spruceui.SpruceUI;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Text;
import net.minecraft.resources.Identifier;
import net.minecraft.util.math.MathHelper;

/**
 * Represents a checkbox widget.
 *
 * @author LambdAurora
 * @version 6.0.0
 * @since 1.0.0
 */
public class SpruceCheckboxWidget extends AbstractSpruceBooleanButtonWidget {
	public static final WidgetSprites BACKGROUND_TEXTURE = new WidgetSprites(
			Identifier.ofDefault("widget/checkbox"),
			Identifier.ofDefault("widget/checkbox_highlighted")
	);
	public static final Identifier CHECKED_TEXTURE = SpruceUI.id("widget/checkbox/checked");
	public static final Identifier CROSSED_TEXTURE = SpruceUI.id("widget/checkbox/crossed");
	private boolean showCross = false;
	private boolean colored = false;

	public SpruceCheckboxWidget(Position position, int width, int height, Text message, boolean value) {
		super(position, width, height, message, value);
	}

	public SpruceCheckboxWidget(Position position, int width, int height, Text message, boolean value, boolean showMessage) {
		super(position, width, height, message, value, showMessage);
	}

	public SpruceCheckboxWidget(Position position, int width, int height, Text message, PressAction action, boolean value) {
		super(position, width, height, message, action, value);
	}

	public SpruceCheckboxWidget(Position position, int width, int height, Text message, PressAction action, boolean value, boolean showMessage) {
		super(position, width, height, message, action, value, showMessage);
	}

	/**
	 * Returns whether this checkbox shows a cross for the {@code false} value.
	 *
	 * @return {@code true} if this checkbox can show a cross, else {@code false}
	 */
	public boolean showCross() {
		return this.showCross;
	}

	/**
	 * Sets whether this checkbox shows a cross for the {@code false} value.
	 *
	 * @param showCross {@code true} if this checkbox can show a cross, else {@code false}
	 */
	public void setShowCross(boolean showCross) {
		this.showCross = showCross;
	}

	/**
	 * Returns whether this checkbox is colored or not.
	 *
	 * @return {@code true} if colored, else {@code false}
	 */
	public boolean isColored() {
		return this.colored;
	}

	/**
	 * Sets whether this checkbox is colored or not.
	 *
	 * @param colored {@code true} if colored, else {@code false}
	 */
	public void setColored(boolean colored) {
		this.colored = colored;
	}

	/* Rendering */

	@Override
	protected void renderButton(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		if (this.getValue()) {
			graphics.drawSprite(
					RenderType::guiTextured, CHECKED_TEXTURE,
					this.getX(), this.getY(),
					this.getHeight(), this.getHeight(),
					this.colored ? 0xff00ff00 : -1
			);
		} else if (this.showCross) {
			graphics.drawSprite(
					RenderType::guiTextured, CROSSED_TEXTURE,
					this.getX(), this.getY(),
					this.getHeight(), this.getHeight(),
					this.colored ? 0xffff0000 : -1
			);
		}

		if (this.showMessage) {
			var message = Language.getInstance().getVisualOrder(this.client.font.substrByWidth(this.getMessage(), this.getWidth() - this.getHeight() - 4));
			graphics.drawShadowedText(this.client.font, message, this.getX() + this.getHeight() + 4, this.getY() + (this.getHeight() - 8) / 2,
					14737632 | MathHelper.ceil(this.alpha * 255.0F) << 24);
		}
	}

	@Override
	protected void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		graphics.drawSprite(
				RenderType::guiTextured, BACKGROUND_TEXTURE.get(this.isActive(), this.isFocusedOrHovered()),
				this.getX(), this.getY(),
				this.getHeight(), this.getHeight()
		);
	}

	/* Narration */

	@Override
	protected Text getNarrationFocusedUsageMessage() {
		return Text.translatable("narration.checkbox.usage.focused");
	}

	@Override
	protected Text getNarrationHoveredUsageMessage() {
		return Text.translatable("narration.checkbox.usage.hovered");
	}
}
