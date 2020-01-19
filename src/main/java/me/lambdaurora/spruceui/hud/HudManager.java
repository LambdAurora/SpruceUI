/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.hud;

import me.lambdaurora.spruceui.event.ResolutionChangedCallback;
import me.lambdaurora.spruceui.event.OpenScreenCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import org.aperlambda.lambdacommon.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Represents the HUD manager.
 *
 * @author LambdAurora
 * @version 1.2.0
 * @since 1.2.0
 */
public class HudManager
{
    private static final HashMap<Identifier, Hud> HUDS = new HashMap<>();

    public void initialize()
    {
        HudRenderCallback.EVENT.register(tick_delta -> HUDS.forEach((id, hud) -> {
            if (hud.is_enabled())
                hud.render(tick_delta);
        }));
        ClientTickCallback.EVENT.register(client -> {
            if (!can_render_huds(client))
                return;
            HUDS.forEach((id, hud) -> {
                if (hud.is_enabled() && hud.has_ticks())
                    hud.tick();
            });
        });
        OpenScreenCallback.EVENT.register((client, screen) -> init_all(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight()));
        ResolutionChangedCallback.EVENT.register(client -> init_all(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight()));
    }

    protected static void init_all(@NotNull MinecraftClient client, int screen_width, int screen_height)
    {
        if (!can_render_huds(client))
            return;
        HUDS.forEach((id, hud) -> {
            if (hud.is_enabled())
                hud.init(client, screen_width, screen_height);
        });
    }

    /**
     * Registers a HUD.
     *
     * @param hud The HUD to register.
     */
    public static void register(@NotNull Hud hud)
    {
        if (HUDS.containsKey(hud.get_identifier()))
            throw new IllegalArgumentException("Cannot register the same HUD twice!");
        HUDS.put(hud.get_identifier(), hud);
    }

    /**
     * Unregisters the specified HUD by its identifier.
     *
     * @param identifier The HUD to unregister
     */
    public static void unregister(@NotNull Identifier identifier)
    {
        HUDS.remove(identifier);
    }

    /**
     * Unregisters the specified HUD.
     *
     * @param hud The HUD to unregister.
     */
    public static void unregister(@NotNull Hud hud)
    {
        unregister(hud.get_identifier());
    }

    /**
     * Returns whether the HUDs can be rendered or not.
     *
     * @param client The client instance.
     * @return True if the HUDs can be rendered, else false.
     */
    public static boolean can_render_huds(@NotNull MinecraftClient client)
    {
        return client.world != null && (!client.options.hudHidden || client.currentScreen != null);
    }
}
