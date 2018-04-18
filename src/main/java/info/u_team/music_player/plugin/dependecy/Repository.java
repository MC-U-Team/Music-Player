package info.u_team.music_player.plugin.dependecy;

public class Repository {
	
	public static final Repository JCenter = new Repository("bintray", "https://jcenter.bintray.com");
	
	private String name, root;
	
	public Repository(String name, String root) {
		this.name = name;
		this.root = root;
	}
	
	public String getName() {
		return name;
	}
	
	public String getRoot() {
		return root;
	}
}
