/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget.text;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.background.Background;
import dev.lambdaurora.spruceui.background.SimpleColorBackground;
import dev.lambdaurora.spruceui.border.Border;
import dev.lambdaurora.spruceui.border.TexturedBorder;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import dev.lambdaurora.spruceui.util.ColorUtil;
import dev.lambdaurora.spruceui.widget.AbstractSpruceWidget;
import dev.lambdaurora.spruceui.widget.WithBackground;
import dev.lambdaurora.spruceui.widget.WithBorder;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

/**
 * Represents a text input widget.
 *
 * @author LambdAurora
 * @version 9.0.0
 * @since 2.1.0
 */
public abstract class AbstractSpruceTextInputWidget<C extends AbstractSpruceTextInputWidget.Cursor<C>>
		extends AbstractSpruceWidget
		implements WithBackground, WithBorder {
	private final Component title;
	private Background background = new SimpleColorBackground(ColorUtil.BLACK);
	private Border border = TexturedBorder.SIMPLE;
	private @Nullable Component placeholder;

	private int editableColor = ColorUtil.TEXT_COLOR;
	private int uneditableColor = ColorUtil.UNEDITABLE_COLOR;

	public AbstractSpruceTextInputWidget(Position position, int width, int height, Component title) {
		this(position, width, height, title, null);
	}

	public AbstractSpruceTextInputWidget(Position position, int width, int height, Component title, @Nullable Component placeholder) {
		super(position);
		this.width = width;
		this.height = height;
		this.title = title;
		this.placeholder = placeholder;
	}

	/**
	 * Returns the text from the text input widget.
	 *
	 * @return the text
	 */
	public abstract String getText();

	/**
	 * Sets the text in the text input widget.
	 *
	 * @param text the text
	 */
	public abstract void setText(@Nullable String text);

	/**
	 * Returns the title of this text input widget.
	 *
	 * @return the title
	 */
	public Component getTitle() {
		return this.title;
	}

	/**
	 * Returns the placeholder of this text input widget.
	 *
	 * @return the placeholder
	 */
	public @Nullable Component getPlaceholder() {
		return this.placeholder;
	}

	/**
	 * Sets the placeholder of this text input widget.
	 *
	 * @param placeholder the placeholder
	 */
	public void setPlaceholder(@Nullable Component placeholder) {
		this.placeholder = placeholder;
	}

	/**
	 * Returns the color for editable text.
	 *
	 * @return the editable text
	 */
	public int getEditableColor() {
		return this.editableColor;
	}

	/**
	 * Sets the color for editable text.
	 *
	 * @param editableColor the editable color
	 */
	public void setEditableColor(int editableColor) {
		this.editableColor = editableColor;
	}

	/**
	 * Returns the color for uneditable text.
	 *
	 * @return the uneditable color
	 */
	public int getUneditableColor() {
		return this.uneditableColor;
	}

	/**
	 * Sets the color for uneditable text.
	 *
	 * @param uneditableColor the uneditable color
	 */
	public void setUneditableColor(int uneditableColor) {
		this.uneditableColor = uneditableColor;
	}

	/**
	 * Returns the text color.
	 *
	 * @return the text color
	 */
	public int getTextColor() {
		return this.isActive() ? this.getEditableColor() : this.getUneditableColor();
	}

	/**
	 * Sets the cursor to the start of the text.
	 */
	public abstract void setCursorToStart();

	/**
	 * Sets the cursor to the end of the text.
	 */
	public abstract void setCursorToEnd();

	@Override
	public Background getBackground() {
		return this.background;
	}

	@Override
	public void setBackground(Background background) {
		this.background = background;
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
	 * Returns the inner width of the text input widget.
	 *
	 * @return the inner width
	 */
	public int getInnerWidth() {
		return this.getWidth() - 6 - this.getBorder().getThickness() * 2;
	}

	/**
	 * Returns the inner height of the text input widget.
	 *
	 * @return the inner height
	 */
	public int getInnerHeight() {
		return this.getHeight() - 6 - this.getBorder().getThickness() * 2;
	}

	/**
	 * Sanitizes the text input widget.
	 */
	protected abstract void sanitize();

	/**
	 * Returns whether this text area is editable or not.
	 *
	 * @return {@code true} if editable, or {@code false} otherwise
	 */
	public boolean isEditable() {
		return this.isActive();
	}

	public boolean isEditorActive() {
		return this.isActive() && this.isFocused();
	}

	/* Logic */

	protected abstract C cursor();

	protected abstract Selection selection();

	protected abstract void insertCharacter(String character);

	/* Input Handling */

	@Override
	protected boolean onCharTyped(CharacterEvent event) {
		if (!this.isEditorActive() || !event.isAllowedChatCharacter())
			return false;

		if (this.isEditable()) {
			this.insertCharacter(event.codepointAsString());
			this.selection().cancel();
		}
		return true;
	}

	/* Rendering */

	@Override
	protected void extractWidgetRenderState(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
		this.getBorder().extractRenderState(graphics, this, mouseX, mouseY, delta);

		if (this.isMouseHovered()) {
			graphics.requestCursor(this.isEditable() ? CursorTypes.IBEAM : CursorTypes.NOT_ALLOWED);
		}
	}

	@Override
	protected void extractBackground(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
		this.getBackground().extractRenderState(graphics, this, 0, mouseX, mouseY, delta);
	}

	/* Narration */

	@Override
	protected Component getNarrationMessage() {
		return Component.translatable("gui.narrate.editBox", this.getTitle(), this.getText());
	}

	protected interface Cursor<C extends Cursor<C>> {
		void toStart();

		void toEnd();

		void copy(C cursor);
	}

	/**
	 * Represents a text selection.
	 */
	protected abstract class Selection {
		protected final C anchor;
		protected final C follower;
		protected boolean active = false;

		protected Selection(C anchor, C follower) {
			this.anchor = anchor;
			this.follower = follower;
		}

		/**
		 * Cancels the selection.
		 */
		public void cancel() {
			this.anchor.toStart();
			this.follower.toStart();
			this.active = false;
		}

		/**
		 * Selects all.
		 */
		public void selectAll() {
			this.anchor.toStart();
			cursor().toEnd();
			this.follower.copy(cursor());
			this.active = true;
		}

		public void startSelection() {
			this.anchor.copy(cursor());
			this.follower.copy(cursor());
			this.active = true;
		}

		public void tryStartSelection(boolean hasShiftDown) {
			if (!this.active && hasShiftDown) {
				this.startSelection();
			}
		}

		public void moveToCursor(boolean hasShiftDown) {
			if (!this.active)
				return;

			if (hasShiftDown) {
				this.follower.copy(cursor());
			} else {
				this.cancel();
			}
		}

		public C getStart() {
			return this.isInverted() ? this.follower : this.anchor;
		}

		public C getEnd() {
			return this.isInverted() ? this.anchor : this.follower;
		}

		protected abstract boolean isInverted();

		/**
		 * Gets the selected text.
		 *
		 * @return the selected text, if no text is selected the return value is an empty string
		 */
		public abstract String getSelectedText();
	}
}
