/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import me.lambdaurora.spruceui.navigation.NavigationDirection;
import me.lambdaurora.spruceui.screen.SpruceScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a tabbed container screen with the tab selection on one of the side.
 * <p>
 * Uses {@link SpruceSideTabbedWidget} but on the entire screen.
 *
 * @author LambdAurora
 * @version 1.7.0
 * @see SpruceSideTabbedWidget
 * @since 1.7.0
 */
public class SpruceSideTabbedScreen extends SpruceScreen {
    private final SideTabbedWidgetFactory factory;
    private SpruceSideTabbedWidget widget;

    protected SpruceSideTabbedScreen(@NotNull Text title, SideTabbedWidgetFactory factory) {
        super(title);
        this.factory = factory;
    }

    @Override
    protected void init() {
        super.init();
        this.widget = this.factory.create(0, 0, this.width, this.height);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.widget.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean onNavigation(@NotNull NavigationDirection direction, boolean tab) {
        return this.widget.onNavigation(direction, tab);
    }

    public interface SideTabbedWidgetFactory {
        SpruceSideTabbedWidget create(int x, int y, int width, int height);
    }
}
