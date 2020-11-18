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
import me.lambdaurora.spruceui.screen.SpruceScreen;
import me.lambdaurora.spruceui.widget.SpruceButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Represents a tabbed screen.
 *
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.7.0
 */
public class SpruceTabbedScreen extends SpruceScreen
{
    private final List<SpruceScreen> screens;
    private final List<SpruceButtonWidget> tabButtons = new ArrayList<>();
    private boolean footer = false;
    private int currentTab = 0;
    private int top = 45;
    private int bottom;

    protected SpruceTabbedScreen(@NotNull Text title, @NotNull Collection<SpruceScreen> screens)
    {
        super(title);
        this.screens = new ArrayList<>(screens);
        if (this.screens.size() == 0) {
            throw new IllegalArgumentException("There must be at least 1 screen in SpruceTabbedScreen.");
        }
    }

    public void setCurrentTab(int tab)
    {
        if (tab >= this.screens.size())
            tab = this.screens.size() - 1;
        this.currentTab = tab;
        this.screens.get(this.currentTab).init(this.client, this.width, this.height);
    }

    public @NotNull SpruceScreen getCurrentScreen()
    {
        return this.screens.get(this.currentTab);
    }

    @Override
    public void init(MinecraftClient client, int width, int height)
    {
        this.client = client;
        this.itemRenderer = client.getItemRenderer();
        this.textRenderer = client.textRenderer;
        this.width = width;
        this.height = height;
        this.buttons.clear();
        this.children.clear();
        this.setFocused(null);

        this.setCurrentTab(0);
        this.tabButtons.clear();

        int x = 30;
        for (int i = 0; i < this.screens.size(); i++) {
            SpruceScreen screen = this.screens.get(i);
            int buttonWidth = this.textRenderer.getWidth(screen.getTitle()) + 6;
            int tabIndex = i;
            this.tabButtons.add(new SpruceButtonWidget(Position.of(this, x, this.top - 25), buttonWidth, 20, screen.getTitle(),
                    btn -> this.setCurrentTab(tabIndex)));
            x += buttonWidth;
        }


        this.bottom = this.height - 29;

        this.init();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        Tooltip.setDelayedRender(true);
        SpruceScreen currentScreen = this.screens.get(this.currentTab);
        currentScreen.render(matrices, mouseX, mouseY, delta);
        Tooltip.setDelayedRender(false);

        // Render header.
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        this.client.getTextureManager().bindTexture(DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
        RenderSystem.color4f(1.f, 1.f, 1.f, 1.f);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(519);
        buffer.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
        buffer.vertex(0.0, this.top, -100.0D).texture(0.f, (float) this.top / 32.f).color(64, 64, 64, 255).next();
        buffer.vertex(this.width, this.top, -100.0D).texture((float) this.width / 32.f, (float) this.top / 32.f).color(64, 64, 64, 255).next();
        buffer.vertex(this.width, 0.0D, -100.0D).texture((float) this.width / 32.f, 0.f).color(64, 64, 64, 255).next();
        buffer.vertex(0.0, 0.0D, -100.0D).texture(0.f, 0.f).color(64, 64, 64, 255).next();
        if (this.footer) {
            buffer.vertex(0.0, this.height, -100.0D).texture(0.f, (float) this.height / 32.f).color(64, 64, 64, 255).next();
            buffer.vertex(this.width, this.height, -100.0D).texture((float) this.width / 32.f, (float) this.height / 32.f).color(64, 64, 64, 255).next();
            buffer.vertex(this.width, this.bottom, -100.0D).texture((float) this.width / 32.f, (float) this.bottom / 32.f).color(64, 64, 64, 255).next();
            buffer.vertex(0.0, this.bottom, -100.0D).texture(0.f, (float) this.bottom / 32.f).color(64, 64, 64, 255).next();
        }
        tessellator.draw();
        RenderSystem.depthFunc(515);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
        RenderSystem.disableAlphaTest();
        RenderSystem.shadeModel(7425);
        RenderSystem.disableTexture();
        buffer.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
        buffer.vertex(0.0, this.top + 4, 0.0D).texture(0.f, 1.f).color(0, 0, 0, 0).next();
        buffer.vertex(this.width, this.top + 4, 0.0D).texture(1.f, 1.f).color(0, 0, 0, 0).next();
        buffer.vertex(this.width, this.top, 0.0D).texture(1.f, 0.f).color(0, 0, 0, 255).next();
        buffer.vertex(0.0, this.top, 0.0D).texture(0.f, 0.f).color(0, 0, 0, 255).next();
        if (this.footer) {
            buffer.vertex(0.0, this.bottom, 0.0D).texture(0.f, 1.f).color(0, 0, 0, 255).next();
            buffer.vertex(this.width, this.bottom, 0.0D).texture(1.f, 1.f).color(0, 0, 0, 255).next();
            buffer.vertex(this.width, this.bottom - 4, 0.0D).texture(1.f, 0.f).color(0, 0, 0, 0).next();
            buffer.vertex(0.0, this.bottom - 4, 0.0D).texture(0.f, 0.f).color(0, 0, 0, 0).next();
        }
        tessellator.draw();

        // Draw the title text.
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
        // Draw tab buttons.
        for (SpruceButtonWidget tabButton : this.tabButtons) {
            tabButton.render(matrices, mouseX, mouseY, delta);
        }
        // Render all the tooltips.
        Tooltip.renderAll(this, matrices);
    }

    @Override
    public Optional<Element> hoveredElement(double mouseX, double mouseY)
    {
        Iterator<SpruceButtonWidget> iterator = this.tabButtons.iterator();

        SpruceButtonWidget element;
        do {
            if (!iterator.hasNext()) {
                return this.getCurrentScreen().hoveredElement(mouseX, mouseY);
            }

            element = iterator.next();
        } while (!element.isMouseOver(mouseX, mouseY));

        return Optional.of(element);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        boolean result = false;
        for (SpruceButtonWidget btn : this.tabButtons) {
            if (btn.isMouseOver(mouseX, mouseY)) {
                if (btn.mouseClicked(mouseX, mouseY, button)) {
                    result = true;
                    break;
                }
            }
        }
        return result || this.getCurrentScreen().mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY)
    {
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
                || this.getCurrentScreen().mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount)
    {
        return super.mouseScrolled(mouseX, mouseY, amount)
                || this.getCurrentScreen().mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        return super.keyPressed(keyCode, scanCode, modifiers)
                || this.getCurrentScreen().keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers)
    {
        return super.keyReleased(keyCode, scanCode, modifiers)
                || this.getCurrentScreen().keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int keyCode)
    {
        return super.charTyped(chr, keyCode)
                || this.getCurrentScreen().charTyped(chr, keyCode);
    }
}
