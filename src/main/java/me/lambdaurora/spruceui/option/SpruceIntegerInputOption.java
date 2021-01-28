/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.option;

import me.lambdaurora.spruceui.Position;
import me.lambdaurora.spruceui.widget.SpruceWidget;
import me.lambdaurora.spruceui.widget.text.SpruceNamedTextFieldWidget;
import me.lambdaurora.spruceui.widget.text.SpruceTextFieldWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.aperlambda.lambdacommon.utils.LambdaUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents an integer input option.
 *
 * @author LambdAurora
 * @version 2.1.0
 * @since 2.1.0
 */
public class SpruceIntegerInputOption extends SpruceOption {
    private final Supplier<Integer> getter;
    private final Consumer<Integer> setter;

    public SpruceIntegerInputOption(String key, Supplier<Integer> getter, Consumer<Integer> setter, @Nullable Text tooltip) {
        super(key);
        this.getter = getter;
        this.setter = setter;
        this.setTooltip(tooltip);
    }

    @Override
    public SpruceWidget createWidget(Position position, int width) {
        SpruceTextFieldWidget textField = new SpruceTextFieldWidget(position, width, 20, this.getPrefix());
        textField.setText(String.valueOf(this.get()));
        textField.setTextPredicate(SpruceTextFieldWidget.INTEGER_INPUT_PREDICATE);
        textField.setRenderTextProvider((displayedText, offset) -> {
            try {
                Integer.parseInt(textField.getText());
                return OrderedText.styledString(displayedText, Style.EMPTY);
            } catch (NumberFormatException e) {
                return OrderedText.styledString(displayedText, Style.EMPTY.withColor(Formatting.RED));
            }
        });
        textField.setChangedListener(input -> {
            int value = LambdaUtils.parseIntFromString(input);
            this.set(value);
        });
        this.getOptionTooltip().ifPresent(textField::setTooltip);
        return new SpruceNamedTextFieldWidget(textField);
    }

    public void set(int value) {
        this.setter.accept(value);
    }

    /**
     * Gets the current value.
     *
     * @return the current value
     */
    public int get() {
        return this.getter.get();
    }
}
