package info.u_team.u_team_core.gui.elements.backport;

import java.util.function.*;

import javax.annotation.Nullable;

import com.google.common.base.Predicates;

import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class GuiTextFieldNew extends Gui implements IGuiEventListener {
	
	private final int id;
	private final FontRenderer fontRenderer;
	public int x;
	public int y;
	/** The width of this text field. */
	public int width;
	public int height;
	/** Has the current text being edited on the textbox. */
	private String text = "";
	private int maxStringLength = 32;
	private int cursorCounter;
	private boolean enableBackgroundDrawing = true;
	/** if true the textbox can lose focus by clicking elsewhere on the screen */
	private boolean canLoseFocus = true;
	/** If this value is true along with isEnabled, keyTyped will process the keys. */
	private boolean isFocused;
	/** If this value is true along with isFocused, keyTyped will process the keys. */
	private boolean isEnabled = true;
	/** The current character index that should be used as start of the rendered text. */
	private int lineScrollOffset;
	private int cursorPosition;
	/** other selection position, maybe the same as the cursor */
	private int selectionEnd;
	private int enabledColor = 14737632;
	private int disabledColor = 7368816;
	/** True if this textbox is visible */
	private boolean visible = true;
	private String suggestion;
	private BiConsumer<Integer, String> guiResponder;
	/** Called to check if the text is valid */
	private Predicate<String> validator = Predicates.alwaysTrue();
	private BiFunction<String, Integer, String> textFormatter = (p_195610_0_, p_195610_1_) -> {
		return p_195610_0_;
	};
	
	public GuiTextFieldNew(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
		this(componentId, fontrendererObj, x, y, par5Width, par6Height, (GuiTextFieldNew) null);
	}
	
	public GuiTextFieldNew(int p_i49853_1_, FontRenderer p_i49853_2_, int p_i49853_3_, int p_i49853_4_, int p_i49853_5_, int p_i49853_6_, @Nullable GuiTextFieldNew p_i49853_7_) {
		this.id = p_i49853_1_;
		this.fontRenderer = p_i49853_2_;
		this.x = p_i49853_3_;
		this.y = p_i49853_4_;
		this.width = p_i49853_5_;
		this.height = p_i49853_6_;
		if (p_i49853_7_ != null) {
			this.setText(p_i49853_7_.getText());
		}
		
	}
	
	public void setTextAcceptHandler(BiConsumer<Integer, String> p_195609_1_) {
		this.guiResponder = p_195609_1_;
	}
	
	public void setTextFormatter(BiFunction<String, Integer, String> p_195607_1_) {
		this.textFormatter = p_195607_1_;
	}
	
	/**
	 * Increments the cursor counter
	 */
	public void tick() {
		++this.cursorCounter;
	}
	
	/**
	 * Sets the text of the textbox, and moves the cursor to the end.
	 */
	public void setText(String textIn) {
		if (this.validator.test(textIn)) {
			if (textIn.length() > this.maxStringLength) {
				this.text = textIn.substring(0, this.maxStringLength);
			} else {
				this.text = textIn;
			}
			
			this.setResponderEntryValue(this.id, textIn);
			this.setCursorPositionEnd();
		}
	}
	
	/**
	 * Returns the contents of the textbox
	 */
	public String getText() {
		return this.text;
	}
	
	/**
	 * returns the text between the cursor and selectionEnd
	 */
	public String getSelectedText() {
		int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
		int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
		return this.text.substring(i, j);
	}
	
	public void setValidator(Predicate<String> p_200675_1_) {
		this.validator = p_200675_1_;
	}
	
	/**
	 * Adds the given text after the cursor, or replaces the currently selected text if there is a selection.
	 */
	public void writeText(String textToWrite) {
		String s = "";
		String s1 = ChatAllowedCharacters.filterAllowedCharacters(textToWrite);
		int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
		int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
		int k = this.maxStringLength - this.text.length() - (i - j);
		if (!this.text.isEmpty()) {
			s = s + this.text.substring(0, i);
		}
		
		int l;
		if (k < s1.length()) {
			s = s + s1.substring(0, k);
			l = k;
		} else {
			s = s + s1;
			l = s1.length();
		}
		
		if (!this.text.isEmpty() && j < this.text.length()) {
			s = s + this.text.substring(j);
		}
		
		if (this.validator.test(s)) {
			this.text = s;
			this.moveCursorBy(i - this.selectionEnd + l);
			this.setResponderEntryValue(this.id, this.text);
		}
	}
	
	/**
	 * Notifies this text box's {@linkplain GuiPageButtonList.GuiResponder responder} that the text has changed.
	 */
	public void setResponderEntryValue(int idIn, String textIn) {
		if (this.guiResponder != null) {
			this.guiResponder.accept(idIn, textIn);
		}
		
	}
	
	/**
	 * Deletes the given number of words from the current cursor's position, unless there is currently a selection, in which
	 * case the selection is deleted instead.
	 */
	public void deleteWords(int num) {
		if (!this.text.isEmpty()) {
			if (this.selectionEnd != this.cursorPosition) {
				this.writeText("");
			} else {
				this.deleteFromCursor(this.getNthWordFromCursor(num) - this.cursorPosition);
			}
		}
	}
	
	/**
	 * Deletes the given number of characters from the current cursor's position, unless there is currently a selection, in
	 * which case the selection is deleted instead.
	 */
	public void deleteFromCursor(int num) {
		if (!this.text.isEmpty()) {
			if (this.selectionEnd != this.cursorPosition) {
				this.writeText("");
			} else {
				boolean flag = num < 0;
				int i = flag ? this.cursorPosition + num : this.cursorPosition;
				int j = flag ? this.cursorPosition : this.cursorPosition + num;
				String s = "";
				if (i >= 0) {
					s = this.text.substring(0, i);
				}
				
				if (j < this.text.length()) {
					s = s + this.text.substring(j);
				}
				
				if (this.validator.test(s)) {
					this.text = s;
					if (flag) {
						this.moveCursorBy(num);
					}
					
					this.setResponderEntryValue(this.id, this.text);
				}
			}
		}
	}
	
	/**
	 * Gets the starting index of the word at the specified number of words away from the cursor position.
	 */
	public int getNthWordFromCursor(int numWords) {
		return this.getNthWordFromPos(numWords, this.getCursorPosition());
	}
	
	/**
	 * Gets the starting index of the word at a distance of the specified number of words away from the given position.
	 */
	public int getNthWordFromPos(int n, int pos) {
		return this.getNthWordFromPosWS(n, pos, true);
	}
	
	/**
	 * Like getNthWordFromPos (which wraps this), but adds option for skipping consecutive spaces
	 */
	public int getNthWordFromPosWS(int n, int pos, boolean skipWs) {
		int i = pos;
		boolean flag = n < 0;
		int j = Math.abs(n);
		
		for (int k = 0; k < j; ++k) {
			if (!flag) {
				int l = this.text.length();
				i = this.text.indexOf(32, i);
				if (i == -1) {
					i = l;
				} else {
					while (skipWs && i < l && this.text.charAt(i) == ' ') {
						++i;
					}
				}
			} else {
				while (skipWs && i > 0 && this.text.charAt(i - 1) == ' ') {
					--i;
				}
				
				while (i > 0 && this.text.charAt(i - 1) != ' ') {
					--i;
				}
			}
		}
		
		return i;
	}
	
	/**
	 * Moves the text cursor by a specified number of characters and clears the selection
	 */
	public void moveCursorBy(int num) {
		this.setCursorPosition(this.selectionEnd + num);
	}
	
	/**
	 * Sets the current position of the cursor.
	 */
	public void setCursorPosition(int pos) {
		this.func_212422_f(pos);
		this.setSelectionPos(this.cursorPosition);
		this.setResponderEntryValue(this.id, this.text);
	}
	
	public void func_212422_f(int p_212422_1_) {
		this.cursorPosition = MathHelper.clamp(p_212422_1_, 0, this.text.length());
	}
	
	/**
	 * Moves the cursor to the very start of this text box.
	 */
	public void setCursorPositionZero() {
		this.setCursorPosition(0);
	}
	
	/**
	 * Moves the cursor to the very end of this text box.
	 */
	public void setCursorPositionEnd() {
		this.setCursorPosition(this.text.length());
	}
	
	public boolean keyPressed(int keyCode, int p_keyPressed_2_, int p_keyPressed_3_) {
		if (this.getVisible() && this.isFocused()) {
			if (GuiScreen.isKeyComboCtrlA(keyCode)) {
				this.setCursorPositionEnd();
				this.setSelectionPos(0);
				return true;
			} else if (GuiScreen.isKeyComboCtrlC(keyCode)) {
				GuiScreen.setClipboardString(this.getSelectedText());
				return true;
			} else if (GuiScreen.isKeyComboCtrlV(keyCode)) {
				if (this.isEnabled) {
					this.writeText(GuiScreen.getClipboardString());
				}
				
				return true;
			} else if (GuiScreen.isKeyComboCtrlX(keyCode)) {
				GuiScreen.setClipboardString(this.getSelectedText());
				
				if (this.isEnabled) {
					this.writeText("");
				}
				
				return true;
			} else {
				switch (keyCode) {
				case 14:
					
					if (GuiScreen.isCtrlKeyDown()) {
						if (this.isEnabled) {
							this.deleteWords(-1);
						}
					} else if (this.isEnabled) {
						this.deleteFromCursor(-1);
					}
					
					return true;
				case 199:
					
					if (GuiScreen.isShiftKeyDown()) {
						this.setSelectionPos(0);
					} else {
						this.setCursorPositionZero();
					}
					
					return true;
				case 203:
					
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) {
							this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
						} else {
							this.setSelectionPos(this.getSelectionEnd() - 1);
						}
					} else if (GuiScreen.isCtrlKeyDown()) {
						this.setCursorPosition(this.getNthWordFromCursor(-1));
					} else {
						this.moveCursorBy(-1);
					}
					
					return true;
				case 205:
					
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) {
							this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
						} else {
							this.setSelectionPos(this.getSelectionEnd() + 1);
						}
					} else if (GuiScreen.isCtrlKeyDown()) {
						this.setCursorPosition(this.getNthWordFromCursor(1));
					} else {
						this.moveCursorBy(1);
					}
					
					return true;
				case 207:
					
					if (GuiScreen.isShiftKeyDown()) {
						this.setSelectionPos(this.text.length());
					} else {
						this.setCursorPositionEnd();
					}
					
					return true;
				case 211:
					
					if (GuiScreen.isCtrlKeyDown()) {
						if (this.isEnabled) {
							this.deleteWords(1);
						}
					} else if (this.isEnabled) {
						this.deleteFromCursor(1);
					}
					
					return true;
				default:
					return false;
				}
			}
		} else {
			return false;
		}
	}
	
	public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
		if (this.getVisible() && this.isFocused()) {
			if (ChatAllowedCharacters.isAllowedCharacter(p_charTyped_1_)) {
				if (this.isEnabled) {
					this.writeText(Character.toString(p_charTyped_1_));
				}
				
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		if (!this.getVisible()) {
			return false;
		} else {
			boolean flag = p_mouseClicked_1_ >= (double) this.x && p_mouseClicked_1_ < (double) (this.x + this.width) && p_mouseClicked_3_ >= (double) this.y && p_mouseClicked_3_ < (double) (this.y + this.height);
			if (this.canLoseFocus) {
				this.setFocused(flag);
			}
			
			if (this.isFocused && flag && p_mouseClicked_5_ == 0) {
				int i = MathHelper.floor(p_mouseClicked_1_) - this.x;
				if (this.enableBackgroundDrawing) {
					i -= 4;
				}
				
				String s = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
				this.setCursorPosition(this.fontRenderer.trimStringToWidth(s, i).length() + this.lineScrollOffset);
				return true;
			} else {
				return false;
			}
		}
	}
	
	public void drawTextField(int mouseX, int mouseY, float partialTicks) {
		if (this.getVisible()) {
			if (this.getEnableBackgroundDrawing()) {
				drawRect(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
				drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
			}
			
			int i = this.isEnabled ? this.enabledColor : this.disabledColor;
			int j = this.cursorPosition - this.lineScrollOffset;
			int k = this.selectionEnd - this.lineScrollOffset;
			String s = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
			boolean flag = j >= 0 && j <= s.length();
			boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
			int l = this.enableBackgroundDrawing ? this.x + 4 : this.x;
			int i1 = this.enableBackgroundDrawing ? this.y + (this.height - 8) / 2 : this.y;
			int j1 = l;
			if (k > s.length()) {
				k = s.length();
			}
			
			if (!s.isEmpty()) {
				String s1 = flag ? s.substring(0, j) : s;
				j1 = this.fontRenderer.drawStringWithShadow(this.textFormatter.apply(s1, this.lineScrollOffset), (float) l, (float) i1, i);
			}
			
			boolean flag2 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
			int k1 = j1;
			if (!flag) {
				k1 = j > 0 ? l + this.width : l;
			} else if (flag2) {
				k1 = j1 - 1;
				--j1;
			}
			
			if (!s.isEmpty() && flag && j < s.length()) {
				j1 = this.fontRenderer.drawStringWithShadow(this.textFormatter.apply(s.substring(j), this.cursorPosition), (float) j1, (float) i1, i);
			}
			
			if (!flag2 && this.suggestion != null) {
				this.fontRenderer.drawStringWithShadow(this.suggestion, (float) (k1 - 1), (float) i1, -8355712);
			}
			
			if (flag1) {
				if (flag2) {
					Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + this.fontRenderer.FONT_HEIGHT, -3092272);
				} else {
					this.fontRenderer.drawStringWithShadow("_", (float) k1, (float) i1, i);
				}
			}
			
			if (k != j) {
				int l1 = l + this.fontRenderer.getStringWidth(s.substring(0, k));
				this.drawSelectionBox(k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRenderer.FONT_HEIGHT);
			}
			
		}
	}
	
	/**
	 * Draws the blue selection box.
	 */
	private void drawSelectionBox(int startX, int startY, int endX, int endY) {
		if (startX < endX) {
			int i = startX;
			startX = endX;
			endX = i;
		}
		
		if (startY < endY) {
			int j = startY;
			startY = endY;
			endY = j;
		}
		
		if (endX > this.x + this.width) {
			endX = this.x + this.width;
		}
		
		if (startX > this.x + this.width) {
			startX = this.x + this.width;
		}
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.enableColorLogic();
		GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos((double) startX, (double) endY, 0.0D).endVertex();
		bufferbuilder.pos((double) endX, (double) endY, 0.0D).endVertex();
		bufferbuilder.pos((double) endX, (double) startY, 0.0D).endVertex();
		bufferbuilder.pos((double) startX, (double) startY, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.disableColorLogic();
		GlStateManager.enableTexture2D();
	}
	
	/**
	 * Sets the maximum length for the text in this text box. If the current text is longer than this length, the current
	 * text will be trimmed.
	 */
	public void setMaxStringLength(int length) {
		this.maxStringLength = length;
		if (this.text.length() > length) {
			this.text = this.text.substring(0, length);
			this.setResponderEntryValue(this.id, this.text);
		}
		
	}
	
	/**
	 * returns the maximum number of character that can be contained in this textbox
	 */
	public int getMaxStringLength() {
		return this.maxStringLength;
	}
	
	/**
	 * returns the current position of the cursor
	 */
	public int getCursorPosition() {
		return this.cursorPosition;
	}
	
	/**
	 * Gets whether the background and outline of this text box should be drawn (true if so).
	 */
	public boolean getEnableBackgroundDrawing() {
		return this.enableBackgroundDrawing;
	}
	
	/**
	 * Sets whether or not the background and outline of this text box should be drawn.
	 */
	public void setEnableBackgroundDrawing(boolean enableBackgroundDrawingIn) {
		this.enableBackgroundDrawing = enableBackgroundDrawingIn;
	}
	
	/**
	 * Sets the color to use when drawing this text box's text. A different color is used if this text box is disabled.
	 */
	public void setTextColor(int color) {
		this.enabledColor = color;
	}
	
	/**
	 * Sets the color to use for text in this text box when this text box is disabled.
	 */
	public void setDisabledTextColour(int color) {
		this.disabledColor = color;
	}
	
	public void focusChanged(boolean focused) {
		this.setFocused(focused);
	}
	
	public boolean canFocus() {
		return true;
	}
	
	/**
	 * Sets focus to this gui element
	 */
	public void setFocused(boolean isFocusedIn) {
		if (isFocusedIn && !this.isFocused) {
			this.cursorCounter = 0;
		}
		
		this.isFocused = isFocusedIn;
	}
	
	/**
	 * Getter for the focused field
	 */
	public boolean isFocused() {
		return this.isFocused;
	}
	
	/**
	 * Sets whether this text box is enabled. Disabled text boxes cannot be typed in.
	 */
	public void setEnabled(boolean enabled) {
		this.isEnabled = enabled;
	}
	
	/**
	 * the side of the selection that is not the cursor, may be the same as the cursor
	 */
	public int getSelectionEnd() {
		return this.selectionEnd;
	}
	
	/**
	 * returns the width of the textbox depending on if background drawing is enabled
	 */
	public int getWidth() {
		return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
	}
	
	/**
	 * Sets the position of the selection anchor (the selection anchor and the cursor position mark the edges of the
	 * selection). If the anchor is set beyond the bounds of the current text, it will be put back inside.
	 */
	public void setSelectionPos(int position) {
		int i = this.text.length();
		if (position > i) {
			position = i;
		}
		
		if (position < 0) {
			position = 0;
		}
		
		this.selectionEnd = position;
		if (this.fontRenderer != null) {
			if (this.lineScrollOffset > i) {
				this.lineScrollOffset = i;
			}
			
			int j = this.getWidth();
			String s = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
			int k = s.length() + this.lineScrollOffset;
			if (position == this.lineScrollOffset) {
				this.lineScrollOffset -= this.fontRenderer.trimStringToWidth(this.text, j, true).length();
			}
			
			if (position > k) {
				this.lineScrollOffset += position - k;
			} else if (position <= this.lineScrollOffset) {
				this.lineScrollOffset -= this.lineScrollOffset - position;
			}
			
			this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, i);
		}
		
	}
	
	/**
	 * Sets whether this text box loses focus when something other than it is clicked.
	 */
	public void setCanLoseFocus(boolean canLoseFocusIn) {
		this.canLoseFocus = canLoseFocusIn;
	}
	
	/**
	 * returns true if this textbox is visible
	 */
	public boolean getVisible() {
		return this.visible;
	}
	
	/**
	 * Sets whether or not this textbox is visible
	 */
	public void setVisible(boolean isVisible) {
		this.visible = isVisible;
	}
	
	public void setSuggestion(@Nullable String p_195612_1_) {
		this.suggestion = p_195612_1_;
	}
	
	public int func_195611_j(int p_195611_1_) {
		return p_195611_1_ > this.text.length() ? this.x : this.x + this.fontRenderer.getStringWidth(this.text.substring(0, p_195611_1_));
	}
}