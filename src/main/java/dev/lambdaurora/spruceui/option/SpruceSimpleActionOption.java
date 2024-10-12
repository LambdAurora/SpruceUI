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
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.SpruceTexturedButtonWidget;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.network.chat.Text;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an option with a simple action.
 *
 * @author LambdAurora
 * @version 3.2.1
 * @since 1.0.1
 */
public final class SpruceSimpleActionOption extends SpruceOption {
	private final ButtonFactory buttonFactory;
	private final SpruceButtonWidget.PressAction action;

	public SpruceSimpleActionOption(String key, ButtonFactory buttonFactory, SpruceButtonWidget.PressAction action, @Nullable Text tooltip) {
		super(key);
		this.buttonFactory = buttonFactory;
		this.action = action;
		this.setTooltip(tooltip);
	}

	public SpruceSimpleActionOption(String key, ButtonFactory buttonFactory, SpruceButtonWidget.PressAction action) {
		this(key, buttonFactory, action, null);
	}

	@Override
	public SpruceWidget createWidget(Position position, int width) {
		var button = this.buttonFactory.build(position, width, Text.translatable(this.key), this.action);
		this.getOptionTooltip().ifPresent(button::setTooltip);
		return button;
	}

	public static SpruceSimpleActionOption of(String key, SpruceButtonWidget.PressAction action) {
		return new SpruceSimpleActionOption(key,
				(position, width, message, action1) -> new SpruceButtonWidget(position, width, 20, message, action1),
				action);
	}

	public static SpruceSimpleActionOption of(String key, SpruceButtonWidget.PressAction action, @Nullable Text tooltip) {
		return new SpruceSimpleActionOption(key,
				(position, width, message, action1) -> new SpruceButtonWidget(position, width, 20, message, action1),
				action, tooltip);
	}

	public static SpruceSimpleActionOption reset(SpruceButtonWidget.PressAction action) {
		return reset(action, null);
	}

	public static SpruceSimpleActionOption reset(SpruceButtonWidget.PressAction action, @Nullable Text tooltip) {
		return new SpruceSimpleActionOption("spruceui.reset",
				(position, width, message, action1) -> new SpruceButtonWidget(position, width, 20, message, action1),
				action, tooltip);
	}

	public static SpruceSimpleActionOption textured(
			String key, SpruceButtonWidget.PressAction action,
			int u, int v, int hoveredVOffset, Identifier texture
	) {
		return textured(key, action, u, v, hoveredVOffset, texture, null);
	}

	public static SpruceSimpleActionOption textured(
			String key, SpruceButtonWidget.PressAction action,
			int u, int v, int hoveredVOffset, Identifier texture, @Nullable Text tooltip
	) {
		return new SpruceSimpleActionOption(key,
				(position, width, message, action1) -> new SpruceTexturedButtonWidget(position, width, 20, message, action1, u, v, hoveredVOffset, texture),
				action, tooltip);
	}

	public static SpruceSimpleActionOption textured(
			String key, SpruceButtonWidget.PressAction action,
			int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight
	) {
		return textured(key, action, u, v, hoveredVOffset, texture, textureWidth, textureHeight, null);
	}

	public static SpruceSimpleActionOption textured(
			String key, SpruceButtonWidget.PressAction action,
			int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight, @Nullable Text tooltip
	) {
		return new SpruceSimpleActionOption(key,
				(position, width, message, action1) ->
						new SpruceTexturedButtonWidget(position, width, 20, message, action1, u, v, hoveredVOffset, texture, textureWidth, textureHeight),
				action, tooltip);
	}

	public static SpruceSimpleActionOption texturedWithMessage(
			String key, SpruceButtonWidget.PressAction action,
			int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight
	) {
		return texturedWithMessage(key, action, u, v, hoveredVOffset, texture, textureWidth, textureHeight, null);
	}

	public static SpruceSimpleActionOption texturedWithMessage(
			String key, SpruceButtonWidget.PressAction action,
			int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight, @Nullable Text tooltip
	) {
		return new SpruceSimpleActionOption(key,
				(position, width, message, action1) ->
						new SpruceTexturedButtonWidget(position, width, 20, message, true, action1, u, v, hoveredVOffset, texture, textureWidth, textureHeight),
				action, tooltip);
	}

	/**
	 * Represents the button factory.
	 *
	 * @version 3.0.0
	 * @since 2.0.0
	 */
	public interface ButtonFactory {
		SpruceButtonWidget build(Position position, int width, Text message, SpruceButtonWidget.PressAction action);
	}
}
