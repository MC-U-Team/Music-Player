package info.u_team.music_player.impl.render.gui.button;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.*;

public class GuiButtonImage extends GuiButtonExt {
	
	protected ResourceLocation resource;
	protected int color, hovercolor;
	
	public GuiButtonImage(int id, int x, int y, int width, int height, ResourceLocation resource) {
		this(id, x, y, width, height, resource, -1, -1);
	}
	
	public GuiButtonImage(int id, int x, int y, int width, int height, ResourceLocation resource, int color, int hovercolor) {
		super(id, x, y, width, height, "");
		this.resource = resource;
		this.color = color;
		this.hovercolor = hovercolor;
	}
	
	public void setResource(ResourceLocation resource) {
		this.resource = resource;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public void setHoverColor(int hovercolor) {
		this.hovercolor = hovercolor;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (visible) {
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
			int k = this.getHoverState(this.hovered);
			GuiUtils.drawContinuousTexturedBox(BUTTON_TEXTURES, this.xPosition, this.yPosition, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
			this.mouseDragged(mc, mouseX, mouseY);
			
			if (hovered) {
				color(hovercolor);
			} else {
				color(color);
			}
			mc.getTextureManager().bindTexture(resource);
			Gui.drawScaledCustomSizeModalRect(xPosition + 2, yPosition + 2, 0, 0, 1, 1, width - 4, height - 4, 1, 1);
		}
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
