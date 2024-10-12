/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget.option;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.option.SpruceDoubleOption;
import dev.lambdaurora.spruceui.widget.SpruceSliderWidget;
import net.minecraft.network.chat.Text;

/**
 * Represents an option slider widget.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 1.0.0
 */
public class SpruceOptionSliderWidget extends SpruceSliderWidget {
	private final SpruceDoubleOption option;

	public SpruceOptionSliderWidget(Position position, int width, int height, SpruceDoubleOption option) {
		super(position, width, height, Text.empty(), option.getRatio(option.get()), slider -> option.set(option.getValue(slider.getValue())));
		this.option = option;
		this.updateMessage();
	}

	@Override
	protected void updateMessage() {
		if (this.option != null)
			this.setMessage(this.option.getDisplayString());
	}
}
