package info.u_team.music_player.gui.playlist.search;

import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_ADDED;
import static info.u_team.music_player.init.MusicPlayerLocalization.getTranslation;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import net.minecraft.ChatFormatting;

public class GuiMusicSearchListEntryMusicTrack extends GuiMusicSearchListEntry {
	
	private final IAudioTrack track;
	private final boolean playlistEntry;
	
	public GuiMusicSearchListEntryMusicTrack(GuiMusicSearch gui, Playlist playlist, IAudioTrack track, boolean playlistEntry) {
		this.track = track;
		this.playlistEntry = playlistEntry;
		addTrackButton.setPressable(() -> {
			playlist.add(track);
			gui.setInformation(ChatFormatting.GREEN + getTranslation(GUI_SEARCH_ADDED), 150);
		});
	}
	
	@Override
	public void render(PoseStack matrixStack, int slotIndex, int entryY, int entryX, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
		super.render(matrixStack, slotIndex, entryY, entryX, entryWidth, entryHeight, mouseX, mouseY, hovered, partialTicks);
		addTrackInfo(matrixStack, track, entryX, entryY, entryWidth, playlistEntry ? 15 : 5, playlistEntry ? 0x42F4F1 : 0x419BF4);
	}
	
	public boolean isPlaylistEntry() {
		return playlistEntry;
	}
	
	public IAudioTrack getTrack() {
		return track;
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 2) {
			final String uri = track.getInfo().getURI();
			if (GuiTrackUtils.openURI(uri)) {
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
}
