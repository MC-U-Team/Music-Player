package info.u_team.music_player.musicplayer;

import java.nio.file.Path;

public interface IGsonLoadable {
	
	void setBasePath(Path path);
	
	void loadFromFile();
	
	void writeToFile();
	
}
