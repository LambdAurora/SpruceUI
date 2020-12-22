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

public class SpruceContainerWidget extends AbstractSpruceParentWidget<SpruceWidget>
        implements ParentElement {
    private final List<SpruceWidget> children = new ArrayList<>();

    public SpruceContainerWidget(Position position) {
        super(position, SpruceWidget.class);
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.children.forEach(child -> child.render(matrices, mouseX, mouseY, delta));
    }

    public void addChild(@NotNull SpruceWidget child) {
        this.children.add(child);
    }

    @Override
    public List<SpruceWidget> children() {
        return this.children;
    }
}
