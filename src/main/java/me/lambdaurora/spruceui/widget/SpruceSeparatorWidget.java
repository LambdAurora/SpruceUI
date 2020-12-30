/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.widget;

import me.lambdaurora.spruceui.Position;
import me.lambdaurora.spruceui.Tooltip;
import me.lambdaurora.spruceui.Tooltipable;
import me.lambdaurora.spruceui.navigation.NavigationDirection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a separator element.
 *
 * @author LambdAurora
 * @version 1.7.0
 * @since 1.0.1
 */
public class SpruceSeparatorWidget extends AbstractSpruceWidget implements Tooltipable {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private Text title;
    private Text tooltip;
    private int tooltipTicks;
    private long lastTick;

    public SpruceSeparatorWidget(Position position, int width, @Nullable Text title) {
        super(position);
        this.width = width;
        this.height = 9;
        this.title = title;
    }

    @Deprecated
    public SpruceSeparatorWidget(@Nullable Text title, int x, int y, int width) {
        this(Position.of(x, y), width, title);
    }

    /**
     * Gets the title of this separator widget.
     *
     * @return the title
     */
    public @NotNull Optional<Text> getTitle() {
        return Optional.ofNullable(this.title);
    }

    /**
     * Sets the title of this separator widget.
     *
     * @param title the title
     */
    public void setTitle(@Nullable Text title) {
        if (!Objects.equals(title, this.title)) {
            this.queueNarration(250);
        }
        this.title = title;
    }

    @Override
    public @NotNull Optional<Text> getTooltip() {
        return Optional.ofNullable(this.tooltip);
    }

    @Override
    public void setTooltip(@Nullable Text tooltip) {
        this.tooltip = tooltip;
    }

    /* Navigation */

    @Override
    public boolean requiresCursor() {
        return this.tooltip == null;
    }

    /* Rendering */

    @Override
    protected void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.title != null) {
            int titleWidth = this.client.textRenderer.getWidth(this.title);
            int titleX = this.getX() + (this.width / 2 - titleWidth / 2);
            if (this.width > titleWidth) {
                fill(matrices, this.getX(), this.getY() + 4, titleX - 5, this.getY() + 6, 0xffe0e0e0);
                fill(matrices, titleX + titleWidth + 5, this.getY() + 4, this.getX() + this.width, this.getY() + 6, 0xffe0e0e0);
            }
            DrawableHelper.drawTextWithShadow(matrices, this.client.textRenderer, this.title, titleX, this.getY(), 0xffffff);
        } else {
            fill(matrices, this.getX(), this.getY() + 4, this.getX() + this.width, this.getY() + 6, 0xffe0e0e0);
        }

        Tooltip.queueFor(this, mouseX, mouseY, this.tooltipTicks, i -> this.tooltipTicks = i, this.lastTick, i -> this.lastTick = i);
    }

    /* Narration */

    @Override
    protected @NotNull Optional<Text> getNarrationMessage() {
        return this.getTitle().map(Text::asString)
                .filter(title -> !title.isEmpty())
                .map(title -> new TranslatableText("spruceui.narrator.separator", title));
    }

    /**
     * Represents a button wrapper for the option.
     *
     * @author LambdAurora
     * @version 1.5.0
     * @since 1.0.1
     */
    public static class ButtonWrapper extends AbstractButtonWidget {
        private final SpruceSeparatorWidget widget;

        public ButtonWrapper(@NotNull SpruceSeparatorWidget separator, int height) {
            super(separator.getX(), separator.getY(), separator.getWidth(), height, separator.getTitle().orElse(LiteralText.EMPTY));
            this.widget = separator;
        }

        @Override
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            this.widget.getPosition().setRelativeY(this.y + this.height / 2 - 9 / 2);
            this.widget.render(matrices, mouseX, mouseY, delta);
        }

        @Override
        public boolean changeFocus(boolean down) {
            return this.widget.onNavigation(down ? NavigationDirection.DOWN : NavigationDirection.UP, true);
        }
    }
}
