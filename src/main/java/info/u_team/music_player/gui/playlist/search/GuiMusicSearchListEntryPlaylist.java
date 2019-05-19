package info.u_team.music_player.gui.playlist.search;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

import java.util.List;

import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.lavaplayer.api.audio.*;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.music_player.util.TimeUtil;
import net.minecraft.util.text.TextFormatting;

public class GuiMusicSearchListEntryPlaylist extends GuiMusicSearchListEntry {
	
	private final IAudioTrackList trackList;
	
	private final String name;
	private final String duration;
	
	public GuiMusicSearchListEntryPlaylist(GuiMusicSearch gui, Playlist playlist, IAudioTrackList trackList) {
		this.trackList = trackList;
		name = trackList.getName();
		
		final List<IAudioTrack> tracks = trackList.getTracks();
		
		if (!tracks.parallelStream().anyMatch(track -> track.getInfo().isStream())) {
			duration = TimeUtil.timeConversion(tracks.parallelStream().mapToLong(track -> track.getDuration()).sum() / 1000);
		} else {
			duration = getTranslation(gui_track_duration_undefined);
		}
		
		addTrackButton.setClickAction(() -> {
			playlist.add(trackList);
			gui.setInformation(TextFormatting.GREEN + getTranslation(gui_search_added_list), 150);
		});
	}
	
	@Override
	public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		super.drawEntry(entryWidth, entryHeight, mouseX, mouseY, mouseInList, partialTicks);
		mc.fontRenderer.drawString(name, getX() + 5, getY() + 15, 0xF4E242);
		mc.fontRenderer.drawString(duration, getX() + entryWidth - 135, getY() + 5, 0xFFFF00);
	}
	
	public IAudioTrackList getTrackList() {
		return trackList;
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 2) {
			final String uri = trackList.getUri();
			if (GuiTrackUtils.openURI(uri)) {
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
