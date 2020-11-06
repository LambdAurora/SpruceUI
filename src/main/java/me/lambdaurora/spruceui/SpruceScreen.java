/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a screen.
 *
 * @author LambdAurora
 * @version 1.6.5
 * @since 1.6.5
 */
public abstract class SpruceScreen extends Screen implements NavigationElement
{
    protected SpruceScreen(@NotNull Text title)
    {
        super(title);
    }
}
