/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.test.mixin;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.test.gui.SpruceMainMenuScreen;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
	protected TitleScreenMixin(Text title) {
		super(title);
	}

	@Inject(method = "init", at = @At("RETURN"))
	private void onInit(CallbackInfo ci) {
		this.addRenderableWidget(new SpruceButtonWidget(Position.of(0, 0), 150, 20, Text.literal("SpruceUI Test Menu"),
				btn -> this.client.setScreen(new SpruceMainMenuScreen(this))).asVanilla());
	}
}
