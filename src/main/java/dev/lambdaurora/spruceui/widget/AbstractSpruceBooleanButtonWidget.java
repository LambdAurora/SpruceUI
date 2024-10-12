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

/**
 * Represents a pressable button that switches between two states which values are {@code true} and {@code false}.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 2.0.0
 */
public abstract class AbstractSpruceBooleanButtonWidget extends AbstractSprucePressableButtonWidget {
	private static final PressAction DEFAULT_ACTION = (button, newValue) -> {
	};

	private final PressAction action;
	private boolean value;
	protected boolean showMessage;

	public AbstractSpruceBooleanButtonWidget(Position position, int width, int height, Text message, boolean value) {
		this(position, width, height, message, value, true);
	}

	public AbstractSpruceBooleanButtonWidget(
			Position position, int width, int height, Text message, boolean value, boolean showMessage
	) {
		this(position, width, height, message, DEFAULT_ACTION, value, showMessage);
	}

	public AbstractSpruceBooleanButtonWidget(
			Position position, int width, int height, Text message, PressAction action, boolean value
	) {
		this(position, width, height, message, action, value, true);
	}

	public AbstractSpruceBooleanButtonWidget(
			Position position, int width, int height, Text message, PressAction action, boolean value, boolean showMessage
	) {
		super(position, width, height, message);
		this.action = action;
		this.value = value;
		this.showMessage = showMessage;
	}

	/**
	 * Returns the value of this button.
	 *
	 * @return {@code true} or {@code false}.
	 */
	public boolean getValue() {
		return this.value;
	}

	@Override
	public void onPress() {
		this.value = !this.value;
		this.action.onPress(this, this.value);
	}

	/**
	 * Represents the press action handler.
	 *
	 * @version 3.0.0
	 * @since 2.0.0
	 */
	public interface PressAction {
		void onPress(AbstractSpruceBooleanButtonWidget button, boolean newValue);
	}
}
