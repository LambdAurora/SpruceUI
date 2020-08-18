/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a text area widget.
 *
 * @author LambdAurora
 * @version 1.6.3
 * @since 1.6.3
 */
public class SpruceTextAreaWidget extends AbstractButtonWidget implements SpruceWidget
{
    private final TextRenderer  textRenderer;
    private final MultilineText lines;
    private final Cursor        cursor          = new Cursor(true);
    private final Selection     selection       = new Selection();
    private       int           firstLine       = 0;
    private       boolean       editable        = true;
    private       int           editableColor   = 0xe0e0e0;
    private       int           uneditableColor = 7368816;
    private       int           displayedLines  = 1;

    public SpruceTextAreaWidget(@NotNull TextRenderer textRenderer, int x, int y, int width, int height, Text message)
    {
        super(x, y, width, height, message);
        this.textRenderer = textRenderer;
        this.lines = new MultilineText(this.getInnerWidth());
        this.cursor.toStart();
    }

    /**
     * Returns the lines.
     *
     * @return The lines.
     */
    public @NotNull List<String> getLines()
    {
        return this.lines.getLines();
    }

    /**
     * Sets the lines.
     *
     * @param lines The lines.
     */
    public void setLines(@NotNull List<String> lines)
    {
        this.lines.setLines(lines);
        this.selection.active = false;
        this.setCursorToEnd();
    }

    /**
     * Returns the text from the text area.
     *
     * @return The text.
     */
    public @NotNull String getText()
    {
        return this.lines.getText();
    }

    /**
     * Sets the text of the text area.
     *
     * @param text The text.
     */
    public void setText(@Nullable String text)
    {
        this.lines.clear();
        if (text != null)
            this.lines.setText(text);
    }

    /**
     * Clears the text area.
     */
    public void clear()
    {
        this.lines.clear();
        this.sanitize();
    }

    /**
     * Returns whether this text area is editable or not.
     *
     * @return True if editable, else false.
     */
    public boolean isEditable()
    {
        return this.editable;
    }

    /**
     * Sets whether this text area is editable or not.
     *
     * @param editable State of editable.
     */
    public void setEditable(boolean editable){
        this.editable = editable;
    }

    /**
     * Sets the number of displayed lines.
     *
     * @param lines Number of displayed lines.
     */
    public void setDisplayedLines(int lines)
    {
        if (lines <= 0)
            lines = 1;
        this.displayedLines = lines;

        this.cursor.adjustFirstLine();
    }

    /**
     * Returns the inner width of the text area.
     *
     * @return The inner width.
     */
    public int getInnerWidth()
    {
        return this.width - 8;
    }

    public int getInnerHeight()
    {
        return this.height - 8;
    }

    public void setCursorToEnd()
    {
        this.cursor.toEnd();
    }

    private void insertCharacter(char character)
    {
        if (this.lines.isEmpty()) {
            this.lines.add(String.valueOf(character));
            this.cursor.toStart();
            return;
        } else {
            this.selection.erase();
        }

        if (character == '\n') {
            String line;
            if (this.cursor.row == this.lines.size() - 1 && (line = this.lines.get(this.cursor.row)) != null && this.cursor.column >= line.length() - 1) {
                int currentRow = this.cursor.row;
                if (!line.endsWith("\n"))
                    this.lines.replaceRow(currentRow, s -> s + "\n");
                this.lines.add(currentRow + 1, "");
                this.cursor.moveDown();
                return;
            }
        }

        String text = this.getText();
        int cursorPosition = this.cursor.getPosition();
        int oldSize = this.lines.size();

        String newText;
        if (cursorPosition >= text.length()) {
            newText = text + character;
        } else {
            newText = text.substring(0, cursorPosition) + character + text.substring(cursorPosition);
        }

        this.lines.clear();
        this.lines.add(newText);

        if (character == '\n') {
            this.cursor.moveRight();
            this.cursor.moveRight();
            return;
        }
        this.cursor.moveRight();
        if (oldSize + 1 == this.lines.size())
            this.cursor.moveRight(); // Extra move right.
    }

    private void eraseCharacter()
    {
        if (this.selection.erase()) {
            this.sanitize();
            return;
        }

        String line = this.lines.get(this.cursor.row);

        if ((line.isEmpty() || line.equals("\n")) && this.lines.size() != 1) {
            this.lines.remove(this.cursor.row);
            this.cursor.moveUp();
            this.cursor.toRowEnd();
            return;
        }

        if (this.cursor.column == 0 && this.cursor.row == 0)
            return;

        String text = this.getText();
        int cursorPosition = this.cursor.getPosition();
        this.cursor.moveLeft();
        this.lines.clear();
        this.lines.add(text.substring(0, cursorPosition - 1) + text.substring(cursorPosition));
        this.sanitize();
    }

    private void removeCharacterForward()
    {
        if (this.selection.erase()) {
            this.sanitize();
            return;
        }

        String line = this.lines.get(this.cursor.row);

        if (line.isEmpty()) {
            int row = this.cursor.row;
            if (row >= this.lines.size() - 1)
                return;
            this.lines.remove(row);
            this.sanitize();
            return;
        }

        if (this.cursor.column >= line.length() && this.cursor.row == this.lines.size() - 1)
            return;

        String text = this.getText();
        int cursorPosition = this.cursor.getPosition();

        String newText = text.substring(0, cursorPosition) + text.substring(cursorPosition + 1);
        this.lines.clear();
        this.lines.add(newText);
        this.sanitize();
    }

    /**
     * Writes text where the cursor is.
     *
     * @param text The text to write.
     */
    public void write(@NotNull String text)
    {
        if (text.isEmpty())
            return;

        if (this.lines.isEmpty()) {
            this.lines.add(text);
            this.cursor.toEnd();
            return;
        }
        this.selection.erase();

        int oldSize = this.lines.size();

        String whole = this.getText();
        int position = this.cursor.getPosition();

        String newText;
        if (position >= whole.length()) {
            newText = whole + text;
        } else {
            newText = whole.substring(0, position) + text + whole.substring(position);
        }

        this.lines.clear();
        this.lines.setLines(Arrays.asList(newText.split("\n")));

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n')
                this.cursor.moveRight();
            this.cursor.moveRight();
        }
        if (oldSize < this.lines.size()) this.cursor.moveRight();
    }

    public boolean isActive()
    {
        return this.isVisible() && this.isFocused() && this.isEditable();
    }

    @Override
    public boolean charTyped(char chr, int keyCode)
    {
        if (!this.isActive() || !SharedConstants.isValidChar(chr))
            return false;

        if (this.isEditable()) {
            this.insertCharacter(chr);
            this.selection.cancel();
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (!this.isActive())
            return false;

        if (Screen.isSelectAll(keyCode)) {
            this.selection.selectAll();
            return true;
        } else if (Screen.isPaste(keyCode)) {
            this.write(MinecraftClient.getInstance().keyboard.getClipboard());
            return true;
        } else if (Screen.isCopy(keyCode) || Screen.isCut(keyCode)) {
            String selected = this.selection.getSelectedText();
            if (!selected.isEmpty())
                MinecraftClient.getInstance().keyboard.setClipboard(selected);
            if (Screen.isCut(keyCode) && this.isEditable()) {
                this.selection.erase();
                this.sanitize();
            }
            return true;
        }

        switch (keyCode) {
            case GLFW.GLFW_KEY_RIGHT:
                return this.onSelectionUpdate(this.cursor::moveRight);
            case GLFW.GLFW_KEY_LEFT:
                return this.onSelectionUpdate(this.cursor::moveLeft);
            case GLFW.GLFW_KEY_UP:
                return this.onSelectionUpdate(this.cursor::moveUp);
            case GLFW.GLFW_KEY_DOWN:
                return this.onSelectionUpdate(this.cursor::moveDown);
            case GLFW.GLFW_KEY_END:
                return this.onSelectionUpdate(Screen.hasControlDown() ? this.cursor::toEnd : this.cursor::toRowEnd);
            case GLFW.GLFW_KEY_HOME:
                return this.onSelectionUpdate(Screen.hasControlDown() ? this.cursor::toStart : this.cursor::toLineStart);
            case GLFW.GLFW_KEY_ENTER:
            case GLFW.GLFW_KEY_KP_ENTER:
                if (this.isEditable()) {
                    this.insertCharacter('\n');
                }
                return true;
            case GLFW.GLFW_KEY_BACKSPACE:
                if (this.isEditable())
                    this.eraseCharacter();
                return true;
            case GLFW.GLFW_KEY_DELETE:
                if (this.isEditable())
                    this.removeCharacterForward();
                return true;
            case GLFW.GLFW_KEY_D:
                if (Screen.hasControlDown() && this.isEditable() && !this.lines.isEmpty()) {
                    this.lines.remove(this.cursor.row);
                    this.sanitize();
                }
                return true;
            default:
                return false;
        }
    }

    private boolean onSelectionUpdate(Runnable action)
    {

        this.selection.tryStartSelection();
        action.run();
        this.selection.moveToCursor();
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (!this.isVisible()) {
            return false;
        }

        boolean mouseOver = this.isMouseOver(mouseX, mouseY);
        if (mouseOver && this.editable)
            this.setFocused(true);

        if (this.isFocused() && mouseOver && button == 0) {
            int x = MathHelper.floor(mouseX) - this.x - 4;
            int y = MathHelper.floor(mouseY) - this.y - 4;

            int row = this.firstLine + y / 9;
            if (row >= this.lines.size()) {
                this.cursor.toEnd();
                return true;
            }

            this.onSelectionUpdate(() -> {
                this.cursor.row = row;

                this.cursor.column = this.textRenderer.trimToWidth(this.lines.get(row), x).length();
            });

            return true;
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount)
    {
        if (!this.isActive()) {
            return false;
        }

        if (amount > 0.) {
            this.cursor.moveUp();
        } else {
            this.cursor.moveDown();
        }
        return true;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        if (!this.isVisible())
            return;

        int borderColor = this.isFocused() ? -1 : -6250336;
        fill(matrices, this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, borderColor);
        fill(matrices, this.x, this.y, this.x + this.width, this.y + this.height, -16777216);

        this.drawText(matrices);
        this.drawCursor(matrices);
    }

    /**
     * Draws the text of the text area.
     *
     * @param matrices The matrices.
     */
    protected void drawText(@NotNull MatrixStack matrices)
    {
        int length = Math.min(this.lines.size(), this.displayedLines);

        int textColor = this.editable ? this.editableColor : this.uneditableColor;
        int textX = this.x + 4;

        int lineY = this.y + 4;
        for (int row = this.firstLine; row < this.firstLine + length; row++) {
            String line = this.lines.get(row);
            if (line == null)
                continue;
            if (line.endsWith("\n")) line = line.substring(0, line.length() - 1);

            drawTextWithShadow(matrices, this.textRenderer, new LiteralText(line), textX, lineY, textColor);
            this.drawSelection(matrices, line, lineY, row);

            lineY += this.textRenderer.fontHeight;
        }
    }

    /**
     * Draws the selection over the text.
     *
     * @param matrices The matrices.
     * @param line     The current line.
     * @param lineY    The line Y-coordinates.
     * @param row      The row number.
     */
    protected void drawSelection(@NotNull MatrixStack matrices, @NotNull String line, int lineY, int row)
    {
        if (!this.selection.isRowSelected(row))
            return;

        int startIndex;
        if (selection.getStart().row != row) startIndex = 0;
        else startIndex = selection.getStart().column;

        int endIndex;
        if (selection.getEnd().row != row) endIndex = line.length();
        else endIndex = selection.getEnd().column;

        if (endIndex >= line.length())
            endIndex = line.length();

        if (startIndex >= line.length() || startIndex == endIndex)
            return;

        int x = this.x + 4 + this.textRenderer.getWidth(line.substring(0, startIndex));
        String selected = line.substring(startIndex, endIndex);

        int x2 = x + this.textRenderer.getWidth(selected);
        int y2 = lineY + this.textRenderer.fontHeight;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        RenderSystem.color4f(0.f, 0.f, 255.f, 255.f);
        RenderSystem.disableTexture();
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
        buffer.begin(7, VertexFormats.POSITION);
        buffer.vertex(x, y2, 0.d).next();
        buffer.vertex(x2, y2, 0.d).next();
        buffer.vertex(x2, lineY, 0.d).next();
        buffer.vertex(x, lineY, 0.d).next();
        tessellator.draw();
        RenderSystem.disableColorLogicOp();
        RenderSystem.enableTexture();
    }

    /**
     * Draws the cursor.
     *
     * @param matrices The matrices.
     */
    protected void drawCursor(@NotNull MatrixStack matrices)
    {
        if (this.lines.isEmpty()) {
            drawTextWithShadow(matrices, this.textRenderer, new LiteralText("_"), this.x, this.y + 4, 0xe0e0e0);
            return;
        }

        this.cursor.sanitize();

        int actualRow = this.cursor.row - firstLine;
        String cursorLine = this.lines.get(this.cursor.row);
        int cursorX = this.x + 4 + this.textRenderer.getWidth(cursorLine.substring(0, this.cursor.column));
        int cursorY = this.y + 4 + actualRow * this.textRenderer.fontHeight;

        if (this.cursor.row < this.lines.size() - 1 || this.cursor.column < cursorLine.length() || this.doesLineOccupyFullSpace(cursorLine))
            fill(matrices, cursorX - 1, cursorY - 1, cursorX, cursorY + 9, 0xffe0e0e0);
        else
            this.textRenderer.drawWithShadow(matrices, "_", cursorX, cursorY, 0xe0e0e0);
    }

    protected boolean doesLineOccupyFullSpace(@NotNull String cursorLine)
    {
        return this.textRenderer.getWidth(cursorLine) >= this.getInnerWidth();
    }

    /**
     * Sanitizes the text area.
     */
    protected void sanitize()
    {
        if (this.lines.isEmpty())
            this.lines.add("");
        this.cursor.sanitize();
    }

    @Override
    public boolean changeFocus(boolean lookForwards)
    {
        return this.visible && this.editable && super.changeFocus(lookForwards);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY)
    {
        return this.visible && mouseX >= (double) this.x && mouseX < (double) (this.x + this.width) && mouseY >= (double) this.y && mouseY < (double) (this.y + this.height);
    }

    @Override
    public int getX()
    {
        return this.x;
    }

    @Override
    public int getY()
    {
        return this.y;
    }

    @Override
    public boolean isVisible()
    {
        return this.visible;
    }

    @Override
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    @Override
    public int getWidth()
    {
        return super.getWidth();
    }

    @Override
    public boolean isFocused()
    {
        return super.isFocused();
    }

    @Override
    public boolean isMouseHovered()
    {
        return this.hovered;
    }

    /**
     * Represents a cursor.
     *
     * @version 1.6.3
     * @since 1.6.3
     */
    public class Cursor
    {
        boolean main;
        int     row    = 0;
        int     column = 0;

        public Cursor(boolean main)
        {
            this.main = main;
        }

        public void toLineStart(){
            this.column = 0;
        }

        public void resetRow(){
            this.row = 0;
        }

        public void moveRight()
        {
            this.moveHorizontal(1);
        }

        public void moveLeft()
        {
            this.moveHorizontal(-1);
        }

        public void moveUp()
        {
            this.moveVertical(-1);
        }

        public void moveDown()
        {
            this.moveVertical(1);
        }

        public void moveHorizontal(int amount)
        {
            this.column += amount;

            if (this.row >= lines.size()) {
                this.row = lines.size() - 1;
                this.column = lines.get(this.row).length();
            }
            if (this.column < 0) {
                if (this.row == 0) this.toLineStart();
                else {
                    this.row -= 1;
                    this.column = lines.get(this.row).length();
                }
            }

            String line = lines.get(this.row);
            if (line.endsWith("\n")) line = line.substring(0, line.length() - 1);

            if (this.column > line.length()) {
                if (amount > 0 && this.row != lines.size() - 1) {
                    this.column = 0;
                    this.row += 1;
                } else
                    this.column = line.length();
            }

            this.adjustFirstLine();
        }

        public void moveVertical(int amount)
        {
            this.row += amount;
            this.sanitize();

            this.adjustFirstLine();
        }

        public void toStart()
        {
            this.resetRow();
            this.toLineStart();
            this.adjustFirstLine();
        }

        public void toEnd()
        {
            this.row = lines.size() - 1;
            String line = lines.get(this.row);
            if (line.endsWith("\n"))
                this.column = line.length() - 1;
            else
                this.column = line.length();
            this.adjustFirstLine();
        }

        public void toRowEnd()
        {
            String line = lines.get(this.row);
            if (line.endsWith("\n"))
                this.column = line.length() - 1;
            else
                this.column = line.length();
        }

        /**
         * Copies the row and column from another cursor.
         *
         * @param cursor The other cursor.
         */
        public void copy(@NotNull Cursor cursor)
        {
            this.row = cursor.row;
            this.column = cursor.column;
        }

        /**
         * Sanitizes the cursor.
         */
        public void sanitize()
        {
            if (lines.size() <= this.row)
                this.row = lines.size() - 1;
            if (this.row < 0)
                this.resetRow();

            String line = lines.get(this.row);
            if (line.endsWith("\n")) line = line.substring(0, line.length() - 1);
            if (this.column > line.length())
                this.column = line.length();

            this.adjustFirstLine();
        }

        /**
         * Returns whether this cursor is at the same place as the other cursor.
         *
         * @param other The other cursor.
         * @return True if this cursor is at the same place as the other cursor, else false.
         */
        public boolean isSame(@NotNull Cursor other)
        {
            return this.row == other.row && this.column == other.column;
        }

        /**
         * Returns the position of the cursor in the text.
         *
         * @return The position.
         */
        public int getPosition()
        {
            int position = 0;
            for (int i = 0; i < this.row; i++) position += lines.get(i).length();
            for (int i = 0; i < this.column; i++) position += 1;
            return position;
        }

        private void adjustFirstLine()
        {
            if (!this.main)
                return;

            if (firstLine > this.row)
                firstLine = this.row;

            if (firstLine == lines.size())
                firstLine--;
            int endLine = firstLine + displayedLines - 1;
            if (endLine >= lines.size()) {
                firstLine = lines.size() - displayedLines - 1;
            }

            if (this.row >= firstLine + displayedLines)
                firstLine = this.row - displayedLines + 1;

            if (firstLine < 0)
                firstLine = 0;
        }

        @Override
        public String toString()
        {
            return "SpruceTextAreaWidget$Cursor{main=" + this.main
                    + ", row=" + this.row
                    + ", column=" + this.column
                    + "}";
        }
    }

    /**
     * Represents a selection.
     *
     * @version 1.6.3
     * @since 1.6.3
     */
    public class Selection
    {
        private Cursor  anchor   = new Cursor(false);
        private Cursor  follower = new Cursor(false);
        private boolean active   = false;

        /**
         * Selects all.
         */
        public void selectAll()
        {
            this.anchor.toStart();
            cursor.toEnd();
            this.follower.copy(cursor);
            this.active = true;
        }

        /**
         * Cancels the selection.
         */
        public void cancel()
        {
            this.anchor.toStart();
            this.follower.toStart();
            this.active = false;
        }

        public void tryStartSelection()
        {
            if (!this.active && Screen.hasShiftDown()) {
                this.startSelection();
            }
        }

        public void startSelection()
        {
            this.anchor.copy(cursor);
            this.follower.copy(cursor);
            this.active = true;
        }

        public boolean isRowSelected(int row)
        {
            return this.active && (this.getStart().row <= row && row <= this.getEnd().row);
        }

        public void moveToCursor()
        {
            if (!this.active)
                return;

            if (Screen.hasShiftDown()) {
                this.follower.copy(cursor);
            } else {
                this.cancel();
            }
        }

        /**
         * Erases the selected text.
         *
         * @return True if the text has been erased, else false.
         */
        public boolean erase()
        {
            if (!this.active)
                return false;

            Cursor start = this.getStart();
            Cursor end = this.getEnd();

            if (start.isSame(end)) {
                this.cancel();
                return false;
            }

            if (start.row == 0 && start.column == 0 && end.row == lines.size() - 1 && end.column >= lines.get(lines.size() - 1).length()) {
                lines.clear();
                lines.add("");
                this.cancel();
                return true;
            }

            if (start.row == end.row) {
                String line = lines.get(start.row);

                if (start.column == 0 && end.column >= line.length())
                    lines.remove(start.row);
                else
                    lines.replaceRow(start.row, line.substring(0, start.column) + line.substring(end.column));

                cursor.copy(start);
                this.cancel();
                return true;
            }

            String text = getText();
            String newText = text.substring(0, start.getPosition()) + text.substring(end.getPosition());

            lines.clear();
            lines.add(newText);

            cursor.copy(start);

            this.cancel();
            return true;
        }

        /**
         * Returns the selected text.
         *
         * @return The selected text, if no text is selected the return value is an empty string.
         */
        public @NotNull String getSelectedText()
        {
            if (!this.active)
                return "";

            Cursor start = this.getStart();
            Cursor end = this.getEnd();

            if (start.isSame(end))
                return "";

            if (start.row == end.row)
                return lines.get(start.row).substring(start.column, end.column);

            return getText().substring(start.getPosition(), end.getPosition());
        }

        public @NotNull Cursor getStart()
        {
            return this.isInverted() ? this.follower : this.anchor;
        }

        public @NotNull Cursor getEnd()
        {
            return this.isInverted() ? this.anchor : this.follower;
        }

        private boolean isInverted()
        {
            return this.anchor.row > this.follower.row || (this.anchor.row == this.follower.row && this.anchor.column > this.follower.column);
        }
    }
}
