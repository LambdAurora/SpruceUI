/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.test.gui.tooltip;

import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import dev.lambdaurora.spruceui.tooltip.components.SpruceClientTooltipComponent;
import dev.lambdaurora.spruceui.widget.SpruceCheckboxWidget;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderPipelines;
import org.jetbrains.annotations.NotNull;

public class CheckboxTooltipComponent implements SpruceClientTooltipComponent {
	private static final long ANIMATION_DURATION = 5000L;

	@Override
	public int getWidth(@NotNull Font font) {
		return 28;
	}

	@Override
	public int getHeight(@NotNull Font font) {
		return 28;
	}

	@Override
	public void renderImage(@NotNull Font font, int x, int y, int width, int height, @NotNull SpruceGuiGraphics graphics) {
		graphics.drawSprite(
				RenderPipelines.GUI_TEXTURED, SpruceCheckboxWidget.BACKGROUND_TEXTURE.get(true, false),
				x + 4, y + 4, 20, 20
		);

		long currentTime = System.currentTimeMillis() % ANIMATION_DURATION;

		if (currentTime > ANIMATION_DURATION / 2) {
			graphics.drawSprite(
					RenderPipelines.GUI_TEXTURED, SpruceCheckboxWidget.CHECKED_TEXTURE,
					x + 4, y + 4, 20, 20,
					0xff00ff00
			);
		}
	}
}
