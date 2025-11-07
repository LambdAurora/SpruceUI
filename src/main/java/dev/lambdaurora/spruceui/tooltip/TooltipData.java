/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.tooltip;

import dev.lambdaurora.spruceui.tooltip.components.ClientSpriteTooltipComponent;
import dev.lambdaurora.spruceui.tooltip.components.ClientTextTooltipComponent;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.narration.NarrationSupplier;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public record TooltipData(
		@Unmodifiable List<Entry> tooltip,
		@Nullable Identifier style
) implements NarrationSupplier {
	public static final TooltipData EMPTY = new TooltipData(List.of(), null);

	public TooltipData(Entry... entries) {
		this(null, entries);
	}

	public TooltipData(@Nullable Identifier style, Entry... entries) {
		this(List.of(entries), style);
	}

	/**
	 * {@return {@code true} if this tooltip is empty, or {@code false} otherwise}
	 * <p>
	 * An empty tooltip is not rendered.
	 */
	public boolean isEmpty() {
		return this.tooltip.isEmpty();
	}

	/**
	 * If a tooltip is present, performs the given action with the value, otherwise does nothing.
	 *
	 * @param action the action to be performed, if a tooltip is present
	 * @throws NullPointerException if tooltip is present and the given action is {@code null}
	 */
	public void ifPresent(Consumer<TooltipData> action) {
		if (!this.isEmpty()) {
			action.accept(this);
		}
	}

	public Stream<Component> streamText() {
		return this.tooltip.stream()
				.filter(TextEntry.class::isInstance)
				.map(TextEntry.class::cast)
				.map(TextEntry::text);
	}

	@Override
	public void updateNarration(NarrationElementOutput builder) {
		this.streamText().forEach(text -> builder.add(NarratedElementType.HINT, text));
	}

	public sealed interface Entry {
		ClientTooltipComponent toComponent();
	}

	/**
	 * Represents a tooltip text entry.
	 *
	 * @param text the text of this entry
	 */
	public record TextEntry(Component text) implements Entry {
		@Override
		public ClientTooltipComponent toComponent() {
			return new ClientTextTooltipComponent(this.text.getVisualOrderText());
		}
	}

	/**
	 * Represents a tooltip component entry.
	 *
	 * @param component the component
	 */
	public record ComponentEntry(ClientTooltipComponent component) implements Entry {
		@Override
		public ClientTooltipComponent toComponent() {
			return this.component;
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private final List<Entry> entries = new ArrayList<>();
		private @Nullable Identifier style;

		public Builder text(String... text) {
			return this.text(String.join("\n", text));
		}

		public Builder text(String text) {
			return this.text(Component.literal(text));
		}

		public Builder text(Component... text) {
			var joiner = Component.empty();
			for (int i = 0; i < text.length; i++) {
				joiner = joiner.append(text[i]);

				if (i < text.length - 1) {
					joiner = joiner.append("\n");
				}
			}
			return this.text(joiner);
		}

		public Builder text(Component text) {
			this.entries.add(new TextEntry(text));
			return this;
		}

		public Builder sprite(Identifier spriteId, int width, int height) {
			return this.component(new ClientSpriteTooltipComponent(spriteId, width, height));
		}

		public Builder component(ClientTooltipComponent component) {
			this.entries.add(new ComponentEntry(component));
			return this;
		}

		public Builder style(@Nullable Identifier style) {
			this.style = style;
			return this;
		}

		public TooltipData build() {
			return new TooltipData(Collections.unmodifiableList(this.entries), this.style);
		}
	}
}
