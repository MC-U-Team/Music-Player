package info.u_team.u_team_core.gui.elements;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonClickActivated extends GuiButtonClick {
	
	protected boolean active;
	
	protected final int activeColor;
	
	public GuiButtonClickActivated(int x, int y, int width, int height, String displayString, int activeColor) {
		super(x, y, width, height, displayString);
		this.activeColor = activeColor;
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
			GlStateManager.color(1F, 1F, 1F, 1F);
			color(activeColor);
		}
		super.drawButton(mc,mouseX, mouseY, partial);
	}
	
	protected void color(int color) {
		if (color == -1) {
			GL11.glColor4f(1F, 1F, 1F, 1F);
			return;
		}
		float red = (color >> 24 & 255) / 255F;
		float green = (color >> 16 & 255) / 255F;
		float blue = (color >> 8 & 255) / 255F;
		float alpha = (color & 255) / 255F;
		
		GL11.glColor4f(red, green, blue, alpha);
	}
}
