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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Text;
import net.minecraft.resources.Identifier;

/**
 * Represents a textured button widget.
 *
 * @author LambdAurora
 * @version 5.0.0
 * @since 2.0.0
 */
public class SpruceTexturedButtonWidget extends SpruceButtonWidget {
	private final Identifier texture;
	private final int u;
	private final int v;
	private final int hoveredVOffset;
	private final int textureWidth;
	private final int textureHeight;
	private final boolean showMessage;

	public SpruceTexturedButtonWidget(Position position, int width, int height, Text message, PressAction action, int u, int v,
			int hoveredVOffset, Identifier texture) {
		this(position, width, height, message, false, action, u, v, hoveredVOffset, texture);
	}

	public SpruceTexturedButtonWidget(Position position, int width, int height, Text message, boolean showMessage, PressAction action,
			int u, int v, int hoveredVOffset, Identifier texture) {
		this(position, width, height, message, showMessage, action, u, v, hoveredVOffset, texture, 256, 256);
	}

	public SpruceTexturedButtonWidget(Position position, int width, int height, Text message, PressAction action,
			int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight) {
		this(position, width, height, message, false, action, u, v, hoveredVOffset, texture, textureWidth, textureHeight);
	}

	public SpruceTexturedButtonWidget(Position position, int width, int height, Text message, boolean showMessage, PressAction action,
			int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight) {
		super(position, width, height, message, action);
		this.texture = texture;
		this.u = u;
		this.v = v;
		this.hoveredVOffset = hoveredVOffset;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.showMessage = showMessage;
	}

	/* Rendering */

	@Override
	protected void renderButton(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		if (this.showMessage)
			super.renderButton(graphics, mouseX, mouseY, delta);
	}

	@Override
	protected void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		int v = this.v;
		if (this.isFocusedOrHovered()) {
			v += this.hoveredVOffset;
		}

		RenderSystem.setShaderColor(1.f, 1.f, 1.f, this.getAlpha());
		RenderSystem.setShaderTexture(0, this.texture);
		RenderSystem.enableDepthTest();
		graphics.drawTexture(this.texture,
				this.getX(), this.getY(),
				this.u, v,
				this.getWidth(), this.getHeight(),
				this.textureWidth, this.textureHeight
		);
	}

	@Override
	public String toString() {
		return "SpruceTexturedButtonWidget{" +
				"position=" + this.getPosition() +
				", width=" + this.getWidth() +
				", height=" + this.getHeight() +
				", visible=" + this.isVisible() +
				", active=" + this.isActive() +
				", message=" + this.getMessage() +
				", focused=" + this.isFocused() +
				", hovered=" + this.isMouseHovered() +
				", wasHovered=" + this.wasHovered +
				", dragging=" + this.dragging +
				", lastDrag=" + this.lastDrag +
				", alpha=" + this.getAlpha() +
				", texture=" + this.texture +
				", u=" + this.u +
				", v=" + this.v +
				", hoveredVOffset=" + this.hoveredVOffset +
				", textureWidth=" + this.textureWidth +
				", textureHeight=" + this.textureHeight +
				", showMessage=" + this.showMessage +
				'}';
	}
}
