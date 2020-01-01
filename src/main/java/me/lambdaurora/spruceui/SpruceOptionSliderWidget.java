/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import me.lambdaurora.spruceui.option.SpruceDoubleOption;
import net.minecraft.client.options.GameOptions;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an option slider widget.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
public class SpruceOptionSliderWidget extends SpruceSliderWidget
{
    private final SpruceDoubleOption option;

    public SpruceOptionSliderWidget(@NotNull GameOptions options, int x, int y, int width, int height, @NotNull SpruceDoubleOption option)
    {
        super(x, y, width, height, "", option.get_ratio(option.get(options)), slider -> option.set(options, option.get_value(slider.get_value())));
        this.option = option;
        this.updateMessage();
    }

    @Override
    protected void updateMessage()
    {
        if (this.option != null)
            this.setMessage(this.option.get_display_string(this.options));
    }
}
