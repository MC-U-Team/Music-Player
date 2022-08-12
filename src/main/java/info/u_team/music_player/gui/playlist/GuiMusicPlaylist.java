package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.gui.GuiMusicPlayer;
import info.u_team.music_player.gui.controls.GuiControls;
import info.u_team.music_player.gui.playlist.search.GuiMusicSearch;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.u_team_core.gui.elements.*;
import info.u_team.u_team_core.gui.elements.backport.GuiScreen1_13;
import net.minecraft.client.Minecraft;

public class GuiMusicPlaylist extends GuiScreen1_13 {
	
	private final Playlist playlist;
	
	private final GuiMusicPlaylistList trackList;
	
	private GuiButtonClickImage addTracksButton;
	
	private GuiControls controls;
	
	public GuiMusicPlaylist(Playlist playlist) {
		this.playlist = playlist;
		
		trackList = new GuiMusicPlaylistList(playlist);
		
		if (!playlist.isLoaded()) {
			playlist.load(() -> {
				if (Minecraft.getMinecraft().currentScreen == this) { // Check if gui is still open
					Minecraft.getMinecraft().addScheduledTask(() -> {
						if (Minecraft.getMinecraft().currentScreen == this) { // Recheck gui because this is async on the main thread.
							trackList.addAllEntries();
							if (addTracksButton != null) {
								addTracksButton.enabled = true;
							}
						}
					});
				}
			});
		}
	}
	
	@Override
	public void initGui() {
		final GuiButtonClick backButton = addNewButton(new GuiButtonClickImage(1, 1, 15, 15, MusicPlayerResources.textureBack));
		backButton.setClickAction(() -> mc.displayGuiScreen(new GuiMusicPlayer()));
		
		addTracksButton = addNewButton(new GuiButtonClickImage(width - 35, 20, 22, 22, MusicPlayerResources.textureAdd));
		addTracksButton.setClickAction(() -> mc.displayGuiScreen(new GuiMusicSearch(playlist)));
		
		if (!playlist.isLoaded()) {
			addTracksButton.enabled = false;
		}
		
		trackList.updateSettings(width - 24, height, 50, height - 10, 12, width - 12);
		trackList.addAllEntries();
		children.add(trackList);
		
		controls = new GuiControls(this, 5, width);
		children.add(controls);
		
		super.initGui();
	}
	
	@Override
	public void updateScreen() {
		controls.tick();
		trackList.tick();
	}
	
	@Override
	public void onResize(Minecraft minecraft, int width, int height) {
		final RenderScrollingText titleRender = controls.getTitleRender();
		final RenderScrollingText authorRender = controls.getAuthorRender();
		this.setWorldAndResolution(minecraft, width, height);
		controls.setTitleRender(titleRender);
		controls.setAuthorRender(authorRender);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawBackground(0);
		trackList.drawScreen(mouseX, mouseY, partialTicks);
		controls.drawScreen(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public GuiMusicPlaylistList getTrackList() {
		return trackList;
	}
	
}
