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
import dev.lambdaurora.spruceui.widget.SpruceToggleSwitch;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.network.chat.Text;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents a boolean option.
 * <p>
 * Works the as {@link SpruceBooleanOption} but uses a toggle switch instead.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 2.0.0
 */
public class SpruceToggleBooleanOption extends SpruceBooleanOption {
	private final boolean showMessage;

	public SpruceToggleBooleanOption(
			String key, Supplier<Boolean> getter, Consumer<Boolean> setter,
			@NotNull TooltipData tooltip, boolean showMessage
	) {
		super(key, getter, setter, tooltip, false);
		this.showMessage = showMessage;
	}

	public SpruceToggleBooleanOption(
			String key, Supplier<Boolean> getter, Consumer<Boolean> setter,
			@NotNull TooltipData tooltip
	) {
		this(key, getter, setter, tooltip, true);
	}

	@Override
	public SpruceWidget createWidget(Position position, int width) {
		var button = new SpruceToggleSwitch(
				position, width, 20, this.getDisplayText(),
				(btn, newValue) -> {
					this.set();
					btn.setMessage(this.getDisplayText());
					this.getTooltip().ifPresent(btn::setTooltip);
				},
				this.get(), this.showMessage
		);
		this.getTooltip().ifPresent(button::setTooltip);
		return button;
	}

	@Override
	public Text getDisplayText() {
		return this.getPrefix();
	}

	@Override
	public Text getDisplayText(Text value) {
		return this.getPrefix();
	}

	public static class Builder extends SpruceBooleanOption.BaseBuilder<Builder, SpruceToggleBooleanOption> {
		private boolean showMessage = true;

		public Builder(String key, Supplier<Boolean> getter, Consumer<Boolean> setter) {
			super(key, getter, setter);
		}

		public Builder showMessage() {
			return this.showMessage(true);
		}

		public Builder showMessage(boolean showMessage) {
			this.showMessage = showMessage;
			return this;
		}

		@Override
		protected Builder self() {
			return this;
		}

		public SpruceToggleBooleanOption build() {
			return new SpruceToggleBooleanOption(
					this.key, this.getter, this.setter,
					this.tooltip, this.showMessage
			);
		}
	}
}
