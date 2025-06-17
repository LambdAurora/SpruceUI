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
import dev.lambdaurora.spruceui.tooltip.Tooltipable;
import dev.lambdaurora.spruceui.util.Nameable;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents an option.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 1.0.3
 */
public abstract class SpruceOption implements Nameable, Tooltipable {
	public final String key;
	private TooltipData tooltip = TooltipData.EMPTY;

	public SpruceOption(String key) {
		Objects.requireNonNull(key, "Cannot create an option without a key.");
		this.key = key;
	}

	@Override
	public String getName() {
		return I18n.get(this.key);
	}

	@Override
	public @NotNull TooltipData getTooltip() {
		return this.tooltip;
	}

	@Override
	public void setTooltip(@NotNull TooltipData tooltip) {
		Objects.requireNonNull(
				tooltip,
				"Tooltip cannot be null, the absence of a tooltip is represented by TooltipData.EMPTY."
		);
		this.tooltip = tooltip;
	}

	/**
	 * Returns the display prefix text.
	 *
	 * @return the display prefix
	 */
	public Text getPrefix() {
		return Text.translatable(this.key);
	}

	/**
	 * Returns the display text.
	 *
	 * @param value the value
	 * @return the display text
	 */
	public Text getDisplayText(Text value) {
		return Text.translatable("spruceui.options.generic", this.getPrefix(), value);
	}

	public abstract SpruceWidget createWidget(Position position, int width);

	public static SpruceBooleanOption.Builder booleanBuilder(
			String key, Supplier<Boolean> getter, Consumer<Boolean> setter
	) {
		return new SpruceBooleanOption.Builder(key, getter, setter);
	}

	public static SpruceCheckboxBooleanOption.Builder checkboxBuilder(
			String key, Supplier<Boolean> getter, Consumer<Boolean> setter
	) {
		return new SpruceCheckboxBooleanOption.Builder(key, getter, setter);
	}

	public static SpruceCyclingOption.Builder cyclingBuilder(
			String key, Consumer<Integer> setter, Function<SpruceCyclingOption, Text> messageProvider
	) {
		return new SpruceCyclingOption.Builder(key, setter, messageProvider);
	}

	public static SpruceDoubleOption.Builder doubleBuilder(
			String key, double min, double max, float step,
			Supplier<Double> getter, Consumer<Double> setter, Function<SpruceDoubleOption, Text> displayStringGetter
	) {
		return new SpruceDoubleOption.Builder(
				key, min, max, step,
				getter, setter, displayStringGetter
		);
	}

	public static SpruceDoubleInputOption.Builder doubleInputBuilder(
			String key, Supplier<Double> getter, Consumer<Double> setter
	) {
		return new SpruceDoubleInputOption.Builder(key, getter, setter);
	}

	public static SpruceFloatInputOption.Builder floatInputBuilder(
			String key, Supplier<Float> getter, Consumer<Float> setter
	) {
		return new SpruceFloatInputOption.Builder(key, getter, setter);
	}

	public static SpruceIntegerInputOption.Builder intInputBuilder(
			String key, Supplier<Integer> getter, Consumer<Integer> setter
	) {
		return new SpruceIntegerInputOption.Builder(key, getter, setter);
	}

	public static SpruceToggleBooleanOption.Builder toggleBuilder(
			String key, Supplier<Boolean> getter, Consumer<Boolean> setter
	) {
		return new SpruceToggleBooleanOption.Builder(key, getter, setter);
	}

	public static abstract class Builder<B extends Builder<B, T>, T extends SpruceOption> {
		protected final String key;
		protected TooltipData tooltip = TooltipData.EMPTY;

		public Builder(String key) {
			this.key = key;
		}

		/**
		 * Sets the tooltip of this option.
		 *
		 * @param tooltip the tooltip
		 * @return this builder
		 */
		public B tooltip(@NotNull Text tooltip) {
			return this.tooltip(new TooltipData(new TooltipData.TextEntry(tooltip)));
		}

		/**
		 * Sets the tooltip of this option.
		 *
		 * @param tooltip the tooltip
		 * @return this builder
		 */
		public B tooltip(@NotNull TooltipData tooltip) {
			this.tooltip = tooltip;
			return this.self();
		}

		protected abstract B self();

		/**
		 * Builds the option.
		 *
		 * @return the built option
		 */
		public abstract T build();
	}
}
