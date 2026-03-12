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
import dev.lambdaurora.spruceui.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;
import java.util.OptionalInt;

/**
 * Represents a thumbnail tooltip component.
 *
 * @param thumbnailComponent the tooltip component that is displayed as a thumbnail
 * @param text the text to surround the thumbnail
 * @param allowExtraTextBelow {@code true} if an extra text can be placed below, or {@code false} if all text should be on the side
 * @param maxWidth the maximum width of this tooltip component
 * @author LambdAurora
 * @version 8.0.0
 * @since 8.0.0
 */
public record ClientThumbnailTooltipComponent(
		SpruceClientTooltipComponent thumbnailComponent,
		FormattedText text,
		boolean allowExtraTextBelow,
		OptionalInt maxWidth
) implements SpruceClientTooltipComponent {
	public ClientThumbnailTooltipComponent(
			SpruceClientTooltipComponent thumbnailComponent,
			FormattedText text,
			boolean allowExtraTextBelow
	) {
		this(thumbnailComponent, text, allowExtraTextBelow, OptionalInt.empty());
	}

	public ClientThumbnailTooltipComponent(
			SpruceClientTooltipComponent thumbnailComponent, FormattedText text
	) {
		this(thumbnailComponent, text, false);
	}

	@Override
	public int getWidth(Font font) {
		return this.getLayout(font).width();
	}

	@Override
	public int getHeight(Font font) {
		return this.getLayout(font).height();
	}

	@Override
	public void extractText(SpruceGuiGraphics graphics, Font font, int x, int y) {
		var layout = this.getLayout(font);

		this.thumbnailComponent.extractText(graphics, font, x, y + layout.thumbnailYOffset());

		int sideY = y;
		for (var line : layout.sideLines) {
			graphics.text(font, line, x + layout.thumbnailWidth + 2, sideY, ColorUtil.WHITE, true);
			sideY += font.lineHeight + 1;
		}

		int belowY = y + layout.belowLinesY;
		for (var line : layout.belowLines) {
			graphics.text(font, line, x, belowY, ColorUtil.WHITE, true);
			belowY += font.lineHeight + 1;
		}
	}

	@Override
	public void extractImage(Font font, int x, int y, int width, int height, SpruceGuiGraphics graphics) {
		var layout = this.getLayout(font);

		this.thumbnailComponent.extractImage(
				font,
				x, y + layout.thumbnailYOffset(),
				layout.thumbnailWidth, layout.thumbnailHeight,
				graphics
		);
	}

	@Override
	public SpruceClientTooltipComponent withMaxWidth(int maxWidth) {
		return new ClientThumbnailTooltipComponent(
				this.thumbnailComponent,
				this.text,
				this.allowExtraTextBelow,
				OptionalInt.of(maxWidth)
		);
	}

	private Layout getLayout(Font font) {
		var window = Minecraft.getInstance().getWindow();
		int thumbnailWidth = this.thumbnailComponent.getWidth(font);
		int thumbnailHeight = this.thumbnailComponent.getHeight(font);

		int absoluteMaxWidth = (int) (window.getGuiScaledWidth() * (2 / 3.f));
		int maxWidth = this.maxWidth.orElse(absoluteMaxWidth);

		int remainingWidth = maxWidth - (thumbnailWidth + 4);

		if (remainingWidth < 20 && (absoluteMaxWidth - (thumbnailWidth + 4)) >= absoluteMaxWidth / 3) {
			remainingWidth = absoluteMaxWidth - (thumbnailWidth + 4);
		} else if (remainingWidth < absoluteMaxWidth / 3) {
			// Give up on placing text aside.
			var lines = font.split(this.text, maxWidth);
			return new Layout(
					thumbnailWidth, thumbnailHeight,
					font.lineHeight, maxWidth,
					List.of(),
					thumbnailHeight + 2, lines
			);
		}

		var sideLines = font.splitIgnoringLanguage(this.text, remainingWidth);
		var belowLines = List.<FormattedText>of();

		int sideTextHeight = 0;
		for (int i = 0; i < sideLines.size(); i++) {
			sideTextHeight += font.lineHeight + 1;

			if (thumbnailHeight < sideTextHeight && this.allowExtraTextBelow) {
				if (i + 1 < sideLines.size()) {
					belowLines = sideLines.subList(i + 1, sideLines.size());
				}
				sideLines = sideLines.subList(0, i + 1);
			}
		}

		return new Layout(
				thumbnailWidth, thumbnailHeight,
				font.lineHeight, maxWidth,
				Language.getInstance().getVisualOrder(sideLines),
				sideTextHeight, Language.getInstance().getVisualOrder(belowLines)
		);
	}

	private record Layout(
			int thumbnailWidth, int thumbnailHeight,
			int lineHeight, int width,
			List<FormattedCharSequence> sideLines,
			int belowLinesY, List<FormattedCharSequence> belowLines
	) {
		public int height() {
			int height = 1 + this.belowLines.size() * (this.lineHeight + 1);

			if (this.sideLines.isEmpty()) {
				height += this.thumbnailHeight;
			} else {
				height += this.sideLines.size() * (this.lineHeight + 1);
			}

			return height;
		}

		public int thumbnailYOffset() {
			if (this.sideLines.isEmpty()) {
				return 0;
			} else {
				return this.belowLinesY / 2 - thumbnailHeight / 2;
			}
		}
	}
}
