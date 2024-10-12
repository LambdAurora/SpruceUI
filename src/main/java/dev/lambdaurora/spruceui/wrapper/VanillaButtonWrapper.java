/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.wrapper;

import dev.lambdaurora.spruceui.navigation.NavigationDirection;
import dev.lambdaurora.spruceui.widget.AbstractSpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.SpruceElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;

/**
 * Represents a vanilla button wrapper for SpruceUI's own button widgets.
 *
 * @author LambdAurora
 * @version 5.0.0
 * @since 2.0.0
 */
@Environment(EnvType.CLIENT)
public class VanillaButtonWrapper extends AbstractWidget implements SpruceElement {
	private final AbstractSpruceButtonWidget widget;

	public VanillaButtonWrapper(AbstractSpruceButtonWidget widget) {
		super(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), widget.getMessage());
		this.widget = widget;
	}

	@Override
	public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		this.widget.getPosition().setRelativeY(this.getY());
		this.widget.render(graphics, mouseX, mouseY, delta);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return this.widget.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return this.widget.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		return this.widget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean onNavigation(NavigationDirection direction, boolean tab) {
		return this.widget.onNavigation(direction, tab);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return this.widget.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean keyReleased(final int keyCode, final int scanCode, final int modifiers) {
		return this.widget.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public NarrationPriority narrationPriority() {
		return this.widget.narrationPriority();
	}

	@Override
	public void updateWidgetNarration(NarrationElementOutput builder) {
		this.widget.updateNarration(builder);
	}
}
