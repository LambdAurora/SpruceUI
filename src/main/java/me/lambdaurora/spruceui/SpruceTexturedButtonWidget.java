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
 * @version 1.3.0
 * @since 1.0.0
 */
public class SpruceTexturedButtonWidget extends TexturedButtonWidget
{
    private       boolean                           silent = false;
    private final BiConsumer<ButtonWidget, Boolean> onChangeState;

    public SpruceTexturedButtonWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, @NotNull BiConsumer<ButtonWidget, Boolean> onChangedState)
    {
        super(x, y, width, height, u, v, hoveredVOffset, texture, 256, 256, btn -> onChangedState.accept(btn, true));
        this.onChangeState = onChangedState;
    }

    public SpruceTexturedButtonWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, @NotNull BiConsumer<ButtonWidget, Boolean> onChangedState, boolean silent)
    {
        this(x, y, width, height, u, v, hoveredVOffset, texture, onChangedState);
        this.silent = silent;
    }

    /**
     * Returns whether the button is silent or not.
     *
     * @return True if the button is silent, else false.
     */
    public boolean isSilent()
    {
        return this.silent;
    }

    /**
     * Sets whether the button is silent or not.
     *
     * @param silent True if the button is silent, else false.
     */
    public void setSilent(boolean silent)
    {
        this.silent = silent;
    }

    @Override
    public void onRelease(double mouseX, double mouseY)
    {
        super.onRelease(mouseX, mouseY);
        this.onChangeState.accept(this, false);
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
            this.onChangeState.accept(this, false);
    }
}
