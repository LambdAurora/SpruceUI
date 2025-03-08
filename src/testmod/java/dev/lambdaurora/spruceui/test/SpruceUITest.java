/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.spruceui.test;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.SpruceTexts;
import dev.lambdaurora.spruceui.option.*;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceContainerWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget;
import dev.lambdaurora.spruceui.widget.text.SpruceTextAreaWidget;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Represents the SpruceUI test mod.
 *
 * @author LambdAurora
 */
public class SpruceUITest implements ClientModInitializer {
	private static SpruceUITest INSTANCE;

	private final SpruceOption booleanOption;
	private final SpruceOption checkboxOption;
	private final SpruceOption toggleSwitchOption;
	private final SpruceOption separatorOption;
	private final SpruceOption doubleOption;
	private final SpruceOption cyclingOption;
	private final SpruceOption intInputOption;
	private final SpruceOption floatInputOption;
	private final SpruceOption doubleInputOption;
	private final SpruceOption actionOption;
	private final SpruceOption resetOption;
	private boolean aBoolean;
	private boolean checkboxBoolean;
	private boolean toggleBoolean;
	private double aDouble;
	private TestEnum cyclingValue = TestEnum.FIRST;
	private int anInt;
	private float aFloat;
	private double anInputDouble;

	public Consumer<SpruceButtonWidget> resetConsumer;

	public SpruceUITest() {
		this.booleanOption = new SpruceBooleanOption("spruceui_test.option.boolean",
				() -> this.aBoolean,
				newValue -> this.aBoolean = newValue,
				Text.literal("Represents a boolean option, can either be true or false.\n" +
						"The option value can be colored."),
				true);
		this.checkboxOption = new SpruceCheckboxBooleanOption("spruceui_test.option.checkbox",
				() -> this.checkboxBoolean,
				newValue -> this.checkboxBoolean = newValue,
				Text.literal("Represents a boolean option as a checkbox, can either be true or false.\n"
						+ "It's another implementation of `SpruceBooleanOption` internally."),
				true);
		this.toggleSwitchOption = new SpruceToggleBooleanOption("spruceui_test.option.toggle_switch",
				() -> this.toggleBoolean,
				newValue -> this.toggleBoolean = newValue,
				Text.literal("Represents a boolean option as a toggle switch, can either be true or false.\n"
						+ "It's another implementation of `SpruceBooleanOption` internally."));

		this.separatorOption = new SpruceSeparatorOption("spruceui_test.option.separator", true, null);

		this.doubleOption = new SpruceDoubleOption("spruceui_test.option.double",
				0.0, 50.0, 1.f,
				() -> this.aDouble,
				newValue -> this.aDouble = newValue,
				option -> option.getDisplayText(Text.literal(String.valueOf(this.aDouble))),
				Text.literal("Represents an option with a floating point value.\n"
						+ "There is a minimum, a maximum and a step.\n"
						+ "There is also a lambda for the display text as you can integrate a suffix/prefix like \"%\" or anything else."));

		this.cyclingOption = new SpruceCyclingOption("spruceui_test.option.cycling",
				amount -> this.cyclingValue = this.cyclingValue.next(),
				option -> option.getDisplayText(this.cyclingValue.getText()),
				Text.literal("Represents a cycling option.\n"
						+ "Each press will cycle the value between some pre-defined values."));

		this.intInputOption = new SpruceIntegerInputOption("spruceui_test.option.int_input",
				() -> this.anInt,
				value -> this.anInt = value,
				Text.literal("Represents an option with an integer value as text."));
		this.floatInputOption = new SpruceFloatInputOption("spruceui_test.option.float_input",
				() -> this.aFloat,
				value -> this.aFloat = value,
				Text.literal("Represents an option with a float value as text."));
		this.doubleInputOption = new SpruceDoubleInputOption("spruceui_test.option.double_input",
				() -> this.anInputDouble,
				value -> this.anInputDouble = value,
				Text.literal("Represents an option with a double value as text."));

		// Choose whatever action this option should do.
		this.actionOption = SpruceSimpleActionOption.of("spruceui_test.option.action",
				btn -> {
					Minecraft client = Minecraft.getInstance();
					SystemToast toast = SystemToast.multiline(client, SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
							Text.literal("Action button pressed!"), Text.literal("I'm a result of the action"));
					client.getToasts().addToast(toast);
				},
				Text.literal("Represents an option with a simple action.\n"
						+ "It's used like a normal button and a press callback."));

		// Reset option to reset values.
		this.resetOption = SpruceSimpleActionOption.reset(btn -> {
			this.aBoolean = false;
			this.checkboxBoolean = false;
			this.aDouble = 0.0;
			this.cyclingValue = TestEnum.FIRST;

			// Re-initialize the screen to update all the values.
			if (this.resetConsumer != null)
				this.resetConsumer.accept(btn);
		}, Text.literal("Represents a reset option.\n"
				+ "The option title is already defined and translated in several languages.\n"
				+ "You have to manage screen re-initialization and reset logic yourself."));
	}

	@Override
	public void onInitializeClient() {
		INSTANCE = this;
	}

	public SpruceOptionListWidget buildOptionList(Position position, int width, int height) {
		var list = new SpruceOptionListWidget(position, width, height);

		list.addOptionEntry(this.booleanOption, this.checkboxOption);
		list.addOptionEntry(this.toggleSwitchOption, null);
		list.addOptionEntry(this.toggleSwitchOption, null);
		list.addOptionEntry(this.toggleSwitchOption, null);
		list.addOptionEntry(this.toggleSwitchOption, null);
		list.addOptionEntry(this.toggleSwitchOption, null);
		list.addSingleOptionEntry(this.separatorOption);
		list.addSingleOptionEntry(this.doubleOption);
		list.addSingleOptionEntry(this.intInputOption);
		list.addSingleOptionEntry(this.floatInputOption);
		list.addSingleOptionEntry(this.doubleInputOption);
		list.addOptionEntry(this.actionOption, this.cyclingOption);

		return list;
	}

	public static SpruceContainerWidget buildTextAreaContainer(Position position, int width, int height,
			Consumer<SpruceTextAreaWidget> textAreaConsumer,
			@Nullable SpruceButtonWidget.PressAction doneButtonAction) {
		int textFieldWidth = (int) (width * (3.0 / 4.0));
		var textArea = new SpruceTextAreaWidget(Position.of(width / 2 - textFieldWidth / 2, 0), textFieldWidth, height - 50,
				Text.literal("Text Area"));
		textArea.setLines(Arrays.asList(
				"Hello world,",
				"",
				"Today I want to present you this text area.",
				"I hope you like it, spent 2 whole days on this stupid widget.",
				"",
				"The underlying implementation was kind of hard to write, especially when the first design had a stupid choice.",
				"The widget uses a list of strings to store the text, each index of the list represents one row, not one line.",
				"The first implementation made the error of making it per line, which made rendering very hard and overflowing issues happened",
				"",
				"Now it has to convert list of lines to list of rows, it's really not funny to do...",
				"",
				"Feature-wise!",
				" - Arrow keys allows you to move the cursor",
				" - HOME and END keys work",
				" - You can select text",
				" - You can copy/cut/paste text.",
				" - You can delete a row with CTRL + D",
				" - CTRL + A selects everything",
				"",
				"This widget can be very useful in some cases."));
		textAreaConsumer.accept(textArea);
		// Display as many lines as possible
		textArea.setCursorToStart();
		var container = new SpruceContainerWidget(position, width, height);
		container.addChild(textArea);

		int printToConsoleX = width / 2 - (doneButtonAction == null ? 75 : 155);
		// Print to console button, may be useful for debugging.
		container.addChild(new SpruceButtonWidget(Position.of(printToConsoleX, height - 29), 150, 20, Text.literal("Print to console"),
				btn -> {
					System.out.println("########################## START TEXT AREA CONTENT ##########################");
					System.out.println(textArea.getText());
					System.out.println("##########################  END TEXT AREA CONTENT  ##########################");
				}));
		// Add done button.
		if (doneButtonAction != null)
			container.addChild(new SpruceButtonWidget(Position.of(width / 2 - 155 + 160, height - 29), 150, 20, SpruceTexts.GUI_DONE,
					doneButtonAction));

		return container;
	}

	public static SpruceUITest get() {
		return INSTANCE;
	}
}
