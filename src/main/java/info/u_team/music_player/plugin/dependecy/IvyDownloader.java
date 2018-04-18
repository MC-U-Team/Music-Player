package info.u_team.music_player.plugin.dependecy;

import static info.u_team.music_player.MusicPlayerConstants.LOGGER;

import java.io.File;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.module.descriptor.*;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.ResolveOptions;
import org.apache.ivy.core.retrieve.RetrieveOptions;
import org.apache.ivy.core.settings.IvySettings;
import org.apache.ivy.plugins.resolver.IBiblioResolver;
import org.apache.ivy.util.Message;

public class IvyDownloader {
	
	private MavenEntry mavenentry;
	private File path, output;
	
	public IvyDownloader(MavenEntry mavenentry, File path, File output) {
		this.mavenentry = mavenentry;
		this.path = path;
		this.output = output;
	}
	
	@SuppressWarnings("unchecked")
	public void execute() throws Exception {
		IvySettings settings = new IvySettings();
		settings.setDefaultCache(new File(path, "cache"));
		
		IBiblioResolver biblioresolver = new IBiblioResolver();
		biblioresolver.setM2compatible(true);
		biblioresolver.setUsepoms(true);
		biblioresolver.setUseMavenMetadata(true);
		biblioresolver.setRoot(mavenentry.getRepository().getRoot());
		biblioresolver.setName(mavenentry.getRepository().getName());
		
		settings.addResolver(biblioresolver);
		settings.setDefaultResolver(biblioresolver.getName());
		
		Ivy ivy = Ivy.newInstance(settings);
		
		ivy.getLoggerEngine().setDefaultLogger(new IvyMessageLogger(Message.MSG_INFO));
		
		ResolveOptions options = new ResolveOptions();
		options.setTransitive(true);
		options.setDownload(true);
		
		DefaultModuleDescriptor defaultmoduledescripter = DefaultModuleDescriptor.newDefaultInstance(ModuleRevisionId.newInstance(mavenentry.getGroup(), mavenentry.getArtifact() + "-envelope", mavenentry.getVersion()));
		ModuleRevisionId revisionid = ModuleRevisionId.newInstance(mavenentry.getGroup(), mavenentry.getArtifact(), mavenentry.getVersion());
		DefaultDependencyDescriptor defaultdependencydescriptor = new DefaultDependencyDescriptor(defaultmoduledescripter, revisionid, false, false, true);
		
		defaultdependencydescriptor.addDependencyConfiguration("default", "runtime");
		defaultdependencydescriptor.addDependencyConfiguration("default", "master");
		defaultmoduledescripter.addDependency(defaultdependencydescriptor);
		
		ResolveReport report = ivy.resolve(defaultmoduledescripter, options);
		if (report.hasError()) {
			report.getAllProblemMessages().forEach(LOGGER::warn);
		}
		
		ModuleDescriptor moduledescriptor = report.getModuleDescriptor();
		ivy.retrieve(moduledescriptor.getModuleRevisionId(), output.getAbsolutePath() + "/[artifact]-[revision].[ext]", new RetrieveOptions().setConfs(new String[] { "default" }));
		
	}
	
}
