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
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Represents an object which can show a tooltip.
 *
 * @author LambdAurora
 * @version 1.0.2
 * @since 1.0.0
 */
public interface Tooltipable
{
    /**
     * Gets the tooltip.
     *
     * @return The tooltip to show.
     */
    @NotNull Optional<Text> get_tooltip();

    /**
     * Sets the tooltip.
     *
     * @param tooltip The tooltip to show.
     */
    void set_tooltip(@Nullable Text tooltip);

    /**
     * Renders a tooltip.
     * <p>
     * X and Y tooltips are often mouse coordinates.
     *
     * @param client The client instance.
     * @param helper The draw helper.
     * @param text   The tooltip text to render.
     * @param x      The X coordinate of the tooltip.
     * @param y      The Y coordinate of the tooltip.
     */
    static void render(@NotNull MinecraftClient client, @NotNull DrawableHelper helper, List<String> text, int x, int y)
    {
        if (!text.isEmpty()) {
            DrawableHelperAccessor helper_accessor = (DrawableHelperAccessor) helper;

            RenderSystem.disableRescaleNormal();
            RenderSystem.disableDepthTest();
            int i = 0;

            for (String string : text) {
                int j = client.textRenderer.getStringWidth(string);
                if (j > i) {
                    i = j;
                }
            }

            int k = x + 12;
            int l = y - 12;
            int n = 8;
            if (text.size() > 1) {
                n += 2 + (text.size() - 1) * 10;
            }

            if (k + i > client.getWindow().getScaledWidth()) {
                k -= 28 + i;
            }

            if (l + n + 6 > client.getWindow().getScaledHeight()) {
                l = client.getWindow().getScaledHeight() - n - 6;
            }

            helper.setBlitOffset(300);
            client.getItemRenderer().zOffset = 300.0F;
            helper_accessor.spruceui_fill_gradient(k - 3, l - 4, k + i + 3, l - 3, -267386864, -267386864);
            helper_accessor.spruceui_fill_gradient(k - 3, l + n + 3, k + i + 3, l + n + 4, -267386864, -267386864);
            helper_accessor.spruceui_fill_gradient(k - 3, l - 3, k + i + 3, l + n + 3, -267386864, -267386864);
            helper_accessor.spruceui_fill_gradient(k - 4, l - 3, k - 3, l + n + 3, -267386864, -267386864);
            helper_accessor.spruceui_fill_gradient(k + i + 3, l - 3, k + i + 4, l + n + 3, -267386864, -267386864);
            helper_accessor.spruceui_fill_gradient(k - 3, l - 3 + 1, k - 3 + 1, l + n + 3 - 1, 1347420415, 1344798847);
            helper_accessor.spruceui_fill_gradient(k + i + 2, l - 3 + 1, k + i + 3, l + n + 3 - 1, 1347420415, 1344798847);
            helper_accessor.spruceui_fill_gradient(k - 3, l - 3, k + i + 3, l - 3 + 1, 1347420415, 1347420415);
            helper_accessor.spruceui_fill_gradient(k - 3, l + n + 2, k + i + 3, l + n + 3, 1344798847, 1344798847);
            MatrixStack matrix_stack = new MatrixStack();
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            matrix_stack.translate(0.0D, 0.0D, client.getItemRenderer().zOffset);
            Matrix4f matrix4f = matrix_stack.peek().getModel();

            for (int r = 0; r < text.size(); ++r) {
                String string2 = text.get(r);
                if (string2 != null) {
                    client.textRenderer.draw(string2, (float) k, (float) l, -1, true, matrix4f, immediate, false, 0, 15728880);
                }

                if (r == 0) {
                    l += 2;
                }

                l += 10;
            }

            immediate.draw();
            helper.setBlitOffset(0);
            client.getItemRenderer().zOffset = 0.0F;
            RenderSystem.enableDepthTest();
            RenderSystem.enableRescaleNormal();
        }
    }
}
