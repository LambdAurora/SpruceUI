/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.widget.container;

import me.lambdaurora.spruceui.Position;
import org.jetbrains.annotations.NotNull;

public abstract class SpruceElementListWidget<E extends SpruceEntryListWidget.Entry> extends SpruceEntryListWidget<E>
{
    public SpruceElementListWidget(@NotNull Position position, int width, int height, Class<E> entryClass)
    {
        super(position, width, height, entryClass);
    }
}
