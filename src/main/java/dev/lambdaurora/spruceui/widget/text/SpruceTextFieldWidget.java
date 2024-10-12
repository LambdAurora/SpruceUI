/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget.text;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.Tooltip;
import dev.lambdaurora.spruceui.Tooltipable;
import dev.lambdaurora.spruceui.navigation.NavigationDirection;
import dev.lambdaurora.spruceui.util.ColorUtil;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Text;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringUtil;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Represents a text field widget.
 *
 * @author LambdAurora
 * @version 5.0.0
 * @since 2.1.0
 */
public class SpruceTextFieldWidget extends AbstractSpruceTextInputWidget implements Tooltipable {
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

	private final Cursor cursor = new Cursor(true);
	private final Selection selection = new Selection();
	private String text = "";
	private Text tooltip;

	private Consumer<String> changedListener;
	private Predicate<String> textPredicate;
	private BiFunction<String, Integer, FormattedCharSequence> renderTextProvider;

	private int firstCharacterIndex = 0;
	private long editingTime;
	private int tooltipTicks;
	private long lastTick;

	public SpruceTextFieldWidget(Position position, int width, int height, Text title) {
		super(position, width, height, title);
		this.cursor.toStart();
		this.sanitize();

		this.changedListener = (input) -> {
		};
		this.textPredicate = Objects::nonNull;
		this.renderTextProvider = (input, firstCharacterIndex) -> FormattedCharSequence.forward(input, Style.EMPTY);
	}

	@Override
	public String getText() {
		return this.text;
	}

	@Override
	public void setText(String text) {
		if (this.textPredicate.test(text)) {
			this.text = text;

			this.setCursorToEnd();
			this.selection.cancel();
			this.sanitize();
			this.onChanged();
		}
	}

	@Override
	public Optional<Text> getTooltip() {
		return Optional.ofNullable(this.tooltip);
	}

	@Override
	public void setTooltip(@Nullable Text tooltip) {
		this.tooltip = tooltip;
	}

	public Consumer<String> getChangedListener() {
		return this.changedListener;
	}

	public void setChangedListener(Consumer<String> changedListener) {
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

		this.firstCharacterIndex = MathHelper.clamp(this.firstCharacterIndex, 0, textLength);
	}

	private void onChanged() {
		if (this.changedListener != null) {
			this.changedListener.accept(this.text);
		}

		this.editingTime = Util.getMillis() + 5000L;
	}

	private boolean onSelectionUpdate(Runnable action) {
		this.selection.tryStartSelection();
		action.run();
		this.selection.moveToCursor();
		this.sanitize();
		return true;
	}

	private void insertCharacter(char character) {
		if (this.getText().isEmpty()) {
			this.setText(String.valueOf(character));
			return;
		} else {
			this.selection.erase();
		}

		if (character == '\n') {
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
	public boolean onNavigation(NavigationDirection direction, boolean tab) {
		if (this.requiresCursor()) return false;
		if (!tab && direction.isHorizontal()) {
			this.setFocused(true);
			boolean result = switch (direction) {
				case RIGHT -> this.onSelectionUpdate(this.cursor::moveRight);
				case LEFT -> this.onSelectionUpdate(this.cursor::moveLeft);
				default -> false;
			};
			if (result)
				return true;
		}
		return super.onNavigation(direction, tab);
	}

	/* Input */

	@Override
	protected boolean onCharTyped(char chr, int keyCode) {
		if (!this.isEditorActive() || !StringUtil.isAllowedChatCharacter(chr))
			return false;

		if (this.isActive()) {
			this.insertCharacter(chr);
			this.selection.cancel();
		}
		return true;
	}

	@Override
	protected boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
		if (!this.isEditorActive())
			return false;

		if (Screen.isSelectAll(keyCode)) {
			this.selection.selectAll();
			this.sanitize();
			return true;
		} else if (Screen.isPaste(keyCode)) {
			this.write(this.client.keyboardHandler.getClipboard());
			return true;
		} else if (Screen.isCopy(keyCode) || Screen.isCut(keyCode)) {
			var selected = this.selection.getSelectedText();
			if (!selected.isEmpty())
				this.client.keyboardHandler.setClipboard(selected);
			if (Screen.isCut(keyCode)) {
				this.selection.erase();
				this.sanitize();
			}
			return true;
		}

		return switch (keyCode) {
			case GLFW.GLFW_KEY_RIGHT -> this.onSelectionUpdate(this.cursor::moveRight);
			case GLFW.GLFW_KEY_LEFT -> this.onSelectionUpdate(this.cursor::moveLeft);
			case GLFW.GLFW_KEY_END -> this.onSelectionUpdate(this.cursor::toEnd);
			case GLFW.GLFW_KEY_HOME -> this.onSelectionUpdate(this.cursor::toStart);
			case GLFW.GLFW_KEY_BACKSPACE -> {
				this.eraseCharacter();
				yield true;
			}
			case GLFW.GLFW_KEY_DELETE -> {
				this.removeCharacterForward();
				yield true;
			}
			case GLFW.GLFW_KEY_D -> {
				if (Screen.hasControlDown() && !this.text.isEmpty()) {
					this.setText("");
				}
				yield true;
			}
			default -> false;
		};
	}

	@Override
	protected boolean onMouseClick(double mouseX, double mouseY, int button) {
		if (button == 0) {
			int x = MathHelper.floor(mouseX) - this.getX() - 4;

			this.setFocused(true);

			this.onSelectionUpdate(() -> {
				var displayedText = this.client.font.plainSubstrByWidth(this.text.substring(this.firstCharacterIndex),
						this.getInnerWidth());
				this.cursor.lastColumn = this.cursor.column = this.firstCharacterIndex
						+ this.client.font.plainSubstrByWidth(displayedText, x).length();
			});

			return true;
		}

		return false;
	}

	/* Rendering */

	@Override
	protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		super.renderWidget(graphics, mouseX, mouseY, delta);

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
	protected void drawText(GuiGraphics graphics) {
		int textColor = this.getTextColor();
		int x = this.getX() + 4;
		int y = this.getY() + this.getHeight() / 2 - 4;

		var displayedText = this.client.font.plainSubstrByWidth(this.text.substring(this.firstCharacterIndex),
				this.getInnerWidth());

		graphics.drawShadowedText(this.client.font, this.renderTextProvider.apply(displayedText, this.firstCharacterIndex),
				x, y, textColor);
		this.drawSelection(displayedText, y);
	}

	/**
	 * Draws the selection over the text.
	 *
	 * @param line the current line
	 * @param lineY the line Y-coordinates
	 */
	protected void drawSelection(String line, int lineY) {
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

		var tessellator = Tessellator.getInstance();
		var buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
		RenderSystem.enableColorLogicOp();
		RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
		RenderSystem.setShader(GameRenderer::getPositionShader);
		RenderSystem.setShaderColor(0.f, 0.f, 255.f, 255.f);
		buffer.addVertex(x, y2, 0.f);
		buffer.addVertex(x2, y2, 0.f);
		buffer.addVertex(x2, lineY, 0.f);
		buffer.addVertex(x, lineY, 0.f);
		MeshData builtBuffer = buffer.build();
		if (builtBuffer != null) {
			BufferUploader.drawWithShader(builtBuffer);
		}
		tessellator.clear();
		RenderSystem.disableColorLogicOp();
	}

	/**
	 * Draws the cursor.
	 *
	 * @param graphics The GUI graphics instance to render with
	 */
	protected void drawCursor(GuiGraphics graphics) {
		if (!this.isFocused())
			return;

		int cursorY = this.getY() + this.getHeight() / 2 - 4;

		if (this.text.isEmpty()) {
			graphics.drawShadowedText(this.client.font, Text.literal("_"),
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
			graphics.drawShadowedText(this.client.font, "_", cursorX, cursorY, ColorUtil.TEXT_COLOR);
	}

	/* Narration */

	@Override
	public void updateNarration(NarrationElementOutput builder) {
		super.updateNarration(builder);
		this.getTooltip().ifPresent(text -> builder.add(NarratedElementType.HINT, text));
	}

	/**
	 * Represents a cursor.
	 *
	 * @version 3.0.0
	 * @since 2.1.0
	 */
	public class Cursor {
		boolean main;
		int column = 0;
		private int lastColumn = 0;

		public Cursor(boolean main) {
			this.main = main;
		}

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
						MathHelper.clamp(SpruceTextFieldWidget.this.firstCharacterIndex = this.column - 1, 0, text.length());
			}
		}

		public void toEnd() {
			this.lastColumn = this.column = text.length();
		}

		/**
		 * Copies the column from another cursor.
		 *
		 * @param cursor the other cursor
		 */
		public void copy(SpruceTextFieldWidget.Cursor cursor) {
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
	 * @version 3.0.0
	 * @since 2.1.0
	 */
	public class Selection {
		private final Cursor anchor = new Cursor(false);
		private final Cursor follower = new Cursor(false);
		private boolean active = false;

		/**
		 * Selects all.
		 */
		public void selectAll() {
			this.anchor.toStart();
			cursor.toEnd();
			this.follower.copy(cursor);
			this.active = true;
		}

		/**
		 * Cancels the selection.
		 */
		public void cancel() {
			this.anchor.toStart();
			this.follower.toStart();
			this.active = false;
		}

		public void tryStartSelection() {
			if (!this.active && Screen.hasShiftDown()) {
				this.startSelection();
			}
		}

		public void startSelection() {
			this.anchor.copy(cursor);
			this.follower.copy(cursor);
			this.active = true;
		}

		public void moveToCursor() {
			if (!this.active)
				return;

			if (Screen.hasShiftDown()) {
				this.follower.copy(cursor);
			} else {
				this.cancel();
			}
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

		/**
		 * Returns the selected text.
		 *
		 * @return the selected text, if no text is selected the return value is an empty string
		 */
		public String getSelectedText() {
			if (!this.active)
				return "";

			var start = this.getStart();
			var end = this.getEnd();

			if (start.isSame(end))
				return "";

			return getText().substring(start.getPosition(), end.getPosition());
		}

		public Cursor getStart() {
			return this.isInverted() ? this.follower : this.anchor;
		}

		public Cursor getEnd() {
			return this.isInverted() ? this.anchor : this.follower;
		}

		private boolean isInverted() {
			return this.anchor.column > this.follower.column;
		}
	}
}
