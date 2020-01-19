/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.mixin;

import me.lambdaurora.spruceui.event.ResolutionChangedCallback;
import me.lambdaurora.spruceui.event.ScreenOpenCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
    @Inject(method = "openScreen", at = @At("RETURN"))
    private void spruceui_on_screen_change(Screen screen, CallbackInfo ci)
    {
        ScreenOpenCallback.EVENT.invoker().apply((MinecraftClient) (Object) this, screen);
    }

    @Inject(method = "onResolutionChanged", at = @At("RETURN"))
    private void spruceui_on_resolution_changed(CallbackInfo ci)
    {
        ResolutionChangedCallback.EVENT.invoker().apply((MinecraftClient) (Object) this);
    }
}
