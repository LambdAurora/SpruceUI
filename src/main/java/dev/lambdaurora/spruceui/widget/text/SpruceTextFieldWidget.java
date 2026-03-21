/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget.text;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.navigation.NavigationEvent;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import dev.lambdaurora.spruceui.tooltip.Tooltip;
import dev.lambdaurora.spruceui.tooltip.TooltipData;
import dev.lambdaurora.spruceui.tooltip.Tooltipable;
import dev.lambdaurora.spruceui.util.ColorUtil;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenAxis;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Represents a text field widget.
 *
 * @author LambdAurora
 * @version 9.0.0
 * @since 2.1.0
 */
public class SpruceTextFieldWidget extends AbstractSpruceTextInputWidget<SpruceTextFieldWidget.Cursor> implements Tooltipable {
	public static final Predicate<String> INTEGER_INPUT_PREDICATE = input -> {
		if (input.isEmpty() || input.equals("-")) return true;
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	};
	public static final Predicate<String> FLOAT_INPUT_PREDICATE = input -> {
		if (input.isEmpty() || input.equals("-") || input.equals(".")) return true;
		try {
			Float.parseFloat(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	};
	public static final Predicate<String> DOUBLE_INPUT_PREDICATE = input -> {
		if (input.isEmpty() || input.equals("-") || input.equals(".")) return true;
		try {
			Double.parseDouble(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	};

	private final dev.lambdaurora.spruceui.widget.text.SpruceTextFieldWidget.Cursor cursor = new dev.lambdaurora.spruceui.widget.text.SpruceTextFieldWidget.Cursor(true);
	private final dev.lambdaurora.spruceui.widget.text.SpruceTextFieldWidget.Selection selection = new dev.lambdaurora.spruceui.widget.text.SpruceTextFieldWidget.Selection();
	private String text = "";
	private TooltipData tooltip = TooltipData.EMPTY;

	private Consumer<String> changedListener;
	private Predicate<@Nullable String> textPredicate;
	private BiFunction<String, Integer, FormattedCharSequence> renderTextProvider;

	private int firstCharacterIndex = 0;
	private long editingTime;
	private int tooltipTicks;
	private long lastTick;

	public SpruceTextFieldWidget(Position position, int width, int height, Component title) {
		this(position, width, height, title, null);
	}

	public SpruceTextFieldWidget(Position position, int width, int height, Component title, @Nullable Component placeholder) {
		super(position, width, height, title, placeholder);
		this.cursor.toStart();
		this.sanitize();

		this.changedListener = (input) -> {
		};
		this.textPredicate = Objects::nonNull;
		this.renderTextProvider = (input, firstCharacterIndex) -> FormattedCharSequence.forward(input, Style.EMPTY);
	}

	public static SpruceTextFieldWidgetBuilder builder(Position position, int width, int height) {
		return new SpruceTextFieldWidgetBuilder(position, width, height);
	}

	@Override
	public String getText() {
		return this.text;
	}

	@Override
	public void setText(@Nullable String text) {
		if (this.textPredicate.test(text)) {
			this.text = text;

			this.setCursorToEnd();
			this.selection.cancel();
			this.sanitize();
			this.onChanged();
		}
	}

	@Override
	public TooltipData getTooltip() {
		return this.tooltip;
	}

	@Override
	public void setTooltip(TooltipData tooltip) {
		Objects.requireNonNull(
				tooltip,
				"Tooltip cannot be null, the absence of a tooltip is represented by TooltipData.EMPTY."
		);
		this.tooltip = tooltip;
	}

	public Consumer<String> getChangedListener() {
		return this.changedListener;
	}

	public void setChangedListener(Consumer<String> changedListener) {
		Objects.requireNonNull(changedListener, "changedListener cannot be null");
		this.changedListener = changedListener;
	}

	public Predicate<String> getTextPredicate() {
		return this.textPredicate;
	}

	public void setTextPredicate(Predicate<String> textPredicate) {
		this.textPredicate = textPredicate;
	}

	public BiFunction<String, Integer, FormattedCharSequence> getRenderTextProvider() {
		return this.renderTextProvider;
	}

	public void setRenderTextProvider(BiFunction<String, Integer, FormattedCharSequence> renderTextProvider) {
		this.renderTextProvider = renderTextProvider;
	}

	@Override
	protected Cursor cursor() {
		return this.cursor;
	}

	@Override
	protected AbstractSpruceTextInputWidget<Cursor>.Selection selection() {
		return this.selection;
	}

	@Override
	public void setCursorToStart() {
		this.cursor.toStart();
	}

	@Override
	public void setCursorToEnd() {
		this.cursor.toEnd();
	}

	@Override
	protected void sanitize() {
		this.cursor.sanitize();

		int textLength = this.text.length();
		if (this.firstCharacterIndex > textLength) {
			this.firstCharacterIndex = textLength;
		}

		int width = this.getInnerWidth();
		var string = this.client.font.plainSubstrByWidth(this.text.substring(this.firstCharacterIndex), width);
		int l = string.length() + this.firstCharacterIndex;
		if (this.cursor.column == this.firstCharacterIndex) {
			this.firstCharacterIndex -= this.client.font.plainSubstrByWidth(this.text, width, true).length();
		}

		if (this.cursor.column > l) {
			this.firstCharacterIndex += this.cursor.column - l;
		} else if (this.cursor.column <= this.firstCharacterIndex) {
			this.firstCharacterIndex -= this.firstCharacterIndex - this.cursor.column;
		}

		this.firstCharacterIndex = Mth.clamp(this.firstCharacterIndex, 0, textLength);
	}

	private void onChanged() {
		this.changedListener.accept(this.text);

		this.editingTime = Util.getMillis() + 5000L;
	}

	private boolean onSelectionUpdate(Runnable action, boolean hasShiftDown) {
		this.selection.tryStartSelection(hasShiftDown);
		action.run();
		this.selection.moveToCursor(hasShiftDown);
		this.sanitize();
		return true;
	}

	@Override
	protected void insertCharacter(String character) {
		if (this.getText().isEmpty()) {
			this.setText(character);
			return;
		} else {
			this.selection.erase();
		}

		if (character.equals("\n")) {
			return;
		}

		var text = this.getText();
		int cursorPosition = this.cursor.getPosition();

		String newText;
		if (cursorPosition >= text.length()) {
			newText = text + character;
		} else {
			newText = text.substring(0, cursorPosition) + character + text.substring(cursorPosition);
		}

		if (this.textPredicate.test(newText)) {
			this.text = newText;
			this.onChanged();
			this.cursor.moveRight();
		}
		this.sanitize();
	}

	private void eraseCharacter() {
		if (this.selection.erase()) {
			this.sanitize();
			this.onChanged();
			return;
		}

		if (this.cursor.column == 0)
			return;

		var text = this.getText();
		int cursorPosition = this.cursor.getPosition();
		var newText = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);
		if (this.textPredicate.test(newText)) {
			this.text = newText;
			this.onChanged();
			this.cursor.moveLeft();
		}
		this.sanitize();
	}

	private void removeCharacterForward() {
		if (this.selection.erase()) {
			this.sanitize();
			this.onChanged();
			return;
		}

		if (this.getText().isEmpty()) {
			this.sanitize();
			return;
		}

		if (this.cursor.column >= this.getText().length())
			return;

		var text = this.getText();
		int cursorPosition = this.cursor.getPosition();

		var newText = text.substring(0, cursorPosition) + text.substring(cursorPosition + 1);
		if (this.textPredicate.test(newText)) {
			this.text = newText;
			this.onChanged();
		}
		this.sanitize();
	}

	/**
	 * Writes text where the cursor is.
	 *
	 * @param text the text to write
	 */
	public void write(String text) {
		if (text.isEmpty())
			return;

		if (this.getText().isEmpty()) {
			this.setText(text);
			this.setCursorToEnd();
			return;
		}
		this.selection.erase();

		var oldText = this.getText();
		int position = this.cursor.getPosition();

		String newText;
		if (position >= oldText.length()) {
			newText = oldText + text;
		} else {
			newText = oldText.substring(0, position) + text + oldText.substring(position);
		}

		if (this.textPredicate.test(newText)) {
			this.text = newText;
			this.onChanged();
			this.cursor.move(text.length());
		}
		this.sanitize();
	}

	/* Navigation */

	@Override
	public boolean onNavigation(NavigationEvent event) {
		if (this.requiresCursor()) return false;
		if (!event.tab() && event.direction().getAxis() == ScreenAxis.HORIZONTAL) {
			this.setFocused(true);
			boolean result = switch (event.direction()) {
				case RIGHT -> this.onSelectionUpdate(this.cursor::moveRight, event.hasShiftDown());
				case LEFT -> this.onSelectionUpdate(this.cursor::moveLeft, event.hasShiftDown());
				default -> false;
			};
			if (result)
				return true;
		}
		return super.onNavigation(event);
	}

	/* Input */

	@Override
	protected boolean onKeyPress(KeyEvent event) {
		if (!this.isEditorActive())
			return false;

		if (event.isSelectAll()) {
			this.selection.selectAll();
			this.sanitize();
			return true;
		} else if (event.isPaste()) {
			this.write(this.client.keyboardHandler.getClipboard());
			return true;
		} else if (event.isCopy() || event.isCut()) {
			var selected = this.selection.getSelectedText();
			if (!selected.isEmpty())
				this.client.keyboardHandler.setClipboard(selected);
			if (event.isCut()) {
				this.selection.erase();
				this.sanitize();
			}
			return true;
		}

		return switch (event.key()) {
			case GLFW.GLFW_KEY_RIGHT -> this.onSelectionUpdate(this.cursor::moveRight, event.hasShiftDown());
			case GLFW.GLFW_KEY_LEFT -> this.onSelectionUpdate(this.cursor::moveLeft, event.hasShiftDown());
			case GLFW.GLFW_KEY_END -> this.onSelectionUpdate(this.cursor::toEnd, event.hasShiftDown());
			case GLFW.GLFW_KEY_HOME -> this.onSelectionUpdate(this.cursor::toStart, event.hasShiftDown());
			case GLFW.GLFW_KEY_BACKSPACE -> {
				this.eraseCharacter();
				yield true;
			}
			case GLFW.GLFW_KEY_DELETE -> {
				this.removeCharacterForward();
				yield true;
			}
			case GLFW.GLFW_KEY_D -> {
				if (event.hasControlDown() && !this.text.isEmpty()) {
					this.setText("");
				}
				yield true;
			}
			default -> false;
		};
	}

	@Override
	protected boolean onMouseClick(MouseButtonEvent event, boolean doubleClick) {
		if (event.button() == 0) {
			int x = Mth.floor(event.x()) - this.getX() - 4;

			this.setFocused(true);

			this.onSelectionUpdate(() -> {
				var displayedText = this.client.font.plainSubstrByWidth(this.text.substring(this.firstCharacterIndex),
						this.getInnerWidth());
				this.cursor.lastColumn = this.cursor.column = this.firstCharacterIndex
						+ this.client.font.plainSubstrByWidth(displayedText, x).length();
			}, event.hasShiftDown());

			return true;
		}

		return false;
	}

	/* Rendering */

	@Override
	protected void extractWidgetRenderState(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
		super.extractWidgetRenderState(graphics, mouseX, mouseY, delta);

		this.drawText(graphics);
		this.drawCursor(graphics);

		if (!this.dragging && this.editingTime == 0) {
			Tooltip.queueFor(this, mouseX, mouseY, this.tooltipTicks,
					i -> this.tooltipTicks = i, this.lastTick, i -> this.lastTick = i);
		} else if (this.editingTime < Util.getMillis()) {
			this.editingTime = 0;
		}
	}

	/**
	 * Draws the text of the text area.
	 *
	 * @param graphics The GUI graphics instance to render with
	 */
	protected void drawText(SpruceGuiGraphics graphics) {
		int textColor = this.getTextColor();
		int x = this.getX() + 4;
		int y = this.getY() + this.getHeight() / 2 - 4;
		var placeholder = this.getPlaceholder();

		if (this.text.isEmpty() && placeholder != null) {
			graphics.shadowedText(this.client.font, placeholder, x, y, textColor);
			return;
		}

		var displayedText = this.client.font.plainSubstrByWidth(
				this.text.substring(this.firstCharacterIndex),
				this.getInnerWidth()
		);

		graphics.shadowedText(
				this.client.font, this.renderTextProvider.apply(displayedText, this.firstCharacterIndex),
				x, y, textColor
		);
		this.drawSelection(graphics, displayedText, y);
	}

	/**
	 * Draws the selection over the text.
	 *
	 * @param graphics the GUI graphics instance to render with
	 * @param line the current line
	 * @param lineY the line Y-coordinates
	 */
	protected void drawSelection(SpruceGuiGraphics graphics, String line, int lineY) {
		if (!this.isFocused() || !this.selection.active)
			return;

		int startIndex = Math.max(0, selection.getStart().column - this.firstCharacterIndex);
		int endIndex = Math.min(line.length(), selection.getEnd().column - this.firstCharacterIndex);

		if (startIndex >= line.length())
			return;

		int x = this.getX() + 4 + this.client.font.width(line.substring(0, startIndex));
		var selected = line.substring(startIndex, endIndex);

		int x2 = x + this.client.font.width(selected);
		int y2 = lineY + this.client.font.lineHeight;

		graphics.fill(RenderPipelines.GUI_TEXT_HIGHLIGHT, x, lineY, x2, y2, 0xff0000ff);
	}

	/**
	 * Draws the cursor.
	 *
	 * @param graphics The GUI graphics instance to render with
	 */
	protected void drawCursor(SpruceGuiGraphics graphics) {
		if (!this.isFocused())
			return;

		int cursorY = this.getY() + this.getHeight() / 2 - 4;

		if (this.text.isEmpty()) {
			graphics.shadowedText(this.client.font, Component.literal("_"),
					this.getX() + 4, cursorY, ColorUtil.TEXT_COLOR);
			return;
		}

		this.cursor.sanitize();

		var cursorLine = this.text.substring(this.firstCharacterIndex);
		int cursorX = this.getX() + 4 + this.client.font.width(
				cursorLine.substring(0, this.cursor.column - this.firstCharacterIndex)
		);

		if (this.cursor.column - this.firstCharacterIndex < cursorLine.length())
			graphics.fill(cursorX - 1, cursorY - 1, cursorX, cursorY + 9, ColorUtil.TEXT_COLOR);
		else
			graphics.shadowedText(this.client.font, "_", cursorX, cursorY, ColorUtil.TEXT_COLOR);
	}

	/* Narration */

	@Override
	public void updateNarration(NarrationElementOutput builder) {
		super.updateNarration(builder);
		this.tooltip.updateNarration(builder);
	}

	/**
	 * Represents a cursor.
	 *
	 * @version 9.0.0
	 * @since 2.1.0
	 */
	public class Cursor implements AbstractSpruceTextInputWidget.Cursor<Cursor> {
		boolean main;
		int column = 0;
		private int lastColumn = 0;

		public Cursor(boolean main) {
			this.main = main;
		}

		@Override
		public void toStart() {
			this.lastColumn = this.column = 0;
		}

		public void moveRight() {
			this.move(1);
		}

		public void moveLeft() {
			this.move(-1);
		}

		public void move(int amount) {
			this.column += amount;

			if (this.column < 0) {
				this.toStart();
			} else if (this.column > text.length()) {
				this.column = text.length();
			}

			this.lastColumn = this.column;

			if (amount < 0 && this.column <= SpruceTextFieldWidget.this.firstCharacterIndex) {
				SpruceTextFieldWidget.this.firstCharacterIndex =
						Mth.clamp(SpruceTextFieldWidget.this.firstCharacterIndex = this.column - 1, 0, text.length());
			}
		}

		@Override
		public void toEnd() {
			this.lastColumn = this.column = text.length();
		}

		/**
		 * Copies the column from another cursor.
		 *
		 * @param cursor the other cursor
		 */
		@Override
		public void copy(Cursor cursor) {
			this.lastColumn = this.column = cursor.column;
		}

		/**
		 * Sanitizes the cursor.
		 */
		public void sanitize() {
			if (this.column < 0)
				this.toStart();
			else if (this.column > text.length())
				this.column = text.length();
		}

		/**
		 * Returns whether this cursor is at the same place as the other cursor.
		 *
		 * @param other the other cursor
		 * @return {@code true} if this cursor is at the same place as the other cursor, else {@code false}
		 */
		public boolean isSame(SpruceTextFieldWidget.Cursor other) {
			return this.column == other.column;
		}

		/**
		 * Returns the position of the cursor in the text.
		 *
		 * @return the position
		 */
		public int getPosition() {
			return this.column;
		}

		@Override
		public String toString() {
			return "SpruceTextAreaWidget$Cursor{main=" + this.main
					+ ", column=" + this.column
					+ ", lastColumn=" + this.lastColumn
					+ "}";
		}
	}

	/**
	 * Represents a selection.
	 *
	 * @version 9.0.0
	 * @since 2.1.0
	 */
	public class Selection extends AbstractSpruceTextInputWidget<Cursor>.Selection {
		protected Selection() {
			super(new Cursor(false), new Cursor(false));
		}

		/**
		 * Erases the selected text.
		 *
		 * @return {@code true} if the text has been erased, else {@code false}
		 */
		public boolean erase() {
			if (!this.active)
				return false;

			var start = this.getStart();
			var end = this.getEnd();

			if (start.isSame(end)) {
				this.cancel();
				return false;
			}

			if (start.column == 0 && end.column >= text.length()) {
				text = "";
				this.cancel();
				return true;
			}

			var text = getText();
			var newText = text.substring(0, start.getPosition()) + text.substring(end.getPosition());
			if (SpruceTextFieldWidget.this.textPredicate.test(newText)) {
				SpruceTextFieldWidget.this.text = newText;
				SpruceTextFieldWidget.this.onChanged();
			}

			cursor.copy(start);

			this.cancel();
			return true;
		}

		@Override
		public String getSelectedText() {
			if (!this.active)
				return "";

			var start = this.getStart();
			var end = this.getEnd();

			if (start.isSame(end))
				return "";

			return getText().substring(start.getPosition(), end.getPosition());
		}

		@Override
		protected boolean isInverted() {
			return this.anchor.column > this.follower.column;
		}
	}
}
