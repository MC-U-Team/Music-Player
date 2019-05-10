package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.musicplayer.Playlist;
import info.u_team.music_player.util.WrappedObject;

public class GuiMusicPlaylistListEntryMusicTrack extends GuiMusicPlaylistListEntryFunctions {
	
	private final IAudioTrack track;
	
	public GuiMusicPlaylistListEntryMusicTrack(GuiMusicPlaylistList guilist, Playlist playlist, WrappedObject<String> uri, IAudioTrack track) {
		super(guilist, playlist, uri);
		this.track = track;
	}
	
	@Override
	public void drawEntryExtended(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		addTrackInfo(track, entryWidth, 5, 0x419BF4);
	}
}
