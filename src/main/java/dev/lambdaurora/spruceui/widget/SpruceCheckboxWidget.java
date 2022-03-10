/*
 * Copyright © 2020-2022 LambdAurora <email@lambdaurora.dev>
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
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.minecraft.util.math.MathHelper;

/**
 * Represents a checkbox widget.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 1.0.0
 */
public class SpruceCheckboxWidget extends AbstractSpruceBooleanButtonWidget {
	private static final Identifier TEXTURE = new Identifier("spruceui", "textures/gui/checkbox.png");
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
	protected void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.enableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
		if (this.getValue()) {
			if (this.colored)
				RenderSystem.setShaderColor(0.f, 1.f, 0.f, this.alpha);
			drawTexture(matrices, this.getX(), this.getY(), 0.f, 40.f, this.getHeight(), this.getHeight(), 64, 64);
		} else if (this.showCross) {
			if (this.colored)
				RenderSystem.setShaderColor(1.f, 0.f, 0.f, this.alpha);
			drawTexture(matrices, this.getX(), this.getY(), 0.f, 20.f, this.getHeight(), this.getHeight(), 64, 64);
		}

		if (this.showMessage) {
			OrderedText message = Language.getInstance().reorder(this.client.textRenderer.trimToWidth(this.getMessage(), this.getWidth() - this.getHeight() - 4));
			this.client.textRenderer.drawWithShadow(matrices, message, this.getX() + this.getHeight() + 4, this.getY() + (this.getHeight() - 8) / 2.f,
					14737632 | MathHelper.ceil(this.alpha * 255.0F) << 24);
		}
	}

	@Override
	protected void renderBackground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.f, 1.f, 1.f, this.alpha);
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
		drawTexture(matrices, this.getX(), this.getY(), this.isFocusedOrHovered() ? 20.f : 0.f, 0.f, this.getHeight(), this.getHeight(), 64, 64);
	}

	/* Narration */

	@Override
	protected Text getNarrationFocusedUsageMessage() {
		return new TranslatableText("narration.checkbox.usage.focused");
	}

	@Override
	protected Text getNarrationHoveredUsageMessage() {
		return new TranslatableText("narration.checkbox.usage.hovered");
	}
}
