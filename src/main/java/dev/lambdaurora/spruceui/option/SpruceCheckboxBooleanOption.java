/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.option;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.tooltip.TooltipData;
import dev.lambdaurora.spruceui.widget.SpruceCheckboxWidget;
import dev.lambdaurora.spruceui.widget.SpruceWidget;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;

/**
 * Represents a boolean option.
 * <p>
 * Works the as {@link SpruceBooleanOption} but uses a checkbox instead.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 1.6.0
 */
public class SpruceCheckboxBooleanOption extends SpruceBooleanOption {
	public SpruceCheckboxBooleanOption(
			String key, Supplier<Boolean> getter, Consumer<Boolean> setter,
			TooltipData tooltip
	) {
		super(key, getter, setter, tooltip);
	}

	public SpruceCheckboxBooleanOption(
			String key, Supplier<Boolean> getter, Consumer<Boolean> setter,
			TooltipData tooltip, boolean colored
	) {
		super(key, getter, setter, tooltip, colored);
	}

	@Override
	public SpruceWidget createWidget(Position position, int width) {
		var button = new SpruceCheckboxWidget(
				position, width, 20, this.getDisplayText(),
				(btn, newValue) -> {
					this.set();
					btn.setMessage(this.getDisplayText());
				},
				this.get()
		);
		button.setColored(this.isColored());
		this.getTooltip().ifPresent(button::setTooltip);
		return button;
	}

	@Override
	public Component getDisplayText() {
		return this.getPrefix();
	}

	@Override
	public Component getDisplayText(Component value) {
		return this.getPrefix();
	}

	public static class Builder extends SpruceBooleanOption.BaseBuilder<Builder, SpruceCheckboxBooleanOption> {
		public Builder(String key, Supplier<Boolean> getter, Consumer<Boolean> setter) {
			super(key, getter, setter);
		}

		@Override
		protected Builder self() {
			return this;
		}

		public SpruceCheckboxBooleanOption build() {
			return new SpruceCheckboxBooleanOption(
					this.key, this.getter, this.setter,
					this.tooltip, this.colored
			);
		}
	}
}
