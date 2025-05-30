/*
 * Copyright © 2025 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.tooltip;

import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.narration.NarrationSupplier;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Text;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

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

	public TooltipData(@NotNull Entry... entries) {
		this(null, entries);
	}

	public TooltipData(@Nullable Identifier style, @NotNull Entry... entries) {
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
	public void ifPresent(@NotNull Consumer<TooltipData> action) {
		if (!this.isEmpty()) {
			action.accept(this);
		}
	}

	public @NotNull Stream<Text> streamText() {
		return this.tooltip.stream()
				.filter(TextEntry.class::isInstance)
				.map(TextEntry.class::cast)
				.map(TextEntry::text);
	}

	@Override
	public void updateNarration(@NotNull NarrationElementOutput builder) {
		this.streamText().forEach(text -> builder.add(NarratedElementType.HINT, text));
	}

	public sealed interface Entry {
		@NotNull ClientTooltipComponent toComponent();
	}

	public record TextEntry(@NotNull Text text) implements Entry {
		@Override
		public @NotNull ClientTooltipComponent toComponent() {
			return ClientTooltipComponent.create(this.text.getVisualOrderText());
		}
	}

	public record ComponentEntry(@NotNull ClientTooltipComponent component) implements Entry {
		@Override
		public @NotNull ClientTooltipComponent toComponent() {
			return this.component;
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private final List<Entry> entries = new ArrayList<>();
		private @Nullable Identifier style;

		public Builder text(@NotNull String... text) {
			return this.text(String.join("\n", text));
		}

		public Builder text(@NotNull String text) {
			return this.text(Text.literal(text));
		}

		public Builder text(@NotNull Text... text) {
			var joiner = Text.empty();
			for (int i = 0; i < text.length; i++) {
				joiner = joiner.append(text[i]);

				if (i < text.length - 1) {
					joiner = joiner.append("\n");
				}
			}
			return this.text(joiner);
		}

		public Builder text(@NotNull Text text) {
			this.entries.add(new TextEntry(text));
			return this;
		}

		public Builder component(@NotNull ClientTooltipComponent component) {
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
