/*
 * Copyright © 2024 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget.text;

import dev.lambdaurora.spruceui.Position;
import net.minecraft.network.chat.Text;
import net.minecraft.util.FormattedCharSequence;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Represents a text field widget builder.
 *
 * @author LambdAurora
 * @version 6.1.0
 * @since 6.1.0
 */
public class SpruceTextFieldWidgetBuilder {
	private final Position position;
	private final int width;
	private final int height;
	private Text title;
	private Text placeholder;
	private Consumer<String> onChange;
	private Predicate<String> textPredicate;
	private BiFunction<String, Integer, FormattedCharSequence> renderTextProvider;

	public SpruceTextFieldWidgetBuilder(Position position, int width, int height) {
		this.position = position;
		this.width = width;
		this.height = height;
	}

	public SpruceTextFieldWidgetBuilder title(Text title) {
		this.title = title;
		return this;
	}

	public SpruceTextFieldWidgetBuilder placeholder() {
		this.placeholder = this.title;
		return this;
	}

	public SpruceTextFieldWidgetBuilder placeholder(Text placeholder) {
		this.placeholder = placeholder;
		return this;
	}

	public SpruceTextFieldWidgetBuilder onChange(Consumer<String> onChange) {
		this.onChange = onChange;
		return this;
	}

	public SpruceTextFieldWidgetBuilder textPredicate(Predicate<String> textPredicate) {
		this.textPredicate = textPredicate;
		return this;
	}

	public SpruceTextFieldWidgetBuilder renderTextProvider(BiFunction<String, Integer, FormattedCharSequence> renderTextProvider) {
		this.renderTextProvider = renderTextProvider;
		return this;
	}

	public SpruceTextFieldWidget build() {
		Objects.requireNonNull(this.title, "Text fields require a title.");
		var widget = new SpruceTextFieldWidget(this.position, this.width, this.height, this.title, this.placeholder);
		if (this.onChange != null) widget.setChangedListener(this.onChange);
		if (this.textPredicate != null) widget.setTextPredicate(this.textPredicate);
		if (this.renderTextProvider != null) widget.setRenderTextProvider(this.renderTextProvider);
		return widget;
	}

	public SpruceNamedTextFieldWidget buildNamed() {
		return new SpruceNamedTextFieldWidget(this.build());
	}
}
