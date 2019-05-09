package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.gui.playlist.searchOLD.GuiMusicSearchOLD;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.musicplayer.Playlist;
import info.u_team.u_team_core.gui.elements.GuiButtonClickImage;
import net.minecraft.client.gui.GuiScreen;

public class GuiMusicPlaylist extends GuiScreen {
	
	private final Playlist playlist;
	
	private GuiMusicPlaylistList trackList;
	
	public GuiMusicPlaylist(Playlist playlist) {
		this.playlist = playlist;
		
		System.out.println("OPEN gui");
		playlist.unload();
		
		trackList = new GuiMusicPlaylistList(playlist);
		
		if (!playlist.isLoaded()) {
			playlist.load(() -> {
				System.out.println("Has finished");
				if (mc.currentScreen == this) {
					System.out.println("Added all tracks");
					trackList.addAllEntries();
				}
			});
		}
	}
	
	@Override
	protected void initGui() {
		final GuiButtonClickImage addTracksButton = addButton(new GuiButtonClickImage(width - 41, 19, 22, 22, MusicPlayerResources.textureAdd));
		addTracksButton.setClickAction(() -> mc.displayGuiScreen(new GuiMusicSearchOLD()));
		
		trackList.updateSettings(width - 24, height, 50, height - 30, 12, width - 12);
		trackList.addAllEntries();
		children.add(trackList);
		super.initGui();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawBackground(0);
		trackList.drawScreen(mouseX, mouseY, partialTicks);
		super.render(mouseX, mouseY, partialTicks);
	}
	
}
