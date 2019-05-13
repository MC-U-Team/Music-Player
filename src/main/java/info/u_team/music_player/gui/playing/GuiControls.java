package info.u_team.music_player.gui.playing;

import java.util.*;

import info.u_team.music_player.gui.playlist.GuiMusicPlaylistList;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.playlist.*;
import info.u_team.u_team_core.gui.elements.*;
import net.minecraft.client.gui.*;

public class GuiControls extends GuiEventHandler {
	
	private final List<GuiButton> buttons;
	
	private final ITrackManager manager;
	
	private final GuiButtonClickImageToggle playButton;
	
	public GuiControls(int x, int y) {
		this(null, x, y);
	}
	
	// With 300, Height 40
	public GuiControls(GuiMusicPlaylistList list, int x, int y) {
		buttons = new ArrayList<>();
		manager = MusicPlayerManager.getPlayer().getTrackManager();
		
		playButton = addButton(new GuiButtonClickImageToggle(x + 140, y + 10, 20, 20, MusicPlayerResources.texturePlay, MusicPlayerResources.texturePause));
		playButton.toggle(!manager.isPaused());
		playButton.setToggleClickAction(play -> {
			manager.setPaused(!play);
		});
		
		final GuiButtonClickImage skipForward = addButton(new GuiButtonClickImage(x + 170, y + 10, 20, 20, MusicPlayerResources.textureSkipForward));
		skipForward.setClickAction(() -> {
			final Playlist playlist = MusicPlayerManager.getPlaylistManager().getPlaylists().getPlaying();
			if (playlist != null) {
				if (playlist.skip(Skip.FORWARD)) {
					manager.skip();
				}
			}
		});
		
		final GuiButtonClickImage skipBack = addButton(new GuiButtonClickImage(x + 110, y + 10, 20, 20, MusicPlayerResources.textureSkipBack));
		skipBack.setClickAction(() -> {
			final IAudioTrack currentlyPlaying = manager.getCurrentTrack();
			
			long maxDuration = currentlyPlaying.getDuration() / 10;
			if (maxDuration > 10000) {
				maxDuration = 10000;
			}
			
			if (currentlyPlaying.getPosition() > maxDuration) {
				currentlyPlaying.setPosition(0);
			} else {
				final Playlist playlist = MusicPlayerManager.getPlaylistManager().getPlaylists().getPlaying();
				if (playlist != null) {
					if (playlist.skip(Skip.PREVIOUS)) {
						manager.skip();
					}
				}
			}
		});
	}
	
	public void tick() {
		if (manager.getCurrentTrack() == null) {
			buttons.forEach(button -> button.enabled = false);
		} else {
			buttons.forEach(button -> button.enabled = true);
		}
		playButton.toggle(!manager.isPaused());
	}
	
	@Override
	protected List<? extends IGuiEventListener> getChildren() {
		return buttons;
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		buttons.forEach(button -> button.render(mouseX, mouseY, partialTicks));
	}
	
	private <B extends GuiButton> B addButton(B button) {
		buttons.add(button);
		return button;
	}
}
