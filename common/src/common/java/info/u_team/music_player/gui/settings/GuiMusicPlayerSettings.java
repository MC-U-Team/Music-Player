package info.u_team.music_player.gui.settings;

import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_MIXER_DEVICE_SELECTION;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_PITCH;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_POSITION_OVERLAY;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_SPEED;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_TOGGLE_INGAME_OVERLAY;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_TOGGLE_KEY_IN_GUI;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_TOGGLE_MENUE_OVERLAY;
import static info.u_team.music_player.init.MusicPlayerLocalization.getTranslation;

import info.u_team.music_player.gui.BetterScreen;
import info.u_team.music_player.gui.controls.GuiControls;
import info.u_team.music_player.init.MusicPlayerColors;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.settings.IngameOverlayPosition;
import info.u_team.music_player.musicplayer.settings.Settings;
import info.u_team.u_team_core.gui.elements.ActivatableButton;
import info.u_team.u_team_core.gui.elements.ImageButton;
import info.u_team.u_team_core.gui.elements.ScrollingText;
import info.u_team.u_team_core.gui.elements.UButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class GuiMusicPlayerSettings extends BetterScreen {
	
	private final Screen previousGui;
	
	private GuiMusicPlayerSettingsMixerDeviceList mixerDeviceList;
	
	private GuiControls controls;
	
	public GuiMusicPlayerSettings(Screen previousGui) {
		super(Component.literal("musicplayersettings"));
		this.previousGui = previousGui;
	}
	
	@Override
	protected void init() {
		addRenderableWidget(new ImageButton(1, 1, 15, 15, MusicPlayerResources.TEXTURE_BACK, button -> minecraft.setScreen(previousGui)));
		
		final Settings settings = MusicPlayerManager.getSettingsManager().getSettings();
		
		final ActivatableButton toggleKeyWorkInGuiButton = addRenderableWidget(new ActivatableButton(12, 60, width / 2 - 24, 20, Component.nullToEmpty(getTranslation(GUI_SETTINGS_TOGGLE_KEY_IN_GUI)), settings.isKeyWorkInGui(), MusicPlayerColors.LIGHT_GREEN));
		toggleKeyWorkInGuiButton.setPressable(() -> {
			settings.setKeyWorkInGui(!settings.isKeyWorkInGui());
			toggleKeyWorkInGuiButton.setActivated(settings.isKeyWorkInGui());
		});
		
		final ActivatableButton toggleIngameMenueDisplayButton = addRenderableWidget(new ActivatableButton(width / 2 + 14, 60, width / 2 - 24, 20, Component.nullToEmpty(getTranslation(GUI_SETTINGS_TOGGLE_MENUE_OVERLAY)), settings.isShowIngameMenueOverlay(), MusicPlayerColors.LIGHT_GREEN));
		toggleIngameMenueDisplayButton.setPressable(() -> {
			settings.setShowIngameMenueOverlay(!settings.isShowIngameMenueOverlay());
			toggleIngameMenueDisplayButton.setActivated(settings.isShowIngameMenueOverlay());
		});
		
		final ActivatableButton toggleIngameDisplayButton = addRenderableWidget(new ActivatableButton(12, 90, width / 2 - 24, 20, Component.nullToEmpty(getTranslation(GUI_SETTINGS_TOGGLE_INGAME_OVERLAY)), settings.isShowIngameOverlay(), MusicPlayerColors.LIGHT_GREEN));
		toggleIngameDisplayButton.setPressable(() -> {
			settings.setShowIngameOverlay(!settings.isShowIngameOverlay());
			toggleIngameDisplayButton.setActivated(settings.isShowIngameOverlay());
		});
		
		final UButton ingameOverlayPositionButton = addRenderableWidget(new UButton(width / 2 + 14, 90, width / 2 - 24, 20, Component.nullToEmpty(getTranslation(GUI_SETTINGS_POSITION_OVERLAY) + ": " + getTranslation(settings.getIngameOverlayPosition().getLocalization()))));
		ingameOverlayPositionButton.setPressable(() -> {
			settings.setIngameOverlayPosition(IngameOverlayPosition.forwardCycle(settings.getIngameOverlayPosition()));
			ingameOverlayPositionButton.setMessage(Component.nullToEmpty(getTranslation(GUI_SETTINGS_POSITION_OVERLAY) + ": " + getTranslation(settings.getIngameOverlayPosition().getLocalization())));
		});
		
		// TODO replace with uslider
		addRenderableWidget(new AbstractSliderButton(12, 120, width / 2 - 24, 20, CommonComponents.EMPTY, MusicPlayerManager.getPlayer().getSpeed() / 4F) {
			
			{
				updateMessage();
			}
			
			@Override
			protected void updateMessage() {
				setMessage(Component.literal(getTranslation(GUI_SETTINGS_SPEED) + ": " + Math.round(MusicPlayerManager.getPlayer().getSpeed() * 100F) / 100F));
			}
			
			@Override
			protected void applyValue() {
				MusicPlayerManager.getPlayer().setSpeed((float) Mth.clampedLerp(0.1F, 4F, value));
			}
		});
		
		// TODO replace with uslider
		addRenderableWidget(new AbstractSliderButton(width / 2 + 14, 120, width / 2 - 24, 20, CommonComponents.EMPTY, MusicPlayerManager.getPlayer().getPitch() / 3F) {
			
			{
				updateMessage();
			}
			
			@Override
			protected void updateMessage() {
				setMessage(Component.literal(getTranslation(GUI_SETTINGS_PITCH) + ": " + Math.round(MusicPlayerManager.getPlayer().getPitch() * 100F) / 100F));
			}
			
			@Override
			protected void applyValue() {
				MusicPlayerManager.getPlayer().setPitch((float) Mth.clampedLerp(0.1F, 3F, value));
			}
		});
		
		mixerDeviceList = new GuiMusicPlayerSettingsMixerDeviceList(width - 24, height, 163, Math.max(163 + 20, height - 12), 12, width - 12);
		addWidget(mixerDeviceList);
		
		controls = new GuiControls(this, 5, width);
		addWidget(controls);
	}
	
	@Override
	public void tick() {
		controls.tick();
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
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		mixerDeviceList.render(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.drawString(minecraft.font, getTranslation(GUI_SETTINGS_MIXER_DEVICE_SELECTION), 13, 147, 0xFFFFFF);
		controls.render(guiGraphics, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		renderDirtBackground(guiGraphics);
	}
	
}
