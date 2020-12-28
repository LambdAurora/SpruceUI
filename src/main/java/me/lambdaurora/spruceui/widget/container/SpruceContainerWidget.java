/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.widget.container;

import me.lambdaurora.spruceui.Position;
import me.lambdaurora.spruceui.border.Border;
import me.lambdaurora.spruceui.border.EmptyBorder;
import me.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a container widget.
 *
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.7.0
 */
public class SpruceContainerWidget extends AbstractSpruceParentWidget<SpruceWidget> implements ParentElement {
    private final List<SpruceWidget> children = new ArrayList<>();
    private Border border = EmptyBorder.EMPTY_BORDER;

    public SpruceContainerWidget(Position position, int width, int height) {
        super(position, SpruceWidget.class);
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the border of this widget.
     *
     * @return the border
     */
    public @NotNull Border getBorder() {
        return this.border;
    }

    /**
     * Sets the border of this widget.
     *
     * @param border the border
     */
    public void setBorder(@NotNull Border border) {
        this.border = border;
    }

    public void addChild(@NotNull SpruceWidget child) {
        this.setOwnerShip(child);
        this.children.add(child);
    }

    public void addChildren(@NotNull ChildrenFactory childrenFactory) {
        childrenFactory.build(this.width, this.height, this::addChild);
    }

    @Override
    public List<SpruceWidget> children() {
        return this.children;
    }

    /* Rendering */

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.children.forEach(child -> child.render(matrices, mouseX, mouseY, delta));
        this.getBorder().render(this.client, this, this.getWidth(), this.getHeight());
    }

    public interface ChildrenFactory {
        void build(int containerWidth, int containerHeight, Consumer<SpruceWidget> widgetAdder);
    }
}
