/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.hud;

import dev.lambdaurora.spruceui.event.OpenScreenCallback;
import dev.lambdaurora.spruceui.event.ResolutionChangeCallback;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Represents the HUD manager.
 *
 * @author LambdAurora
 * @version 3.2.0
 * @since 1.2.0
 */
public class HudManager {
	private static final Map<Identifier, Hud> HUDS = new Object2ObjectOpenHashMap<>();

	public void initialize() {
		HudRenderCallback.EVENT.register((graphics, deltaTracker) -> HUDS.forEach((id, hud) -> {
			if (hud.isEnabled() && hud.isVisible())
				hud.render(graphics, deltaTracker);
		}));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (!canRenderHuds(client))
				return;
			HUDS.forEach((id, hud) -> {
				if (hud.isEnabled() && hud.isVisible() && hud.hasTicks())
					hud.tick();
			});
		});
		OpenScreenCallback.EVENT.register((client, screen) -> initAll(client, client.getWindow().getGuiScaledWidth(), client.getWindow().getGuiScaledHeight()));
		ResolutionChangeCallback.EVENT.register(client -> initAll(client, client.getWindow().getGuiScaledWidth(), client.getWindow().getGuiScaledHeight()));
	}

	protected static void initAll(@NotNull Minecraft client, int screenWidth, int screenHeight) {
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
	public static void register(@NotNull Hud hud) {
		if (HUDS.containsKey(hud.getIdentifier()))
			throw new IllegalArgumentException("Cannot register the same HUD twice!");
		HUDS.put(hud.getIdentifier(), hud);
	}

	/**
	 * Unregisters the specified HUD by its identifier.
	 *
	 * @param identifier The HUD to unregister
	 */
	public static void unregister(@NotNull Identifier identifier) {
		HUDS.remove(identifier);
	}

	/**
	 * Unregisters the specified HUD.
	 *
	 * @param hud The HUD to unregister.
	 */
	public static void unregister(@NotNull Hud hud) {
		unregister(hud.getIdentifier());
	}

	/**
	 * Returns whether the HUDs can be rendered or not.
	 *
	 * @param client The client instance.
	 * @return True if the HUDs can be rendered, else false.
	 */
	public static boolean canRenderHuds(@NotNull Minecraft client) {
		return client.level != null && (!client.options.hideGui || client.screen != null);
	}

	/**
	 * Returns the HUD from its identifier.
	 *
	 * @param identifier The identifier of the HUD.
	 * @return The optional HUD.
	 */
	public static Optional<Hud> getHud(@NotNull Identifier identifier) {
		return Optional.ofNullable(HUDS.get(identifier));
	}

	/**
	 * Returns a collection of the registered HUDs.
	 *
	 * @return The registered HUDs.
	 */
	public static Collection<Hud> getHuds() {
		return HUDS.values();
	}
}
