/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.test.init;

import dev.lambdaurora.spruceui.test.SpruceUITest;
import net.fabricmc.api.ClientModInitializer;

public final class FabricInit implements ClientModInitializer {
	private final SpruceUITest instance = new SpruceUITest();

	@Override
	public void onInitializeClient() {
		this.instance.initialize();
	}
}
