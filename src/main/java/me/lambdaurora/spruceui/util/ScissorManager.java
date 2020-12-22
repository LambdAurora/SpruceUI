package me.lambdaurora.spruceui.util;

import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;
import java.util.Deque;

public final class ScissorManager {
    private static final Deque<ScissorHandle> SCISSOR_STACK = new ArrayDeque<>();

    private ScissorManager() {
        throw new UnsupportedOperationException("ScissorManager only contains static definitions.");
    }

    public static void push(int x, int y, int width, int height) {
        if (SCISSOR_STACK.isEmpty())
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
        ScissorHandle handle = new ScissorHandle(x, y, width, height);
        handle.apply();
        SCISSOR_STACK.push(handle);
    }

    public static void pop() {
        SCISSOR_STACK.pop();
        if (SCISSOR_STACK.isEmpty()) {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        } else {
            SCISSOR_STACK.getFirst().apply();
        }
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
