package info.u_team.u_team_core.gui.elements.backport;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonNew extends GuiButton implements IGuiEventListener {
	
	public GuiButtonNew(int buttonId, int x, int y, String buttonText) {
		super(buttonId, x, y, buttonText);
	}
	
	public GuiButtonNew(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
	}
	
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		if (p_mouseClicked_5_ == 0) {
			boolean flag = this.mousePressed(Minecraft.getMinecraft(), (int) p_mouseClicked_1_, (int) p_mouseClicked_3_);
			if (flag) {
				this.playPressSound(Minecraft.getMinecraft().getSoundHandler());
				this.onClick(p_mouseClicked_1_, p_mouseClicked_3_);
				return true;
			}
		}
		return false;
	}
	
	public void onClick(double p_mouseClicked_1_, double p_mouseClicked_3_) {
		
	}
	
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		mouseReleased((int) p_mouseReleased_1_, (int) p_mouseReleased_3_);
		if (p_mouseReleased_5_ == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		super.mouseDragged(Minecraft.getMinecraft(), (int) p_mouseDragged_1_, (int) p_mouseDragged_3_);
		return false;
	}
}
