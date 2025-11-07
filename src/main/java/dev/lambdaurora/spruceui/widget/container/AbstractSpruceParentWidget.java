/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget.container;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.navigation.NavigationEvent;
import dev.lambdaurora.spruceui.navigation.NavigationUtils;
import dev.lambdaurora.spruceui.widget.AbstractSpruceWidget;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

/**
 * Represents a parent widget, contains children which are other widgets.
 *
 * @param <E> the type of children widgets
 * @author LambdAurora
 * @version 9.0.0
 * @since 2.0.0
 */
public abstract class AbstractSpruceParentWidget<E extends SpruceWidget> extends AbstractSpruceWidget implements SpruceParentWidget<E> {
	private final Class<E> childClass;
	private @Nullable E focused;

	public AbstractSpruceParentWidget(Position position, Class<E> childClass) {
		super(position);
		this.childClass = childClass;
	}

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
	public void setFocused(@Nullable E focused) {
		if (this.focused == focused)
			return;
		if (this.focused != null)
			this.focused.setFocused(false);
		if (focused == null)
			this.focused = null;
		else if (this.childClass.isInstance(focused)) {
			this.focused = focused;
			this.focused.setFocused(true);
		}
	}

	protected void setOwnerShip(E child) {
		child.getPosition().setAnchor(this);
	}

	/* Navigation */

	@Override
	public boolean onNavigation(NavigationEvent event) {
		if (this.requiresCursor()) return false;
		boolean result = NavigationUtils.tryNavigate(event, this.children(), this.focused, this::setFocused, false);
		if (result)
			this.setFocused(true);
		return result;
	}

	/* Input */

	@Override
	protected boolean onMouseClick(MouseButtonEvent event, boolean doubleClick) {
		var it = this.iterator();

		E element;
		do {
			if (!it.hasNext()) {
				return false;
			}

			element = it.next();
		} while (!element.mouseClicked(event, doubleClick));

		this.setFocused(element);
		if (event.button() == GLFW.GLFW_MOUSE_BUTTON_1) {
			this.setDragging(true);
		}

		return true;
	}

	@Override
	protected boolean onMouseRelease(MouseButtonEvent event) {
		this.setDragging(false);
		return this.hoveredElement(event.x(), event.y())
				.filter(element -> element.mouseReleased(event))
				.isPresent();
	}

	@Override
	protected boolean onMouseDrag(MouseButtonEvent event, double deltaX, double deltaY) {
		return this.getFocused() != null && this.isDragging() && event.button() == GLFW.GLFW_MOUSE_BUTTON_1
				&& this.getFocused().mouseDragged(event, deltaX, deltaY);
	}

	@Override
	protected boolean onMouseScroll(double mouseX, double mouseY, double scrollX, double scrollY) {
		return this.hoveredElement(mouseX, mouseY).filter(element -> element.mouseScrolled(mouseX, mouseY, scrollX, scrollY)).isPresent();
	}

	@Override
	protected boolean onKeyPress(KeyEvent event) {
		return this.getFocused() != null && this.getFocused().keyPressed(event);
	}

	@Override
	protected boolean onKeyRelease(KeyEvent event) {
		return this.getFocused() != null && this.getFocused().keyReleased(event);
	}

	@Override
	protected boolean onCharTyped(CharacterEvent event) {
		return this.getFocused() != null && this.getFocused().charTyped(event);
	}
}
