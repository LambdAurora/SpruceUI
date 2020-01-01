/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * Represents a textured button widget.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
public class SpruceTexturedButtonWidget extends TexturedButtonWidget
{
    private       boolean                           silent = false;
    private final BiConsumer<ButtonWidget, Boolean> on_change_state;

    public SpruceTexturedButtonWidget(int x, int y, int width, int height, int u, int v, int hovered_v_offset, Identifier texture, @NotNull BiConsumer<ButtonWidget, Boolean> on_changed_state)
    {
        super(x, y, width, height, u, v, hovered_v_offset, texture, 256, 256, btn -> on_changed_state.accept(btn, true));
        this.on_change_state = on_changed_state;
    }

    public SpruceTexturedButtonWidget(int x, int y, int width, int height, int u, int v, int hovered_v_offset, Identifier texture, @NotNull BiConsumer<ButtonWidget, Boolean> on_changed_state, boolean silent)
    {
        this(x, y, width, height, u, v, hovered_v_offset, texture, on_changed_state);
        this.silent = silent;
    }

    /**
     * Returns whether the button is silent or not.
     *
     * @return True if the button is silent, else false.
     */
    public boolean is_silent()
    {
        return this.silent;
    }

    /**
     * Sets whether the button is silent or not.
     *
     * @param silent True if the button is silent, else false.
     */
    public void set_silent(boolean silent)
    {
        this.silent = silent;
    }

    @Override
    public void onRelease(double mouseX, double mouseY)
    {
        super.onRelease(mouseX, mouseY);
        this.on_change_state.accept(this, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (this.active && this.visible && this.isValidClickButton(button)) {
            boolean clicked = this.clicked(mouseX, mouseY);
            if (clicked) {
                if (!this.silent)
                    this.playDownSound(MinecraftClient.getInstance().getSoundManager());
                this.onClick(mouseX, mouseY);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY)
    {
        super.onDrag(mouseX, mouseY, deltaX, deltaY);
        if (this.active && !this.isHovered)
            this.on_change_state.accept(this, false);
    }
}
