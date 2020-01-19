/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import org.aperlambda.lambdacommon.Identifier;
import org.aperlambda.lambdacommon.utils.Identifiable;
import org.aperlambda.lambdacommon.utils.function.Predicates;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a HUD.
 *
 * @author LambdAurora
 * @version 1.2.0
 * @since 1.2.0
 */
public abstract class Hud extends DrawableHelper implements Identifiable
{
    protected final Identifier         identifier;
    protected final List<HudComponent> components = new ArrayList<>();
    protected       boolean            enabled    = true;

    public Hud(@NotNull Identifier identifier)
    {
        this.identifier = identifier;
    }

    public Hud(@NotNull net.minecraft.util.Identifier identifier)
    {
        this(new Identifier(identifier.toString()));
    }

    /**
     * Returns whether the HUD is enabled or not.
     *
     * @return True if the HUD is enabled, else false.
     */
    public boolean is_enabled()
    {
        return this.enabled;
    }

    /**
     * Sets whether the HUD is enabled or not.
     *
     * @param enabled True if the HUD is enabled, else false.
     */
    public void set_enabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void init(@NotNull MinecraftClient client, int screen_width, int screen_height)
    {
        this.components.clear();
    }

    /**
     * Renders the HUD if enabled.
     *
     * @param tick_delta Progress for linearly interpolating between the previous and current game state.
     * @see #is_enabled()
     */
    public void render(float tick_delta)
    {
        this.components.stream().filter(HudComponent::is_enabled).forEach(component -> component.render(tick_delta));
    }

    /**
     * Updates the HUD each tick if enabled and has tick updates.
     *
     * @see #is_enabled()
     * @see #has_ticks()
     */
    public void tick()
    {
        this.components.stream().filter(Predicates.and(HudComponent::has_ticks, HudComponent::is_enabled)).forEach(HudComponent::tick);
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
