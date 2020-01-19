/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.hud;

import net.minecraft.client.gui.DrawableHelper;
import org.aperlambda.lambdacommon.Identifier;
import org.aperlambda.lambdacommon.utils.Identifiable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a HUD component.
 *
 * @author LambdAurora
 * @version 1.2.0
 * @since 1.2.0
 */
public abstract class HudComponent extends DrawableHelper implements Identifiable
{
    protected final Identifier identifier;
    protected       boolean    enabled = true;
    protected       int        x;
    protected       int        y;

    protected HudComponent(Identifier identifier, int x, int y)
    {
        this.identifier = identifier;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns whether the HUD component is enabled or not.
     *
     * @return True if the HUD component is enabled, else false.
     */
    public boolean is_enabled()
    {
        return this.enabled;
    }

    /**
     * Sets whether the HUD component is enabled or not.
     *
     * @param enabled True if the HUD component is enabled, else false.
     */
    public void set_enabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    /**
     * Renders the HUD component if enabled.
     *
     * @param tick_delta Progress for linearly interpolating between the previous and current game state.
     * @see #is_enabled()
     */
    public abstract void render(float tick_delta);

    /**
     * Updates the HUD each tick if enabled and has tick updates.
     *
     * @see #is_enabled()
     * @see #has_ticks()
     */
    public void tick()
    {
    }

    /**
     * Returns whether this HUD has tick updates.
     *
     * @return True if this HUD has tick updates, else false.
     * @see #tick()
     */
    public boolean has_ticks()
    {
        return false;
    }

    @Override
    public @NotNull Identifier get_identifier()
    {
        return this.identifier;
    }
}
