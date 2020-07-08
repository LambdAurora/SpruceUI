/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.accessor;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Represents an accessor to the {@code net.minecraft.client.gui.DrawableHelper} class.
 *
 * @author LambdAurora
 * @version 1.5.0
 * @since 1.0.0
 */
@Mixin(DrawableHelper.class)
public interface DrawableHelperAccessor
{
    @Invoker("fillGradient")
    static void spruceui_fillGradient(Matrix4f matrix, BufferBuilder bufferBuilder, int top, int left, int right, int bottom, int i, int color1, int color2)
    {
        throw new IllegalStateException("Dummy method body invoked. A critical mixin failure has occured.");
    }
}
