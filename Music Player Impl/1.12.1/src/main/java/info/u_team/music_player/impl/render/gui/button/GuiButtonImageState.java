package info.u_team.music_player.impl.render.gui.button;

import net.minecraft.util.ResourceLocation;

public class GuiButtonImageState extends GuiButtonImage {
	
	protected ResourceLocation resourceFirst, resourceSecond;
	
	public GuiButtonImageState(int id, int x, int y, int width, int height, ResourceLocation resourceFirst, ResourceLocation resourceSecond) {
		this(id, x, y, width, height, resourceFirst, resourceSecond, -1, -1);
	}
	
	public GuiButtonImageState(int id, int x, int y, int width, int height, ResourceLocation resourceFirst, ResourceLocation resourceSecond, int color, int hovercolor) {
		super(id, x, y, width, height, resourceFirst, color, hovercolor);
		this.resourceFirst = resourceFirst;
		this.resourceSecond = resourceSecond;
	}
	
	public void switchTextures() {
		resource = resource.equals(resourceFirst) ? resourceSecond : resourceFirst;
	}
	
	public void setTexture1() {
		resource = resourceFirst;
	}
	
	public void setTexture2() {
		resource = resourceSecond;
	}
}
