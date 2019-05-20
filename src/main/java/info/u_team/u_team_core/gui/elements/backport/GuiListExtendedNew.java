package info.u_team.u_team_core.gui.elements.backport;

import java.util.*;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public abstract class GuiListExtendedNew<E extends GuiListExtendedNew.IGuiListEntryNew<E>> extends GuiSlotNew {
	
	/** The entries in this list. */
	private final List<E> entries = new UpdatingList();
	
	public GuiListExtendedNew(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
		super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
	}
	
	/**
	 * Called when the mouse is clicked onto an entry.
	 * 
	 * @return true if the entry did something with the click and it should be selected.
	 */
	protected boolean mouseClicked(int index, int button, double mouseX, double mouseY) {
		return this.getListEntry(index).mouseClicked(mouseX, mouseY, button);
	}
	
	/**
	 * Returns true if the element passed in is currently selected
	 */
	protected boolean isSelected(int slotIndex) {
		return false;
	}
	
	protected void drawBackground() {
	}
	
	protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
		this.getListEntry(slotIndex).drawEntry(this.getListWidth(), heightIn, mouseXIn, mouseYIn, this.isMouseInList((double) mouseXIn, (double) mouseYIn) && this.getEntryAt((double) mouseXIn, (double) mouseYIn) == slotIndex, partialTicks);
	}
	
	protected void updateItemPos(int entryID, int insideLeft, int yPos, float partialTicks) {
		this.getListEntry(entryID).func_195000_a(partialTicks);
	}
	
	/**
	 * Gets a mutable list of child listeners. For a {@link GuiListExtendedNew}, this is a list of the entries of the list
	 * (in the order they are displayed); for a {@link GuiScreen} this is the sub-controls.
	 */
	public final List<E> getChildren() {
		return this.entries;
	}
	
	/**
	 * Removes all entries from this list.
	 */
	protected final void clearEntries() {
		this.entries.clear();
	}
	
	/**
	 * Gets the IGuiListEntry object for the given index
	 */
	private E getListEntry(int index) {
		return (E) (this.getChildren().get(index));
	}
	
	/**
	 * Adds an entry to this list.
	 */
	protected final void addEntry(E entry) {
		this.entries.add(entry);
	}
	
	/**
	 * Called when the given entry is selected; sets {@link #selectedElement} to the index and updates {@link #lastClicked}.
	 */
	public void setSelectedEntry(int index) {
		this.selectedElement = index;
		this.lastClicked = System.currentTimeMillis();
	}
	
	protected final int getSize() {
		return this.getChildren().size();
	}
	
	@SideOnly(Side.CLIENT)
	public abstract static class IGuiListEntryNew<E extends GuiListExtendedNew.IGuiListEntryNew<E>> implements IGuiEventListener {
		
		protected GuiListExtendedNew<E> list;
		protected int index;
		
		protected GuiListExtendedNew<E> getList() {
			return this.list;
		}
		
		protected int getIndex() {
			return this.index;
		}
		
		protected int getY() {
			return this.list.top + 4 - this.list.getAmountScrolled() + this.index * this.list.slotHeight + this.list.headerPadding;
		}
		
		protected int getX() {
			return this.list.left + this.list.width / 2 - this.list.getListWidth() / 2 + 2;
		}
		
		protected void func_195000_a(float p_195000_1_) {
		}
		
		public abstract void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks);
	}
	
	@SideOnly(Side.CLIENT)
	class UpdatingList extends AbstractList<E> {
		
		/** The actual list that is managed by this list. */
		private final List<E> innerList = Lists.newArrayList();
		
		private UpdatingList() {
		}
		
		public E get(int p_get_1_) {
			return (E) (this.innerList.get(p_get_1_));
		}
		
		public int size() {
			return this.innerList.size();
		}
		
		public E set(int p_set_1_, E p_set_2_) {
			E e = this.innerList.set(p_set_1_, p_set_2_);
			p_set_2_.list = GuiListExtendedNew.this;
			p_set_2_.index = p_set_1_;
			return e;
		}
		
		public void add(int p_add_1_, E p_add_2_) {
			this.innerList.add(p_add_1_, p_add_2_);
			p_add_2_.list = GuiListExtendedNew.this;
			p_add_2_.index = p_add_1_;
			
			for (int i = p_add_1_ + 1; i < this.size(); this.get(i).index = i++) {
				;
			}
			
		}
		
		public E remove(int p_remove_1_) {
			E e = this.innerList.remove(p_remove_1_);
			
			for (int i = p_remove_1_; i < this.size(); this.get(i).index = i++) {
				;
			}
			
			return e;
		}
	}
}