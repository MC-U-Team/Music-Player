package info.u_team.music_player.gui.controls;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

import java.util.*;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.music_player.gui.*;
import info.u_team.music_player.gui.settings.GuiMusicPlayerSettings;
import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.*;
import info.u_team.music_player.musicplayer.settings.*;
import info.u_team.u_team_core.gui.elements.*;
import info.u_team.u_team_core.gui.render.ScrollingTextRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;

public class GuiControls extends FocusableGui implements BetterNestedGui {
	
	private final int middleX;
	private final int y, width;
	private final boolean small;
	private final int buttonSize, halfButtonSize;
	
	private final List<Widget> buttons;
	private final List<Widget> disableButtons;
	private final List<IGuiEventListener> children;
	
	private final ITrackManager manager;
	
	private final ToggleImageButton playButton;
	
	private final GuiMusicProgressBar songProgress;
	
	private ScrollingTextRender titleRender;
	private ScrollingTextRender authorRender;
	
	public GuiControls(Screen gui, int y, int width) {
		this.y = y;
		this.width = width;
		middleX = width / 2;
		
		buttons = new ArrayList<>();
		disableButtons = new ArrayList<>();
		children = new ArrayList<>();
		manager = MusicPlayerManager.getPlayer().getTrackManager();
		
		final Minecraft mc = Minecraft.getInstance();
		
		final boolean isSettings = gui instanceof GuiMusicPlayerSettings;
		final boolean isIngame = gui instanceof IngameMenuScreen;
		
		small = isIngame;
		
		buttonSize = small ? 15 : 20;
		halfButtonSize = buttonSize / 2;
		
		// Play button
		playButton = addButton(new ToggleImageButton(middleX - halfButtonSize, y, buttonSize, buttonSize, MusicPlayerResources.TEXTURE_PLAY, MusicPlayerResources.TEXTURE_PAUSE));
		playButton.toggle(!manager.isPaused());
		playButton.setPressable(() -> {
			final boolean play = playButton.isToggled();
			manager.setPaused(!play);
		});
		
		// Skip forward
		final ImageButton skipForwardButton = addButton(new ImageButton(middleX + halfButtonSize + 5, y, buttonSize, buttonSize, MusicPlayerResources.TEXTURE_SKIP_FORWARD));
		skipForwardButton.setPressable(() -> {
			MusicPlayerUtils.skipForward();
		});
		
		// Skip back
		final ImageButton skipBackButton = addButton(new ImageButton(middleX - (buttonSize + halfButtonSize + 5), y, buttonSize, buttonSize, MusicPlayerResources.TEXTURE_SKIP_BACK));
		skipBackButton.setPressable(() -> {
			MusicPlayerUtils.skipBack();
		});
		
		final Settings settings = MusicPlayerManager.getSettingsManager().getSettings();
		
		// Shuffle button
		final ActiveImageButton shuffleButton = addButton(new ActiveImageButton(middleX - (2 * buttonSize + halfButtonSize + 10), y, buttonSize, buttonSize, MusicPlayerResources.TEXTURE_SHUFFLE, 0x80FF00FF));
		
		final Runnable updateShuffleButton = () -> {
			shuffleButton.setActive(settings.isShuffle());
		};
		
		updateShuffleButton.run();
		shuffleButton.setPressable(() -> {
			settings.setShuffle(!settings.isShuffle());
			updateShuffleButton.run();
		});
		
		// Repeat button
		final ActiveImageButton repeatButton = addButton(new ActiveImageButton(middleX + +buttonSize + halfButtonSize + 10, y, buttonSize, buttonSize, MusicPlayerResources.TEXTURE_REPEAT, 0x80FF00FF));
		final Runnable updateRepeatButton = () -> {
			repeatButton.setActive(settings.getRepeat().isActive());
			repeatButton.setResource(settings.getRepeat().getResource());
		};
		
		updateRepeatButton.run();
		repeatButton.setPressable(() -> {
			settings.setRepeat(Repeat.forwardCycle(settings.getRepeat()));
			updateRepeatButton.run();
		});
		
		// Song progress
		songProgress = new GuiMusicProgressBar(manager, middleX - (small ? 50 : 100), y + (small ? 20 : 30), small ? 100 : 200, small ? 3 : 5, small ? 0.5F : 1);
		children.add(songProgress);
		
		// Open Settings
		if (!isSettings) {
			final ImageButton settingsButton = addButtonNonDisable(new ImageButton(width - (15 + 1), 1, 15, 15, MusicPlayerResources.TEXTURE_SETTINGS));
			settingsButton.setPressable(() -> mc.displayGuiScreen(new GuiMusicPlayerSettings(gui)));
		}
		
		// Open musicplayer gui
		if (isIngame) {
			final ImageButton guiButton = addButtonNonDisable(new ImageButton(width - (15 * 2 + 2), 1, 15, 15, MusicPlayerResources.TEXTURE_OPEN));
			guiButton.setPressable(() -> mc.displayGuiScreen(new GuiMusicPlayer()));
		}
		
		// Volume
		final int volumeY = width - (70 + (isIngame ? 15 * 2 + 3 : (!isSettings ? 15 + 2 : 1)));
		addButtonNonDisable(new BetterFontSlider(volumeY, 1, 70, 15, ITextComponent.func_241827_a_(getTranslation(GUI_CONTROLS_VOLUME) + ": "), ITextComponent.func_241827_a_("%"), 0, 100, settings.getVolume(), false, true, 0.7F, slider -> {
			settings.setVolume(slider.getValueInt());
			MusicPlayerManager.getPlayer().setVolume(settings.getVolume());
		}) {
			
			@Override
			public boolean isMouseOver(double mouseX, double mouseY) {
				return active; // Return always true here to mouseRelease is always called to the slider
			}
		});
		
		// Render playing track
		// Title and author
		titleRender = new ScrollingTextRender(() -> mc.fontRenderer, () -> GuiTrackUtils.getValueOfPlayingTrack(track -> track.getInfo().getFixedTitle()));
		titleRender.setStepSize(0.5F);
		titleRender.setColor(0xFFFF00);
		titleRender.setSpeedTime(35);
		
		authorRender = new ScrollingTextRender(() -> mc.fontRenderer, () -> GuiTrackUtils.getValueOfPlayingTrack(track -> track.getInfo().getFixedAuthor()));
		authorRender.setStepSize(0.5F);
		authorRender.setColor(0xFFFF00);
		authorRender.setScale(0.75F);
		authorRender.setSpeedTime(35);
		
		// Disable all buttons first
		disableButtons.forEach(button -> button.active = false);
		
		// Add all buttons to children
		buttons.forEach(children::add);
	}
	
	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return true; // Return always true here to mouseRelease is always called to our entry
	}
	
	public void tick() {
		if (manager.getCurrentTrack() == null) {
			disableButtons.forEach(button -> button.active = false);
		} else {
			disableButtons.forEach(button -> button.active = true);
		}
		playButton.toggle(!manager.isPaused());
	}
	
	@Override
	public List<? extends IGuiEventListener> getEventListeners() {
		return children;
	}
	
	public void drawScreen(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		buttons.forEach(button -> button.render(matrixStack, mouseX, mouseY, partialTicks));
		songProgress.render(matrixStack, mouseX, mouseY, partialTicks);
		
		final int textRenderWidth = middleX - (2 * buttonSize + halfButtonSize + 10) - (small ? 15 : 35);
		
		titleRender.setWidth(textRenderWidth);
		authorRender.setWidth(textRenderWidth);
		
		final int textRenderY = small ? y : y + 2;
		
		titleRender.draw(small ? 10 : 25, textRenderY);
		authorRender.draw(small ? 10 : 25, textRenderY + 10);
	}
	
	private <B extends Widget> B addButton(B button) {
		buttons.add(button);
		disableButtons.add(button);
		return button;
	}
	
	private <B extends Widget> B addButtonNonDisable(B button) {
		buttons.add(button);
		return button;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public ScrollingTextRender getTitleRender() {
		return titleRender;
	}
	
	public void setTitleRender(ScrollingTextRender titleRender) {
		this.titleRender = titleRender;
	}
	
	public ScrollingTextRender getAuthorRender() {
		return authorRender;
	}
	
	public void setAuthorRender(ScrollingTextRender authorRender) {
		this.authorRender = authorRender;
	}
}
