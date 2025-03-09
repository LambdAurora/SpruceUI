/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.SpruceUI;
import dev.lambdaurora.spruceui.resources.GuiSpriteManager;
import net.minecraft.client.gui.GuiGraphics;
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
	private static final Identifier TEXTURE = new Identifier("textures/gui/checkbox.png");
	public static final Identifier CHECKED_TEXTURE = SpruceUI.id("textures/gui/sprites/widget/checkbox/checked.png");
	public static final Identifier CROSSED_TEXTURE = SpruceUI.id("textures/gui/sprites/widget/checkbox/crossed.png");
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
		int alpha = (int) (this.alpha * 255) << 24;

		if (this.getValue()) {
			GuiSpriteManager.get().drawSprite(graphics,
					CHECKED_TEXTURE,
					this.getX(), this.getY(),
					this.getHeight(), this.getHeight(),
					alpha | (this.colored ? 0x0000ff00 : 0x00ffffff)
			);
		} else if (this.showCross) {
			GuiSpriteManager.get().drawSprite(graphics,
					CROSSED_TEXTURE,
					this.getX(), this.getY(),
					this.getHeight(), this.getHeight(),
					alpha | (this.colored ? 0x00ff0000 : 0x00ffffff)
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
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.f, 1.f, 1.f, this.alpha);
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		graphics.drawTexture(TEXTURE,
				this.getX(), this.getY(),
				this.isFocused() ? 20.f : 0.f, 0.f,
				this.getHeight(), this.getHeight(),
				64, 64
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
