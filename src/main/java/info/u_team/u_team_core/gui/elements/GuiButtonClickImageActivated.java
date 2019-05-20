package info.u_team.u_team_core.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiButtonClickImageActivated extends GuiButtonClickImage {
	
	protected boolean active;
	
	protected final int activecolor;
	
	public GuiButtonClickImageActivated(int x, int y, int width, int height, ResourceLocation resource, int activecolor) {
		super(x, y, width, height, resource);
		this.activecolor = activecolor;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		if (active) {
			color(activecolor);
		}
		super.drawButton(mc, mouseX, mouseY, partial);
	}
	
}
