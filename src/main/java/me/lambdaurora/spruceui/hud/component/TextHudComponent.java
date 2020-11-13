/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.hud.component;

import me.lambdaurora.spruceui.hud.HudComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.aperlambda.lambdacommon.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a text HUD component.
 *
 * @author LambdAurora
 * @version 1.6.0
 * @since 1.3.5
 */
public class TextHudComponent extends HudComponent
{
    protected MinecraftClient client;
    protected Text text;
    protected int color;

    public TextHudComponent(@NotNull Identifier identifier, int x, int y, Text text)
    {
        this(identifier, x, y, text, 0xffffffff);
    }

    public TextHudComponent(@NotNull Identifier identifier, int x, int y, Text text, int color)
    {
        super(identifier, x, y);
        this.client = MinecraftClient.getInstance();
        this.text = text;
        this.color = color;
    }

    /**
     * Gets this component's text.
     *
     * @return The component's text.
     */
    public Text getText()
    {
        return this.text;
    }

    /**
     * Sets this component's text.
     *
     * @param text The text.
     */
    public void setText(Text text)
    {
        this.text = text;
    }

    /**
     * Gets this component's text color.
     *
     * @return The text color.
     */
    public int getColor()
    {
        return this.color;
    }

    /**
     * Sets this component's text color.
     *
     * @param color The text color.
     */
    public void setColor(int color)
    {
        this.color = color;
    }

    @Override
    public void render(MatrixStack matrices, float tickDelta)
    {
        DrawableHelper.drawTextWithShadow(matrices, client.textRenderer, this.text, this.x, this.y, this.color);
    }
}
