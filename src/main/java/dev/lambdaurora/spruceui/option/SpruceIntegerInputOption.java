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
import dev.lambdaurora.spruceui.tooltip.Tooltip;
import dev.lambdaurora.spruceui.tooltip.TooltipData;
import dev.lambdaurora.spruceui.util.SpruceUtil;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import dev.lambdaurora.spruceui.widget.text.SpruceNamedTextFieldWidget;
import dev.lambdaurora.spruceui.widget.text.SpruceTextFieldWidget;
import net.minecraft.TextFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Text;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents an integer input option.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 2.1.0
 */
public class SpruceIntegerInputOption extends SpruceOption {
	private final Supplier<Integer> getter;
	private final Consumer<Integer> setter;

	public SpruceIntegerInputOption(
			String key, Supplier<Integer> getter, Consumer<Integer> setter,
			TooltipData tooltip
	) {
		super(key);
		this.getter = getter;
		this.setter = setter;
		this.setTooltip(tooltip);
	}

	@Override
	public SpruceWidget createWidget(Position position, int width) {
		var textField = new SpruceTextFieldWidget(position, width, 20, this.getPrefix());
		textField.setText(String.valueOf(this.get()));
		textField.setTextPredicate(SpruceTextFieldWidget.INTEGER_INPUT_PREDICATE);
		textField.setRenderTextProvider((displayedText, offset) -> {
			try {
				Integer.parseInt(textField.getText());
				return FormattedCharSequence.forward(displayedText, Style.EMPTY);
			} catch (NumberFormatException e) {
				return FormattedCharSequence.forward(displayedText, Style.EMPTY.withColor(TextFormatting.RED));
			}
		});
		textField.setChangedListener(input -> {
			int value = SpruceUtil.parseIntFromString(input);
			this.set(value);
		});
		this.getTooltip().ifPresent(textField::setTooltip);
		return new SpruceNamedTextFieldWidget(textField);
	}

	public void set(int value) {
		this.setter.accept(value);
	}

	/**
	 * Gets the current value.
	 *
	 * @return the current value
	 */
	public int get() {
		return this.getter.get();
	}

	public static class Builder extends SpruceOption.Builder<Builder, SpruceIntegerInputOption> {
		private final Supplier<Integer> getter;
		private final Consumer<Integer> setter;

		public Builder(String key, Supplier<Integer> getter, Consumer<Integer> setter) {
			super(key);
			this.getter = getter;
			this.setter = setter;
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		public SpruceIntegerInputOption build() {
			return new SpruceIntegerInputOption(this.key, this.getter, this.setter, this.tooltip);
		}
	}
}
