package info.u_team.music_player.plugin.dependecy;

import static info.u_team.music_player.MusicPlayerConstants.LOGGER;

import org.apache.ivy.util.*;

public class IvyMessageLogger extends AbstractMessageLogger {
	
	private int level = Message.MSG_INFO;
	
	public IvyMessageLogger(int level) {
		this.level = level;
	}
	
	public void log(String msg, int level) {
		if (level <= this.level) {
			LOGGER.info(msg);
		}
	}
	
	public void rawlog(String msg, int level) {
		log(msg, level);
	}
	
	public void doProgress() {
		LOGGER.info(".");
	}
	
	public void doEndProgress(String msg) {
		LOGGER.info(msg);
	}
	
	public int getLevel() {
		return level;
	}
}
