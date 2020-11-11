package me.lambdaurora.spruceui;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSpruceWidget extends DrawableHelper implements SpruceWidget
{
    protected final Position position;
    private boolean visible;
    protected int width;
    protected int height;
    protected boolean focused = false;
    protected boolean hovered = false;

    protected AbstractSpruceWidget(@NotNull Position position)
    {
        this.position = position;
        this.visible = true;
    }

    @Override
    public @NotNull Position getPosition()
    {
        return this.position;
    }

    @Override
    public boolean isVisible()
    {
        return this.visible;
    }

    @Override
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    @Override
    public int getWidth()
    {
        return this.width;
    }

    @Override
    public int getHeight()
    {
        return this.height;
    }

    @Override
    public boolean isFocused()
    {
        return this.focused;
    }

    protected void setFocused(boolean focused)
    {
        this.focused = focused;
    }

    @Override
    public boolean changeFocus(boolean lookForwards)
    {
        return this.onNavigation(lookForwards ? NavigationDirection.DOWN : NavigationDirection.UP);
    }

    @Override
    public boolean onNavigation(@NotNull NavigationDirection direction)
    {
        if (this.isVisible()) {
            this.focused = !this.focused;
            return this.focused;
        }
        return false;
    }

    @Override
    public boolean isMouseHovered()
    {
        return this.hovered;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        if (this.isVisible()) {
            this.hovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight();

            this.renderWidget(matrices, mouseX, mouseY, delta);
        } else {
            this.hovered = false;
        }
    }

    public abstract void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta);
}
