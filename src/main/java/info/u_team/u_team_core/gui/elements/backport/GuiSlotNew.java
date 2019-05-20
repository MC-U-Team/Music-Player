package info.u_team.u_team_core.gui.elements.backport;

import java.util.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
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
		int k = MathHelper.floor(y - (double) this.top) - this.headerPadding + (int) this.amountScrolled - 4;
		int l = k / this.slotHeight;
		return x < (double) this.getScrollBarX() && x >= (double) i && x <= (double) j && l >= 0 && k >= 0 && l < this.getSize() ? l : -1;
	}
	
	/**
	 * Stop the thing from scrolling out of bounds
	 */
	protected void bindAmountScrolled() {
		this.amountScrolled = MathHelper.clamp(this.amountScrolled, 0.0D, (double) this.getMaxScroll());
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
			int i = this.getScrollBarX();
			int j = i + 6;
			this.bindAmountScrolled();
			GlStateManager.disableLighting();
			GlStateManager.disableFog();
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			// Forge: background rendering moved into separate method.
			this.drawContainerBackground(tessellator);
			int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
			int l = this.top + 4 - (int) this.amountScrolled;
			if (this.hasListHeader) {
				this.drawListHeader(k, l, tessellator);
			}
			
			this.drawSelectionBox(k, l, mouseXIn, mouseYIn, partialTicks);
			GlStateManager.disableDepth();
			this.overlayBackground(0, this.top, 255, 255);
			this.overlayBackground(this.bottom, this.height, 255, 255);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
			GlStateManager.disableAlpha();
			GlStateManager.shadeModel(7425);
			GlStateManager.disableTexture2D();
			// int i1 = 4;
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			bufferbuilder.pos((double) this.left, (double) (this.top + 4), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 0).endVertex();
			bufferbuilder.pos((double) this.right, (double) (this.top + 4), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 0).endVertex();
			bufferbuilder.pos((double) this.right, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos((double) this.left, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
			tessellator.draw();
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			bufferbuilder.pos((double) this.left, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos((double) this.right, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos((double) this.right, (double) (this.bottom - 4), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 0).endVertex();
			bufferbuilder.pos((double) this.left, (double) (this.bottom - 4), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 0).endVertex();
			tessellator.draw();
			int j1 = this.getMaxScroll();
			if (j1 > 0) {
				int k1 = (int) ((float) ((this.bottom - this.top) * (this.bottom - this.top)) / (float) this.getContentHeight());
				k1 = MathHelper.clamp(k1, 32, this.bottom - this.top - 8);
				int l1 = (int) this.amountScrolled * (this.bottom - this.top - k1) / j1 + this.top;
				if (l1 < this.top) {
					l1 = this.top;
				}
				
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				bufferbuilder.pos((double) i, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos((double) j, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos((double) j, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos((double) i, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
				tessellator.draw();
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				bufferbuilder.pos((double) i, (double) (l1 + k1), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
				bufferbuilder.pos((double) j, (double) (l1 + k1), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
				bufferbuilder.pos((double) j, (double) l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
				bufferbuilder.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
				tessellator.draw();
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				bufferbuilder.pos((double) i, (double) (l1 + k1 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
				bufferbuilder.pos((double) (j - 1), (double) (l1 + k1 - 1), 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
				bufferbuilder.pos((double) (j - 1), (double) l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
				bufferbuilder.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
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
				i = MathHelper.clamp(i, 32, this.bottom - this.top - 8);
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
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		
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
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				bufferbuilder.pos((double) i1, (double) (k + l + 2), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
				bufferbuilder.pos((double) j1, (double) (k + l + 2), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
				bufferbuilder.pos((double) j1, (double) (k - 2), 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
				bufferbuilder.pos((double) i1, (double) (k - 2), 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
				bufferbuilder.pos((double) (i1 + 1), (double) (k + l + 1), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos((double) (j1 - 1), (double) (k + l + 1), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos((double) (j1 - 1), (double) (k - 1), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos((double) (i1 + 1), (double) (k - 1), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
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
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		// float f = 32.0F;
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		bufferbuilder.pos((double) this.left, (double) endY, 0.0D).tex(0.0D, (double) ((float) endY / 32.0F)).color(64, 64, 64, endAlpha).endVertex();
		bufferbuilder.pos((double) (this.left + this.width), (double) endY, 0.0D).tex((double) ((float) this.width / 32.0F), (double) ((float) endY / 32.0F)).color(64, 64, 64, endAlpha).endVertex();
		bufferbuilder.pos((double) (this.left + this.width), (double) startY, 0.0D).tex((double) ((float) this.width / 32.0F), (double) ((float) startY / 32.0F)).color(64, 64, 64, startAlpha).endVertex();
		bufferbuilder.pos((double) this.left, (double) startY, 0.0D).tex(0.0D, (double) ((float) startY / 32.0F)).color(64, 64, 64, startAlpha).endVertex();
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
		BufferBuilder buffer = tessellator.getBuffer();
		this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		float scale = 32.0F;
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		buffer.pos((double) this.left, (double) this.bottom, 0.0D).tex(this.left / scale, (this.bottom + (int) this.amountScrolled) / scale).color(32, 32, 32, 255).endVertex();
		buffer.pos((double) this.right, (double) this.bottom, 0.0D).tex(this.right / scale, (this.bottom + (int) this.amountScrolled) / scale).color(32, 32, 32, 255).endVertex();
		buffer.pos((double) this.right, (double) this.top, 0.0D).tex(this.right / scale, (this.top + (int) this.amountScrolled) / scale).color(32, 32, 32, 255).endVertex();
		buffer.pos((double) this.left, (double) this.top, 0.0D).tex(this.left / scale, (this.top + (int) this.amountScrolled) / scale).color(32, 32, 32, 255).endVertex();
		tessellator.draw();
	}
}