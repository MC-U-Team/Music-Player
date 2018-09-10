package info.u_team.music_player.impl.render.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiButtonImageActivated extends GuiButtonImage {
	
	protected boolean activated;
	protected int activecolor;
	
	public GuiButtonImageActivated(int id, int x, int y, int width, int height, ResourceLocation resource, int activecolor) {
		this(id, x, y, width, height, resource, -1, -1, activecolor);
	}
	
	public GuiButtonImageActivated(int id, int x, int y, int width, int height, ResourceLocation resource, int color, int hovercolor, int activecolor) {
		super(id, x, y, width, height, resource, color, hovercolor);
		this.activated = false;
		this.activecolor = activecolor;
	}
	
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	
	public boolean isActivated() {
		return activated;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		if (activated) {
			color(activecolor);
		}
		super.drawButton(mc, mouseX, mouseY, partial);
	}
	
}
