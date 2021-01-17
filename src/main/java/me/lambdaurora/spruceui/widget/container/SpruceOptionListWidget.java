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
import me.lambdaurora.spruceui.navigation.NavigationDirection;
import me.lambdaurora.spruceui.navigation.NavigationUtils;
import me.lambdaurora.spruceui.option.SpruceOption;
import me.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Represents a list widget holding {@link SpruceOption} entries.
 * <p>
 * A {@link SpruceOption} allows to have an easy control over the widgets present in the list.
 *
 * @author LambdAurora
 * @version 2.0.4
 * @since 2.0.0
 */
public class SpruceOptionListWidget extends SpruceEntryListWidget<SpruceOptionListWidget.OptionEntry> {
    private int lastIndex = 0;

    public SpruceOptionListWidget(@NotNull Position position, int width, int height) {
        super(position, width, height, 4, OptionEntry.class);
    }

    /**
     * Adds a single option entry. The option will use all the width available.
     *
     * @param option the option
     * @return the index of the added entry
     */
    public int addSingleOptionEntry(SpruceOption option) {
        return this.addEntry(OptionEntry.create(this, option, false));
    }

    /**
     * Adds a single option entry. The option will center the element and will not use the full width.
     *
     * @param option the option
     * @return the index of the added entry
     */
    public int addSmallSingleOptionEntry(SpruceOption option) {
        return this.addEntry(OptionEntry.create(this, option, true));
    }

    /**
     * Adds two option as one entry of the list. The second option can be {@code null}.
     * <p>
     * If no second option is specified, the first option will not use the full width.
     *
     * @param firstOption the first option
     * @param secondOption the second option
     */
    public void addOptionEntry(SpruceOption firstOption, @Nullable SpruceOption secondOption) {
        this.addEntry(OptionEntry.create(this, firstOption, secondOption));
    }

    public void addAll(SpruceOption[] options) {
        for (int i = 0; i < options.length; i += 2) {
            this.addOptionEntry(options[i], i < options.length - 1 ? options[i + 1] : null);
        }
    }

    public static class OptionEntry extends SpruceEntryListWidget.Entry implements SpruceParentWidget<SpruceWidget> {
        private final List<SpruceWidget> children = new ArrayList<>();
        private final SpruceOptionListWidget parent;
        private @Nullable SpruceWidget focused;
        private boolean dragging;

        private OptionEntry(SpruceOptionListWidget parent) {
            this.parent = parent;
        }

        public static OptionEntry create(SpruceOptionListWidget parent, SpruceOption option, boolean small) {
            OptionEntry entry = new OptionEntry(parent);
            entry.children.add(option.createWidget(Position.of(entry, entry.getWidth() / 2 - (small ? 75 : 155), 2), small ? 150 : 310));
            return entry;
        }

        public static OptionEntry create(SpruceOptionListWidget parent, SpruceOption firstOption, @Nullable SpruceOption secondOption) {
            OptionEntry entry = new OptionEntry(parent);
            entry.children.add(firstOption.createWidget(Position.of(entry, entry.getWidth() / 2 - 155, 2), 150));
            if (secondOption != null) {
                entry.children.add(secondOption.createWidget(Position.of(entry, entry.getWidth() / 2 - 155 + 160, 2), 150));
            }
            return entry;
        }

        @Override
        public int getWidth() {
            return this.parent.getWidth() - (this.parent.getBorder().getThickness() * 2);
        }

        @Override
        public int getHeight() {
            return this.children.stream().mapToInt(SpruceWidget::getHeight).reduce(Integer::max).orElse(0) + 4;
        }

        @Override
        public List<SpruceWidget> children() {
            return this.children;
        }

        @Override
        public @Nullable SpruceWidget getFocused() {
            return this.focused;
        }

        @Override
        public void setFocused(@Nullable SpruceWidget focused) {
            if (this.focused == focused)
                return;
            if (this.focused != null)
                this.focused.setFocused(false);
            this.focused = focused;
        }

        @Override
        public void setFocused(boolean focused) {
            super.setFocused(focused);
            if (!focused) {
                this.setFocused(null);
            }
        }

        /* Input */

        @Override
        protected boolean onMouseClick(double mouseX, double mouseY, int button) {
            Iterator<SpruceWidget> it = this.children().iterator();

            SpruceWidget element;
            do {
                if (!it.hasNext()) {
                    return false;
                }

                element = it.next();
            } while (!element.mouseClicked(mouseX, mouseY, button));

            this.setFocused(element);
            if (button == GLFW.GLFW_MOUSE_BUTTON_1)
                this.dragging = true;

            return true;
        }

        @Override
        protected boolean onMouseRelease(double mouseX, double mouseY, int button) {
            this.dragging = false;
            return this.hoveredElement(mouseX, mouseY).filter(element -> element.mouseReleased(mouseX, mouseY, button)).isPresent();
        }

        @Override
        protected boolean onMouseDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            return this.getFocused() != null && this.dragging && button == GLFW.GLFW_MOUSE_BUTTON_1
                    && this.getFocused().mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }

        @Override
        protected boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
            return this.focused != null && this.focused.keyPressed(keyCode, scanCode, modifiers);
        }

        /* Rendering */

        @Override
        protected void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            this.children.forEach(widget -> widget.render(matrices, mouseX, mouseY, delta));
        }

        /* Navigation */

        @Override
        public boolean onNavigation(@NotNull NavigationDirection direction, boolean tab) {
            if (this.requiresCursor()) return false;
            if (!tab && direction.isVertical()) {
                if (this.isFocused()) {
                    this.setFocused(null);
                    return false;
                }
                int lastIndex = this.parent.lastIndex;
                if (lastIndex >= this.children.size())
                    lastIndex = this.children.size() - 1;
                if (!this.children.get(lastIndex).onNavigation(direction, tab))
                    return false;
                this.setFocused(this.children.get(lastIndex));
                return true;
            }

            boolean result = NavigationUtils.tryNavigate(direction, tab, this.children, this.focused, this::setFocused, true);
            if (result) {
                this.setFocused(true);
                if (direction.isHorizontal() && this.getFocused() != null) {
                    this.parent.lastIndex = this.children.indexOf(this.getFocused());
                }
            }
            return result;
        }
    }
}
