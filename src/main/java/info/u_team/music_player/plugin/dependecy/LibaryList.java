package info.u_team.music_player.plugin.dependecy;

import java.util.*;

public class LibaryList {
	
	private static List<MavenEntry> list = new ArrayList<>();
	
	public static void addEntry(MavenEntry entry) {
		list.add(entry);
	}
	
	public static List<MavenEntry> getEntryList() {
		return list;
	}
}
