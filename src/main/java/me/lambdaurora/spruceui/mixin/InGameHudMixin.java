/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.mixin;

import me.lambdaurora.spruceui.event.HudRenderCallback;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Represents the injection point of the {@link HudRenderCallback} event.
 * <p>
 * It is a placeholder mixin for the missing HudRenderCallback in the 1.14.4 Fabric API.
 *
 * @author LambdAurora
 * @version 1.2.1-1.14.4
 * @since 1.2.1-1.14.4
 */
@Mixin(InGameHud.class)
public class InGameHudMixin
{
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay()V", shift = At.Shift.AFTER))
    public void render(float tick_delta, CallbackInfo ci)
    {
        HudRenderCallback.EVENT.invoker().on_hud_render(tick_delta);
    }
}
