package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.lavaplayer.api.IAudioTrack;

public class GuiMusicPlaylistListEntryPlaylistTrack extends GuiMusicPlaylistListEntryPlayable {
	
	private final GuiMusicPlaylistListEntryPlaylistStart start;
	private final IAudioTrack track;
	
	public GuiMusicPlaylistListEntryPlaylistTrack(GuiMusicPlaylistListEntryPlaylistStart start, IAudioTrack track) {
		this.start = start;
		this.track = track;
	}
	
	@Override
	public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		addTrackInfo(track, entryWidth, 15, 0x42F4F1);
	}
}
