/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.hud;

import me.lambdaurora.spruceui.event.HudRenderCallback;
import me.lambdaurora.spruceui.event.OpenScreenCallback;
import me.lambdaurora.spruceui.event.ResolutionChangedCallback;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import org.aperlambda.lambdacommon.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Represents the HUD manager.
 *
 * @author LambdAurora
 * @version 1.3.4
 * @since 1.2.0
 */
public class HudManager
{
    private static final HashMap<Identifier, Hud> HUDS = new HashMap<>();

    public void initialize()
    {
        HudRenderCallback.EVENT.register(tickDelta -> HUDS.forEach((id, hud) -> {
            if (hud.isEnabled() && hud.isVisible())
                hud.render(tickDelta);
        }));
        ClientTickCallback.EVENT.register(client -> {
            if (!canRenderHuds(client))
                return;
            HUDS.forEach((id, hud) -> {
                if (hud.isEnabled() && hud.isVisible() && hud.hasTicks())
                    hud.tick();
            });
        });
        OpenScreenCallback.EVENT.register((client, screen) -> initAll(client, client.window.getScaledWidth(), client.window.getScaledHeight()));
        ResolutionChangedCallback.EVENT.register(client -> initAll(client, client.window.getScaledWidth(), client.window.getScaledHeight()));
    }

    protected static void initAll(@NotNull MinecraftClient client, int screenWidth, int screenHeight)
    {
        if (!canRenderHuds(client))
            return;
        HUDS.forEach((id, hud) -> {
            if (hud.isEnabled())
                hud.init(client, screenWidth, screenHeight);
        });
    }

    /**
     * Registers a HUD.
     *
     * @param hud The HUD to register.
     */
    public static void register(@NotNull Hud hud)
    {
        if (HUDS.containsKey(hud.getIdentifier()))
            throw new IllegalArgumentException("Cannot register the same HUD twice!");
        HUDS.put(hud.getIdentifier(), hud);
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
        unregister(hud.getIdentifier());
    }

    /**
     * Returns whether the HUDs can be rendered or not.
     *
     * @param client The client instance.
     * @return True if the HUDs can be rendered, else false.
     */
    public static boolean canRenderHuds(@NotNull MinecraftClient client)
    {
        return client.world != null && (!client.options.hudHidden || client.currentScreen != null);
    }
}
