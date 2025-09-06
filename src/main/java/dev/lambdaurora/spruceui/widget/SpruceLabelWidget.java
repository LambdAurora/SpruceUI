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
import dev.lambdaurora.spruceui.SpruceTextAlignment;
import dev.lambdaurora.spruceui.border.Border;
import dev.lambdaurora.spruceui.border.EmptyBorder;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import dev.lambdaurora.spruceui.tooltip.Tooltip;
import dev.lambdaurora.spruceui.tooltip.TooltipData;
import dev.lambdaurora.spruceui.tooltip.Tooltipable;
import dev.lambdaurora.spruceui.util.ColorUtil;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Text;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents a label widget.
 *
 * @author LambdAurora
 * @version 9.0.0
 * @since 1.0.0
 */
public class SpruceLabelWidget extends AbstractSpruceWidget implements Tooltipable, WithBorder {
	public static final Consumer<SpruceLabelWidget> DEFAULT_ACTION = label -> {
	};

	private Text text;
	private List<FormattedCharSequence> lines;
	private SpruceTextAlignment alignment;
	private int color = ColorUtil.WHITE;
	private int maxWidth;
	private final Consumer<SpruceLabelWidget> action;
	private int baseX;
	//private final int                         maxHeight;

	private TooltipData tooltip = TooltipData.EMPTY;
	private Border border = EmptyBorder.EMPTY_BORDER;

	private int tooltipTicks;
	private long lastTick;

	public SpruceLabelWidget(
			Position position, Text text, int maxWidth, Consumer<SpruceLabelWidget> action,
			SpruceTextAlignment alignment
	) {
		super(position);
		this.alignment = alignment;
		this.maxWidth = maxWidth;
		this.baseX = position.getRelativeX();
		this.action = action;
		this.setText(text);
	}

	public SpruceLabelWidget(
			Position position, Text text, int maxWidth, Consumer<SpruceLabelWidget> action
	) {
		this(position, text, maxWidth, action, SpruceTextAlignment.LEFT);
	}

	public SpruceLabelWidget(
			Position position, Text text, int maxWidth,
			SpruceTextAlignment alignment
	) {
		this(position, text, maxWidth, DEFAULT_ACTION, alignment);
	}

	public SpruceLabelWidget(Position position, Text text, int maxWidth) {
		this(position, text, maxWidth, DEFAULT_ACTION);
	}

	private int getInnerX() {
		return this.getPosition().getAnchor().getX() + this.baseX;
	}

	/**
	 * Gets the text of the label.
	 *
	 * @return the text
	 */
	public Text getText() {
		return this.text;
	}

	/**
	 * Sets the text of this label.
	 *
	 * @param text the text to set
	 */
	public void setText(Text text) {
		this.text = text;
		this.lines = this.client.font.wrapLines(text, this.maxWidth);

		int width = this.lines.stream().mapToInt(this.client.font::width).max().orElse(this.maxWidth);
		if (width > this.maxWidth) {
			width = this.maxWidth;
		}

		if (this.isCentered()) {
			this.position.setRelativeX(this.baseX + this.maxWidth / 2 - width / 2);
		} else {
			this.position.setRelativeX(this.baseX);
		}
		this.width = width;
		this.height = this.lines.size() * this.client.font.lineHeight + 2;
	}

	/**
	 * Returns whether this label is centered or not.
	 *
	 * @return {@code true} if this label is centered, else {@code false}
	 */
	public boolean isCentered() {
		return this.alignment == SpruceTextAlignment.CENTER;
	}

	/**
	 * {@return the text alignment of this label}
	 */
	public SpruceTextAlignment getAlignment() {
		return this.alignment;
	}

	/**
	 * Sets this label's text alignment.
	 *
	 * @param alignment the text alignment of this label
	 */
	public void setAlignment(SpruceTextAlignment alignment) {
		this.alignment = alignment;
	}

	/**
	 * {@return the ARGB text color of this label}
	 */
	public int getColor() {
		return this.color;
	}

	/**
	 * Sets the text color of this label
	 *
	 * @param color the ARGB color
	 */
	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public @NotNull TooltipData getTooltip() {
		return this.tooltip;
	}

	@Override
	public void setTooltip(@NotNull TooltipData tooltip) {
		Objects.requireNonNull(
				tooltip,
				"Tooltip cannot be null, the absence of a tooltip is represented by TooltipData.EMPTY."
		);
		this.tooltip = tooltip;
	}

	@Override
	public Border getBorder() {
		return this.border;
	}

	@Override
	public void setBorder(Border border) {
		this.border = border;
	}

	/**
	 * Fires the press event on this label widget.
	 */
	public void onPress() {
		this.action.accept(this);
	}

	/* Navigation */

	@Override
	public boolean requiresCursor() {
		return this.action == DEFAULT_ACTION;
	}

	/* Input */

	@Override
	protected boolean onMouseClick(@NotNull MouseButtonEvent event, boolean doubleClick) {
		if (event.button() == GLFW.GLFW_MOUSE_BUTTON_1) {
			if (this.hovered) {
				this.onPress();
				return true;
			}
		}
		return false;
	}

	/* Rendering */

	@Override
	protected void renderWidget(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
		int y = this.getY() + 2;
		for (var it = this.lines.iterator(); it.hasNext(); y += 9) {
			var line = it.next();
			int x = switch (this.alignment) {
				case LEFT -> this.getInnerX();
				case CENTER -> (this.getInnerX() + this.maxWidth / 2) - this.client.font.width(line) / 2;
				case RIGHT -> this.getInnerX() + this.maxWidth - this.client.font.width(line);
			};
			graphics.drawShadowedText(this.client.font, line, x, y, this.color);
		}

		this.getBorder().render(graphics, this, mouseX, mouseY, delta);

		if (!this.dragging) {
			Tooltip.queueFor(this, mouseX, mouseY, this.tooltipTicks,
					i -> this.tooltipTicks = i, this.lastTick, i -> this.lastTick = i);
		}
	}

	/* Narration */

	@Override
	protected Text getNarrationMessage() {
		return this.getText();
	}
}
