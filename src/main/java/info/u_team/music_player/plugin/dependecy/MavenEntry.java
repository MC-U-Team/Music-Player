package info.u_team.music_player.plugin.dependecy;

public class MavenEntry {
	
	private Repository repository;
	private String group, artifact, version;
	
	public MavenEntry(String group, String artifact, String version) {
		this(Repository.JCenter, group, artifact, version);
	}
	
	public MavenEntry(Repository repository, String group, String artifact, String version) {
		this.repository = repository;
		this.group = group;
		this.artifact = artifact;
		this.version = version;
	}
	
	public Repository getRepository() {
		return repository;
	}
	
	public String getGroup() {
		return group;
	}
	
	public String getArtifact() {
		return artifact;
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getFileName() {
		return group + "-" + artifact + "-" + version;
	}
	
	@Override
	public String toString() {
		return group + ":" + artifact + ":" + version;
	}
	
}
