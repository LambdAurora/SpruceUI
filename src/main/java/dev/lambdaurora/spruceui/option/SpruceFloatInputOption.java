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
 * Represents a float input option.
 *
 * @author LambdAurora
 * @version 3.0.0
 * @since 2.1.0
 */
public class SpruceFloatInputOption extends SpruceOption {
	private final Supplier<Float> getter;
	private final Consumer<Float> setter;

	public SpruceFloatInputOption(String key, Supplier<Float> getter, Consumer<Float> setter, @Nullable Text tooltip) {
		super(key);
		this.getter = getter;
		this.setter = setter;
		this.setTooltip(tooltip);
	}

	@Override
	public SpruceWidget createWidget(Position position, int width) {
		var textField = new SpruceTextFieldWidget(position, width, 20, this.getPrefix());
		textField.setText(String.valueOf(this.get()));
		textField.setTextPredicate(SpruceTextFieldWidget.FLOAT_INPUT_PREDICATE);
		textField.setRenderTextProvider((displayedText, offset) -> {
			try {
				Float.parseFloat(textField.getText());
				return FormattedCharSequence.forward(displayedText, Style.EMPTY);
			} catch (NumberFormatException e) {
				return FormattedCharSequence.forward(displayedText, Style.EMPTY.withColor(TextFormatting.RED));
			}
		});
		textField.setChangedListener(input -> {
			float value;
			try {
				value = Float.parseFloat(input);
			} catch (NumberFormatException e) {
				value = 0;
			}
			this.set(value);
		});
		this.getOptionTooltip().ifPresent(textField::setTooltip);
		return new SpruceNamedTextFieldWidget(textField);
	}

	public void set(float value) {
		this.setter.accept(value);
	}

	/**
	 * Gets the current value.
	 *
	 * @return the current value
	 */
	public float get() {
		return this.getter.get();
	}
}
