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
import net.minecraft.network.chat.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Represents a string option.
 *
 * @author LambdAurora
 * @version 3.0.0
 * @since 2.1.0
 */
public class SpruceStringOption extends SpruceOption {
	private final Supplier<String> getter;
	private final Consumer<String> setter;
	private final @Nullable Predicate<String> predicate;

	public SpruceStringOption(String key, Supplier<String> getter, Consumer<String> setter, @Nullable Predicate<String> predicate, @Nullable Text tooltip) {
		super(key);
		this.getter = getter;
		this.setter = setter;
		this.predicate = predicate;
		this.setTooltip(tooltip);
	}

	@Override
	public SpruceWidget createWidget(Position position, int width) {
		var textField = new SpruceTextFieldWidget(position, width, 20, this.getPrefix());
		textField.setText(this.get());
		if (this.predicate != null)
			textField.setTextPredicate(this.predicate);
		textField.setChangedListener(this::set);
		this.getOptionTooltip().ifPresent(textField::setTooltip);
		return new SpruceNamedTextFieldWidget(textField);
	}

	public void set(String value) {
		this.setter.accept(value);
	}

	/**
	 * Gets the current value.
	 *
	 * @return the current value
	 */
	public String get() {
		return this.getter.get();
	}
}
