package info.u_team.u_team_core.gui.elements.backport;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.gui.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public abstract class GuiEventHandlerNew extends Gui implements IGuiEventListenerDeferred {
	
	@Nullable
	private IGuiEventListener focused;
	private boolean dragging;
	
	/**
	 * Gets a mutable list of child listeners. For a {@link GuiListExtended}, this is a list of the entries of the list (in
	 * the order they are displayed); for a {@link GuiScreen} this is the sub-controls.
	 */
	protected abstract List<? extends IGuiEventListener> getChildren();
	
	private final boolean isDragging() {
		return this.dragging;
	}
	
	protected final void setDragging(boolean p_195072_1_) {
		this.dragging = p_195072_1_;
	}
	
	@Nullable
	public IGuiEventListener getFocused() {
		return this.focused;
	}
	
	protected void setFocused(@Nullable IGuiEventListener p_195073_1_) {
		this.focused = p_195073_1_;
	}
	
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		for (IGuiEventListener iguieventlistener : this.getChildren()) {
			boolean flag = iguieventlistener.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
			if (flag) {
				this.focusOn(iguieventlistener);
				if (p_mouseClicked_5_ == 0) {
					this.setDragging(true);
				}
				return true;
			}
		}
		
		return false;
	}
	
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		return IGuiEventListenerDeferred.super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
	}
	
	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		return this.getFocused() != null && this.isDragging() && p_mouseDragged_5_ == 0 ? this.getFocused().mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_) : false;
	}
	
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		this.setDragging(false);
		return IGuiEventListenerDeferred.super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
	}
	
	public void focusOn(@Nullable IGuiEventListener p_205725_1_) {
		this.switchFocus(p_205725_1_, this.getChildren().indexOf(this.getFocused()));
	}
	
	public void focusNext() {
		int i = this.getChildren().indexOf(this.getFocused());
		int j = i == -1 ? 0 : (i + 1) % this.getChildren().size();
		this.switchFocus(this.getNextFocusable(j), i);
	}
	
	@Nullable
	private IGuiEventListener getNextFocusable(int p_207713_1_) {
		List<? extends IGuiEventListener> list = this.getChildren();
		int i = list.size();
		
		for (int j = 0; j < i; ++j) {
			IGuiEventListener iguieventlistener = list.get((p_207713_1_ + j) % i);
			if (iguieventlistener.canFocus()) {
				return iguieventlistener;
			}
		}
		
		return null;
	}
	
	private void switchFocus(@Nullable IGuiEventListener p_205728_1_, int p_205728_2_) {
		IGuiEventListener iguieventlistener = p_205728_2_ == -1 ? null : this.getChildren().get(p_205728_2_);
		if (iguieventlistener != p_205728_1_) {
			if (iguieventlistener != null) {
				iguieventlistener.focusChanged(false);
			}
			
			if (p_205728_1_ != null) {
				p_205728_1_.focusChanged(true);
			}
			
			this.setFocused(p_205728_1_);
		}
	}
}