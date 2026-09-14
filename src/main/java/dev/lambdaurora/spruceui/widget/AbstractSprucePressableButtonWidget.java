/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget;

import com.mojang.blaze3d.platform.InputConstants;
import dev.lambdaurora.spruceui.Position;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

/**
 * Represents a pressable button widget.
 *
 * @author LambdAurora
 * @version 12.0.0
 * @since 2.0.0
 */
public abstract class AbstractSprucePressableButtonWidget extends AbstractSpruceButtonWidget {
	public AbstractSprucePressableButtonWidget(Position position, int width, int height, Component message) {
		super(position, width, height, message);
	}

	public abstract void onPress();

	@Override
	public void onClick(double mouseX, double mouseY) {
		this.onPress();
		this.playDownSound();
	}

	@Override
	protected boolean onKeyPress(KeyEvent event) {
		if (event.key() == InputConstants.KEY_RETURN
				|| event.key() == InputConstants.KEY_NUMPADENTER
				|| event.key() == InputConstants.KEY_SPACE
		) {
			this.onPress();
			this.playDownSound();
			return true;
		}
		return false;
	}
}
