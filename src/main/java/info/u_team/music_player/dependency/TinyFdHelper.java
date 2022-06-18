package info.u_team.music_player.dependency;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.Platform;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

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
		classLoader.addTransformer(TINYFD_CLASS, classNode -> {
			classNode.visitSource(".dynamic", null);
			
			// Remove load library call from clinit and rename
			{
				final MethodNode method = classNode.methods.stream().filter(node -> node.name.equals("<clinit>")).findFirst().get();
				method.name = "__$$_clinit";
				method.access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_SYNTHETIC;
				final ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
				for (int index = 0; index < 9; index++) {
					iterator.next();
					iterator.remove();
				}
			}
			
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
					method.visitMaxs(1, 0);
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
				
				classNode.methods.add(0, method);
			}
			
			classNode.visitEnd();
		});
		
		try {
			tinyFdClass = Class.forName(TINYFD_CLASS, true, classLoader);
			
			final Lookup lookup = MethodHandles.publicLookup();
			openFileDialogHandle = lookup.findStatic(tinyFdClass, "tinyfd_openFileDialog", MethodType.methodType(String.class, CharSequence.class, CharSequence.class, PointerBuffer.class, CharSequence.class, boolean.class));
			selectFolderDialogHandle = lookup.findStatic(tinyFdClass, "tinyfd_selectFolderDialog", MethodType.methodType(String.class, CharSequence.class, CharSequence.class));
		} catch (Throwable th) {
			throw new RuntimeException("Cannot load tinyfd", th);
		}
	}
	
	// Internal
	public static void loadTinyFdLibrary() throws Throwable {
		final Lookup lookup = MethodHandles.publicLookup();
		
		final Method mapLibraryNameMethod = Platform.class.getDeclaredMethod("mapLibraryName", String.class);
		mapLibraryNameMethod.setAccessible(true);
		final MethodHandle mapLibraryNameHandle = lookup.unreflect(mapLibraryNameMethod);
		final String libraryName = (String) mapLibraryNameHandle.invoke(Platform.get(), Platform.mapLibraryNameBundled("lwjgl_tinyfd"));
		
		final URL libraryUrl = classLoader.getResource(libraryName);
		
		if (libraryUrl == null) {
			throw new IllegalStateException("Cannot bind tinyfd native library");
		}
		
		final Path baseDirectory = Paths.get(System.getProperty("java.io.tmpdir", "/tmp"), MusicPlayerMod.MODID + "-tinyfd-tmp");
		// Try to clean base directory before
		try {
			FileUtils.deleteDirectory(baseDirectory.toFile());
		} catch (Exception ex) {
		}
		final Path libraryPath = baseDirectory.resolve(System.currentTimeMillis() + "_" + libraryName);
		final File libraryFile = libraryPath.toFile();
		
		FileUtils.copyURLToFile(libraryUrl, libraryFile);
		
		final String absolutePath = libraryFile.getAbsolutePath();
		LOGGER.info("Load tinyfd libary from {} ", absolutePath);
		
		final Class<?> clazz = Class.forName(TINYFD_CLASS, false, classLoader);
		
		lookup.findStatic(clazz, "__$$_load_native_library", MethodType.methodType(void.class, String.class)).invoke(absolutePath);
		lookup.findStatic(clazz, "__$$_clinit", MethodType.methodType(void.class)).invoke();
	}
	
	private static class TinyFdClassLoader extends URLClassLoader {
		
		private final Map<String, Consumer<ClassNode>> transformers;
		private final ClassLoader ourClassLoader;
		
		TinyFdClassLoader(Set<URL> url) {
			super(url.stream().toArray(URL[]::new), null);
			transformers = new HashMap<>();
			ourClassLoader = getClass().getClassLoader();
		}
		
		void addTransformer(String className, Consumer<ClassNode> node) {
			transformers.put(className, node);
		}
		
		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {
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
	
	public static String openFileDialog(CharSequence aTitle, CharSequence aDefaultPathAndFile, PointerBuffer aFilterPatterns, CharSequence aSingleFilterDescription, boolean aAllowMultipleSelects) {
		try {
			return (String) openFileDialogHandle.invoke(aTitle, aDefaultPathAndFile, aFilterPatterns, aSingleFilterDescription, aAllowMultipleSelects);
		} catch (final Throwable th) {
			throw new RuntimeException("Cannot invoke tinyfd_openFileDialog", th);
		}
	}
	
	public static String selectFolderDialog(CharSequence aTitle, CharSequence aDefaultPath) {
		try {
			return (String) selectFolderDialogHandle.invoke(aTitle, aDefaultPath);
		} catch (final Throwable th) {
			throw new RuntimeException("Cannot invoke tinyfd_selectFolderDialog", th);
		}
	}
}
