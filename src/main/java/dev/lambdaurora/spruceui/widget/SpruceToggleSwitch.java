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
import dev.lambdaurora.spruceui.SpruceTexts;
import dev.lambdaurora.spruceui.SpruceUI;
import dev.lambdaurora.spruceui.resources.GuiSpriteManager;
import dev.lambdaurora.spruceui.util.WidgetSprites;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a checkbox widget.
 *
 * @author LambdAurora
 * @version 6.0.0
 * @since 1.0.0
 */
public class SpruceToggleSwitch extends AbstractSpruceBooleanButtonWidget {
	public static final WidgetSprites BACKGROUND_TEXTURE = new WidgetSprites(
			SpruceUI.id("textures/gui/sprites/widget/toggle_switch/background.png"),
			SpruceUI.id("textures/gui/sprites/widget/toggle_switch/background_highlighted.png")
	);
	public static final WidgetSprites ON_TEXTURE = new WidgetSprites(
			SpruceUI.id("textures/gui/sprites/widget/toggle_switch/on.png"),
			SpruceUI.id("textures/gui/sprites/widget/toggle_switch/on_highlighted.png")
	);
	public static final WidgetSprites OFF_TEXTURE = new WidgetSprites(
			SpruceUI.id("textures/gui/sprites/widget/toggle_switch/off.png"),
			SpruceUI.id("textures/gui/sprites/widget/toggle_switch/off_highlighted.png")
	);

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
	protected void renderButton(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		GuiSpriteManager.get().drawSprite(graphics,
				(this.getValue() ? ON_TEXTURE : OFF_TEXTURE).get(this.isActive(), this.isFocusedOrHovered()),
				this.getX() + (this.getValue() ? 14 : 0), this.getY() + (this.getHeight() / 2 - 9),
				18, 18
		);

		if (this.showMessage) {
			var message = Language.getInstance().getVisualOrder(
					this.client.font.substrByWidth(this.getMessage(), this.getWidth() - 40)
			);
			graphics.drawShadowedText(this.client.font, message, this.getX() + 36, this.getY() + (this.getHeight() - 8) / 2,
					14737632 | MathHelper.ceil(this.alpha * 255.0F) << 24);
		}
	}

	@Override
	protected void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		GuiSpriteManager.get().drawSprite(graphics,
				BACKGROUND_TEXTURE.get(this.isActive(), this.isFocusedOrHovered()),
				this.getX(), this.getY() + (this.getHeight() / 2 - 9),
				32, 18
		);
	}

	/* Narration */

	@Override
	protected @Nullable Text getNarrationMessage() {
		return Text.translatable("spruceui.narration.toggle_switch", this.getMessage(),
				SpruceTexts.getToggleText(this.getValue()));
	}
}
