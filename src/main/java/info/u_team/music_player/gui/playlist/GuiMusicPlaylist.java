package info.u_team.music_player.gui.playlist;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.music_player.gui.BetterScreen;
import info.u_team.music_player.gui.GuiMusicPlayer;
import info.u_team.music_player.gui.controls.GuiControls;
import info.u_team.music_player.gui.playlist.search.GuiMusicSearch;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.u_team_core.gui.elements.ImageButton;
import info.u_team.u_team_core.gui.elements.ScrollingText;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class GuiMusicPlaylist extends BetterScreen {
	
	private final Playlist playlist;
	
	private final GuiMusicPlaylistList trackList;
	
	private ImageButton addTracksButton;
	
	private GuiControls controls;
	
	public GuiMusicPlaylist(Playlist playlist) {
		super(Component.literal("musicplaylist"));
		this.playlist = playlist;
		
		trackList = new GuiMusicPlaylistList(playlist);
		
		if (!playlist.isLoaded()) {
			playlist.load(() -> {
				if (minecraft.screen == this) { // Check if gui is still open
					minecraft.execute(() -> {
						if (minecraft.screen == this) { // Recheck gui because this is async on the main thread.
							trackList.addAllEntries();
							if (addTracksButton != null) {
								addTracksButton.active = true;
							}
						}
					});
				}
			});
		}
	}
	
	@Override
	protected void init() {
		final ImageButton backButton = addRenderableWidget(new ImageButton(1, 1, 15, 15, MusicPlayerResources.TEXTURE_BACK));
		backButton.setPressable(() -> minecraft.setScreen(new GuiMusicPlayer()));
		
		addTracksButton = addRenderableWidget(new ImageButton(width - 35, 20, 22, 22, MusicPlayerResources.TEXTURE_ADD));
		addTracksButton.setPressable(() -> minecraft.setScreen(new GuiMusicSearch(playlist)));
		
		if (!playlist.isLoaded()) {
			addTracksButton.active = false;
		}
		
		trackList.updateSettings(width - 24, height, 50, height - 10, 12, width - 12);
		trackList.addAllEntries();
		addWidget(trackList);
		
		controls = new GuiControls(this, 5, width);
		addWidget(controls);
	}
	
	@Override
	public void tick() {
		controls.tick();
		trackList.tick();
	}
	
	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		final ScrollingText titleRender = controls.getTitleRender();
		final ScrollingText authorRender = controls.getAuthorRender();
		this.init(minecraft, width, height);
		controls.copyTitleRendererState(titleRender);
		controls.copyAuthorRendererState(authorRender);
	}
	
	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderDirtBackground(0);
		trackList.render(matrixStack, mouseX, mouseY, partialTicks);
		controls.render(matrixStack, mouseX, mouseY, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	public GuiMusicPlaylistList getTrackList() {
		return trackList;
	}
	
}
