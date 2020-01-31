/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a button with an icon.
 *
 * @author LambdAurora
 * @version 1.3.1
 * @since 1.0.0
 */
public abstract class AbstractIconButtonWidget extends ButtonWidget
{
    private int iconSize = 0;

    public AbstractIconButtonWidget(int x, int y, int width, int height, @NotNull String message, @NotNull PressAction action)
    {
        super(x, y, width, height, message, action);
    }

    /**
     * Renders the icon.
     *
     * @param mouseX Mouse X.
     * @param mouseY Mouse Y.
     * @param delta  Delta.
     * @param x      X coordinates of the icon.
     * @param y      Y coordinates of the icon.
     * @return The size of the icon.
     */
    protected abstract int renderIcon(int mouseX, int mouseY, float delta, int x, int y);

    @Override
    public void renderButton(int mouseX, int mouseY, float delta)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        client.getTextureManager().bindTexture(WIDGETS_LOCATION);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        this.blit(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
        this.blit(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.renderBg(client, mouseX, mouseY);

        this.iconSize = this.renderIcon(mouseX, mouseY, delta, this.x + 4, this.y + (this.height / 2 - this.iconSize / 2));

        if (!this.getMessage().isEmpty()) {
            int j = this.active ? 16777215 : 10526880;
            this.drawCenteredString(client.textRenderer, this.getMessage(), this.x + 8 + this.iconSize + (this.width - 8 - this.iconSize - 6) / 2,
                    this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
        }
    }
}
