/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.widget;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.navigation.NavigationEvent;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

/**
 * Represents a widget.
 *
 * @author LambdAurora
 * @version 9.0.0
 * @since 2.0.0
 */
public abstract class AbstractSpruceWidget implements SpruceWidget {
	protected final Minecraft client = Minecraft.getInstance();
	protected final Position position;
	private boolean visible;
	protected int width;
	protected int height;
	protected boolean active = true;
	protected boolean focused = false;
	protected boolean hovered = false;
	protected boolean wasHovered = false;
	protected boolean dragging = false;
	protected long lastDrag = 0L;

	public AbstractSpruceWidget(Position position) {
		this.position = position;
		this.visible = true;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public NarrationPriority narrationPriority() {
		if (this.focused) return NarrationPriority.FOCUSED;
		else if (this.hovered) return NarrationPriority.HOVERED;
		else return NarrationPriority.NONE;
	}

	@Override
	public boolean isMouseHovered() {
		return this.hovered;
	}

	@Override
	public boolean isFocused() {
		return this.focused;
	}

	@Override
	public void setFocused(boolean focused) {
		this.focused = focused;
		if (!focused) {
			this.dragging = false;
		}
	}

	@Override
	public boolean isDragging() {
		return this.dragging;
	}

	@Override
	public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}

	/* Navigation */

	@Override
	public boolean onNavigation(NavigationEvent event) {
		if (this.requiresCursor()) return false;
		if (this.isVisible() && this.isActive()) {
			this.setFocused(!this.isFocused());
			return this.isFocused();
		}
		return false;
	}

	/* Input */

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if (!this.isActive() || !this.isVisible() || !this.isMouseOver(event.x(), event.y()))
			return false;

		return this.onMouseClick(event, doubleClick);
	}

	protected boolean onMouseClick(MouseButtonEvent event, boolean doubleClick) {
		return false;
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		boolean result = this.onMouseRelease(event);
		if (result) this.dragging = false;
		return result;
	}

	protected boolean onMouseRelease(MouseButtonEvent event) {
		return false;
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
		if (!this.isActive() || !this.isVisible())
			return false;

		boolean result = this.onMouseDrag(event, deltaX, deltaY);
		if (result) {
			this.dragging = true;
			this.lastDrag = Util.getMillis();
		}
		return result;
	}

	protected boolean onMouseDrag(MouseButtonEvent event, double deltaX, double deltaY) {
		return false;
	}

	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		if (this.isActive() && this.isVisible() && this.isMouseOver(mouseX, mouseY)) {
			return this.onMouseScroll(mouseX, mouseY, scrollX, scrollY);
		}
		return false;
	}

	protected boolean onMouseScroll(double mouseX, double mouseY, double scrollX, double scrollY) {
		return false;
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (this.isActive() && this.isVisible()) {
			return this.onKeyPress(event);
		}
		return false;
	}

	/**
	 * Handles the key press event.
	 *
	 * @param event the key event
	 * @return {@code true} to indicate that the event handling is successful/valid, else {@code false}
	 */
	protected boolean onKeyPress(KeyEvent event) {
		return false;
	}

	@Override
	public boolean keyReleased(KeyEvent event) {
		if (this.isActive() && this.isVisible()) {
			return this.onKeyRelease(event);
		}
		return false;
	}

	/**
	 * Handles the key release event.
	 * <p>
	 * The key code is identified by the constants in {@link org.lwjgl.glfw.GLFW GLFW} class.
	 *
	 * @param event the key event
	 * @return {@code true} to indicate that the event handling is successful/valid, else {@code false}
	 * @see org.lwjgl.glfw.GLFW#GLFW_KEY_Q
	 * @see org.lwjgl.glfw.GLFWKeyCallbackI#invoke(long, int, int, int, int)
	 */
	protected boolean onKeyRelease(KeyEvent event) {
		return false;
	}

	@Override
	public boolean charTyped(CharacterEvent event) {
		if (this.isActive() && this.isVisible()) {
			return this.onCharTyped(event);
		}
		return false;
	}

	/**
	 * Handles a character input event.
	 * <p>
	 * The key code is identified by the constants in {@link org.lwjgl.glfw.GLFW GLFW} class.
	 *
	 * @param event the character input event
	 * @return {@code true} to indicate that the event handling is successful/valid, else {@code false}
	 */
	protected boolean onCharTyped(CharacterEvent event) {
		return false;
	}

	/* Rendering */

	@Override
	public final void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		this.render(new SpruceGuiGraphics(graphics), mouseX, mouseY, delta);
	}

	public void render(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
		if (this.isVisible()) {
			this.hovered = mouseX >= this.getX() && mouseY >= this.getY()
					&& mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight();

			if (this.dragging && !this.isMouseHovered()) {
				if (Util.getMillis() - this.lastDrag > 60) {
					this.dragging = false;
				}
			}

			this.renderBackground(graphics, mouseX, mouseY, delta);
			this.renderWidget(graphics, mouseX, mouseY, delta);

			this.wasHovered = this.isMouseHovered();
		} else {
			this.hovered = this.wasHovered = false;
		}
	}

	/**
	 * Renders the widget.
	 *
	 * @param graphics the GUI graphics instance to render with
	 * @param mouseX the mouse X-coordinate
	 * @param mouseY the mouse Y-coordinate
	 * @param delta the tick delta
	 */
	protected abstract void renderWidget(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta);

	/**
	 * Renders the background of the widget.
	 *
	 * @param graphics the GUI graphics instance to render with
	 * @param mouseX the mouse X-coordinate
	 * @param mouseY the mouse Y-coordinate
	 * @param delta the tick delta
	 */
	protected void renderBackground(SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
	}

	/* Sound */

	public void playDownSound() {
		this.client.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 1.f));
	}

	/* Narration */

	@Override
	public void updateNarration(NarrationElementOutput builder) {
		var narrationMessage = this.getNarrationMessage();
		if (narrationMessage != null)
			builder.add(NarratedElementType.TITLE, narrationMessage);
	}

	/**
	 * Returns the narration message.
	 *
	 * @return the narration message if present
	 */
	protected @Nullable Component getNarrationMessage() {
		return null;
	}
}
