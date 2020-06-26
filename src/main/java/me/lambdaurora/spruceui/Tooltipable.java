/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.lambdaurora.spruceui.accessor.DrawableHelperAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Represents an object which can show a tooltip.
 *
 * @author LambdAurora
 * @version 1.5.0
 * @since 1.0.0
 */
public interface Tooltipable
{
    /**
     * Gets the tooltip.
     *
     * @return The tooltip to show.
     */
    @NotNull Optional<Text> getTooltip();

    /**
     * Sets the tooltip.
     *
     * @param tooltip The tooltip to show.
     */
    void setTooltip(@Nullable Text tooltip);

    /**
     * Renders a tooltip.
     * <p>
     * X and Y tooltips are often mouse coordinates.
     *
     * @param client   The client instance.
     * @param matrices The matrices.
     * @param text     The tooltip text to render.
     * @param x        The X coordinate of the tooltip.
     * @param y        The Y coordinate of the tooltip.
     */
    static void render(@NotNull MinecraftClient client, @NotNull MatrixStack matrices, List<? extends StringRenderable> text, int x, int y)
    {
        if (!text.isEmpty()) {
            RenderSystem.disableRescaleNormal();
            RenderSystem.disableDepthTest();
            int i = 0;

            for (StringRenderable string : text) {
                int j = client.textRenderer.getWidth(string);
                if (j > i) {
                    i = j;
                }
            }

            int textX = x + 12;
            int textY = y - 12;
            int n = 8;
            if (text.size() > 1) {
                n += 2 + (text.size() - 1) * 10;
            }

            if (textX + i > client.getWindow().getScaledWidth()) {
                textX -= 28 + i;
            }

            if (textY + n + 6 > client.getWindow().getScaledHeight()) {
                textY = client.getWindow().getScaledHeight() - n - 6;
            }

            matrices.push();

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
            Matrix4f matrix4f = matrices.peek().getModel();
            DrawableHelperAccessor.spruceui_fillGradient(matrix4f, bufferBuilder, textX - 3, textY - 4, textX + i + 3, textY - 3, 400, -267386864, -267386864);
            DrawableHelperAccessor.spruceui_fillGradient(matrix4f, bufferBuilder, textX - 3, textY + n + 3, textX + i + 3, textY + n + 4, 400, -267386864, -267386864);
            DrawableHelperAccessor.spruceui_fillGradient(matrix4f, bufferBuilder, textX - 3, textY - 3, textX + i + 3, textY + n + 3, 400, -267386864, -267386864);
            DrawableHelperAccessor.spruceui_fillGradient(matrix4f, bufferBuilder, textX - 4, textY - 3, textX - 3, textY + n + 3, 400, -267386864, -267386864);
            DrawableHelperAccessor.spruceui_fillGradient(matrix4f, bufferBuilder, textX + i + 3, textY - 3, textX + i + 4, textY + n + 3, 400, -267386864, -267386864);
            DrawableHelperAccessor.spruceui_fillGradient(matrix4f, bufferBuilder, textX - 3, textY - 3 + 1, textX - 3 + 1, textY + n + 3 - 1, 400, 1347420415, 1344798847);
            DrawableHelperAccessor.spruceui_fillGradient(matrix4f, bufferBuilder, textX + i + 2, textY - 3 + 1, textX + i + 3, textY + n + 3 - 1, 400, 1347420415, 1344798847);
            DrawableHelperAccessor.spruceui_fillGradient(matrix4f, bufferBuilder, textX - 3, textY - 3, textX + i + 3, textY - 3 + 1, 400, 1347420415, 1347420415);
            DrawableHelperAccessor.spruceui_fillGradient(matrix4f, bufferBuilder, textX - 3, textY + n + 2, textX + i + 3, textY + n + 3, 400, 1344798847, 1344798847);
            RenderSystem.enableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.shadeModel(7425);
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            RenderSystem.shadeModel(7424);
            RenderSystem.disableBlend();
            RenderSystem.enableTexture();
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            matrices.translate(0.0D, 0.0D, 400);

            for (int lineIndex = 0; lineIndex < text.size(); ++lineIndex) {
                StringRenderable line = text.get(lineIndex);
                if (line != null) {
                    client.textRenderer.draw(line, (float) textX, (float) textY, -1, true, matrix4f, immediate, false, 0, 15728880);
                }

                if (lineIndex == 0) {
                    textY += 2;
                }

                textY += 10;
            }

            immediate.draw();
            matrices.pop();
        }
    }
}
