/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.background;

import dev.lambdaurora.spruceui.util.RenderUtil;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.util.math.MatrixStack;

public record DirtTexturedBackground(int red, int green, int blue, int alpha) implements Background {
	public static final Background NORMAL = new DirtTexturedBackground(64, 64, 64, 255);
	public static final Background DARKENED = new DirtTexturedBackground(32, 32, 32, 255);

	@Override
	public void render(MatrixStack matrices, SpruceWidget widget, int vOffset, int mouseX, int mouseY, float delta) {
		RenderUtil.renderBackgroundTexture(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(),
				vOffset / 32.f, this.red, this.green, this.blue, this.alpha);
	}

	@Override
	public String toString() {
		return "DirtTexturedBackground{" +
				"red=" + this.red +
				", green=" + this.green +
				", blue=" + this.blue +
				", alpha=" + this.alpha +
				'}';
	}
}
