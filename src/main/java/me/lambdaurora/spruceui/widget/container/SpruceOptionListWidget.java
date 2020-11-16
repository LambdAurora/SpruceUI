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
import me.lambdaurora.spruceui.option.SpruceOption;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SpruceOptionListWidget extends SpruceElementListWidget<SpruceOptionListWidget.OptionEntry>
{
    public SpruceOptionListWidget(@NotNull Position position)
    {
        super(position, OptionEntry.class);
    }

    public static class OptionEntry extends SpruceEntryListWidget.Entry<OptionEntry>
    {
        private final List<AbstractButtonWidget> widgets = new ArrayList<>();

        private OptionEntry()
        {
        }

        public static OptionEntry create(int width, SpruceOption option)
        {
            OptionEntry entry = new OptionEntry();
            entry.widgets.add(option.createWidget(Position.of(entry, width / 2 - 155, 0), 310));
            return entry;
        }

        public static OptionEntry create(int width, SpruceOption firstOption, @Nullable SpruceOption secondOption)
        {
            OptionEntry entry = new OptionEntry();
            entry.widgets.add(firstOption.createWidget(Position.of(entry, width / 2 - 155, 0), 150));
            if (secondOption != null) {
                entry.widgets.add(secondOption.createWidget(Position.of(entry, width / 2 - 155 + 160, 0), 150));
            }
            return entry;
        }

        @Override
        public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta)
        {
            this.widgets.forEach(widget -> widget.render(matrices, mouseX, mouseY, delta));
        }

        public List<? extends Element> children()
        {
            return this.widgets;
        }
    }
}
