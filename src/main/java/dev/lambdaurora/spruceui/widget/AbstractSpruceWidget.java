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
import dev.lambdaurora.spruceui.navigation.NavigationDirection;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Text;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a widget.
 *
 * @author LambdAurora
 * @version 5.0.0
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
	public @NotNull NarrationPriority narrationPriority() {
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
	public boolean onNavigation(NavigationDirection direction, boolean tab) {
		if (this.requiresCursor()) return false;
		if (this.isVisible() && this.isActive()) {
			this.setFocused(!this.isFocused());
			return this.isFocused();
		}
		return false;
	}

	/* Input */

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (!this.isActive() || !this.isVisible() || !this.isMouseOver(mouseX, mouseY))
			return false;

		return this.onMouseClick(mouseX, mouseY, button);
	}

	protected boolean onMouseClick(double mouseX, double mouseY, int button) {
		return false;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		boolean result = this.onMouseRelease(mouseX, mouseY, button);
		if (result) this.dragging = false;
		return result;
	}

	protected boolean onMouseRelease(double mouseX, double mouseY, int button) {
		return false;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (!this.isActive() || !this.isVisible())
			return false;

		boolean result = this.onMouseDrag(mouseX, mouseY, button, deltaX, deltaY);
		if (result) {
			this.dragging = true;
			this.lastDrag = Util.getMillis();
		}
		return result;
	}

	protected boolean onMouseDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
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
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (this.isActive() && this.isVisible()) {
			return this.onKeyPress(keyCode, scanCode, modifiers);
		}
		return false;
	}

	/**
	 * Handles the key press event.
	 *
	 * @param keyCode the named key code of the event as described in the {@link org.lwjgl.glfw.GLFW GLFW} class
	 * @param scanCode the unique/platform-specific scan code of the keyboard input
	 * @param modifiers a GLFW bitfield describing the modifier keys that are held down
	 * (see <a href="https://www.glfw.org/docs/3.3/group__mods.html">GLFW Modifier key flags</a>})
	 * @return {@code true} to indicate that the event handling is successful/valid, else {@code false}
	 */
	protected boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
		return false;
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (this.isActive() && this.isVisible()) {
			return this.onKeyRelease(keyCode, scanCode, modifiers);
		}
		return false;
	}

	/**
	 * Handles the key release event.
	 * <p>
	 * The key code is identified by the constants in {@link org.lwjgl.glfw.GLFW GLFW} class.
	 *
	 * @param keyCode the named key code of the event as described in the {@link org.lwjgl.glfw.GLFW GLFW} class
	 * @param scanCode the unique/platform-specific scan code of the keyboard input
	 * @param modifiers a GLFW bitfield describing the modifier keys that are held down
	 * (see <a href="https://www.glfw.org/docs/3.3/group__mods.html">GLFW Modifier key flags</a>)
	 * @return {@code true} to indicate that the event handling is successful/valid, else {@code false}
	 * @see org.lwjgl.glfw.GLFW#GLFW_KEY_Q
	 * @see org.lwjgl.glfw.GLFWKeyCallbackI#invoke(long, int, int, int, int)
	 */
	protected boolean onKeyRelease(int keyCode, int scanCode, int modifiers) {
		return false;
	}

	@Override
	public boolean charTyped(char chr, int keyCode) {
		if (this.isActive() && this.isVisible()) {
			return this.onCharTyped(chr, keyCode);
		}
		return false;
	}

	/**
	 * Handles a character input event.
	 * <p>
	 * The key code is identified by the constants in {@link org.lwjgl.glfw.GLFW GLFW} class.
	 *
	 * @param chr the captured character
	 * @param keyCode the associated key code
	 * @return {@code true} to indicate that the event handling is successful/valid, else {@code false}
	 */
	protected boolean onCharTyped(char chr, int keyCode) {
		return false;
	}

	/* Rendering */

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
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
	protected abstract void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta);

	/**
	 * Renders the background of the widget.
	 *
	 * @param graphics the GUI graphics instance to render with
	 * @param mouseX the mouse X-coordinate
	 * @param mouseY the mouse Y-coordinate
	 * @param delta the tick delta
	 */
	protected void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
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
	protected @Nullable Text getNarrationMessage() {
		return null;
	}
}
