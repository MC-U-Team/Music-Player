package info.u_team.music_player.gui.playing;

import java.util.*;

import info.u_team.music_player.gui.GuiMusicPlayer;
import info.u_team.music_player.gui.settings.GuiMusicPlayerSettings;
import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.playlist.*;
import info.u_team.music_player.musicplayer.settings.*;
import info.u_team.to_u_team_core.export.*;
import info.u_team.u_team_core.gui.elements.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;

public class GuiControls extends GuiEventHandler {
	
	private final int middleX;
	private final int y, width;
	private final boolean small;
	
	private final List<GuiButton> buttons;
	private final List<GuiButton> disableButtons;
	private final List<IGuiEventListener> children;
	
	private final ITrackManager manager;
	
	private final GuiButtonClickImageToggle playButton;
	
	private final GuiMusicProgressBar songProgress;
	
	private final RenderScrollingText title;
	private final RenderScrollingText author;
	
	public GuiControls(GuiScreen gui, int y, int width) {
		this.y = y;
		this.width = width;
		middleX = width / 2;
		
		buttons = new ArrayList<>();
		disableButtons = new ArrayList<>();
		children = new ArrayList<>();
		manager = MusicPlayerManager.getPlayer().getTrackManager();
		
		final Minecraft mc = Minecraft.getInstance();
		
		final boolean isSettings = gui instanceof GuiMusicPlayerSettings;
		final boolean isIngame = gui instanceof GuiIngameMenu;
		
		small = isIngame;
		
		final int buttonSize = small ? 15 : 20;
		final int halfButtonSize = buttonSize / 2;
		
		// Play button
		playButton = addButton(new GuiButtonClickImageToggle(middleX - halfButtonSize, y, buttonSize, buttonSize, MusicPlayerResources.texturePlay, MusicPlayerResources.texturePause));
		playButton.toggle(!manager.isPaused());
		playButton.setToggleClickAction(play -> {
			manager.setPaused(!play);
		});
		
		// Skip forward
		final GuiButtonClickImage skipForwardButton = addButton(new GuiButtonClickImage(middleX + halfButtonSize + 5, y, buttonSize, buttonSize, MusicPlayerResources.textureSkipForward));
		skipForwardButton.setClickAction(() -> {
			final Playlist playlist = MusicPlayerManager.getPlaylistManager().getPlaylists().getPlaying();
			if (playlist != null) {
				if (playlist.skip(Skip.FORWARD)) {
					manager.skip();
				}
			}
		});
		
		// Skip back
		final GuiButtonClickImage skipBackButton = addButton(new GuiButtonClickImage(middleX - (buttonSize + halfButtonSize + 5), y, buttonSize, buttonSize, MusicPlayerResources.textureSkipBack));
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
		final GuiButtonClickImageActivated shuffleButton = addButton(new GuiButtonClickImageActivated(middleX - (2 * buttonSize + halfButtonSize + 10), y, buttonSize, buttonSize, MusicPlayerResources.textureShuffle, 0x80FF00FF));
		
		final Runnable updateShuffleButton = () -> {
			shuffleButton.setActive(settings.isShuffle());
		};
		
		updateShuffleButton.run();
		shuffleButton.setClickAction(() -> {
			settings.setShuffle(!settings.isShuffle());
			updateShuffleButton.run();
		});
		
		// Repeat button
		final GuiButtonClickImageActivated repeatButton = addButton(new GuiButtonClickImageActivated(middleX + +buttonSize + halfButtonSize + 10, y, buttonSize, buttonSize, MusicPlayerResources.textureRepeat, 0x80FF00FF));
		
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
		songProgress = new GuiMusicProgressBar(manager, middleX - (small ? 50 : 100), y + (small ? 20 : 30), small ? 100 : 200, small ? 3 : 5, small ? 0.5F : 1);
		children.add(songProgress);
		
		// Open Settings
		if (!isSettings) {
			final GuiButtonClickImage settingsButton = addButtonNonDisable(new GuiButtonClickImage(width - (15 + 1), 1, 15, 15, MusicPlayerResources.textureSettings));
			settingsButton.setClickAction(() -> mc.displayGuiScreen(new GuiMusicPlayerSettings(gui)));
		}
		
		// Open musicplayer gui
		if (isIngame) {
			final GuiButtonClickImage guiButton = addButtonNonDisable(new GuiButtonClickImage(width - (15 * 2 + 2), 1, 15, 15, MusicPlayerResources.textureOpen));
			guiButton.setClickAction(() -> mc.displayGuiScreen(new GuiMusicPlayer()));
		}
		
		// Volume
		final int volumeY = width - (70 + (isIngame ? 15 * 2 + 3 : (!isSettings ? 15 + 2 : 1)));
		addButtonNonDisable(new GuiSliderBetterFont(-1, volumeY, 1, 70, 15, "Volume: ", "%", 0, 100, settings.getVolume(), false, true, 0.7F, slider -> {
			settings.setVolume(slider.getValueInt());
			MusicPlayerManager.getPlayer().setVolume(settings.getVolume());
		}));
		
		// Render playing track
		// Title and author
		title = new RenderScrollingText(() -> mc.fontRenderer, () -> GuiTrackUtils.getValueOfPlayingTrack(track -> track.getInfo().getFixedTitle()));
		title.setStepSize(0.5F);
		title.setColor(0xFFFF00);
		title.setWidth(114);
		title.setSpeedTime(35);
		
		author = new RenderScrollingText(() -> mc.fontRenderer, () -> GuiTrackUtils.getValueOfPlayingTrack(track -> track.getInfo().getFixedAuthor()));
		author.setStepSize(0.5F);
		author.setColor(0xFFFF00);
		author.setScale(0.75F);
		author.setWidth(114);
		author.setSpeedTime(35);
		
		// Disable all buttons first
		disableButtons.forEach(button -> button.enabled = false);
		
		// Add all buttons to children
		buttons.forEach(children::add);
	}
	
	public void tick() {
		if (manager.getCurrentTrack() == null) {
			disableButtons.forEach(button -> button.enabled = false);
		} else {
			disableButtons.forEach(button -> button.enabled = true);
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
		
		title.draw(2 + 3, y + 2);
		author.draw(2 + 3, y + 12);
	}
	
	private <B extends GuiButton> B addButton(B button) {
		buttons.add(button);
		disableButtons.add(button);
		return button;
	}
	
	private <B extends GuiButton> B addButtonNonDisable(B button) {
		buttons.add(button);
		return button;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
}
