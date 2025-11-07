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
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.SpruceWidget;

import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.network.chat.Component;

/**
 * Represents a cycling option.
 * <p>
 * Works the same as the vanilla one but can provide a tooltip.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 1.0.0
 */
public class SpruceCyclingOption extends SpruceOption {
	private final Consumer<Integer> setter;
	private final Function<SpruceCyclingOption, Component> messageProvider;

	public SpruceCyclingOption(
			String key, Consumer<Integer> setter, Function<SpruceCyclingOption, Component> messageProvider,
			TooltipData tooltip
	) {
		super(key);
		this.setter = setter;
		this.messageProvider = messageProvider;
		this.setTooltip(tooltip);
	}

	/**
	 * Cycles the option.
	 *
	 * @param amount The amount to cycle.
	 */
	public void cycle(int amount) {
		this.setter.accept(amount);
	}

	@Override
	public SpruceWidget createWidget(Position position, int width) {
		var button = new SpruceButtonWidget(position, width, 20, this.getMessage(), btn -> {
			this.cycle(1);
			btn.setMessage(this.getMessage());
		});
		this.getTooltip().ifPresent(button::setTooltip);
		return button;
	}

	/**
	 * Gets the option message.
	 *
	 * @return The option message.
	 */
	public Component getMessage() {
		return this.messageProvider.apply(this);
	}

	public static class Builder extends SpruceOption.Builder<Builder, SpruceCyclingOption> {
		private final Consumer<Integer> setter;
		private final Function<SpruceCyclingOption, Component> messageProvider;

		public Builder(
				String key, Consumer<Integer> setter, Function<SpruceCyclingOption, Component> messageProvider
		) {
			super(key);
			this.setter = setter;
			this.messageProvider = messageProvider;
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		public SpruceCyclingOption build() {
			return new SpruceCyclingOption(this.key, this.setter, this.messageProvider, this.tooltip);
		}
	}
}
