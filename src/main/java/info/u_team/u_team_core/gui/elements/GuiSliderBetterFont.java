package info.u_team.u_team_core.gui.elements;

import org.lwjgl.opengl.GL11;

import info.u_team.u_team_core.gui.elements.backport.GuiSliderNew;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiSliderBetterFont extends GuiSliderNew {
	
	protected float scale;
	
	public GuiSliderBetterFont(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, float scale, ISlider par) {
		super(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, par);
		this.scale = scale;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		if (this.visible) {
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int k = this.getHoverState(this.hovered);
			GuiUtils.drawContinuousTexturedBox(BUTTON_TEXTURES, this.x, this.y, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
			mouseDragged(mc, mouseX, mouseY);
			int color = 14737632;
			
			if (packedFGColour != 0) {
				color = packedFGColour;
			} else if (!this.enabled) {
				color = 10526880;
			} else if (this.hovered) {
				color = 16777120;
			}
			
			String buttonText = this.displayString;
			int strWidth = MathHelper.ceil(scale * mc.fontRenderer.getStringWidth(buttonText));
			int ellipsisWidth = MathHelper.ceil(scale * mc.fontRenderer.getStringWidth("..."));
			
			if (strWidth > width - 6 && strWidth > ellipsisWidth)
				buttonText = mc.fontRenderer.trimStringToWidth(buttonText, width - 6 - ellipsisWidth).trim() + "...";
			
			GL11.glPushMatrix();
			GL11.glTranslatef(x + width / 2, y + (height - 8 * scale) / 2, 0);
			GL11.glScalef(scale, scale, 0);
			drawCenteredString(mc.fontRenderer, buttonText, 0, 0, color);
			GL11.glPopMatrix();
		}
	}
}
