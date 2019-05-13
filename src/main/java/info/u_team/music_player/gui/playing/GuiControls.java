package info.u_team.music_player.gui.playing;

import java.util.*;

import info.u_team.music_player.gui.playlist.GuiMusicPlaylistList;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.playlist.*;
import info.u_team.music_player.musicplayer.settings.*;
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
		
		// Play button
		playButton = addButton(new GuiButtonClickImageToggle(x + 140, y + 10, 20, 20, MusicPlayerResources.texturePlay, MusicPlayerResources.texturePause));
		playButton.toggle(!manager.isPaused());
		playButton.setToggleClickAction(play -> {
			manager.setPaused(!play);
		});
		
		// Skip buttons
		final GuiButtonClickImage skipForwardButton = addButton(new GuiButtonClickImage(x + 170, y + 10, 20, 20, MusicPlayerResources.textureSkipForward));
		skipForwardButton.setClickAction(() -> {
			final Playlist playlist = MusicPlayerManager.getPlaylistManager().getPlaylists().getPlaying();
			if (playlist != null) {
				if (playlist.skip(Skip.FORWARD)) {
					manager.skip();
				}
			}
		});
		
		final GuiButtonClickImage skipBackButton = addButton(new GuiButtonClickImage(x + 110, y + 10, 20, 20, MusicPlayerResources.textureSkipBack));
		skipBackButton.setClickAction(() -> {
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
		
		final Settings settings = MusicPlayerManager.getSettingsManager().getSettings();
		
		// Shuffle button
		final GuiButtonClickImageActivated shuffleButton = addButton(new GuiButtonClickImageActivated(x + 80, y + 10, 20, 20, MusicPlayerResources.textureShuffle, 0x80FF00FF));
		
		final Runnable updateShuffleButton = () -> {
			shuffleButton.setActive(settings.isShuffle());
		};
		
		updateShuffleButton.run();
		shuffleButton.setClickAction(() -> {
			settings.setShuffle(!settings.isShuffle());
			updateShuffleButton.run();
		});
		
		// Repeat button
		final GuiButtonClickImageActivated repeatButton = addButton(new GuiButtonClickImageActivated(x + 200, y + 10, 20, 20, MusicPlayerResources.textureRepeat, 0x80FF00FF));
		
		final Runnable updateRepeatButton = () -> {
			repeatButton.setActive(settings.getRepeat().isActive());
			repeatButton.setResource(settings.getRepeat().getResource());
		};
		
		updateRepeatButton.run();
		repeatButton.setClickAction(() -> {
			settings.setRepeat(Repeat.forwardCycle(settings.getRepeat()));
			updateRepeatButton.run();
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
