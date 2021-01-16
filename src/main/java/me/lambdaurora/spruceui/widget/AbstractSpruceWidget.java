/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.widget;

import me.lambdaurora.spruceui.Position;
import me.lambdaurora.spruceui.navigation.NavigationDirection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents a widget.
 *
 * @author LambdAurora
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class AbstractSpruceWidget extends DrawableHelper implements SpruceWidget {
    protected final MinecraftClient client = MinecraftClient.getInstance();
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
    private long nextNarration = 0;

    public AbstractSpruceWidget(@NotNull Position position) {
        this.position = position;
        this.visible = true;
    }

    @Override
    public @NotNull Position getPosition() {
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
        this.dragging = false;
    }

    /* Navigation */

    @Override
    public boolean changeFocus(boolean lookForwards) {
        return this.onNavigation(lookForwards ? NavigationDirection.DOWN : NavigationDirection.UP, true);
    }

    @Override
    public boolean onNavigation(@NotNull NavigationDirection direction, boolean tab) {
        if (this.requiresCursor()) return false;
        if (this.isVisible() && this.isActive()) {
            this.setFocused(!this.isFocused());
            if (this.isFocused()) {
                this.queueNarration(200);
            }
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
            this.lastDrag = Util.getMeasuringTimeMs();
        }
        return result;
    }

    protected boolean onMouseDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.isActive() && this.isVisible() && this.isMouseOver(mouseX, mouseY)) {
            return this.onMouseScroll(mouseX, mouseY, amount);
        }
        return false;
    }

    protected boolean onMouseScroll(double mouseX, double mouseY, double amount) {
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
     * @param modifiers a GLFW bitfield describing the modifier keys that are held down (see <a href="https://www.glfw.org/docs/3.3/group__mods.html">GLFW Modifier key flags</a>})
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
     * @param modifiers a GLFW bitfield describing the modifier keys that are held down (see <a href="https://www.glfw.org/docs/3.3/group__mods.html">GLFW Modifier key flags</a>)
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
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.isVisible()) {
            this.hovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight();

            if (this.wasHovered != this.isMouseHovered()) {
                if (this.isMouseHovered()) {
                    if (this.isFocused())
                        this.queueNarration(200);
                    else
                        this.queueNarration(750);
                } else
                    this.nextNarration = Long.MAX_VALUE;
            }

            if (this.dragging && !this.isMouseHovered()) {
                if (Util.getMeasuringTimeMs() - this.lastDrag > 60) {
                    this.dragging = false;
                }
            }

            this.renderBackground(matrices, mouseX, mouseY, delta);
            this.renderWidget(matrices, mouseX, mouseY, delta);

            this.narrate();
            this.wasHovered = this.isMouseHovered();
        } else {
            this.hovered = this.wasHovered = false;
        }
    }

    /**
     * Renders the widget.
     *
     * @param matrices the matrix stack
     * @param mouseX the mouse X-coordinate
     * @param mouseY the mouse Y-coordinate
     * @param delta the tick delta
     */
    protected abstract void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta);

    /**
     * Renders the background of the widget.
     *
     * @param matrices the matrix stack
     * @param mouseX the mouse X-coordinate
     * @param mouseY the mouse Y-coordinate
     * @param delta the tick delta
     */
    protected void renderBackground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    }

    /* Sound */

    public void playDownSound() {
        this.client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.f));
    }

    /* Narration */

    /**
     * Narrates the narration message if queued and the delay is passed.
     */
    protected void narrate() {
        if (this.isActive() && this.isFocusedOrHovered() && Util.getMeasuringTimeMs() > this.nextNarration && this.nextNarration != 0) {
            this.getNarrationMessage().map(Text::getString).ifPresent(message -> {
                if (!message.isEmpty()) {
                    NarratorManager.INSTANCE.narrate(message);
                    this.nextNarration = Long.MAX_VALUE;
                }
            });
        }
    }

    /**
     * Returns the narration message.
     *
     * @return the narration message if present
     */
    protected @NotNull Optional<Text> getNarrationMessage() {
        return Optional.empty();
    }

    /**
     * Queues the next narration.
     *
     * @param delay the delay in milliseconds to wait for the next narration
     */
    public void queueNarration(int delay) {
        this.nextNarration = Util.getMeasuringTimeMs() + (long) delay;
    }
}
