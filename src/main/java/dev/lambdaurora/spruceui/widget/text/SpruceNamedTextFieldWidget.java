/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget.text;

import dev.lambdaurora.spruceui.background.Background;
import dev.lambdaurora.spruceui.border.Border;
import dev.lambdaurora.spruceui.navigation.NavigationEvent;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import dev.lambdaurora.spruceui.util.ColorUtil;
import dev.lambdaurora.spruceui.widget.AbstractSpruceWidget;
import dev.lambdaurora.spruceui.widget.WithBackground;
import dev.lambdaurora.spruceui.widget.WithBorder;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.util.FormattedCharSequence;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Represents a text field widget with a title on top.
 *
 * @author LambdAurora
 * @version 9.0.0
 * @since 2.1.0
 */
public class SpruceNamedTextFieldWidget extends AbstractSpruceWidget implements WithBackground, WithBorder {
	private static final int Y_OFFSET = 13;
	private final SpruceTextFieldWidget textFieldWidget;

	public SpruceNamedTextFieldWidget(SpruceTextFieldWidget widget) {
		super(widget.getPosition().copy());
		widget.getPosition().setAnchor(this);
		widget.getPosition().setRelativeX(0);
		widget.getPosition().setRelativeY(Y_OFFSET);

		this.textFieldWidget = widget;
	}

	public SpruceTextFieldWidget getTextFieldWidget() {
		return this.textFieldWidget;
	}

	@Override
	public int getWidth() {
		return this.getTextFieldWidget().getWidth();
	}

	@Override
	public int getHeight() {
		return this.getTextFieldWidget().getHeight() + Y_OFFSET;
	}

	@Override
	public boolean isVisible() {
		return this.getTextFieldWidget().isVisible();
	}

	@Override
	public void setVisible(boolean visible) {
		this.getTextFieldWidget().setVisible(visible);
	}

	@Override
	public boolean isFocused() {
		return this.getTextFieldWidget().isFocused();
	}

	@Override
	public void setFocused(boolean focused) {
		this.getTextFieldWidget().setFocused(focused);
	}

	public String getText() {
		return this.getTextFieldWidget().getText();
	}

	public void setText(String text) {
		this.getTextFieldWidget().setText(text);
	}

	public Consumer<String> getChangedListener() {
		return this.getTextFieldWidget().getChangedListener();
	}

	public void setChangedListener(Consumer<String> changedListener) {
		this.getTextFieldWidget().setChangedListener(changedListener);
	}

	public Predicate<String> getTextPredicate() {
		return this.getTextFieldWidget().getTextPredicate();
	}

	public void setTextPredicate(Predicate<String> textPredicate) {
		this.getTextFieldWidget().setTextPredicate(textPredicate);
	}

	public BiFunction<String, Integer, FormattedCharSequence> getRenderTextProvider() {
		return this.getTextFieldWidget().getRenderTextProvider();
	}

	public void setRenderTextProvider(BiFunction<String, Integer, FormattedCharSequence> renderTextProvider) {
		this.getTextFieldWidget().setRenderTextProvider(renderTextProvider);
	}

	/**
	 * Returns the color for editable text.
	 *
	 * @return the editable text
	 */
	public int getEditableColor() {
		return this.getTextFieldWidget().getEditableColor();
	}

	/**
	 * Sets the color for editable text.
	 *
	 * @param editableColor the editable color
	 */
	public void setEditableColor(int editableColor) {
		this.getTextFieldWidget().setEditableColor(editableColor);
	}

	/**
	 * Returns the color for uneditable text.
	 *
	 * @return the uneditable color
	 */
	public int getUneditableColor() {
		return this.getTextFieldWidget().getUneditableColor();
	}

	/**
	 * Sets the color for uneditable text.
	 *
	 * @param uneditableColor the uneditable color
	 */
	public void setUneditableColor(int uneditableColor) {
		this.getTextFieldWidget().setUneditableColor(uneditableColor);
	}

	/**
	 * Returns the text color.
	 *
	 * @return the text color
	 */
	public int getTextColor() {
		return this.getTextFieldWidget().getTextColor();
	}

	@Override
	public Background getBackground() {
		return this.getTextFieldWidget().getBackground();
	}

	@Override
	public void setBackground(Background background) {
		this.getTextFieldWidget().setBackground(background);
	}

	@Override
	public Border getBorder() {
		return this.getTextFieldWidget().getBorder();
	}

	@Override
	public void setBorder(Border border) {
		this.getTextFieldWidget().setBorder(border);
	}

	/* Navigation */

	@Override
	public boolean onNavigation(NavigationEvent event) {
		return this.getTextFieldWidget().onNavigation(event);
	}

	/* Input */

	@Override
	protected boolean onMouseClick(MouseButtonEvent event, boolean doubleClick) {
		return this.getTextFieldWidget().mouseClicked(event, doubleClick);
	}

	@Override
	protected boolean onMouseRelease(MouseButtonEvent event) {
		return this.getTextFieldWidget().mouseReleased(event);
	}

	@Override
	protected boolean onMouseDrag(MouseButtonEvent event, double deltaX, double deltaY) {
		return this.getTextFieldWidget().mouseDragged(event, deltaX, deltaY);
	}

	@Override
	protected boolean onMouseScroll(double mouseX, double mouseY, double scrollX, double scrollY) {
		return this.getTextFieldWidget().mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}

	@Override
	protected boolean onKeyPress(KeyEvent event) {
		return this.getTextFieldWidget().keyPressed(event);
	}

	@Override
	protected boolean onKeyRelease(KeyEvent event) {
		return this.getTextFieldWidget().keyReleased(event);
	}

	@Override
	protected boolean onCharTyped(CharacterEvent event) {
		return this.getTextFieldWidget().charTyped(event);
	}

	/* Rendering */

	@Override
	protected void extractWidgetRenderState(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
		graphics.shadowedText(this.client.font, this.getTextFieldWidget().getTitle(), this.getX() + 2, this.getY() + 2, ColorUtil.TEXT_COLOR);

		this.getTextFieldWidget().extractRenderState(graphics, mouseX, mouseY, delta);
	}
}
