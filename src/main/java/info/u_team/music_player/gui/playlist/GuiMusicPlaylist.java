package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.gui.playlist.search.GuiMusicSearch;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.musicplayer.Playlist;
import info.u_team.to_export_to_u_team_core.gui.GuiButtonClickImage;
import net.minecraft.client.gui.GuiScreen;

public class GuiMusicPlaylist extends GuiScreen {
	
	private final Playlist playlist;
	
	private GuiMusicPlaylistList trackList;
	
	public GuiMusicPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}
	
	@Override
	protected void initGui() {
		GuiButtonClickImage addTracksButton = addButton(new GuiButtonClickImage(width - 41, 19, 22, 22, MusicPlayerResources.textureAdd));
		addTracksButton.setClickAction(() -> mc.displayGuiScreen(new GuiMusicSearch())); // TODO implement this in the right way
		
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
