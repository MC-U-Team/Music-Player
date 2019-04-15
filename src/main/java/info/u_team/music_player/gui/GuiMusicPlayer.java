package info.u_team.music_player.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiMusicPlayer extends GuiScreen {
	
	private GuiMusicPlayerList list;
	
	public GuiMusicPlayer() {
		list = new GuiMusicPlayerList(Minecraft.getInstance(), 0, 0, 0, 0, 20);
	}
	
	@Override
	protected void initGui() {
		list.width = width - 24;
		list.height = height;
		list.top = 160;
		list.bottom = height - 10;
		list.right = width - 12;
		children.add(list);
		super.initGui();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawBackground(0);
		list.drawScreen(mouseX, mouseY, partialTicks);
		super.render(mouseX, mouseY, partialTicks);
	}
	
}
