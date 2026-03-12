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
import dev.lambdaurora.spruceui.border.Border;
import dev.lambdaurora.spruceui.navigation.NavigationEvent;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import dev.lambdaurora.spruceui.util.ColorUtil;
import dev.lambdaurora.spruceui.util.MultilineText;
import net.minecraft.client.gui.Font;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a text area widget.
 *
 * @author LambdAurora
 * @version 9.0.0
 * @since 1.6.3
 */
public class SpruceTextAreaWidget extends AbstractSpruceTextInputWidget<SpruceTextAreaWidget.Cursor> {
	private final Font font;
	private final MultilineText lines;
	private final Cursor cursor = new Cursor(true);
	private final Selection selection = new Selection();
	private int firstLine = 0;
	private int displayedLines;

	public SpruceTextAreaWidget(Position position, int width, int height, Component title) {
		this(position, width, height, title, null);
	}

	public SpruceTextAreaWidget(Position position, int width, int height, Component title, @Nullable Component placeholder) {
		super(position, width, height, title, placeholder);
		this.font = this.client.font;
		this.displayedLines = this.getInnerHeight() / this.font.lineHeight;
		this.lines = new MultilineText(this.getInnerWidth());
		this.cursor.toStart();
		this.sanitize();
	}

	/**
	 * Returns the lines.
	 *
	 * @return the lines
	 */
	public List<String> getLines() {
		return this.lines.getLines();
	}

	/**
	 * Sets the lines.
	 *
	 * @param lines the lines
	 */
	public void setLines(List<String> lines) {
		this.lines.setLines(lines);
		this.selection.active = false;
		this.setCursorToEnd();
	}

	@Override
	public String getText() {
		return this.lines.getText();
	}

	@Override
	public void setText(@Nullable String text) {
		this.lines.clear();
		if (text != null)
			this.lines.setText(text);
	}

	/**
	 * Clears the text area.
	 */
	public void clear() {
		this.lines.clear();
		this.sanitize();
	}

	/**
	 * Sets whether this text area is editable or not.
	 *
	 * @param editable state of editable
	 * @since 1.6.4
	 */
	public void setEditable(boolean editable) {
		this.setActive(editable);
	}

	/**
	 * Sets the number of displayed lines.
	 *
	 * @param lines number of displayed lines
	 */
	public void setDisplayedLines(int lines) {
		if (lines <= 0)
			lines = 1;
		this.displayedLines = lines;

		this.cursor.adjustFirstLine();
	}

	@Override
	public void setBorder(Border border) {
		super.setBorder(border);
		this.lines.setWidth(this.getInnerWidth());
		this.sanitize();
	}

	@Override
	protected dev.lambdaurora.spruceui.widget.text.SpruceTextAreaWidget.Cursor cursor() {
		return this.cursor;
	}

	@Override
	protected AbstractSpruceTextInputWidget<dev.lambdaurora.spruceui.widget.text.SpruceTextAreaWidget.Cursor>.Selection selection() {
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
	protected void insertCharacter(String character) {
		if (this.lines.isEmpty()) {
			this.lines.add(character);
			this.setCursorToStart();
			return;
		} else {
			this.selection.erase();
		}

		if (character.equals("\n")) {
			String line;
			if (this.cursor.row == this.lines.size() - 1 && (line = this.lines.get(this.cursor.row)) != null && this.cursor.column >= line.length() - 1) {
				int currentRow = this.cursor.row;
				if (!line.endsWith("\n"))
					this.lines.replaceRow(currentRow, s -> s + "\n");
				this.lines.add(currentRow + 1, "");
				this.cursor.moveDown();
				return;
			}
		}

		var text = this.getText();
		int cursorPosition = this.cursor.getPosition();
		int oldSize = this.lines.size();

		String newText;
		if (cursorPosition >= text.length()) {
			newText = text + character;
		} else {
			newText = text.substring(0, cursorPosition) + character + text.substring(cursorPosition);
		}

		this.lines.clear();
		this.lines.add(newText);

		if (character.equals("\n")) {
			this.cursor.moveRight();
			//this.cursor.moveRight();
		} else {
			this.cursor.moveRight();
			if (oldSize + 1 == this.lines.size())
				this.cursor.moveRight(); // Extra move right.
		}
	}

	private void eraseCharacter() {
		if (this.selection.erase()) {
			this.sanitize();
			return;
		}

		var line = this.lines.get(this.cursor.row);

		if (line == null) {
			this.cursor.row = this.lines.getRows().size() - 1;
			this.cursor.toRowEnd();
			return;
		} else if ((line.isEmpty() || line.equals("\n")) && this.lines.size() != 1) {
			this.lines.remove(this.cursor.row);
			this.cursor.moveUp();
			this.cursor.toRowEnd();
			return;
		}

		if (this.cursor.column == 0 && this.cursor.row == 0)
			return;

		var text = this.getText();
		int cursorPosition = this.cursor.getPosition();
		this.cursor.moveLeft();
		this.lines.clear();
		this.lines.add(text.substring(0, cursorPosition - 1) + text.substring(cursorPosition));
		this.sanitize();
	}

	private void removeCharacterForward() {
		if (this.selection.erase()) {
			this.sanitize();
			return;
		}

		var line = this.lines.get(this.cursor.row);

		if (line == null || line.isEmpty()) {
			int row = this.cursor.row;
			if (row >= this.lines.size() - 1)
				return;
			this.lines.remove(row);
			this.sanitize();
			return;
		}

		if (this.cursor.column >= line.length() && this.cursor.row == this.lines.size() - 1)
			return;

		var text = this.getText();
		int cursorPosition = this.cursor.getPosition();

		var newText = text.substring(0, cursorPosition) + text.substring(cursorPosition + 1);
		this.lines.clear();
		this.lines.add(newText);
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

		if (this.lines.isEmpty()) {
			this.lines.add(text);
			this.cursor.toEnd();
			return;
		}
		this.selection.erase();

		int oldSize = this.lines.size();

		var whole = this.getText();
		int position = this.cursor.getPosition();

		String newText;
		if (position >= whole.length()) {
			newText = whole + text;
		} else {
			newText = whole.substring(0, position) + text + whole.substring(position);
		}

		this.lines.clear();
		this.lines.setLines(Arrays.asList(newText.split("\n")));

		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '\n')
				this.cursor.moveRight();
			this.cursor.moveRight();
		}
		if (oldSize < this.lines.size()) this.cursor.moveRight();
	}

	protected boolean doesLineOccupyFullSpace(String cursorLine) {
		return this.font.width(cursorLine) >= this.getInnerWidth();
	}

	@Override
	protected void sanitize() {
		if (this.lines.isEmpty())
			this.lines.add("");
		this.cursor.sanitize();
	}

	/* Navigation */

	@Override
	public boolean onNavigation(NavigationEvent event) {
		if (this.requiresCursor()) return false;
		if (!event.tab()) {
			this.setFocused(true);
			boolean result = switch (event.direction()) {
				case RIGHT -> this.onSelectionUpdate(this.cursor::moveRight, event.hasShiftDown());
				case LEFT -> this.onSelectionUpdate(this.cursor::moveLeft, event.hasShiftDown());
				case UP -> this.onSelectionUpdate(this.cursor::moveUp, event.hasShiftDown());
				case DOWN -> this.onSelectionUpdate(this.cursor::moveDown, event.hasShiftDown());
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
			return true;
		} else if (event.isPaste()) {
			this.write(this.client.keyboardHandler.getClipboard());
			return true;
		} else if (event.isCopy() || event.isCut()) {
			var selected = this.selection.getSelectedText();
			if (!selected.isEmpty())
				this.client.keyboardHandler.setClipboard(selected);
			if (event.isCut() && this.isEditable()) {
				this.selection.erase();
				this.sanitize();
			}
			return true;
		}

		return switch (event.key()) {
			case GLFW.GLFW_KEY_RIGHT -> this.onSelectionUpdate(this.cursor::moveRight, event.hasShiftDown());
			case GLFW.GLFW_KEY_LEFT -> this.onSelectionUpdate(this.cursor::moveLeft, event.hasShiftDown());
			case GLFW.GLFW_KEY_UP -> this.onSelectionUpdate(this.cursor::moveUp, event.hasShiftDown());
			case GLFW.GLFW_KEY_DOWN -> this.onSelectionUpdate(this.cursor::moveDown, event.hasShiftDown());
			case GLFW.GLFW_KEY_END -> this.onSelectionUpdate(event.hasControlDown() ? this.cursor::toEnd : this.cursor::toRowEnd, event.hasShiftDown());
			case GLFW.GLFW_KEY_HOME -> this.onSelectionUpdate(event.hasControlDown() ? this.cursor::toStart : this.cursor::toLineStart, event.hasShiftDown());
			case GLFW.GLFW_KEY_PAGE_UP -> this.onSelectionUpdate(() -> this.cursor.moveVertical(-this.cursor.row), event.hasShiftDown());
			case GLFW.GLFW_KEY_PAGE_DOWN -> this.onSelectionUpdate(() -> this.cursor.moveVertical(this.lines.size() - this.cursor.row), event.hasShiftDown());
			case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
				if (this.isEditable())
					this.insertCharacter("\n");
				yield true;
			}
			case GLFW.GLFW_KEY_BACKSPACE -> {
				if (this.isEditable())
					this.eraseCharacter();
				yield true;
			}
			case GLFW.GLFW_KEY_DELETE -> {
				if (this.isEditable())
					this.removeCharacterForward();
				yield true;
			}
			case GLFW.GLFW_KEY_D -> {
				if (event.hasControlDown() && this.isEditable() && !this.lines.isEmpty()) {
					this.lines.remove(this.cursor.row);
					this.sanitize();
				}
				yield true;
			}
			default -> false;
		};
	}

	private boolean onSelectionUpdate(Runnable action, boolean hasShiftDown) {
		this.selection.tryStartSelection(hasShiftDown);
		action.run();
		this.selection.moveToCursor(hasShiftDown);
		return true;
	}

	@Override
	protected boolean onMouseClick(MouseButtonEvent event, boolean doubleClick) {
		if (event.button() == 0) {
			int x = Mth.floor(event.x()) - this.getX() - 4;
			int y = Mth.floor(event.y()) - this.getY() - 4;

			this.setFocused(true);

			int row = this.firstLine + y / 9;
			if (row >= this.lines.size()) {
				this.cursor.toEnd();
				return true;
			} else if (row < 0) {
				this.cursor.toStart();
				return true;
			}

			this.onSelectionUpdate(() -> {
				this.cursor.row = row;

				this.cursor.lastColumn = this.cursor.column = this.font.plainSubstrByWidth(this.lines.get(row), x).length();
			}, event.hasShiftDown());

			return true;
		}

		return false;
	}

	@Override
	protected boolean onMouseScroll(double mouseX, double mouseY, double scrollX, double scrollY) {
		if (!this.isEditorActive()) {
			return false;
		}

		if (scrollY > 0.) {
			this.cursor.moveUp();
		} else {
			this.cursor.moveDown();
		}
		return true;
	}

	/* Rendering */

	@Override
	protected void extractWidgetRenderState(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
		super.extractWidgetRenderState(graphics, mouseX, mouseY, delta);

		this.drawText(graphics);
		this.drawCursor(graphics);
	}

	/**
	 * Draws the text of the text area.
	 *
	 * @param graphics the GUI graphics instance to render with
	 */
	protected void drawText(SpruceGuiGraphics graphics) {
		int length = Math.min(this.lines.size(), this.displayedLines);

		int textColor = this.getTextColor();
		int textX = this.getX() + 4;
		int lineY = this.getY() + 4;
		var placeholder = this.getPlaceholder();

		if (this.getText().isEmpty() && placeholder != null) {
			graphics.shadowedText(this.client.font, placeholder, textX, lineY, textColor);
			return;
		}

		for (int row = this.firstLine; row < this.firstLine + length; row++) {
			var line = this.lines.get(row);
			if (line == null)
				continue;
			if (line.endsWith("\n")) line = line.substring(0, line.length() - 1);

			graphics.shadowedText(this.font, Component.literal(line), textX, lineY, textColor);
			this.drawSelection(graphics, line, lineY, row);

			lineY += this.font.lineHeight;
		}
	}

	/**
	 * Draws the selection over the text.
	 *
	 * @param graphics the GUI graphics instance to render with
	 * @param line the current line
	 * @param lineY the line Y-coordinates
	 * @param row the row number
	 */
	protected void drawSelection(SpruceGuiGraphics graphics, String line, int lineY, int row) {
		if (!this.isFocused())
			return;
		if (!this.selection.isRowSelected(row))
			return;

		int startIndex;
		if (selection.getStart().row != row) startIndex = 0;
		else startIndex = selection.getStart().column;

		int endIndex;
		if (selection.getEnd().row != row) endIndex = line.length();
		else endIndex = selection.getEnd().column;

		if (endIndex >= line.length())
			endIndex = line.length();

		if (startIndex >= line.length() || startIndex == endIndex)
			return;

		int x = this.getX() + 4 + this.font.width(line.substring(0, startIndex));
		var selected = line.substring(startIndex, endIndex);

		int x2 = x + this.font.width(selected);
		int y2 = lineY + this.font.lineHeight;

		graphics.fill(RenderPipelines.GUI_TEXT_HIGHLIGHT, x, lineY, x2, y2, 0xff0000ff);
	}

	/**
	 * Draws the cursor.
	 *
	 * @param graphics the GUI graphics instance to render with
	 */
	protected void drawCursor(SpruceGuiGraphics graphics) {
		if (!this.isFocused())
			return;
		if (this.lines.isEmpty()) {
			graphics.shadowedText(this.font, Component.literal("_"), this.getX(), this.getY() + 4, ColorUtil.TEXT_COLOR);
			return;
		}

		this.cursor.sanitize();

		int actualRow = this.cursor.row - firstLine;
		String cursorLine = this.lines.get(this.cursor.row);
		int cursorX = this.getX() + 4 + this.font.width(cursorLine.substring(0, this.cursor.column));
		int cursorY = this.getY() + 4 + actualRow * this.font.lineHeight;

		if (this.cursor.row < this.lines.size() - 1 || this.cursor.column < cursorLine.length() || this.doesLineOccupyFullSpace(cursorLine))
			graphics.fill(cursorX - 1, cursorY - 1, cursorX, cursorY + 9, ColorUtil.TEXT_COLOR);
		else
			graphics.shadowedText(this.font, "_", cursorX, cursorY, ColorUtil.TEXT_COLOR);
	}

	/**
	 * Represents a cursor.
	 *
	 * @version 3.0.0
	 * @since 1.6.3
	 */
	public class Cursor implements AbstractSpruceTextInputWidget.Cursor<Cursor> {
		boolean main;
		int row = 0;
		int column = 0;
		private int lastColumn = 0;

		public Cursor(boolean main) {
			this.main = main;
		}

		public void toLineStart() {
			this.lastColumn = this.column = 0;
		}

		public void resetRow() {
			this.row = 0;
		}

		public void moveRight() {
			this.moveHorizontal(1);
		}

		public void moveLeft() {
			this.moveHorizontal(-1);
		}

		public void moveUp() {
			this.moveVertical(-1);
		}

		public void moveDown() {
			this.moveVertical(1);
		}

		public void moveHorizontal(int amount) {
			this.column += amount;

			if (this.row >= lines.size()) {
				this.row = lines.size() - 1;
				this.column = lines.get(this.row).length();
			}
			if (this.column < 0) {
				if (this.row == 0) this.toLineStart();
				else {
					this.row -= 1;
					this.column = lines.get(this.row).length();
				}
			}

			var line = lines.get(this.row);
			if (line.endsWith("\n")) line = line.substring(0, line.length() - 1);

			if (this.column > line.length()) {
				if (amount > 0 && this.row != lines.size() - 1) {
					this.column = 0;
					this.row += 1;
				} else
					this.column = line.length();
			}

			this.lastColumn = this.column;

			this.adjustFirstLine();
		}

		public void moveVertical(int amount) {
			this.row += amount;
			this.column = this.lastColumn;
			this.sanitize();

			this.adjustFirstLine();
		}

		@Override
		public void toStart() {
			this.resetRow();
			this.toLineStart();
			this.adjustFirstLine();
		}

		@Override
		public void toEnd() {
			this.row = Math.max(0, lines.size() - 1);
			String line = SpruceTextAreaWidget.this.lines.get(this.row);
			if (line == null)
				this.lastColumn = this.column = 0;
			else if (line.endsWith("\n"))
				this.lastColumn = this.column = line.length() - 1;
			else
				this.lastColumn = this.column = line.length();
			this.adjustFirstLine();
		}

		public void toRowEnd() {
			String line = lines.get(this.row);
			if (line == null)
				this.lastColumn = this.column = 0;
			else if (line.endsWith("\n"))
				this.lastColumn = this.column = line.length() - 1;
			else
				this.lastColumn = this.column = line.length();
		}

		/**
		 * Copies the row and column from another cursor.
		 *
		 * @param cursor the other cursor
		 */
		@Override
		public void copy(Cursor cursor) {
			this.row = cursor.row;
			this.lastColumn = this.column = cursor.column;
		}

		/**
		 * Sanitizes the cursor.
		 */
		public void sanitize() {
			if (lines.size() <= this.row)
				this.row = lines.size() - 1;
			if (this.row < 0)
				this.resetRow();

			String line = lines.get(this.row);
			if (line.endsWith("\n")) line = line.substring(0, line.length() - 1);
			if (this.column > line.length())
				this.column = line.length();

			this.adjustFirstLine();
		}

		/**
		 * Returns whether this cursor is at the same place as the other cursor.
		 *
		 * @param other the other cursor
		 * @return {@code true} if this cursor is at the same place as the other cursor, else {@code false}
		 */
		public boolean isSame(Cursor other) {
			return this.row == other.row && this.column == other.column;
		}

		/**
		 * Returns the position of the cursor in the text.
		 *
		 * @return the position
		 */
		public int getPosition() {
			int position = 0;
			for (int i = 0; i < this.row; i++) position += lines.get(i).length();
			for (int i = 0; i < this.column; i++) position += 1;
			return position;
		}

		private void adjustFirstLine() {
			if (!this.main)
				return;

			if (firstLine > this.row)
				firstLine = this.row;

			if (firstLine == lines.size())
				firstLine--;
			int endLine = firstLine + displayedLines - 1;
			if (endLine >= lines.size()) {
				firstLine = lines.size() - displayedLines - 1;
			}

			if (this.row >= firstLine + displayedLines)
				firstLine = this.row - displayedLines + 1;

			if (firstLine < 0)
				firstLine = 0;
		}

		@Override
		public String toString() {
			return "SpruceTextAreaWidget$Cursor{main=" + this.main
					+ ", row=" + this.row
					+ ", column=" + this.column
					+ ", lastColumn=" + this.lastColumn
					+ "}";
		}
	}

	/**
	 * Represents a selection.
	 *
	 * @version 3.0.0
	 * @since 1.6.3
	 */
	public class Selection extends AbstractSpruceTextInputWidget<Cursor>.Selection {
		protected Selection() {
			super(new Cursor(false), new Cursor(false));
		}

		public boolean isRowSelected(int row) {
			return this.active && (this.getStart().row <= row && row <= this.getEnd().row);
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

			if (start.row == 0 && start.column == 0 && end.row == lines.size() - 1 && end.column >= lines.get(lines.size() - 1).length()) {
				lines.clear();
				lines.add("");
				this.cancel();
				return true;
			}

			if (start.row == end.row) {
				String line = lines.get(start.row);

				if (start.column == 0 && end.column >= line.length())
					lines.remove(start.row);
				else
					lines.replaceRow(start.row, line.substring(0, start.column) + line.substring(end.column));

				cursor.copy(start);
				this.cancel();
				return true;
			}

			String text = getText();
			var newText = text.substring(0, start.getPosition()) + text.substring(end.getPosition());

			lines.clear();
			lines.add(newText);

			cursor.copy(start);

			this.cancel();
			return true;
		}

		/**
		 * Returns the selected text.
		 *
		 * @return the selected text, if no text is selected the return value is an empty string
		 */
		@Override
		public String getSelectedText() {
			if (!this.active)
				return "";

			var start = this.getStart();
			var end = this.getEnd();

			if (start.isSame(end))
				return "";

			if (start.row == end.row)
				return lines.get(start.row).substring(start.column, end.column);

			return getText().substring(start.getPosition(), end.getPosition());
		}

		@Override
		protected boolean isInverted() {
			return this.anchor.row > this.follower.row
					|| (this.anchor.row == this.follower.row && this.anchor.column > this.follower.column);
		}
	}
}
