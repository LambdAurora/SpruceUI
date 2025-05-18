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
import net.neoforged.fml.common.Mod;

@Mod(SpruceUITest.NAMESPACE)
public class NeoForgeInit {
	@SuppressWarnings("FieldCanBeLocal")
	private final SpruceUITest instance = new SpruceUITest();

	public NeoForgeInit() {
		this.instance.initialize();
	}
}
