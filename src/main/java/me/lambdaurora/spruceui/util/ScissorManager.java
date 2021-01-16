/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.util;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Represents a manager for {@link GL11#glScissor(int, int, int, int)}.
 *
 * @author LambdAurora
 * @version 2.0.0
 * @since 2.0.0
 */
public final class ScissorManager {
    private static final Deque<ScissorHandle> SCISSOR_STACK = new ArrayDeque<>();
    private static final DoubleList SCALE_FACTOR_STACK = new DoubleArrayList();

    private ScissorManager() {
        throw new UnsupportedOperationException("ScissorManager only contains static definitions.");
    }

    public static void pushScaleFactor(double scaleFactor) {
        SCALE_FACTOR_STACK.add(scaleFactor);
    }

    public static void pushScaleFactorMultiplier(double scaleFactor) {
        pushScaleFactor(scaleFactor * getCurrentScaleFactor());
    }

    public static void popScaleFactor() {
        if (SCALE_FACTOR_STACK.size() == 0)
            return;
        SCALE_FACTOR_STACK.removeDouble(SCALE_FACTOR_STACK.size() - 1);
    }

    public static double getCurrentScaleFactor() {
        if (SCALE_FACTOR_STACK.size() == 0)
            return 1.f;
        return SCALE_FACTOR_STACK.getDouble(SCALE_FACTOR_STACK.size() - 1);
    }

    public static void push(int x, int y, int width, int height, double scaleFactor) {
        pushScaleFactor(scaleFactor);
        push(x, y, width, height);
    }

    /**
     * Pushes a new scissor state.
     *
     * @param x the X coordinate of the drawable area
     * @param y the Y coordinate of the drawable area
     * @param width the width of the drawable area
     * @param height the height of the drawable area
     */
    public static void push(int x, int y, int width, int height) {
        if (SCISSOR_STACK.isEmpty())
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
        double scaleFactor = getCurrentScaleFactor();
        ScissorHandle handle = new ScissorHandle((int) (scaleFactor * x), adaptY(y, height, scaleFactor),
                (int) (scaleFactor * width), (int) (scaleFactor * height));
        handle.apply();
        SCISSOR_STACK.push(handle);
    }

    /**
     * Pops the last pushed scissor state.
     */
    public static void pop() {
        SCISSOR_STACK.pop();
        if (SCISSOR_STACK.isEmpty()) {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        } else {
            SCISSOR_STACK.getFirst().apply();
        }
    }

    private static int adaptY(int y, int height, double scaleFactor) {
        Window window = MinecraftClient.getInstance().getWindow();
        int tmpHeight = (int) (window.getFramebufferHeight() / scaleFactor);
        int scaledHeight = window.getFramebufferHeight() / scaleFactor > (double) tmpHeight ? tmpHeight + 1 : tmpHeight;
        return (int) (scaleFactor * (scaledHeight - height - y));
    }

    static class ScissorHandle {
        int x;
        int y;
        int width;
        int height;

        public ScissorHandle(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        void apply() {
            GL11.glScissor(this.x, this.y, this.width, this.height);
        }
    }
}
