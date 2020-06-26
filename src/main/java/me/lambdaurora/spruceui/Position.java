/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import java.util.Objects;

/**
 * Represents a position.
 *
 * @author LambdAurora
 * @version 1.4.0
 * @since 1.4.0
 */
public final class Position implements SprucePositioned
{
    public static final Position ORIGIN = new Position(new SprucePositioned()
    {
    });

    protected SprucePositioned anchor;
    protected int              x = 0;
    protected int              y = 0;

    protected Position(SprucePositioned anchor)
    {
        this.anchor = anchor;
    }

    public static Position of(SprucePositioned anchor, int x, int y)
    {
        return new Position(anchor).set(x, y);
    }

    public static Position of(int x, int y)
    {
        return of(ORIGIN, x, y);
    }

    /**
     * Returns the anchor.
     *
     * @return The anchor.
     */
    public SprucePositioned getAnchor()
    {
        return this.anchor;
    }

    @Override
    public int getX()
    {
        return this.anchor.getX() + this.x;
    }

    @Override
    public int getY()
    {
        return this.anchor.getY() + this.y;
    }

    public Position set(int x, int y)
    {
        this.setRelativeX(x);
        this.setRelativeY(y);
        return this;
    }

    /**
     * Gets the relative X of this position.
     *
     * @return The relative X.
     */
    public int getRelativeX()
    {
        return this.x;
    }

    /**
     * Sets the relative X of this position.
     *
     * @param x The relative X.
     */
    public void setRelativeX(int x)
    {
        this.x = x;
    }

    /**
     * Gets the relative Y of this position.
     *
     * @return The relative Y.
     */
    public int getRelativeY()
    {
        return this.y;
    }

    /**
     * Sets the relative Y of this position.
     *
     * @param y The relative Y.
     */
    public void setRelativeY(int y)
    {
        this.y = y;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return this.getX() == position.getX() && this.getY() == position.getY();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(anchor, x, y);
    }
}
