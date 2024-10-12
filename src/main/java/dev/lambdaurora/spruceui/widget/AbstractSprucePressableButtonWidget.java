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
import net.minecraft.network.chat.Text;
import org.lwjgl.glfw.GLFW;

/**
 * Represents a pressable button widget.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 2.0.0
 */
public abstract class AbstractSprucePressableButtonWidget extends AbstractSpruceButtonWidget {
	public AbstractSprucePressableButtonWidget(Position position, int width, int height, Text message) {
		super(position, width, height, message);
	}

	public abstract void onPress();

	@Override
	public void onClick(double mouseX, double mouseY) {
		this.onPress();
		this.playDownSound();
	}

	@Override
	protected boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER || keyCode == GLFW.GLFW_KEY_SPACE) {
			this.onPress();
			this.playDownSound();
			return true;
		}
		return false;
	}
}
