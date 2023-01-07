package info.u_team.music_player.gui.controls;

import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_CONTROLS_VOLUME;
import static info.u_team.music_player.init.MusicPlayerLocalization.getTranslation;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.music_player.gui.BetterNestedGui;
import info.u_team.music_player.gui.GuiMusicPlayer;
import info.u_team.music_player.gui.settings.GuiMusicPlayerSettings;
import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.init.MusicPlayerColors;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.MusicPlayerUtils;
import info.u_team.music_player.musicplayer.settings.Repeat;
import info.u_team.music_player.musicplayer.settings.Settings;
import info.u_team.u_team_core.gui.elements.ImageActivatableButton;
import info.u_team.u_team_core.gui.elements.ImageButton;
import info.u_team.u_team_core.gui.elements.ImageToggleButton;
import info.u_team.u_team_core.gui.elements.ScrollingText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class GuiControls extends AbstractContainerEventHandler implements BetterNestedGui, Renderable, NarratableEntry {
	
	private final int middleX;
	private final int y, width;
	private final int buttonSize, halfButtonSize;
	
	private final List<AbstractWidget> buttons;
	private final List<AbstractWidget> disableButtons;
	private final List<GuiEventListener> children;
	
	private final ITrackManager manager;
	
	private final ImageToggleButton playButton;
	
	private final GuiMusicProgressBar songProgress;
	
	private final ScrollingText titleRender;
	private final ScrollingText authorRender;
	
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
		final boolean isIngame = gui instanceof PauseScreen;
		
		final boolean small = isIngame;
		
		buttonSize = small ? 15 : 20;
		halfButtonSize = buttonSize / 2;
		
		// Play button
		playButton = addButton(new ImageToggleButton(middleX - halfButtonSize, y, buttonSize, buttonSize, MusicPlayerResources.TEXTURE_PLAY, MusicPlayerResources.TEXTURE_PAUSE, !manager.isPaused()));
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
		final ImageActivatableButton shuffleButton = addButton(new ImageActivatableButton(middleX - (2 * buttonSize + halfButtonSize + 10), y, buttonSize, buttonSize, MusicPlayerResources.TEXTURE_SHUFFLE, settings.isShuffle(), MusicPlayerColors.LIGHT_GREEN));
		
		shuffleButton.setPressable(() -> {
			settings.setShuffle(!settings.isShuffle());
			shuffleButton.setActivated(settings.isShuffle());
		});
		
		// Repeat button
		final ImageActivatableButton repeatButton = addButton(new ImageActivatableButton(middleX + +buttonSize + halfButtonSize + 10, y, buttonSize, buttonSize, MusicPlayerResources.TEXTURE_REPEAT, settings.getRepeat().isActive(), MusicPlayerColors.LIGHT_GREEN));
		repeatButton.setImage(settings.getRepeat().getResource());
		
		repeatButton.setPressable(() -> {
			settings.setRepeat(Repeat.forwardCycle(settings.getRepeat()));
			repeatButton.setActivated(settings.getRepeat().isActive());
			repeatButton.setImage(settings.getRepeat().getResource());
		});
		
		// Song progress
		songProgress = new GuiMusicProgressBar(manager, middleX - (small ? 50 : 100), y + (small ? 20 : 30), small ? 100 : 200, small ? 3 : 5, small ? 0.5F : 1);
		children.add(songProgress);
		
		// Open Settings
		if (!isSettings) {
			final ImageButton settingsButton = addButtonNonDisable(new ImageButton(width - (15 + 1), 1, 15, 15, MusicPlayerResources.TEXTURE_SETTINGS));
			settingsButton.setPressable(() -> mc.setScreen(new GuiMusicPlayerSettings(gui)));
		}
		
		// Open musicplayer gui
		if (isIngame) {
			final ImageButton guiButton = addButtonNonDisable(new ImageButton(width - (15 * 2 + 2), 1, 15, 15, MusicPlayerResources.TEXTURE_OPEN));
			guiButton.setPressable(() -> mc.setScreen(new GuiMusicPlayer()));
		}
		
		// Volume
		final int volumeY = width - (70 + (isIngame ? 15 * 2 + 3 : (!isSettings ? 15 + 2 : 1)));
		addButtonNonDisable(new GuiVolumeSlider(volumeY, 1, 70, 15, Component.nullToEmpty(getTranslation(GUI_CONTROLS_VOLUME) + ": "), Component.nullToEmpty("%"), 0, 100, settings.getVolume(), false, true, false, 0.7F, slider -> {
			settings.setVolume(slider.getValueInt());
			MusicPlayerManager.getPlayer().setVolume(settings.getVolume());
		}));
		
		final int textRenderWidth = middleX - (2 * buttonSize + halfButtonSize + 10) - (small ? 15 : 35);
		final int textRenderY = small ? y : y + 2;
		
		// Render playing track
		// Title and author
		titleRender = new ScrollingText(mc.font, () -> GuiTrackUtils.getValueOfPlayingTrack(track -> track.getInfo().getFixedTitle()), small ? 10 : 25, textRenderY);
		titleRender.setWidth(textRenderWidth);
		titleRender.setStepSize(0.5F);
		titleRender.setColor(MusicPlayerColors.YELLOW);
		titleRender.setSpeedTime(35);
		
		authorRender = new ScrollingText(mc.font, () -> GuiTrackUtils.getValueOfPlayingTrack(track -> track.getInfo().getFixedAuthor()), small ? 10 : 25, textRenderY + 10);
		authorRender.setWidth(textRenderWidth);
		authorRender.setStepSize(0.5F);
		authorRender.setColor(MusicPlayerColors.YELLOW);
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
		playButton.setToggled(!manager.isPaused());
	}
	
	@Override
	public List<? extends GuiEventListener> children() {
		return children;
	}
	
	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		buttons.forEach(button -> button.render(matrixStack, mouseX, mouseY, partialTicks));
		songProgress.render(matrixStack, mouseX, mouseY, partialTicks);
		
		titleRender.render(matrixStack, mouseX, mouseY, partialTicks);
		authorRender.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (manager.getCurrentTrack() != null && button == 0 && (checkClick(titleRender, mouseX, mouseY) || checkClick(authorRender, mouseX, mouseY))) {
			if (GuiTrackUtils.openURI(manager.getCurrentTrack().getInfo().getURI())) {
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	private boolean checkClick(ScrollingText renderer, double mouseX, double mouseY) {
		return mouseX >= renderer.getX() && mouseY >= renderer.getY() && mouseX < renderer.getX() + renderer.getWidth() && mouseY < renderer.getY() + (Minecraft.getInstance().font.lineHeight + 1) * renderer.getScale();
	}
	
	private <B extends AbstractWidget> B addButton(B button) {
		buttons.add(button);
		disableButtons.add(button);
		return button;
	}
	
	private <B extends AbstractWidget> B addButtonNonDisable(B button) {
		buttons.add(button);
		return button;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public ScrollingText getTitleRender() {
		return titleRender;
	}
	
	public ScrollingText getAuthorRender() {
		return authorRender;
	}
	
	public void copyTitleRendererState(ScrollingText renderer) {
		titleRender.copyState(renderer);
	}
	
	public void copyAuthorRendererState(ScrollingText renderer) {
		authorRender.copyState(renderer);
	}
	
	// Narration stuff
	@Override
	public void updateNarration(NarrationElementOutput p_169152_) {
	}
	
	@Override
	public boolean isActive() {
		return false;
	}
	
	@Override
	public NarrationPriority narrationPriority() {
		return NarrationPriority.NONE;
	}
	
}
