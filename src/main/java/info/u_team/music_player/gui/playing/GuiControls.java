package info.u_team.music_player.gui.playing;

import java.util.*;

import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.playlist.*;
import info.u_team.music_player.musicplayer.settings.*;
import info.u_team.to_u_team_core.export.GuiButtonClickImageActivated;
import info.u_team.u_team_core.gui.elements.*;
import net.minecraft.client.gui.*;

public class GuiControls extends GuiEventHandler {
	
	private final int middleX;
	private final int x, y;
	
	private final List<GuiButton> buttons;
	private final List<IGuiEventListener> children;
	
	private final ITrackManager manager;
	
	private final GuiButtonClickImageToggle playButton;
	
	private final GuiMusicProgressBar songProgress;
	
	// With 300, Height 40
	public GuiControls(int x, int y) {
		this.middleX = x + 150;
		this.x = x;
		this.y = y;
		
		buttons = new ArrayList<>();
		children = new ArrayList<>();
		manager = MusicPlayerManager.getPlayer().getTrackManager();
		
		// Play button
		playButton = addButton(new GuiButtonClickImageToggle(x + 140, y + 5, 20, 20, MusicPlayerResources.texturePlay, MusicPlayerResources.texturePause));
		playButton.toggle(!manager.isPaused());
		playButton.setToggleClickAction(play -> {
			manager.setPaused(!play);
		});
		
		// Skip buttons
		final GuiButtonClickImage skipForwardButton = addButton(new GuiButtonClickImage(x + 170, y + 5, 20, 20, MusicPlayerResources.textureSkipForward));
		skipForwardButton.setClickAction(() -> {
			final Playlist playlist = MusicPlayerManager.getPlaylistManager().getPlaylists().getPlaying();
			if (playlist != null) {
				if (playlist.skip(Skip.FORWARD)) {
					manager.skip();
				}
			}
		});
		
		final GuiButtonClickImage skipBackButton = addButton(new GuiButtonClickImage(x + 110, y + 5, 20, 20, MusicPlayerResources.textureSkipBack));
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
		final GuiButtonClickImageActivated shuffleButton = addButton(new GuiButtonClickImageActivated(x + 80, y + 5, 20, 20, MusicPlayerResources.textureShuffle, 0x80FF00FF));
		
		final Runnable updateShuffleButton = () -> {
			shuffleButton.setActive(settings.isShuffle());
		};
		
		updateShuffleButton.run();
		shuffleButton.setClickAction(() -> {
			settings.setShuffle(!settings.isShuffle());
			updateShuffleButton.run();
		});
		
		// Repeat button
		final GuiButtonClickImageActivated repeatButton = addButton(new GuiButtonClickImageActivated(x + 200, y + 5, 20, 20, MusicPlayerResources.textureRepeat, 0x80FF00FF));
		
		final Runnable updateRepeatButton = () -> {
			repeatButton.setActive(settings.getRepeat().isActive());
			repeatButton.setResource(settings.getRepeat().getResource());
		};
		
		updateRepeatButton.run();
		repeatButton.setClickAction(() -> {
			settings.setRepeat(Repeat.forwardCycle(settings.getRepeat()));
			updateRepeatButton.run();
		});
		
		// Song progress
		songProgress = new GuiMusicProgressBar(manager, middleX - 80, y + 35, 160, 5);
		children.add(songProgress);
		
		buttons.forEach(children::add);
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
		return children;
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		buttons.forEach(button -> button.render(mouseX, mouseY, partialTicks));
		songProgress.render(mouseX, mouseY, partialTicks);
		
		//
		// final IAudioTrack track = manager.getCurrentTrack();
		// if (track != null) {
		// double progress = (double) track.getPosition() / track.getDuration();
		// // Progressbar
		//
		// drawRect(middleX - 80, y + 30, middleX + 80, y + 35, 0xFF555555);
		// drawRect(middleX - 80, y + 30, (int) (middleX - 80 + (progress * 160)), y + 35, 0xFF3e9100);
		// }
	}
	
	private <B extends GuiButton> B addButton(B button) {
		buttons.add(button);
		return button;
	}
}
