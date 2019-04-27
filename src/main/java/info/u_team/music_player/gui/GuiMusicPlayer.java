package info.u_team.music_player.gui;

import org.apache.commons.lang3.StringUtils;

import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.to_export_to_u_team_core.gui.GuiButtonClickImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;

public class GuiMusicPlayer extends GuiScreen {
	
	private GuiTextField namePlaylistField;
	
	private GuiMusicPlayerListPlaylists playlistsList;
	
	@Override
	protected void initGui() {
		namePlaylistField = new GuiTextField(-1, mc.fontRenderer, 100, 20, width - 150, 20);
		namePlaylistField.setMaxStringLength(500);
		children.add(namePlaylistField);
		
		GuiButtonClickImage addPlaylistButton = addButton(new GuiButtonClickImage(width - 41, 19, 22, 22, MusicPlayerResources.textureCreate));
		addPlaylistButton.setClickAction(() -> {
			String name = namePlaylistField.getText();
			if (StringUtils.isBlank(name) || name.equals("Enter a name")) {
				namePlaylistField.setText("Enter a name");
				return;
			}
			playlistsList.addPlaylist(name);
			namePlaylistField.setText("");
		});
		
		playlistsList = new GuiMusicPlayerListPlaylists(width - 24, height, 50, height - 30, 12, width - 12);
		children.add(playlistsList);
		super.initGui();
	}
	
	@Override
	public void onResize(Minecraft minecraft, int width, int height) {
		String text = namePlaylistField.getText();
		this.setWorldAndResolution(minecraft, width, height);
		namePlaylistField.setText(text);
	}
	
	@Override
	public void tick() {
		namePlaylistField.tick();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawBackground(0);
		playlistsList.drawScreen(mouseX, mouseY, partialTicks);
		mc.fontRenderer.drawString("Add playlist", 20, 25, 0xFFFFFF);
		namePlaylistField.drawTextField(mouseX, mouseY, partialTicks);
		super.render(mouseX, mouseY, partialTicks);
	}
	
}
