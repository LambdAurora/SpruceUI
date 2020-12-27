/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.widget.container;

import me.lambdaurora.spruceui.Position;
import me.lambdaurora.spruceui.navigation.NavigationDirection;
import me.lambdaurora.spruceui.navigation.NavigationUtils;
import me.lambdaurora.spruceui.widget.AbstractSpruceWidget;
import me.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.Iterator;
import java.util.List;

/**
 * Represents a parent widget, contains children which are other widgets.
 *
 * @param <E> the type of children widgets
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.7.0
 */
public abstract class AbstractSpruceParentWidget<E extends SpruceWidget> extends AbstractSpruceWidget implements ParentElement {
    private final Class<E> childClass;
    private @Nullable E focused;
    private boolean dragging;

    public AbstractSpruceParentWidget(@NotNull Position position, Class<E> childClass) {
        super(position);
        this.childClass = childClass;
    }

    @Override
    public boolean isDragging() {
        return this.dragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Override
    public abstract List<E> children();

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused) {
            this.setFocused(null);
        }
    }

    @Override
    public @Nullable E getFocused() {
        return this.focused;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setFocused(@Nullable Element focused) {
        if (this.focused == focused)
            return;
        if (this.focused != null)
            this.focused.setFocused(false);
        if (focused == null)
            this.focused = null;
        else if (this.childClass.isInstance(focused)) {
            this.focused = (E) focused;
        }
    }

    protected void setOwnerShip(E child) {
        child.getPosition().setAnchor(this);
    }

    /* Navigation */

    @Override
    public boolean onNavigation(@NotNull NavigationDirection direction, boolean tab) {
        if (this.requiresCursor()) return false;
        boolean result = NavigationUtils.tryNavigate(direction, tab, this.children(), this.focused, this::setFocused, false);
        if (result)
            this.setFocused(true);
        return result;
    }

    /* Input */

    @Override
    protected boolean onMouseClick(double mouseX, double mouseY, int button) {
        Iterator<E> it = this.children().iterator();

        E element;
        do {
            if (!it.hasNext()) {
                return false;
            }

            element = it.next();
        } while (!element.mouseClicked(mouseX, mouseY, button));

        this.setFocused(element);
        if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
            this.setDragging(true);
        }

        return true;
    }

    @Override
    protected boolean onMouseRelease(double mouseX, double mouseY, int button) {
        this.setDragging(false);
        return this.hoveredElement(mouseX, mouseY).filter(element -> element.mouseReleased(mouseX, mouseY, button)).isPresent();
    }

    @Override
    protected boolean onMouseDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return this.getFocused() != null && this.isDragging() && button == GLFW.GLFW_MOUSE_BUTTON_1
                && this.getFocused().mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    protected boolean onMouseScroll(double mouseX, double mouseY, double amount) {
        return this.hoveredElement(mouseX, mouseY).filter(element -> element.mouseScrolled(mouseX, mouseY, amount)).isPresent();
    }

    @Override
    protected boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        return this.getFocused() != null && this.getFocused().keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected boolean onKeyRelease(int keyCode, int scanCode, int modifiers) {
        return this.getFocused() != null && this.getFocused().keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    protected boolean onCharTyped(char chr, int keyCode) {
        return this.getFocused() != null && this.getFocused().charTyped(chr, keyCode);
    }
}
