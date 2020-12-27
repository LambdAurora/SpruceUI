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
import me.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SpruceContainerWidget extends AbstractSpruceParentWidget<SpruceWidget>
        implements ParentElement {
    private final List<SpruceWidget> children = new ArrayList<>();

    public SpruceContainerWidget(Position position, int width, int height) {
        super(position, SpruceWidget.class);
        this.width = width;
        this.height = height;
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.children.forEach(child -> child.render(matrices, mouseX, mouseY, delta));
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

    public interface ChildrenFactory {
        void build(int containerWidth, int containerHeight, Consumer<SpruceWidget> widgetAdder);
    }
}
