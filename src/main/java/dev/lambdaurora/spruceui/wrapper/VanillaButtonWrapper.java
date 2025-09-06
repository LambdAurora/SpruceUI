/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.wrapper;

import dev.lambdaurora.spruceui.navigation.NavigationEvent;
import dev.lambdaurora.spruceui.widget.AbstractSpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.SpruceElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a vanilla button wrapper for SpruceUI's own button widgets.
 *
 * @author LambdAurora
 * @version 9.0.0
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
	public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean doubleClick) {
		return this.widget.mouseClicked(event, doubleClick);
	}

	@Override
	public boolean mouseReleased(@NotNull MouseButtonEvent event) {
		return this.widget.mouseReleased(event);
	}

	@Override
	public boolean mouseDragged(@NotNull MouseButtonEvent event, double deltaX, double deltaY) {
		return this.widget.mouseDragged(event, deltaX, deltaY);
	}

	@Override
	public boolean onNavigation(@NotNull NavigationEvent event) {
		return this.widget.onNavigation(event);
	}

	@Override
	public boolean keyPressed(@NotNull KeyEvent event) {
		return this.widget.keyPressed(event);
	}

	@Override
	public boolean keyReleased(@NotNull KeyEvent event) {
		return this.widget.keyReleased(event);
	}

	@Override
	public @NotNull NarrationPriority narrationPriority() {
		return this.widget.narrationPriority();
	}

	@Override
	public void updateWidgetNarration(NarrationElementOutput builder) {
		this.widget.updateNarration(builder);
	}
}
