/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.tooltip;

import com.google.common.collect.Queues;
import dev.lambdaurora.spruceui.SprucePositioned;
import dev.lambdaurora.spruceui.SpruceUI;
import dev.lambdaurora.spruceui.event.ScreenEvents;
import dev.lambdaurora.spruceui.tooltip.components.SpruceClientTooltipComponent;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.inventory.tooltip.BelowOrAboveWidgetTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Queue;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.Stream;

/**
 * Represents a tooltip.
 *
 * @author LambdAurora
 * @version 8.0.0
 * @since 1.0.0
 */
public final class Tooltip implements SprucePositioned {
	private static final Queue<Tooltip> TOOLTIPS = Queues.newConcurrentLinkedQueue();
	private static boolean delayed = false;
	private final int x;
	private final int y;
	private final List<ClientTooltipComponent> components;
	private final ClientTooltipPositioner positioner;
	private final @Nullable Identifier style;

	public Tooltip(int x, int y, List<ClientTooltipComponent> components, ClientTooltipPositioner positioner, @Nullable Identifier style) {
		this.x = x;
		this.y = y;
		this.components = components;
		this.positioner = positioner;
		this.style = style;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	/**
	 * Renders the tooltip.
	 *
	 * @param graphics The GuiGraphics instance used to render.
	 */
	public void render(GuiGraphics graphics) {
		graphics.renderTooltip(Minecraft.getInstance().font, this.components, this.x, this.y, this.positioner, this.style);
	}

	/**
	 * Queues the tooltip to render.
	 */
	public void queue() {
		TOOLTIPS.add(this);
	}

	/**
	 * Queues the tooltip of the widget to render.
	 *
	 * @param widget the widget
	 * @param mouseX the mouse X coordinate
	 * @param mouseY the mouse Y coordinate
	 * @param <T> the type of the widget
	 * @since 1.6.0
	 */
	public static <T extends Tooltipable & SpruceWidget> void queueFor(
			T widget,
			int mouseX,
			int mouseY,
			int tooltipTicks,
			IntConsumer tooltipTicksSetter,
			long lastTick,
			LongConsumer lastTickSetter
	) {
		if (widget.isVisible()) {
			var tooltip = widget.getTooltip();

			if (tooltip.isEmpty()) return;

			long currentRender = System.currentTimeMillis();
			if (lastTick != 0) {
				if (currentRender - lastTick >= 20) {
					tooltipTicksSetter.accept(tooltipTicks + 1);
					lastTickSetter.accept(currentRender);
				}
			} else lastTickSetter.accept(currentRender);

			if (!widget.isFocused() && !widget.isMouseHovered())
				tooltipTicksSetter.accept(0);

			if (tooltipTicks < 45) return;

			int recommendedMaxWidth = Math.max(widget.getWidth() * 2 / 3, 200);

			var tooltipComponents = tooltip.tooltip().stream()
					.flatMap(entry -> {
						if (entry instanceof TooltipData.TextEntry(var text)) {
							var wrappedTooltipText = Minecraft.getInstance().font.split(text, recommendedMaxWidth);
							return wrappedTooltipText.stream()
									.map(ClientTooltipComponent::create);
						} else {
							var component = entry.toComponent();

							if (component instanceof SpruceClientTooltipComponent spruceTooltipComponent) {
								return Stream.of(spruceTooltipComponent.withMaxWidth(recommendedMaxWidth));
							} else {
								return Stream.of(entry.toComponent());
							}
						}
					}).toList();

			if (widget.isMouseHovered())
				new Tooltip(mouseX, mouseY, tooltipComponents, DefaultTooltipPositioner.INSTANCE, tooltip.style())
						.queue();
			else if (widget.isFocused())
				new Tooltip(
						widget.getX() - 12, widget.getY() + widget.getHeight() + 16,
						tooltipComponents,
						new BelowOrAboveWidgetTooltipPositioner(
								new ScreenRectangle(widget.getScreenPosition(), widget.getWidth(), widget.getHeight())
						),
						tooltip.style()
				)
						.queue();
		}
	}

	/**
	 * Sets whether tooltip rendering is delayed or not.
	 *
	 * @param delayed true if tooltip rendering is delayed
	 */
	@ApiStatus.Internal
	static void setDelayedRender(boolean delayed) {
		Tooltip.delayed = delayed;
	}

	/**
	 * Renders all the tooltips.
	 *
	 * @param graphics the GUI graphics to render from
	 */
	public static void renderAll(GuiGraphics graphics) {
		if (delayed)
			return;
		synchronized (TOOLTIPS) {
			Tooltip tooltip;

			while ((tooltip = TOOLTIPS.poll()) != null)
				tooltip.render(graphics);
		}
	}

	static {
		var tooltipPhase = SpruceUI.id("tooltip");
		ScreenEvents.AFTER_RENDER.addPhaseOrdering(ScreenEvents.AFTER_RENDER.defaultPhaseId(), tooltipPhase);
		ScreenEvents.AFTER_RENDER.register(tooltipPhase,
				(screen, graphics, mouseX, mouseY, tickDelta) -> renderAll(graphics.vanilla())
		);
	}
}
