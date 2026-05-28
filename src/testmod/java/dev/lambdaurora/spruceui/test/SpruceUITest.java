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
import dev.lambdaurora.spruceui.event.ScreenEvents;
import dev.lambdaurora.spruceui.option.SpruceOption;
import dev.lambdaurora.spruceui.option.SpruceSeparatorOption;
import dev.lambdaurora.spruceui.option.SpruceSimpleActionOption;
import dev.lambdaurora.spruceui.test.gui.SpruceMainMenuScreen;
import dev.lambdaurora.spruceui.test.gui.tooltip.CheckboxTooltipComponent;
import dev.lambdaurora.spruceui.tooltip.TooltipData;
import dev.lambdaurora.spruceui.tooltip.components.ClientThumbnailTooltipComponent;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceContainerWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget;
import dev.lambdaurora.spruceui.widget.text.SpruceTextAreaWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents the SpruceUI test mod.
 *
 * @author LambdAurora
 */
public final class SpruceUITest {
	public static final String NAMESPACE = "spruceui_test";
	private static final Logger LOGGER = LoggerFactory.getLogger(SpruceUITest.class);

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
		INSTANCE = this;

		this.booleanOption = SpruceOption.booleanBuilder("spruceui_test.option.boolean",
						() -> this.aBoolean,
						newValue -> this.aBoolean = newValue
				).tooltip(
						TooltipData.builder()
								.text(
										"Represents a boolean option, can either be true or false.",
										"The option value can be colored"
								)
								.build()
				)
				.colored()
				.build();
		this.checkboxOption = SpruceOption.checkboxBuilder("spruceui_test.option.checkbox",
						() -> this.checkboxBoolean,
						newValue -> this.checkboxBoolean = newValue
				).tooltip(
						TooltipData.builder()
								.component(new ClientThumbnailTooltipComponent(
										new CheckboxTooltipComponent(),
										Component.literal("Represents a boolean option as a checkbox, can either be true or false.")
												.append("\n")
												.append("It's another implementation of ")
												.append(Component.literal("`SpruceBooleanOption`").withStyle(ChatFormatting.ITALIC))
												.append(" internally.")
								))
								.build()
				)
				.colored()
				.build();
		this.toggleSwitchOption = SpruceOption.toggleBuilder("spruceui_test.option.toggle_switch",
						() -> this.toggleBoolean,
						newValue -> this.toggleBoolean = newValue
				).tooltip(TooltipData.builder()
						.text(
								Component.literal("Represents a boolean option as a toggle switch, can either be true or false."),
								Component.literal("It's another implementation of ")
										.append(Component.literal("`SpruceBooleanOption`").withStyle(ChatFormatting.ITALIC))
										.append(" internally.")
						)
						.build()
				)
				.build();

		this.separatorOption = new SpruceSeparatorOption("spruceui_test.option.separator", true, TooltipData.EMPTY);

		this.doubleOption = SpruceOption.doubleBuilder("spruceui_test.option.double",
						0.0, 50.0, 1.f,
						() -> this.aDouble,
						newValue -> this.aDouble = newValue,
						option -> option.getDisplayText(Component.literal(String.valueOf(this.aDouble)))
				).tooltip(TooltipData.builder()
						.text(
								"Represents an option with a floating point value.",
								"There is a minimum, a maximum and a step.",
								"There is also a lambda for the display text as you can integrate a suffix/prefix like \"%\" or anything else."
						)
						.build()
				)
				.build();

		this.cyclingOption = SpruceOption.cyclingBuilder("spruceui_test.option.cycling",
						amount -> this.cyclingValue = this.cyclingValue.next(),
						option -> option.getDisplayText(this.cyclingValue.getText())
				).tooltip(TooltipData.builder()
						.text(
								"Represents a cycling option",
								"Each press will cycle the value between some pre-defined values."
						)
						.build()
				)
				.build();

		this.intInputOption = SpruceOption.intInputBuilder("spruceui_test.option.int_input",
						() -> this.anInt,
						value -> this.anInt = value
				).tooltip(Component.literal("Represents an option with an integer value as text."))
				.build();
		this.floatInputOption = SpruceOption.floatInputBuilder("spruceui_test.option.float_input",
						() -> this.aFloat,
						value -> this.aFloat = value
				).tooltip(Component.literal("Represents an option with a float value as text."))
				.build();
		this.doubleInputOption = SpruceOption.doubleInputBuilder("spruceui_test.option.double_input",
						() -> this.anInputDouble,
						value -> this.anInputDouble = value
				).tooltip(Component.literal("Represents an option with a double value as text."))
				.build();

		// Choose whatever action this option should do.
		this.actionOption = SpruceSimpleActionOption.of("spruceui_test.option.action",
				btn -> {
					Minecraft client = Minecraft.getInstance();
					var toast = new SystemToast(SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
							Component.literal("Action button pressed!"), Component.literal("I'm a result of the action"));
					client.gui.toastManager().addToast(toast);
				},
				TooltipData.builder()
						.component(
								new ClientBundleTooltip(
										new BundleContents(
												List.of(new ItemStackTemplate(Items.POPPY))
										)
								)
						)
						.text(
								"Represents an option with a simple action.",
								"It's used like a normal button and a press callback."
						)
						.build()
		);

		// Reset option to reset values.
		this.resetOption = SpruceSimpleActionOption.reset(btn -> {
					this.aBoolean = false;
					this.checkboxBoolean = false;
					this.aDouble = 0.0;
					this.cyclingValue = TestEnum.FIRST;

					// Re-initialize the screen to update all the values.
					if (this.resetConsumer != null)
						this.resetConsumer.accept(btn);
				},
				TooltipData.builder()
						.text(
								"Represents a reset option.",
								"The option title is already defined and translated in several languages.",
								"You have to manage screen re-initialization and reset logic yourself."
						)
						.build()
		);
	}

	public void initialize() {
		LOGGER.info("Initializing SpruceUI test mod...");

		ScreenEvents.AFTER_INIT.register(context -> {
			context.addRenderableWidget(
					new SpruceButtonWidget(
							Position.of(0, 0), 150, 20, Component.literal("SpruceUI Test Menu"),
							btn -> context.client().setScreenAndShow(new SpruceMainMenuScreen(context.screen()))
					).asVanilla()
			);

			ScreenEvents.REMOVE.forContext(context.screen()).register(_ -> {
				LOGGER.info("bye bye title screen");
			});
		}, TitleScreen.class::isInstance);

		ScreenEvents.AFTER_INIT.register(context -> {
			final int[] tick = new int[]{0};

			ScreenEvents.BEFORE_TICK.forContext(context.screen()).register(_ -> {
				tick[0]++;
			});

			ScreenEvents.AFTER_RENDER.forContext(context.screen()).register(
					(screen, graphics, _, _, _) -> {
						var text = "Greetings from SpruceUI";
						int width = screen.getFont().width(text);
						graphics.shadowedText(screen.getFont(), text, screen.width - width - 2, 2, 0xffffffff);

						var tickText = String.valueOf(tick[0]);
						int tickWidth = screen.getFont().width(tickText);
						graphics.shadowedText(
								screen.getFont(), tickText,
								screen.width - tickWidth - 2, 4 + screen.getFont().lineHeight,
								0xffffffff
						);
					}
			);
		}, screen -> !screen.getClass().getPackageName().contains("spruceui"));
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
				Component.literal("Text Area"));
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
		container.addChild(new SpruceButtonWidget(Position.of(printToConsoleX, height - 29), 150, 20, Component.literal("Print to console"),
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
