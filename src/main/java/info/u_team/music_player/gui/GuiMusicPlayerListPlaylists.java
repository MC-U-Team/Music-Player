package info.u_team.music_player.gui;

import info.u_team.music_player.MusicPlayerMod;
import info.u_team.music_player.musicplayer.*;
import info.u_team.to_export_to_u_team_core.gui.*;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;

public class GuiMusicPlayerListPlaylists extends GuiScrollableList {
	
	private final Playlists playlists;
	
	private final GuiTextField namePlaylistField;
	private final GuiButtonExtImage createPlaylistButton;
	
	public GuiMusicPlayerListPlaylists() {
		super(50, 20, 5);
		
		playlists = MusicPlayerManager.getPlaylistManager().getPlaylists();
		
		selectedElement = -1;
		
		namePlaylistField = new GuiTextField(-1, mc.fontRenderer, 0, 0, 0, 20);
		namePlaylistField.setMaxStringLength(10000);
		
		createPlaylistButton = new GuiButtonExtImage(-1, 0, 0, 20, 20, new ResourceLocation(MusicPlayerMod.modid, "textures/gui/create.png")) {
			
			@Override
			public void onClick(double mouseX, double mouseY) {
				System.out.println("TEST");
				playlists.add(new Playlist(namePlaylistField.getText()));
			}
		};
	}
	
	@Override
	protected int getSize() {
		return playlists.size() + 1;
	}
	
	@Override
	protected void drawSlot(int index, int xPos, int yPos, int height, int mouseX, int mouseY, float partialTicks) {
		if (index == 0) {
			drawAddPlaylist(xPos, yPos, mouseX, mouseY, partialTicks);
		} else {
			drawPlaylist(--index, xPos, yPos, mouseX, mouseY, partialTicks);
		}
	}
	
	private void drawAddPlaylist(int xPos, int yPos, int mouseX, int mouseY, float partialTicks) {
		namePlaylistField.x = xPos + 10;
		namePlaylistField.y = yPos + 15;
		namePlaylistField.width = width - 60;
		namePlaylistField.drawTextField(mouseX, mouseY, partialTicks);
		
		createPlaylistButton.x = xPos + width - 30;
		createPlaylistButton.y = yPos;
		createPlaylistButton.render(mouseX, mouseY, partialTicks);
		
		createPlaylistButton.x = xPos + width - 30;
		createPlaylistButton.y = yPos+50;
		createPlaylistButton.render(mouseX, mouseY, partialTicks);
	}
	
	private void drawPlaylist(int index, int xPos, int yPos, int mouseX, int mouseY, float partialTicks) {
		Playlist playlist = playlists.get(index);
		FontRenderer font = mc.fontRenderer;
		font.drawString(playlist.getName(), xPos + 5, yPos + 5, 0xFFF00F);
		font.drawString(playlist.getTrackSize() + " Songs", xPos + 5, yPos + 25, 0xFFFFFF);
	}
	
	@Override
	protected boolean isSelected(int index) {
		return selectedElement == index;
	}
	
	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		namePlaylistField.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
		createPlaylistButton.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}
	
	@Override
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		createPlaylistButton.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
		return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
	}
	
	@Override
	public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
		return namePlaylistField.charTyped(p_charTyped_1_, p_charTyped_2_);
	}
	
	@Override
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		return namePlaylistField.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
	}
	
}
