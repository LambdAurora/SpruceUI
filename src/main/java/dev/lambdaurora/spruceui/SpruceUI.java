/*
 * Copyright © 2024 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui;

import dev.lambdaurora.spruceui.resources.GuiSpriteManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.io.ResourceType;

/**
 * Contains common constants from SpruceUI.
 *
 * @author LambdAurora
 * @version 6.0.0
 * @since 6.0.0
 */
public final class SpruceUI implements ClientModInitializer {
	/**
	 * The namespace of SpruceUI, whose value is {@value}.
	 */
	public static final String NAMESPACE = "spruceui";

	/**
	 * {@return a SpruceUI identifier from the given path}
	 *
	 * @param path the path
	 */
	public static Identifier id(String path) {
		return new Identifier(NAMESPACE, path);
	}

	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(GuiSpriteManager.get());
	}
}
