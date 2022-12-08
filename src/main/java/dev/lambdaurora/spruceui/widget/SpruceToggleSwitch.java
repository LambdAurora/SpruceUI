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
import dev.lambdaurora.spruceui.SpruceTexts;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a checkbox widget.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 1.0.0
 */
public class SpruceToggleSwitch extends AbstractSpruceBooleanButtonWidget {
	private static final Identifier TEXTURE = new Identifier("spruceui", "textures/gui/toggle_switch.png");

	public SpruceToggleSwitch(Position position, int width, int height, Text message, boolean value) {
		super(position, width, height, message, value);
	}

	public SpruceToggleSwitch(Position position, int width, int height, Text message, boolean value,
	                          boolean showMessage) {
		super(position, width, height, message, value, showMessage);
	}

	public SpruceToggleSwitch(Position position, int width, int height, Text message, PressAction action,
	                          boolean value) {
		super(position, width, height, message, action, value);
	}

	public SpruceToggleSwitch(Position position, int width, int height, Text message, PressAction action,
	                          boolean value, boolean showMessage) {
		super(position, width, height, message, action, value, showMessage);
	}

	/* Rendering */

	@Override
	protected void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.enableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		drawTexture(matrices, this.getX() + (this.getValue() ? 14 : 0), this.getY() + (this.getHeight() / 2 - 9),
				this.getValue() ? 50.f : 32.f, this.isFocusedOrHovered() ? 18.f : 0.f,
				18, 18, 68, 36);

		if (this.showMessage) {
			var message = Language.getInstance().reorder(
					this.client.textRenderer.trimToWidth(this.getMessage(), this.getWidth() - 40)
			);
			this.client.textRenderer.drawWithShadow(matrices, message, this.getX() + 36, this.getY() + (this.getHeight() - 8) / 2.f,
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
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		drawTexture(matrices, this.getX(), this.getY() + (this.getHeight() / 2 - 9),
				0.f, this.isFocusedOrHovered() ? 18.f : 0.f, 32, 18, 68, 36);
	}

	/* Narration */

	@Override
	protected @Nullable Text getNarrationMessage() {
		return Text.translatable("spruceui.narration.toggle_switch", this.getMessage(),
				SpruceTexts.getToggleText(this.getValue()));
	}
}
