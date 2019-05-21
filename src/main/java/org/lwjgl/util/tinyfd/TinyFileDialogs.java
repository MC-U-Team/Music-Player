package org.lwjgl.util.tinyfd;

import java.io.*;
import java.nio.file.*;

import org.apache.logging.log4j.*;

import com.sun.jna.*;

import info.u_team.music_player.MusicPlayerMod;

/**
 * Backported basic java wrapper implementation for tinyfd for lwjgl 3.x. Can be used in lwjgl 2.9 for minecraft 1.12
 * for example. We need this package and file name because of the compiled JNI C file. We use JNA is this wrapper, so it
 * must be installed. This class is very simple and dont offer all functions as the original. All right belongs to lwjgl
 * and the author of tinyfd.
 * 
 * Copyright (c) 2012-present Lightweight Java Game Library All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. Neither the name
 * Lightweight Java Game Library nor the names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class TinyFileDialogs {
	
	static final Logger log = LogManager.getLogger();
	
	static {
		System.load(extractFile(getFileDependingOnSystem()).getAbsolutePath());
	}
	
	private static String getFileDependingOnSystem() {
		String name = "lwjgl_tinyfd";
		if (Platform.isWindows()) {
			if (!Platform.is64Bit()) {
				name += "32";
			}
			name += ".dll";
		} else if (Platform.isLinux() || Platform.isMac()) {
			name = "lib" + name + (Platform.isMac() ? ".dylib" : ".so");
		} else {
			throw new UnsatisfiedLinkError("System no supported");
		}
		return name;
	}
	
	private static File extractFile(String resource) {
		log.info(resource);
		try {
			final Path path = Files.createTempFile(resource, null);
			InputStream stream = MusicPlayerMod.class.getResourceAsStream("/" + resource);
			Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
			return path.toFile();
		} catch (Exception ex) {
			throw new LinkageError("Error occured when extracting file", ex);
		}
	}
	
	protected TinyFileDialogs() {
		throw new UnsupportedOperationException();
	}
	
	// Native methods
	
	public static native long ntinyfd_openFileDialog(long aTitle, long aDefaultPathAndFile, int aNumOfFilterPatterns, long aFilterPatterns, long aSingleFilterDescription, int aAllowMultipleSelects);
	
	public static native long ntinyfd_selectFolderDialog(long aTitle, long aDefaultPath);
	
	// Handy wrapper methods
	
	public static String openFileDialog(String aTitle, String aDefaultPathAndFile, String aSingleFilterDescription, boolean aAllowMultipleSelects) {
		long result = ntinyfd_openFileDialog(getAddressOfString(aTitle), getAddressOfString(aDefaultPathAndFile), 0, 0, getAddressOfString(aSingleFilterDescription), aAllowMultipleSelects ? 1 : 0);
		return getStringOfAddress(result);
	}
	
	public static String selectFolderDialog(String aTitle, String aDefaultPath) {
		long result = ntinyfd_selectFolderDialog(getAddressOfString(aTitle), getAddressOfString(aDefaultPath));
		return getStringOfAddress(result);
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
		} catch (UnsupportedEncodingException e) {
			return string.getBytes().length + 1;
		}
	}
	
}