package info.u_team.music_player.gui.playlist.search;

import java.util.List;

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
			duration = "undefined";
		}
		
		addTrackButton.setClickAction(() -> {
			playlist.add(trackList);
			gui.setInformation(TextFormatting.GREEN + "Added track list", 150);
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
}
