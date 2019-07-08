package info.u_team.u_team_core.gui.elements.backport;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiButtonExtNew extends GuiButtonNew {
	
	public GuiButtonExtNew(int id, int xPos, int yPos, String displayString) {
		super(id, xPos, yPos, displayString);
	}
	
	public GuiButtonExtNew(int id, int xPos, int yPos, int width, int height, String displayString) {
		super(id, xPos, yPos, width, height, displayString);
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
			int k = this.getHoverState(this.hovered);
			GuiUtils.drawContinuousTexturedBox(buttonTextures, this.xPosition, this.yPosition, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
			this.mouseDragged(mc, mouseX, mouseY);
			int color = 14737632;
			
			if (packedFGColour != 0) {
				color = packedFGColour;
			} else if (!this.enabled) {
				color = 10526880;
			} else if (this.hovered) {
				color = 16777120;
			}
			
			String buttonText = this.displayString;
			int strWidth = mc.fontRendererObj.getStringWidth(buttonText);
			int ellipsisWidth = mc.fontRendererObj.getStringWidth("...");
			
			if (strWidth > width - 6 && strWidth > ellipsisWidth)
				buttonText = mc.fontRendererObj.trimStringToWidth(buttonText, width - 6 - ellipsisWidth).trim() + "...";
			
			this.drawCenteredString(mc.fontRendererObj, buttonText, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, color);
		}
	}
}