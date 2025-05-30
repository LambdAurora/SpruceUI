/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.tooltip.Tooltip;
import dev.lambdaurora.spruceui.tooltip.TooltipData;
import dev.lambdaurora.spruceui.tooltip.Tooltipable;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import dev.lambdaurora.spruceui.util.ColorUtil;
import net.minecraft.network.chat.Text;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Represents a separator element.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 1.0.1
 */
public class SpruceSeparatorWidget extends AbstractSpruceWidget implements Tooltipable {
	private Text title;
	private List<FormattedCharSequence> titleToRender = List.of();
	private TooltipData tooltip = TooltipData.EMPTY;
	private int tooltipTicks;
	private long lastTick;

	public SpruceSeparatorWidget(Position position, int width, @Nullable Text title) {
		super(position);
		this.width = width;
		this.setTitle(title);
	}

	@Deprecated
	public SpruceSeparatorWidget(@Nullable Text title, int x, int y, int width) {
		this(Position.of(x, y), width, title);
	}

	/**
	 * Gets the title of this separator widget.
	 *
	 * @return the title
	 */
	public Optional<Text> getTitle() {
		return Optional.ofNullable(this.title);
	}

	protected int getTitleWidth() {
		if (this.titleToRender.isEmpty()) {
			return 0;
		}

		int max = this.getWidth() - 8;
		int width = 0;

		for (var line : this.titleToRender) {
			width = Math.max(width, this.client.font.width(line));
		}

		return Math.min(width, max);
	}

	/**
	 * Sets the title of this separator widget.
	 *
	 * @param title the title
	 */
	public void setTitle(@Nullable Text title) {
		this.title = title;

		if (this.title != null) {
			this.titleToRender = this.client.font.wrapLines(this.title, this.getWidth() - 8);
		} else {
			this.titleToRender = List.of();
		}

		this.height = this.client.font.lineHeight;

		for (int i = 1; i < this.titleToRender.size(); i++) {
			this.height += 2 + this.client.font.lineHeight;
		}
	}

	@Override
	public @NotNull TooltipData getTooltip() {
		return this.tooltip;
	}

	@Override
	public void setTooltip(@NotNull TooltipData tooltip) {
		this.tooltip = tooltip;
	}

	/* Navigation */

	@Override
	public boolean requiresCursor() {
		return this.tooltip.isEmpty();
	}

	/* Rendering */

	@Override
	protected void renderWidget(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
		int lineY = this.getY() + this.getHeight() / 2 - 1;

		if (this.title != null) {
			int titleWidth = this.getTitleWidth();
			int titleX = this.getX() + (this.getWidth() / 2 - titleWidth / 2);
			graphics.fill(this.getX(), lineY, titleX - 5, lineY + 2, ColorUtil.TEXT_COLOR);
			graphics.fill(titleX + titleWidth + 5, lineY, this.getX() + this.getWidth(), lineY + 2, ColorUtil.TEXT_COLOR);

			int y = this.getY();
			for (var line : this.titleToRender) {
				int lineX = this.getX() + (this.getWidth() / 2 - this.client.font.width(line) / 2);
				graphics.drawShadowedText(this.client.font, line, lineX, y, ColorUtil.WHITE);
				y += 2 + this.client.font.lineHeight;
			}
		} else {
			graphics.fill(this.getX(), lineY, this.getX() + this.getWidth(), lineY + 2, ColorUtil.TEXT_COLOR);
		}

		Tooltip.queueFor(this, mouseX, mouseY, this.tooltipTicks, i -> this.tooltipTicks = i, this.lastTick, i -> this.lastTick = i);
	}

	/* Narration */

	@Override
	protected Text getNarrationMessage() {
		return this.getTitle().map(Text::getString)
				.filter(title -> !title.isEmpty())
				.map(title -> Text.translatable("spruceui.narrator.separator", title))
				.orElse(null);
	}
}
