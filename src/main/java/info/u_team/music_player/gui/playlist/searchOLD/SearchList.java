package info.u_team.music_player.gui.playlist.searchOLD;

import java.util.ArrayList;

import info.u_team.music_player.lavaplayer.api.IAudioTrack;

public class SearchList extends ArrayList<IAudioTrack> {
	
	private static final long serialVersionUID = 1L;
	
	private boolean changed = false;
	
	@Override
	public boolean add(IAudioTrack e) {
		changed = true;
		return super.add(e);
	}
	
	public boolean isChanged() {
		return changed;
	}
	
	public boolean isChangedAndReset() {
		if (changed) {
			changed = false;
			return true;
		}
		return false;
	}
}
