package info.u_team.music_player.gui.playing;

import java.util.*;

import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.playlist.*;
import info.u_team.music_player.musicplayer.settings.*;
import info.u_team.u_team_core.gui.elements.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiControls extends GuiEventHandler {
	
	private final int middleX;
	private final int x, y, width;
	
	private final List<GuiButton> buttons;
	private final List<IGuiEventListener> children;
	
	private final ITrackManager manager;
	
	private final GuiButtonClickImageToggle playButton;
	
	private final GuiMusicProgressBar songProgress;
	
	public GuiControls(int y, int width) {
		this.middleX = width / 2;
		this.x = middleX - 150;
		this.y = y;
		this.width = width;
		
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
			
			if (currentlyPlaying.getPosition() > maxDuration && !currentlyPlaying.getInfo().isStream()) {
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
		
		// Volume
		final boolean isTooSmall = (width - 115) < (x + 230);
		
		addButton(new GuiSlider(-1, isTooSmall ? x + 230 : width - 115, y + 5, isTooSmall ? 40 : 70, 20, "Volume: ", "%", 0, 100, settings.getVolume(), false, true, slider -> {
			settings.setVolume(slider.getValueInt());
			MusicPlayerManager.getPlayer().setVolume(settings.getVolume());
		}));
		
		// Add all buttons to children
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
		
		final FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
		final IAudioTrack track = manager.getCurrentTrack();
		if (track != null) {
			String title = GuiTrackUtils.trimToWith(track.getInfo().getFixedTitle(), x + 20);
			fontRenderer.drawString(title, 30, y + 8, 0xcc401a);
			
			String author = GuiTrackUtils.trimToWith(track.getInfo().getFixedAuthor(), x + 20);
			fontRenderer.drawString(author, 30, y + 20, 0xf4aa42);
		}
	}
	
	private <B extends GuiButton> B addButton(B button) {
		buttons.add(button);
		return button;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
}
