package info.u_team.music_player.gui;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

import org.apache.commons.lang3.StringUtils;

import info.u_team.music_player.gui.controls.GuiControls;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.u_team_core.gui.elements.ImageButton;
import info.u_team.u_team_core.gui.render.ScrollingTextRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;

public class GuiMusicPlayer extends Screen {
	
	private TextFieldWidget namePlaylistField;
	
	private GuiMusicPlayerList playlistsList;
	
	private GuiControls controls;
	
	public GuiMusicPlayer() {
		super(new StringTextComponent("musicplayer"));
	}
	
	@Override
	protected void init() {
		addButton(new ImageButton(1, 1, 15, 15, MusicPlayerResources.textureBack, button -> minecraft.displayGuiScreen(null)));
		
		namePlaylistField = new TextFieldWidget(font, 100, 60, width - 150, 20, null);
		namePlaylistField.setMaxStringLength(500);
		children.add(namePlaylistField);
		
		final ImageButton addPlaylistButton = addButton(new ImageButton(width - 41, 59, 22, 22, MusicPlayerResources.textureCreate));
		addPlaylistButton.setPressable(() -> {
			String name = namePlaylistField.getText();
			if (StringUtils.isBlank(name) || name.equals(getTranslation(gui_create_playlist_insert_name))) {
				namePlaylistField.setText(getTranslation(gui_create_playlist_insert_name));
				return;
			}
			playlistsList.addPlaylist(name);
			namePlaylistField.setText("");
		});
		
		playlistsList = new GuiMusicPlayerList(width - 24, height, 90, height - 10, 12, width - 12);
		children.add(playlistsList);
		
		controls = new GuiControls(this, 5, width);
		children.add(controls);
	}
	
	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		final String text = namePlaylistField.getText();
		final ScrollingTextRender titleRender = controls.getTitleRender();
		final ScrollingTextRender authorRender = controls.getAuthorRender();
		this.init(minecraft, width, height);
		namePlaylistField.setText(text);
		controls.setTitleRender(titleRender);
		controls.setAuthorRender(authorRender);
	}
	
	@Override
	public void tick() {
		namePlaylistField.tick();
		controls.tick();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderDirtBackground(0);
		playlistsList.render(mouseX, mouseY, partialTicks);
		font.drawString(getTranslation(gui_create_playlist_add_list), 20, 65, 0xFFFFFF);
		namePlaylistField.render(mouseX, mouseY, partialTicks);
		controls.drawScreen(mouseX, mouseY, partialTicks);
		super.render(mouseX, mouseY, partialTicks);
	}
	
	public GuiMusicPlayerList getPlaylistsList() {
		return playlistsList;
	}
	
}
