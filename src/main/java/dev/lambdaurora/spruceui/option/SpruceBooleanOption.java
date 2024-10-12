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
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.TextFormatting;
import net.minecraft.network.chat.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents a boolean option.
 * <p>
 * Works the same as the vanilla one but can provide a tooltip.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 1.0.0
 */
public class SpruceBooleanOption extends SpruceOption {
	private final Supplier<Boolean> getter;
	private final Consumer<Boolean> setter;
	private final boolean colored;

	public SpruceBooleanOption(String key, Supplier<Boolean> getter, Consumer<Boolean> setter, @Nullable Text tooltip) {
		this(key, getter, setter, tooltip, false);
	}

	public SpruceBooleanOption(String key, Supplier<Boolean> getter, Consumer<Boolean> setter, @Nullable Text tooltip, boolean colored) {
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
		this.getOptionTooltip().ifPresent(button::setTooltip);
		return button;
	}

	/**
	 * Gets the display string.
	 *
	 * @return the display string
	 */
	public Text getDisplayText() {
		boolean value = this.get();
		var toggleText = SpruceTexts.getToggleText(value);
		if (this.colored)
			toggleText = toggleText.copy().setStyle(toggleText.getStyle().withColor(value ? TextFormatting.GREEN : TextFormatting.RED));
		return this.getDisplayText(toggleText);
	}
}
