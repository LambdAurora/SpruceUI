/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.accessor;

import net.minecraft.client.options.Option;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Represents an accessor to the {@code net.minecraft.client.options.Option} class.
 *
 * @author LambdAurora
 * @version 1.0.2
 * @since 1.0.1
 */
@Mixin(Option.class)
public interface OptionAccessor
{
    @Accessor("key")
    @NotNull String spruceui_get_key();
}
