/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.hud;

import com.google.common.collect.ImmutableList;
import dev.lambdaurora.spruceui.util.Identifiable;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a HUD.
 *
 * @author LambdAurora
 * @version 5.0.0
 * @since 1.2.0
 */
public abstract class Hud implements Identifiable {
	protected final Identifier identifier;
	protected final List<HudComponent> components = new ArrayList<>();
	protected final String translationKey;
	private boolean enabled = true;
	protected boolean visible = true;

	public Hud(@NotNull Identifier id) {
		this.identifier = id;
		this.translationKey = this.identifier.namespace() + ".hud." + this.identifier.path()
				.replace('/', '.');
	}

	/**
	 * Returns the translation key of this HUD.
	 *
	 * @return The translation key.
	 */
	public String getTranslationKey() {
		return this.translationKey;
	}

	/**
	 * Returns whether the HUD is enabled or not.
	 *
	 * @return True if the HUD is enabled, else false.
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * Sets whether the HUD is enabled or not.
	 *
	 * @param enabled True if the HUD is enabled, else false.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			var client = Minecraft.getInstance();
			this.init(client, client.getWindow().getGuiScaledWidth(), client.getWindow().getGuiScaledHeight());
		}
	}

	/**
	 * Returns whether the HUD is visible or not.
	 *
	 * @return True if the HUD is visible, else false.
	 */
	public boolean isVisible() {
		return this.visible;
	}

	/**
	 * Sets whether the HUD is visible or not.
	 *
	 * @param visible True if the HUD is visible, else false.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void init(@NotNull Minecraft client, int screenWidth, int screenHeight) {
		this.components.clear();
	}

	/**
	 * Renders the HUD if enabled.
	 *
	 * @param deltaTracker Progress for linearly interpolating between the previous and current game state.
	 * @see #isEnabled()
	 */
	public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
		this.components.stream().filter(HudComponent::isEnabled).forEach(component -> component.render(graphics, deltaTracker));
	}

	/**
	 * Updates the HUD each tick if enabled and has tick updates.
	 *
	 * @see #isEnabled()
	 * @see #hasTicks()
	 */
	public void tick() {
		this.components.stream().filter(((Predicate<HudComponent>) HudComponent::hasTicks).and(HudComponent::isEnabled))
				.forEach(HudComponent::tick);
	}

	/**
	 * Returns whether this HUD has tick updates.
	 *
	 * @return True if this HUD has tick updates, else false.
	 * @see #tick()
	 */
	public boolean hasTicks() {
		return false;
	}

	/**
	 * Returns a list of this HUD's components.
	 *
	 * @return The HUD's components.
	 */
	public @NotNull List<HudComponent> getComponents() {
		return ImmutableList.copyOf(this.components);
	}

	@Override
	public @NotNull Identifier getIdentifier() {
		return this.identifier;
	}
}
