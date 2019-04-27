package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.musicplayer.*;
import net.minecraft.client.gui.GuiScreen;

public class GuiMusicPlaylist extends GuiScreen {
	
	private final Playlist playlist;
	
	private GuiMusicPlaylistList trackList;
	
	public GuiMusicPlaylist(Playlist playlist) {
		this.playlist = playlist;
		if (!playlist.isLoaded()) {
			playlist.load();
		}
	}
	
	@Override
	protected void initGui() {
		trackList = new GuiMusicPlaylistList(playlist, width - 24, height, 50, height - 30, 12, width - 12);
		children.add(trackList);
		super.initGui();
	}
	
	@Override
	public void tick() {
		if (trackList != null) {
			trackList.tick();
		}
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawBackground(0);
		trackList.drawScreen(mouseX, mouseY, partialTicks);
		super.render(mouseX, mouseY, partialTicks);
	}
	
}
