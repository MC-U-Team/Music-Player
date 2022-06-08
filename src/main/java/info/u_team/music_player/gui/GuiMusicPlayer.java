package info.u_team.music_player.gui;

import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_CREATE_PLAYLIST_ADD_LIST;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_CREATE_PLAYLIST_INSERT_NAME;
import static info.u_team.music_player.init.MusicPlayerLocalization.getTranslation;

import org.apache.commons.lang3.StringUtils;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.music_player.gui.controls.GuiControls;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.u_team_core.gui.elements.ImageButton;
import info.u_team.u_team_core.gui.elements.ScrollingText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class GuiMusicPlayer extends BetterScreen {
	
	private EditBox namePlaylistField;
	
	private GuiMusicPlayerList playlistsList;
	
	private GuiControls controls;
	
	public GuiMusicPlayer() {
		super(Component.literal("musicplayer"));
	}
	
	@Override
	protected void init() {
		addRenderableWidget(new ImageButton(1, 1, 15, 15, MusicPlayerResources.TEXTURE_BACK, button -> minecraft.setScreen(null)));
		
		namePlaylistField = new EditBox(font, 100, 60, width - 150, 20, Component.nullToEmpty(null));
		namePlaylistField.setMaxLength(500);
		addWidget(namePlaylistField);
		
		final ImageButton addPlaylistButton = addRenderableWidget(new ImageButton(width - 41, 59, 22, 22, MusicPlayerResources.TEXTURE_CREATE));
		addPlaylistButton.setPressable(() -> {
			final String name = namePlaylistField.getValue();
			if (StringUtils.isBlank(name) || name.equals(getTranslation(GUI_CREATE_PLAYLIST_INSERT_NAME))) {
				namePlaylistField.setValue(getTranslation(GUI_CREATE_PLAYLIST_INSERT_NAME));
				return;
			}
			playlistsList.addPlaylist(name);
			namePlaylistField.setValue("");
		});
		
		playlistsList = new GuiMusicPlayerList(width - 24, height, 90, height - 10, 12, width - 12);
		addWidget(playlistsList);
		
		controls = new GuiControls(this, 5, width);
		addWidget(controls);
	}
	
	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		final String text = namePlaylistField.getValue();
		final ScrollingText titleRender = controls.getTitleRender();
		final ScrollingText authorRender = controls.getAuthorRender();
		this.init(minecraft, width, height);
		namePlaylistField.setValue(text);
		controls.copyTitleRendererState(titleRender);
		controls.copyAuthorRendererState(authorRender);
	}
	
	@Override
	public void tick() {
		namePlaylistField.tick();
		controls.tick();
	}
	
	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderDirtBackground(0);
		playlistsList.render(matrixStack, mouseX, mouseY, partialTicks);
		font.draw(matrixStack, getTranslation(GUI_CREATE_PLAYLIST_ADD_LIST), 20, 65, 0xFFFFFF);
		namePlaylistField.render(matrixStack, mouseX, mouseY, partialTicks);
		controls.render(matrixStack, mouseX, mouseY, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	public GuiMusicPlayerList getPlaylistsList() {
		return playlistsList;
	}
	
}
