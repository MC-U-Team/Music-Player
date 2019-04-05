package info.u_team.music_player.lavaplayer.api;

public interface IMusicPlayer {
	
	public ITrackScheduler getTrackScheduler();
	
	ITrackSearch getTrackSearch();
	
	@Deprecated
	public ITrackSearchOLD getTrackSearchOLD();
	
	@Deprecated
	public ITrackSearchOLD getTrackDirectSearch();
	
	public void startAudioOutput();
	
	public int getVolume();
	
	public void setVolume(int volume);
	
	public void registerEventHandler(IMusicPlayerEvents events);
	
	public void unregisterEventHandler(IMusicPlayerEvents events);
}
