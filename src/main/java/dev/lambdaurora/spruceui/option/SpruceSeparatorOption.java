/*
 * Copyright © 2020-2022 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.option;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.widget.SpruceSeparatorWidget;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a separator option.
 *
 * @author LambdAurora
 * @version 3.0.0
 * @since 1.0.1
 */
public class SpruceSeparatorOption extends SpruceOption {
	private final boolean showTitle;

	public SpruceSeparatorOption(String key, boolean showTitle, @Nullable Text tooltip) {
		super(key);
		this.showTitle = showTitle;
		this.setTooltip(tooltip);
	}

	@Override
	public SpruceWidget createWidget(Position position, int width) {
		var separator = new SpruceSeparatorWidget(position, width, this.showTitle ? new TranslatableText(this.key) : null);
		this.getOptionTooltip().ifPresent(separator::setTooltip);
		return separator;
	}
}
