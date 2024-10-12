/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.util;

import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Represents a multiline text.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 1.6.3
 */
public final class MultilineText {
	private final List<String> rows = new ArrayList<>();
	private int width;

	public MultilineText(int width) {
		this.width = width;
	}

	public MultilineText(int width, @Nullable String text) {
		this(width);
		if (text == null)
			return;
		this.rows.addAll(wrap(text, width));
	}

	public MultilineText(int width, Collection<? extends String> lines) {
		this(width);
		this.rows.addAll(wrap(lines, width));
	}

	/**
	 * Returns the maximum width of the multiline text.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Sets the maximum width of the multiline text.
	 *
	 * @param width the width
	 */
	public void setWidth(int width) {
		if (this.width != width) {
			this.width = width;
			this.recompute();
		}
	}

	public List<String> getRows() {
		return this.rows;
	}

	public List<String> getLines() {
		var lines = new ArrayList<String>();

		var stringBuilder = new StringBuilder();
		for (var row : this.rows) {
			stringBuilder.append(row.replace("\n", ""));
			if (row.endsWith("\n")) {
				lines.add(stringBuilder.toString());
				stringBuilder = new StringBuilder();
			}
		}

		return lines;
	}

	public void setLines(Collection<? extends String> lines) {
		this.clear();
		this.addAll(wrap(lines, this.width));
	}

	/**
	 * Returns the text as a string.
	 *
	 * @return The text.
	 */
	public String getText() {
		var builder = new StringBuilder();
		for (var row : this.rows)
			builder.append(row);
		return builder.toString();
	}

	/**
	 * Sets the text.
	 *
	 * @param text The text.
	 */
	public void setText(String text) {
		this.clear();
		this.add(text);
	}

	/**
	 * Recomputes the lines wrapping.
	 */
	public void recompute() {
		var text = this.getText();
		this.clear();
		this.add(text);
	}

	public boolean isEmpty() {
		return this.rows.isEmpty();
	}

	public int size() {
		return this.rows.size();
	}

	public @Nullable String get(int row) {
		return this.rows.get(row);
	}

	public void addAll(Collection<? extends String> lines) {
		this.rows.addAll(lines);
	}

	/**
	 * Adds a new line.
	 *
	 * @param line The line to add.
	 */
	public void add(String line) {
		if (line.length() > 1)
			this.rows.addAll(wrap(line, this.width));
		else
			this.rows.add(line);
	}

	/**
	 * Adds a new line at the specified row.
	 *
	 * @param row The row.
	 * @param line The line to add.
	 */
	public void add(int row, String line) {
		this.rows.add(row, line);
	}

	/**
	 * Removes a line.
	 *
	 * @param row The row to remove.
	 * @return The removed line content.
	 */
	public @Nullable String remove(int row) {
		if (row < 0 || row >= this.rows.size())
			return null;
		return this.rows.remove(row);
	}

	/**
	 * Replaces the line.
	 *
	 * @param row The row of the line.
	 * @param line The new text.
	 */
	public void replaceRow(int row, String line) {
		if (row < 0 || row >= this.rows.size())
			return;
		this.remove(row);
		this.rows.add(row, line);
	}

	public void replaceRow(int row, Function<String, String> replacer) {
		if (row < 0 || row >= this.rows.size())
			return;
		var line = this.get(row);
		this.replaceRow(row, replacer.apply(line));
	}

	/**
	 * Clears the text.
	 */
	public void clear() {
		this.rows.clear();
	}

	public static Collection<? extends String> wrap(String text, int width) {
		return wrap(Arrays.asList(text.split("\n")), width);
	}

	public static Collection<? extends String> wrap(Collection<? extends String> text, int width) {
		var client = Minecraft.getInstance();
		if (client == null)
			return text;

		var lines = new ArrayList<String>();

		for (String line : text) {
			if (line.equals("\n") || line.isEmpty()) {
				lines.add("\n");
				continue;
			}

			if (line.endsWith("\n")) line = line.substring(0, line.length() - 1);
			while (!line.isEmpty()) {
				var part = client.font.plainSubstrByWidth(line, width);
				line = line.substring(part.length());
				lines.add(part);
			}
			var part = lines.remove(lines.size() - 1);
			lines.add(part + "\n");
		}

		return lines;
	}
}
