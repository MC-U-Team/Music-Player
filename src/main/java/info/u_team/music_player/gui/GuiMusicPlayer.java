package info.u_team.music_player.gui;

import net.minecraft.client.gui.GuiScreen;

public class GuiMusicPlayer extends GuiScreen {
	
	private final GuiMusicPlayerListPlaylists playlists;
	
	public GuiMusicPlayer() {
		playlists = new GuiMusicPlayerListPlaylists();
	}
	
	@Override
	protected void initGui() {
		playlists.updateSettings(width - 24, height, 10, height - 10, 12, width - 12);
		children.add(playlists);
		super.initGui();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawBackground(0);
		playlists.drawScreen(mouseX, mouseY, partialTicks);
		super.render(mouseX, mouseY, partialTicks);
	}
	
}
