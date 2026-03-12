/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.screen;

import dev.lambdaurora.spruceui.SprucePositioned;
import dev.lambdaurora.spruceui.navigation.NavigationEvent;
import dev.lambdaurora.spruceui.tooltip.Tooltip;
import dev.lambdaurora.spruceui.widget.SpruceElement;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Represents a handled screen.
 *
 * @param <T> the type of the screen handler
 * @author LambdAurora
 * @version 9.0.0
 * @since 3.3.0
 */
public abstract class SpruceHandledScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements SprucePositioned, SpruceElement {
	public SpruceHandledScreen(T handler, Inventory inventory, Component title) {
		super(handler, inventory, title);
	}

	@Override
	public void setFocused(@Nullable GuiEventListener focused) {
		var old = this.getFocused();
		if (old == focused) return;
		if (old instanceof SpruceWidget)
			old.setFocused(false);
		super.setFocused(focused);
		if (focused instanceof SpruceWidget)
			focused.setFocused(true);
	}

	/* Input */

	@Override
	public boolean keyPressed(KeyEvent event) {
		return super.keyPressed(event) || NavigationEvent.fromKey(event.key(), event.hasShiftDown())
				.map(this::onNavigation)
				.orElse(false);
	}

	/* Navigation */

	@Override
	public boolean onNavigation(NavigationEvent event) {
		if (this.requiresCursor()) return false;
		var focused = this.getFocused();
		boolean isNonNull = focused != null;
		if (!isNonNull || !this.tryNavigating(focused, event)) {
			var children = this.children();
			int i = children.indexOf(focused);
			int next;
			if (isNonNull && i >= 0) next = i + (event.isLookingForward() ? 1 : 0);
			else if (event.isLookingForward()) next = 0;
			else next = children.size();

			var iterator = children.listIterator(next);
			BooleanSupplier hasNext = event.isLookingForward() ? iterator::hasNext : iterator::hasPrevious;
			Supplier<GuiEventListener> nextGetter = event.isLookingForward() ? iterator::next : iterator::previous;

			GuiEventListener nextElement;
			do {
				if (!hasNext.getAsBoolean()) {
					this.setFocused(null);
					return false;
				}

				nextElement = nextGetter.get();
			} while (!this.tryNavigating(nextElement, event));

			this.setFocused(nextElement);
		}
		return true;
	}

	private boolean tryNavigating(GuiEventListener element, NavigationEvent event) {
		if (element instanceof SpruceElement) {
			return ((SpruceElement) element).onNavigation(event);
		}
		element.setFocused(event.direction().isPositive());
		return true;
	}

	/* Render */

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		super.extractRenderState(graphics, mouseX, mouseY, delta);
		this.extractWidgets(graphics, mouseX, mouseY, delta);
		this.extractTitle(graphics, mouseX, mouseY, delta);
		Tooltip.extractAllRenderStates(graphics);
	}

	public void extractTitle(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
	}

	public void extractWidgets(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		for (var element : this.children()) {
			if (element instanceof Renderable drawable)
				drawable.extractRenderState(graphics, mouseX, mouseY, delta);
		}
	}
}
