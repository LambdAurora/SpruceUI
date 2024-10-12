/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.test;

import dev.lambdaurora.spruceui.util.Nameable;
import net.minecraft.network.chat.Text;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a dummy enum.
 *
 * @author LambdAurora
 */
public enum TestEnum implements Nameable {
	FIRST,
	SECOND,
	THIRD,
	ANOTHER_VALUE;

	private final Text text;

	TestEnum() {
		this.text = Text.literal(this.getName());
	}

	/**
	 * Returns the next enum value available.
	 *
	 * @return The next available enum value.
	 */
	public @NotNull TestEnum next() {
		var v = values();
		if (v.length == this.ordinal() + 1)
			return v[0];
		return v[this.ordinal() + 1];
	}

	/**
	 * Gets the text of this enum value.
	 *
	 * @return The text of this enum value.
	 */
	public @NotNull Text getText() {
		return this.text;
	}

	@Override
	public @NotNull String getName() {
		return this.name().toLowerCase();
	}
}
