package info.u_team.music_player.dependency;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.PointerBuffer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;

import info.u_team.music_player.MusicPlayerMod;

public class TinyFdHelper {
	
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String TINYFD_CLASS = "org.lwjgl.util.tinyfd.TinyFileDialogs";
	
	private static TinyFdClassLoader classLoader;
	private static Class<?> tinyFdClass;
	
	private static MethodHandle openFileDialogHandle;
	private static MethodHandle selectFolderDialogHandle;
	
	public static void load(Set<URL> url) {
		classLoader = new TinyFdClassLoader(url);
		classLoader.addSyntheticClass(TINYFD_CLASS, classNode -> {
			classNode.access = Opcodes.ACC_PUBLIC;
			
			classNode.visitSource(".dynamic", null);
			
			// Add clinit library init method
			{
				final MethodNode method = new MethodNode();
				method.name = "<clinit>";
				method.desc = "()V";
				method.exceptions = Collections.emptyList();
				method.access = Opcodes.ACC_STATIC;
				
				method.visitCode();
				{
					final Label label = new Label();
					method.visitLabel(label);
					method.visitLineNumber(40000, label);
					method.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(TinyFdHelper.class), "loadTinyFdLibrary", "()V", false);
					method.visitInsn(Opcodes.RETURN);
					method.visitMaxs(0, 0);
				}
				method.visitEnd();
				
				classNode.methods.add(0, method);
			}
			
			// Add load library method
			{
				final MethodNode method = new MethodNode();
				method.name = "__$$_load_native_library";
				method.desc = "(Ljava/lang/String;)V";
				method.exceptions = Arrays.asList(Type.getInternalName(UnsatisfiedLinkError.class));
				method.access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_SYNTHETIC;
				
				method.visitCode();
				{
					final Label label = new Label();
					method.visitLabel(label);
					method.visitLineNumber(50000, label);
					method.visitVarInsn(Opcodes.ALOAD, 0);
					method.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(System.class), "load", "(Ljava/lang/String;)V", false);
					method.visitInsn(Opcodes.RETURN);
					method.visitMaxs(1, 1);
				}
				method.visitEnd();
				
				classNode.methods.add(method);
			}
			
			// Add native openFileDialog method
			{
				final MethodNode method = new MethodNode();
				method.name = "ntinyfd_openFileDialog";
				method.desc = "(JJIJJI)J";
				method.exceptions = Collections.emptyList();
				method.access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_NATIVE | Opcodes.ACC_FINAL;
				
				classNode.methods.add(method);
			}
			
			// Add native selectFolderDialog method
			{
				final MethodNode method = new MethodNode();
				method.name = "ntinyfd_selectFolderDialog";
				method.desc = "(JJ)J";
				method.exceptions = Collections.emptyList();
				method.access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_NATIVE | Opcodes.ACC_FINAL;
				
				classNode.methods.add(method);
			}
			
			classNode.visitEnd();
		});
		
		try {
			tinyFdClass = Class.forName(TINYFD_CLASS, true, classLoader);
			
			final Lookup lookup = MethodHandles.publicLookup();
			openFileDialogHandle = lookup.findStatic(tinyFdClass, "ntinyfd_openFileDialog", MethodType.methodType(long.class, long.class, long.class, int.class, long.class, long.class, int.class));
			selectFolderDialogHandle = lookup.findStatic(tinyFdClass, "ntinyfd_selectFolderDialog", MethodType.methodType(long.class, long.class, long.class));
		} catch (Throwable th) {
			throw new RuntimeException("Cannot load tinyfd", th);
		}
	}
	
	// Internal
	public static void loadTinyFdLibrary() throws Throwable {
		final String name = Platform.is64Bit() ? "lwjgl_tinyfd" : "lwjgl_tinyfd32";
		final String libraryName = System.mapLibraryName(name);
		
		final URL libraryUrl = classLoader.getResource(libraryName);
		
		if (libraryUrl == null) {
			throw new IllegalStateException("Cannot bind tinyfd native library");
		}
		
		final Path baseDirectory = createExtractDirectory();
		final Path libraryPath = baseDirectory.resolve(System.currentTimeMillis() + "_" + libraryName);
		final File libraryFile = libraryPath.toFile();
		
		FileUtils.copyURLToFile(libraryUrl, libraryFile);
		
		final String absolutePath = libraryFile.getAbsolutePath();
		LOGGER.info("Load tinyfd libary from {} ", absolutePath);
		
		final Class<?> clazz = Class.forName(TINYFD_CLASS, false, classLoader);
		
		final Lookup lookup = MethodHandles.publicLookup();
		lookup.findStatic(clazz, "__$$_load_native_library", MethodType.methodType(void.class, String.class)).invoke(absolutePath);
	}
	
	private static Path createExtractDirectory() {
		try {
			final Path baseDirectory = Paths.get(System.getProperty("java.io.tmpdir", "/tmp"), MusicPlayerMod.modid + "-tinyfd-tmp");
			// Try to clean base directory before
			try {
				FileUtils.deleteDirectory(baseDirectory.toFile());
			} catch (Exception ex) {
			}
			
			Files.createDirectories(baseDirectory);
			return baseDirectory;
		} catch (final IOException unused) {
			try {
				return Files.createTempDirectory(MusicPlayerMod.modid + "-tinyfd-tmp");
			} catch (final IOException ex) {
				throw new RuntimeException("Cannot create extract directory for tinyfd", ex);
			}
		}
	}
	
	private static class TinyFdClassLoader extends URLClassLoader {
		
		private final Map<String, Consumer<ClassNode>> syntheticClasses;
		private final Map<String, Consumer<ClassNode>> transformers;
		private final ClassLoader ourClassLoader;
		
		TinyFdClassLoader(Set<URL> url) {
			super(url.stream().toArray(URL[]::new), null);
			syntheticClasses = new HashMap<>();
			transformers = new HashMap<>();
			ourClassLoader = getClass().getClassLoader();
		}
		
		@SuppressWarnings("unused")
		void addTransformer(String className, Consumer<ClassNode> node) {
			transformers.put(className, node);
		}
		
		void addSyntheticClass(String className, Consumer<ClassNode> node) {
			syntheticClasses.put(className, node);
		}
		
		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			final Consumer<ClassNode> syntheticClass = syntheticClasses.get(name);
			if (syntheticClass != null) {
				final byte[] generatedClass = syntheticClass(syntheticClass, name);
				LOGGER.info("Defined synthetic {} class successfully", name);
				return defineClass(name, generatedClass, 0, generatedClass.length);
			}
			
			final Consumer<ClassNode> transformer = transformers.get(name);
			if (transformer != null) {
				final String resourceName = name.replace('.', '/') + ".class";
				try (final InputStream inputStream = Optional.ofNullable(getResourceAsStream(resourceName)).orElseGet(() -> ourClassLoader.getResourceAsStream(resourceName))) {
					if (inputStream == null) {
						throw new ClassCastException();
					}
					final byte[] transformed = transform(transformer, inputStream);
					LOGGER.info("Transformed {} class successfully", name);
					return defineClass(name, transformed, 0, transformed.length);
				} catch (IOException ex) {
					throw new ClassNotFoundException("Cannot read or transform class bytes", ex);
				}
			} else {
				try {
					return super.loadClass(name);
				} catch (ClassNotFoundException ex) {
					return ourClassLoader.loadClass(name);
				}
			}
		}
		
		@Override
		public URL getResource(String name) {
			return Optional.ofNullable(super.getResource(name)).orElseGet(() -> ourClassLoader.getResource(name));
		}
		
		private byte[] syntheticClass(Consumer<ClassNode> syntheticClass, String name) {
			final ClassNode node = new ClassNode();
			node.version = Opcodes.V1_8;
			node.name = name.replace('.', '/');
			node.superName = Type.getInternalName(Object.class);
			
			syntheticClass.accept(node);
			
			node.access = node.access | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_SUPER;
			
			final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			node.accept(writer);
			
			return writer.toByteArray();
		}
		
		private byte[] transform(Consumer<ClassNode> transformer, InputStream inputStream) throws IOException {
			final ClassNode node = new ClassNode();
			
			final ClassReader reader = new ClassReader(inputStream);
			reader.accept(node, ClassReader.EXPAND_FRAMES);
			
			transformer.accept(node);
			
			final ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
			node.accept(writer);
			
			return writer.toByteArray();
		}
	}
	
	public static String openFileDialog(String aTitle, String aDefaultPathAndFile, PointerBuffer aFilterPatterns, String aSingleFilterDescription, boolean aAllowMultipleSelects) {
		try {
			final long result = (long) openFileDialogHandle.invoke(getAddressOfString(aTitle), getAddressOfString(aDefaultPathAndFile), 0, 0, getAddressOfString(aSingleFilterDescription), aAllowMultipleSelects ? 1 : 0);
			return getStringOfAddress(result);
		} catch (final Throwable th) {
			throw new RuntimeException("Cannot invoke tinyfd_openFileDialog", th);
		}
	}
	
	public static String selectFolderDialog(String aTitle, String aDefaultPath) {
		try {
			final long result = (long) selectFolderDialogHandle.invoke(getAddressOfString(aTitle), getAddressOfString(aDefaultPath));
			return getStringOfAddress(result);
		} catch (final Throwable th) {
			throw new RuntimeException("Cannot invoke tinyfd_selectFolderDialog", th);
		}
	}
	
	// Utility methods
	
	private static long getAddressOfString(String string) {
		if (string == null) {
			return 0;
		}
		final Pointer pointer = new Memory(getStringSize(string));
		pointer.setString(0, string);
		return Pointer.nativeValue(pointer);
	}
	
	private static String getStringOfAddress(long address) {
		if (address == 0) {
			return null;
		}
		final Pointer pointer = new Pointer(address);
		return pointer.getString(0);
	}
	
	private static int getStringSize(String string) {
		try {
			return string.getBytes(Native.DEFAULT_ENCODING).length + 1;
		} catch (UnsupportedEncodingException ex) {
			return string.getBytes().length + 1;
		}
	}
}
