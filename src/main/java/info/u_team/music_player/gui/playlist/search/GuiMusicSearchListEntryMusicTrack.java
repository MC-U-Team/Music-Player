package info.u_team.music_player.gui.playlist.search;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import net.minecraft.util.text.TextFormatting;

public class GuiMusicSearchListEntryMusicTrack extends GuiMusicSearchListEntry {
	
	private final IAudioTrack track;
	private final boolean playlistEntry;
	
	public GuiMusicSearchListEntryMusicTrack(GuiMusicSearch gui, Playlist playlist, IAudioTrack track, boolean playlistEntry) {
		this.track = track;
		this.playlistEntry = playlistEntry;
		addTrackButton.setClickAction(() -> {
			playlist.add(track);
			gui.setInformation(TextFormatting.GREEN + getTranslation(gui_search_added), 150);
		});
	}
	
	@Override
	public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		super.drawEntry(entryWidth, entryHeight, mouseX, mouseY, mouseInList, partialTicks);
		addTrackInfo(track, entryWidth, playlistEntry ? 15 : 5, playlistEntry ? 0x42F4F1 : 0x419BF4);
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
