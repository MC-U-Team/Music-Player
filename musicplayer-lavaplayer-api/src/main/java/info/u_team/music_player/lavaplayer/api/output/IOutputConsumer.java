package info.u_team.music_player.lavaplayer.api.output;

@FunctionalInterface
public interface IOutputConsumer {
	
	void accept(byte[] buffer, int chunkSize);
	
}
