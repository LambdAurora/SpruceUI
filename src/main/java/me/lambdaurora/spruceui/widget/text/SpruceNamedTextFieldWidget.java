/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.widget.text;

import me.lambdaurora.spruceui.navigation.NavigationDirection;
import me.lambdaurora.spruceui.util.ColorUtil;
import me.lambdaurora.spruceui.widget.AbstractSpruceWidget;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * Represents a text field widget with a title on top.
 *
 * @author LambdAurora
 * @version 2.1.0
 * @since 2.1.0
 */
public class SpruceNamedTextFieldWidget extends AbstractSpruceWidget {
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

    public Predicate<String> getTextPredicate() {
        return this.getTextFieldWidget().getTextPredicate();
    }

    public void setTextPredicate(Predicate<String> textPredicate) {
        this.getTextFieldWidget().setTextPredicate(textPredicate);
    }

    public BiFunction<String, Integer, OrderedText> getRenderTextProvider() {
        return this.getTextFieldWidget().getRenderTextProvider();
    }

    public void setRenderTextProvider(BiFunction<String, Integer, OrderedText> renderTextProvider) {
        this.getTextFieldWidget().setRenderTextProvider(renderTextProvider);
    }

    /* Navigation */

    @Override
    public boolean onNavigation(@NotNull NavigationDirection direction, boolean tab) {
        return this.getTextFieldWidget().onNavigation(direction, tab);
    }

    /* Input */

    @Override
    protected boolean onMouseClick(double mouseX, double mouseY, int button) {
        return this.getTextFieldWidget().mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected boolean onMouseRelease(double mouseX, double mouseY, int button) {
        return this.getTextFieldWidget().mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected boolean onMouseDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return this.getTextFieldWidget().mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    protected boolean onMouseScroll(double mouseX, double mouseY, double amount) {
        return this.getTextFieldWidget().mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    protected boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        return this.getTextFieldWidget().keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected boolean onKeyRelease(int keyCode, int scanCode, int modifiers) {
        return this.getTextFieldWidget().keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    protected boolean onCharTyped(char chr, int keyCode) {
        return this.getTextFieldWidget().charTyped(chr, keyCode);
    }

    /* Rendering */

    @Override
    protected void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        DrawableHelper.drawTextWithShadow(matrices, this.client.textRenderer, this.getTextFieldWidget().getTitle(), this.getX() + 2, this.getY() + 2, ColorUtil.TEXT_COLOR);

        this.getTextFieldWidget().render(matrices, mouseX, mouseY, delta);
    }
}
