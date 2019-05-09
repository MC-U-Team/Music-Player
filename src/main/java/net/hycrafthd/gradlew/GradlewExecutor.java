package net.hycrafthd.gradlew;

import java.io.*;
import java.net.URL;
import java.util.*;

import org.apache.commons.compress.archivers.zip.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;

public class GradlewExecutor {
	
	private File project;
	private File cache;
	
	private IExecutorLog log;
	
	public GradlewExecutor(File path, IExecutorLog log) {
		project = new File(path, "project");
		cache = new File(path, "cache");
		this.log = log;
	}
	
	public void execute(String... args) {
		project.mkdirs();
		cache.mkdirs();
		setupGradlew();
		runGradlew(args);
	}
	
	public File getProject() {
		return project;
	}
	
	public File getCache() {
		return cache;
	}
	
	private void runGradlew(String[] args) {
		File classpath = new File(project, "gradle/wrapper/gradle-wrapper.jar");
		try {
			String[] startargs = new String[] { "\"" + getJavaExe() + "\"", "-Dorg.gradle.appname=GradlewExecutor", "-classpath", "\"" + classpath.getPath() + "\"", "org.gradle.wrapper.GradleWrapperMain", "--g", "\"" + cache.getPath() + "\"", "--no-daemon" };
			String[] finalargs = concat(startargs, args);
			log.print("Executing gradlew process with params: " + Arrays.toString(finalargs));
			ProcessBuilder builder = new ProcessBuilder(finalargs);
			builder.directory(project);
			builder.redirectErrorStream(true);
			Process process = builder.start();
			new Thread(() -> {
				log.log(process.getInputStream());
			}, "Gradle Logger").start();
			process.waitFor();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void setupGradlew() {
		URL buildurl = getClass().getResource("/gradlew/build.gradle");
		URL wrapperurl = getClass().getResource("/gradlew/gradle-wrapper.zip");
		
		try {
			File wrapperfile;
			FileUtils.copyURLToFile(wrapperurl, wrapperfile = new File(cache, "gradle-wrapper.zip"));
			unzip(wrapperfile, project);
			FileUtils.copyURLToFile(buildurl, new File(project, "build.gradle"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void unzip(File file, File destination) throws IOException {
		ZipFile zip = new ZipFile(file);
		Enumeration<ZipArchiveEntry> enumeration = zip.getEntries();
		while (enumeration.hasMoreElements()) {
			ZipArchiveEntry entry = enumeration.nextElement();
			if (entry.isDirectory()) {
				new File(destination, entry.getName()).mkdirs();
				continue;
			}
			FileUtils.copyInputStreamToFile(zip.getInputStream(entry), new File(destination, entry.getName()));
		}
		zip.close();
	}
	
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	
	private String getJavaExe() {
		File javaexe = new File(System.getProperty("java.home"), "bin/java" + getFileEndExe());
		return javaexe.getPath();
	}

	private String getFileEndExe() {
		return SystemUtils.IS_OS_WINDOWS ? ".exe" : "";
	}
}
