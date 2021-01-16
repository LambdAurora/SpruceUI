/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.screen;

import me.lambdaurora.spruceui.SprucePositioned;
import me.lambdaurora.spruceui.Tooltip;
import me.lambdaurora.spruceui.navigation.NavigationDirection;
import me.lambdaurora.spruceui.util.ScissorManager;
import me.lambdaurora.spruceui.widget.SpruceElement;
import me.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Represents a screen.
 *
 * @author LambdAurora
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class SpruceScreen extends Screen implements SprucePositioned, SpruceElement {
    protected double scaleFactor;

    protected SpruceScreen(@NotNull Text title) {
        super(title);
    }

    @Override
    public void setFocused(Element focused) {
        Element old = this.getFocused();
        if (old == focused) return;
        if (old instanceof SpruceWidget)
            ((SpruceWidget) old).setFocused(false);
        super.setFocused(focused);
        if (focused instanceof SpruceWidget)
            ((SpruceWidget) focused).setFocused(true);
    }

    @Override
    protected void init() {
        this.scaleFactor = this.client.getWindow().getScaleFactor();
    }

    /* Input */

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Optional<NavigationDirection> direction = NavigationDirection.fromKey(keyCode, Screen.hasShiftDown());
        return direction.map(dir -> this.onNavigation(dir, keyCode == GLFW.GLFW_KEY_TAB))
                .orElseGet(() -> super.keyPressed(keyCode, scanCode, modifiers));
    }

    /* Navigation */

    @Override
    public boolean onNavigation(@NotNull NavigationDirection direction, boolean tab) {
        if (this.requiresCursor()) return false;
        Element focused = this.getFocused();
        boolean isNonNull = focused != null;
        if (!isNonNull || !this.tryNavigating(focused, direction, tab)) {
            List<? extends Element> list = this.children();
            int i = list.indexOf(focused);
            int next;
            if (isNonNull && i >= 0) next = i + (direction.isLookingForward() ? 1 : 0);
            else if (direction.isLookingForward()) next = 0;
            else next = list.size();

            ListIterator<? extends Element> iterator = list.listIterator(next);
            BooleanSupplier hasNext = direction.isLookingForward() ? iterator::hasNext : iterator::hasPrevious;
            Supplier<Element> nextGetter = direction.isLookingForward() ? iterator::next : iterator::previous;

            Element nextElement;
            do {
                if (!hasNext.getAsBoolean()) {
                    this.setFocused(null);
                    return false;
                }

                nextElement = nextGetter.get();
            } while (!this.tryNavigating(nextElement, direction, tab));

            this.setFocused(nextElement);
        }
        return true;
    }

    private boolean tryNavigating(@NotNull Element element, @NotNull NavigationDirection direction, boolean tab) {
        if (element instanceof SpruceElement) {
            return ((SpruceElement) element).onNavigation(direction, tab);
        }
        return element.changeFocus(direction.isLookingForward());
    }

    /* Render */

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        ScissorManager.pushScaleFactor(this.scaleFactor);
        this.renderBackground(matrices);
        this.renderWidgets(matrices, mouseX, mouseY, delta);
        this.renderTitle(matrices, mouseX, mouseY, delta);
        Tooltip.renderAll(this, matrices);
        ScissorManager.popScaleFactor();
    }

    public void renderTitle(MatrixStack matrices, int mouseX, int mouseY, float delta) {}

    public void renderWidgets(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for (Element element : this.children()) {
            if (element instanceof Drawable)
                ((Drawable) element).render(matrices, mouseX, mouseY, delta);
        }
    }
}
