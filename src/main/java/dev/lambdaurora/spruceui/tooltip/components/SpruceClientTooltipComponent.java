/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.tooltip.components;

import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a client tooltip component, a SpruceUI-extended variant of {@link ClientTooltipComponent}.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 8.0.0
 */
public interface SpruceClientTooltipComponent extends ClientTooltipComponent {
	default void renderText(@NotNull SpruceGuiGraphics graphics, @NotNull Font font, int x, int y) {
	}

	default void renderImage(
			@NotNull Font font, int x, int y, int width, int height, @NotNull SpruceGuiGraphics graphics
	) {
	}

	@Override
	default void renderText(@NotNull GuiGraphics graphics, @NotNull Font font, int x, int y) {
		this.renderText(SpruceGuiGraphics.of(graphics), font, x, y);
	}

	@Override
	default void renderImage(@NotNull Font font, int x, int y, int width, int height, @NotNull GuiGraphics graphics) {
		this.renderImage(font, x, y, width, height, SpruceGuiGraphics.of(graphics));
	}

	/**
	 * {@return this client tooltip component that is aware of the recommended maximum width}
	 *
	 * @param maxWidth the recommended maximum width
	 */
	default SpruceClientTooltipComponent withMaxWidth(int maxWidth) {
		return this;
	}

	/**
	 * Wraps a Vanilla {@link ClientTooltipComponent} into the SpruceUI variant.
	 *
	 * @param component the tooltip component to wrap
	 * @return the wrapped tooltip component
	 */
	static SpruceClientTooltipComponent sprucify(ClientTooltipComponent component) {
		if (component instanceof SpruceClientTooltipComponent spruced) {
			return spruced;
		} else {
			return new SpruceClientTooltipComponent() {
				@Override
				public int getWidth(@NotNull Font font) {
					return component.getWidth(font);
				}

				@Override
				public int getHeight(@NotNull Font font) {
					return component.getHeight(font);
				}

				@Override
				public boolean showTooltipWithItemInHand() {
					return component.showTooltipWithItemInHand();
				}

				@Override
				public void renderText(@NotNull SpruceGuiGraphics graphics, @NotNull Font font, int x, int y) {
					component.renderText(graphics.vanilla(), font, x, y);
				}

				@Override
				public void renderImage(
						@NotNull Font font, int x, int y, int width, int height, @NotNull SpruceGuiGraphics graphics
				) {
					component.renderImage(font, x, y, width, height, graphics.vanilla());
				}
			};
		}
	}
}
