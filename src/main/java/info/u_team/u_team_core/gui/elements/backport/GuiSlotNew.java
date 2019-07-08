package info.u_team.u_team_core.gui.elements.backport;

import java.util.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public abstract class GuiSlotNew extends GuiEventHandlerNew {
	
	protected final Minecraft mc;
	public int width;
	public int height;
	/** The top of the slot container. Affects the overlays and scrolling. */
	public int top;
	/** The bottom of the slot container. Affects the overlays and scrolling. */
	public int bottom;
	public int right;
	public int left;
	/** The height of a slot. */
	public final int slotHeight;
	protected boolean centerListVertically = true;
	/** Where the mouse was in the window when you first clicked to scroll */
	protected int initialClickY = -2;
	/** How far down this slot has been scrolled */
	protected double amountScrolled;
	/** The element in the list that was selected */
	protected int selectedElement;
	/** The time when this button was last clicked. */
	protected long lastClicked = Long.MIN_VALUE;
	protected boolean visible = true;
	/** Set to true if a selected element in this gui will show an outline box */
	protected boolean showSelectionBox = true;
	protected boolean hasListHeader;
	public int headerPadding;
	/**
	 * True if the scrollbar was clicked when the mouse went down; used to determine if the scrollbar should be used to
	 * drag.
	 */
	private boolean clickedScrollbar;
	
	public GuiSlotNew(Minecraft mcIn, int width, int height, int topIn, int bottomIn, int slotHeightIn) {
		this.mc = mcIn;
		this.width = width;
		this.height = height;
		this.top = topIn;
		this.bottom = bottomIn;
		this.slotHeight = slotHeightIn;
		this.left = 0;
		this.right = width;
	}
	
	public void setDimensions(int widthIn, int heightIn, int topIn, int bottomIn) {
		this.width = widthIn;
		this.height = heightIn;
		this.top = topIn;
		this.bottom = bottomIn;
		this.left = 0;
		this.right = widthIn;
	}
	
	public void setShowSelectionBox(boolean showSelectionBoxIn) {
		this.showSelectionBox = showSelectionBoxIn;
	}
	
	/**
	 * Sets hasListHeader and headerHeight. Params: hasListHeader, headerHeight. If hasListHeader is false headerHeight is
	 * set to 0.
	 */
	protected void setHasListHeader(boolean hasListHeaderIn, int headerPaddingIn) {
		this.hasListHeader = hasListHeaderIn;
		this.headerPadding = headerPaddingIn;
		if (!hasListHeaderIn) {
			this.headerPadding = 0;
		}
		
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	protected abstract int getSize();
	
	/**
	 * Called when the given entry is selected; sets {@link #selectedElement} to the index and updates {@link #lastClicked}.
	 */
	public void setSelectedEntry(int index) {
	}
	
	/**
	 * Gets a mutable list of child listeners. For a {@link GuiListExtended}, this is a list of the entries of the list (in
	 * the order they are displayed); for a {@link GuiScreen} this is the sub-controls.
	 */
	protected List<? extends IGuiEventListener> getChildren() {
		return Collections.emptyList();
	}
	
	/**
	 * Called when the mouse is clicked onto an entry.
	 * 
	 * @return true if the entry did something with the click and it should be selected.
	 */
	protected boolean mouseClicked(int index, int button, double mouseX, double mouseY) {
		return true;
	}
	
	/**
	 * Returns true if the element passed in is currently selected
	 */
	protected abstract boolean isSelected(int slotIndex);
	
	/**
	 * Return the height of the content being scrolled
	 */
	protected int getContentHeight() {
		return this.getSize() * this.slotHeight + this.headerPadding;
	}
	
	protected abstract void drawBackground();
	
	protected void updateItemPos(int entryID, int insideLeft, int yPos, float partialTicks) {
	}
	
	protected abstract void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks);
	
	/**
	 * Handles drawing a list's header row.
	 */
	protected void drawListHeader(int insideLeft, int insideTop, Tessellator tessellatorIn) {
	}
	
	/**
	 * Called when the mouse left-clicks the header or anywhere else that isn't on an entry.
	 */
	protected void clickedHeader(int p_148132_1_, int p_148132_2_) {
	}
	
	protected void renderDecorations(int mouseXIn, int mouseYIn) {
	}
	
	/**
	 * Gets the entry at the given position on screen.
	 * 
	 * @return The entry's index, or -1 if no entry is at that position (e.g. wrong x coordinate, or below the bottom of the
	 *         list)
	 */
	public int getEntryAt(double x, double y) {
		int i = this.left + this.width / 2 - this.getListWidth() / 2;
		int j = this.left + this.width / 2 + this.getListWidth() / 2;
		int k = floor(y - (double) this.top) - this.headerPadding + (int) this.amountScrolled - 4;
		int l = k / this.slotHeight;
		return x < (double) this.getScrollBarX() && x >= (double) i && x <= (double) j && l >= 0 && k >= 0 && l < this.getSize() ? l : -1;
	}
	
	public static int floor(double value) {
		int i = (int) value;
		return value < (double) i ? i - 1 : i;
	}
	
	/**
	 * Stop the thing from scrolling out of bounds
	 */
	protected void bindAmountScrolled() {
		this.amountScrolled = clamp(this.amountScrolled, 0.0D, (double) this.getMaxScroll());
	}
	
	public static double clamp(double num, double min, double max) {
		if (num < min) {
			return min;
		} else {
			return num > max ? max : num;
		}
	}
	
	public int getMaxScroll() {
		return Math.max(0, this.getContentHeight() - (this.bottom - this.top - 4));
	}
	
	/**
	 * Returns the amountScrolled field as an integer.
	 */
	public int getAmountScrolled() {
		return (int) this.amountScrolled;
	}
	
	public boolean isMouseInList(double p_195079_1_, double p_195079_3_) {
		return p_195079_3_ >= (double) this.top && p_195079_3_ <= (double) this.bottom && p_195079_1_ >= (double) this.left && p_195079_1_ <= (double) this.right;
	}
	
	/**
	 * Scrolls the slot by the given amount. A positive value scrolls down, and a negative value scrolls up.
	 */
	public void scrollBy(int amount) {
		this.amountScrolled += (double) amount;
		this.bindAmountScrolled();
		this.initialClickY = -2;
	}
	
	public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks) {
		if (this.visible) {
			this.drawBackground();
			int k = this.getScrollBarX();
			int l = k + 6;
			this.bindAmountScrolled();
			GlStateManager.disableLighting();
			GlStateManager.disableFog();
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			this.drawContainerBackground(tessellator);
			int i1 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
			int j1 = this.top + 4 - (int) this.amountScrolled;
			
			if (this.hasListHeader) {
				this.drawListHeader(i1, j1, tessellator);
			}
			
			this.drawSelectionBox(i1, j1, mouseXIn, mouseYIn, partialTicks);
			GlStateManager.disableDepth();
			byte b0 = 4;
			this.overlayBackground(0, this.top, 255, 255);
			this.overlayBackground(this.bottom, this.height, 255, 255);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
			GlStateManager.disableAlpha();
			GlStateManager.shadeModel(7425);
			GlStateManager.disableTexture2D();
			worldrenderer.startDrawingQuads();
			worldrenderer.setColorRGBA_I(0, 0);
			worldrenderer.addVertexWithUV((double) this.left, (double) (this.top + b0), 0.0D, 0.0D, 1.0D);
			worldrenderer.addVertexWithUV((double) this.right, (double) (this.top + b0), 0.0D, 1.0D, 1.0D);
			worldrenderer.setColorRGBA_I(0, 255);
			worldrenderer.addVertexWithUV((double) this.right, (double) this.top, 0.0D, 1.0D, 0.0D);
			worldrenderer.addVertexWithUV((double) this.left, (double) this.top, 0.0D, 0.0D, 0.0D);
			tessellator.draw();
			worldrenderer.startDrawingQuads();
			worldrenderer.setColorRGBA_I(0, 255);
			worldrenderer.addVertexWithUV((double) this.left, (double) this.bottom, 0.0D, 0.0D, 1.0D);
			worldrenderer.addVertexWithUV((double) this.right, (double) this.bottom, 0.0D, 1.0D, 1.0D);
			worldrenderer.setColorRGBA_I(0, 0);
			worldrenderer.addVertexWithUV((double) this.right, (double) (this.bottom - b0), 0.0D, 1.0D, 0.0D);
			worldrenderer.addVertexWithUV((double) this.left, (double) (this.bottom - b0), 0.0D, 0.0D, 0.0D);
			tessellator.draw();
			int k1 = this.getMaxScroll();
			
			if (k1 > 0) {
				int l1 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
				l1 = MathHelper.clamp_int(l1, 32, this.bottom - this.top - 8);
				int i2 = (int) this.amountScrolled * (this.bottom - this.top - l1) / k1 + this.top;
				
				if (i2 < this.top) {
					i2 = this.top;
				}
				
				worldrenderer.startDrawingQuads();
				worldrenderer.setColorRGBA_I(0, 255);
				worldrenderer.addVertexWithUV((double) k, (double) this.bottom, 0.0D, 0.0D, 1.0D);
				worldrenderer.addVertexWithUV((double) l, (double) this.bottom, 0.0D, 1.0D, 1.0D);
				worldrenderer.addVertexWithUV((double) l, (double) this.top, 0.0D, 1.0D, 0.0D);
				worldrenderer.addVertexWithUV((double) k, (double) this.top, 0.0D, 0.0D, 0.0D);
				tessellator.draw();
				worldrenderer.startDrawingQuads();
				worldrenderer.setColorRGBA_I(8421504, 255);
				worldrenderer.addVertexWithUV((double) k, (double) (i2 + l1), 0.0D, 0.0D, 1.0D);
				worldrenderer.addVertexWithUV((double) l, (double) (i2 + l1), 0.0D, 1.0D, 1.0D);
				worldrenderer.addVertexWithUV((double) l, (double) i2, 0.0D, 1.0D, 0.0D);
				worldrenderer.addVertexWithUV((double) k, (double) i2, 0.0D, 0.0D, 0.0D);
				tessellator.draw();
				worldrenderer.startDrawingQuads();
				worldrenderer.setColorRGBA_I(12632256, 255);
				worldrenderer.addVertexWithUV((double) k, (double) (i2 + l1 - 1), 0.0D, 0.0D, 1.0D);
				worldrenderer.addVertexWithUV((double) (l - 1), (double) (i2 + l1 - 1), 0.0D, 1.0D, 1.0D);
				worldrenderer.addVertexWithUV((double) (l - 1), (double) i2, 0.0D, 1.0D, 0.0D);
				worldrenderer.addVertexWithUV((double) k, (double) i2, 0.0D, 0.0D, 0.0D);
				tessellator.draw();
			}
			
			this.renderDecorations(mouseXIn, mouseYIn);
			GlStateManager.enableTexture2D();
			GlStateManager.shadeModel(7424);
			GlStateManager.enableAlpha();
			GlStateManager.disableBlend();
		}
	}
	
	/**
	 * Checks if the mouse clicked on the scrollbar.
	 */
	protected void checkScrollbarClick(double mouseX, double mouseY, int button) {
		this.clickedScrollbar = button == 0 && mouseX >= (double) this.getScrollBarX() && mouseX < (double) (this.getScrollBarX() + 6);
	}
	
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		this.checkScrollbarClick(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
		if (this.isVisible() && this.isMouseInList(p_mouseClicked_1_, p_mouseClicked_3_)) {
			int i = this.getEntryAt(p_mouseClicked_1_, p_mouseClicked_3_);
			if (i == -1 && p_mouseClicked_5_ == 0) {
				this.clickedHeader((int) (p_mouseClicked_1_ - (double) (this.left + this.width / 2 - this.getListWidth() / 2)), (int) (p_mouseClicked_3_ - (double) this.top) + (int) this.amountScrolled - 4);
				return true;
			} else if (i != -1 && this.mouseClicked(i, p_mouseClicked_5_, p_mouseClicked_1_, p_mouseClicked_3_)) {
				if (this.getChildren().size() > i) {
					this.setFocused(this.getChildren().get(i));
				}
				
				this.setDragging(true);
				this.setSelectedEntry(i);
				return true;
			} else {
				return this.clickedScrollbar;
			}
		} else {
			return false;
		}
	}
	
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		if (this.getFocused() != null) {
			this.getFocused().mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
		}
		
		this.getChildren().forEach((p_195081_5_) -> {
			p_195081_5_.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
		});
		return false;
	}
	
	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		if (super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_)) {
			return true;
		} else if (this.isVisible() && p_mouseDragged_5_ == 0 && this.clickedScrollbar) {
			if (p_mouseDragged_3_ < (double) this.top) {
				this.amountScrolled = 0.0D;
			} else if (p_mouseDragged_3_ > (double) this.bottom) {
				this.amountScrolled = (double) this.getMaxScroll();
			} else {
				double d0 = (double) this.getMaxScroll();
				if (d0 < 1.0D) {
					d0 = 1.0D;
				}
				
				int i = (int) ((float) ((this.bottom - this.top) * (this.bottom - this.top)) / (float) this.getContentHeight());
				i = clamp(i, 32, this.bottom - this.top - 8);
				double d1 = d0 / (double) (this.bottom - this.top - i);
				if (d1 < 1.0D) {
					d1 = 1.0D;
				}
				
				this.amountScrolled += p_mouseDragged_8_ * d1;
				this.bindAmountScrolled();
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	public static int clamp(int num, int min, int max) {
		if (num < min) {
			return min;
		} else {
			return num > max ? max : num;
		}
	}
	
	public boolean mouseScrolled(double p_mouseScrolled_1_) {
		if (!this.isVisible()) {
			return false;
		} else {
			this.amountScrolled -= p_mouseScrolled_1_ * (double) this.slotHeight / 2.0D;
			return true;
		}
	}
	
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		return !this.isVisible() ? false : super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
	}
	
	public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
		return !this.isVisible() ? false : super.charTyped(p_charTyped_1_, p_charTyped_2_);
	}
	
	/**
	 * Gets the width of the list
	 */
	public int getListWidth() {
		return 220;
	}
	
	/**
	 * Draws the selection box around the selected slot element.
	 */
	protected void drawSelectionBox(int insideLeft, int insideTop, int mouseXIn, int mouseYIn, float partialTicks) {
		int i = this.getSize();
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		
		for (int j = 0; j < i; ++j) {
			int k = insideTop + j * this.slotHeight + this.headerPadding;
			int l = this.slotHeight - 4;
			if (k > this.bottom || k + l < this.top) {
				this.updateItemPos(j, insideLeft, k, partialTicks);
			}
			
			if (this.showSelectionBox && this.isSelected(j)) {
				int i1 = this.left + this.width / 2 - this.getListWidth() / 2;
				int j1 = this.left + this.width / 2 + this.getListWidth() / 2;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.disableTexture2D();
				worldrenderer.startDrawingQuads();
				worldrenderer.setColorOpaque_I(8421504);
				worldrenderer.addVertexWithUV((double) i1, (double) (k + l + 2), 0.0D, 0.0D, 1.0D);
				worldrenderer.addVertexWithUV((double) j1, (double) (k + l + 2), 0.0D, 1.0D, 1.0D);
				worldrenderer.addVertexWithUV((double) j1, (double) (k - 2), 0.0D, 1.0D, 0.0D);
				worldrenderer.addVertexWithUV((double) i1, (double) (k - 2), 0.0D, 0.0D, 0.0D);
				worldrenderer.setColorOpaque_I(0);
				worldrenderer.addVertexWithUV((double) (i1 + 1), (double) (k + l + 1), 0.0D, 0.0D, 1.0D);
				worldrenderer.addVertexWithUV((double) (j1 - 1), (double) (k + l + 1), 0.0D, 1.0D, 1.0D);
				worldrenderer.addVertexWithUV((double) (j1 - 1), (double) (k - 1), 0.0D, 1.0D, 0.0D);
				worldrenderer.addVertexWithUV((double) (i1 + 1), (double) (k - 1), 0.0D, 0.0D, 0.0D);
				tessellator.draw();
				GlStateManager.enableTexture2D();
			}
			
			this.drawSlot(j, insideLeft, k, l, mouseXIn, mouseYIn, partialTicks);
		}
		
	}
	
	protected int getScrollBarX() {
		return this.width / 2 + 124;
	}
	
	/**
	 * Overlays the background to hide scrolled items
	 */
	protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		float f = 32.0F;
		worldrenderer.startDrawingQuads();
		worldrenderer.setColorRGBA_I(4210752, endAlpha);
		worldrenderer.addVertexWithUV((double) this.left, (double) endY, 0.0D, 0.0D, (double) ((float) endY / f));
		worldrenderer.addVertexWithUV((double) (this.left + this.width), (double) endY, 0.0D, (double) ((float) this.width / f), (double) ((float) endY / f));
		worldrenderer.setColorRGBA_I(4210752, startAlpha);
		worldrenderer.addVertexWithUV((double) (this.left + this.width), (double) startY, 0.0D, (double) ((float) this.width / f), (double) ((float) startY / f));
		worldrenderer.addVertexWithUV((double) this.left, (double) startY, 0.0D, 0.0D, (double) ((float) startY / f));
		tessellator.draw();
	}
	
	/**
	 * Sets the left and right bounds of the slot. Param is the left bound, right is calculated as left + width.
	 */
	public void setSlotXBoundsFromLeft(int leftIn) {
		this.left = leftIn;
		this.right = leftIn + this.width;
	}
	
	public int getSlotHeight() {
		return this.slotHeight;
	}
	
	protected void drawContainerBackground(Tessellator tessellator) {
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		float f1 = 32.0F;
		worldrenderer.startDrawingQuads();
		worldrenderer.setColorOpaque_I(2105376);
		worldrenderer.addVertexWithUV((double) this.left, (double) this.bottom, 0.0D, (double) ((float) this.left / f1), (double) ((float) (this.bottom + (int) this.amountScrolled) / f1));
		worldrenderer.addVertexWithUV((double) this.right, (double) this.bottom, 0.0D, (double) ((float) this.right / f1), (double) ((float) (this.bottom + (int) this.amountScrolled) / f1));
		worldrenderer.addVertexWithUV((double) this.right, (double) this.top, 0.0D, (double) ((float) this.right / f1), (double) ((float) (this.top + (int) this.amountScrolled) / f1));
		worldrenderer.addVertexWithUV((double) this.left, (double) this.top, 0.0D, (double) ((float) this.left / f1), (double) ((float) (this.top + (int) this.amountScrolled) / f1));
		tessellator.draw();
	}
}