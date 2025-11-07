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
import dev.lambdaurora.spruceui.SpruceTexts;
import dev.lambdaurora.spruceui.tooltip.TooltipData;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.SpruceWidget;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

/**
 * Represents a boolean option.
 * <p>
 * Works the same as the vanilla one but can provide a tooltip.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 1.0.0
 */
public class SpruceBooleanOption extends SpruceOption {
	private final Supplier<Boolean> getter;
	private final Consumer<Boolean> setter;
	private final boolean colored;

	public SpruceBooleanOption(
			String key, Supplier<Boolean> getter, Consumer<Boolean> setter,
			TooltipData tooltip
	) {
		this(key, getter, setter, tooltip, false);
	}

	public SpruceBooleanOption(
			String key, Supplier<Boolean> getter, Consumer<Boolean> setter,
			TooltipData tooltip, boolean colored
	) {
		super(key);
		this.getter = getter;
		this.setter = setter;
		this.colored = colored;
		this.setTooltip(tooltip);
	}

	public void set(String value) {
		this.set("true".equals(value));
	}

	public void set() {
		this.set(!this.get());
	}

	private void set(boolean value) {
		this.setter.accept(value);
	}

	/**
	 * Gets the current value.
	 *
	 * @return the current value
	 */
	public boolean get() {
		return this.getter.get();
	}

	/**
	 * Returns whether the option value is colored or not.
	 *
	 * @return {@code true} if the option value is colored, else {@code false}
	 */
	public boolean isColored() {
		return this.colored;
	}

	@Override
	public SpruceWidget createWidget(Position position, int width) {
		var button = new SpruceButtonWidget(position, width, 20, this.getDisplayText(), btn -> {
			this.set();
			btn.setMessage(this.getDisplayText());
		});
		this.getTooltip().ifPresent(button::setTooltip);
		return button;
	}

	/**
	 * Gets the display string.
	 *
	 * @return the display string
	 */
	public Component getDisplayText() {
		boolean value = this.get();
		var toggleText = SpruceTexts.getToggleText(value);
		if (this.colored)
			toggleText = toggleText.copy().setStyle(
					toggleText.getStyle().withColor(value ? ChatFormatting.GREEN : ChatFormatting.RED)
			);
		return this.getDisplayText(toggleText);
	}

	protected static abstract class BaseBuilder
			<B extends BaseBuilder<B, T>, T extends SpruceBooleanOption>
			extends SpruceOption.Builder<B, T> {
		protected final Supplier<Boolean> getter;
		protected final Consumer<Boolean> setter;
		protected boolean colored;

		public BaseBuilder(String key, Supplier<Boolean> getter, Consumer<Boolean> setter) {
			super(key);
			this.getter = getter;
			this.setter = setter;
		}

		public B colored() {
			return this.colored(true);
		}

		public B colored(boolean colored) {
			this.colored = colored;
			return this.self();
		}
	}

	public static class Builder extends BaseBuilder<Builder, SpruceBooleanOption> {
		public Builder(String key, Supplier<Boolean> getter, Consumer<Boolean> setter) {
			super(key, getter, setter);
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		public SpruceBooleanOption build() {
			return new SpruceBooleanOption(
					this.key, this.getter, this.setter,
					this.tooltip, this.colored
			);
		}
	}
}
