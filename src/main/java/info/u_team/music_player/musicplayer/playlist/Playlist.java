package info.u_team.music_player.musicplayer.playlist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.IntPredicate;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrackList;
import info.u_team.music_player.lavaplayer.api.queue.ITrackQueue;
import info.u_team.music_player.lavaplayer.api.search.ITrackSearch;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.settings.Settings;
import info.u_team.music_player.util.WrappedObject;

/**
 * This class represents a playlist. This list can be serialized or deserialized. After a serialization the tracks must
 * be loaded, because only the uris are saved. {@link IAudioTrack} and {@link IAudioTrackList} can be added. Tracks can
 * be removed. Tracks can be moved in the order. Any changes to the serializable fields are saved
 *
 * @author HyCraftHD
 */
public class Playlist implements ITrackQueue {
	
	// Used in gson serialization and deserialization
	public String name;
	public final ArrayList<WrappedObject<String>> uris;
	
	// Should not be serialized or deserialized
	private transient final Executor executor;
	private transient boolean loaded;
	private transient final ArrayList<LoadedTracks> loadedTracks;
	
	/**
	 * Only used for gson deserialization
	 */
	@SuppressWarnings("unused")
	private Playlist() {
		uris = new ArrayList<>();
		loadedTracks = new ArrayList<>();
		executor = Executors.newSingleThreadExecutor();
	}
	
	/**
	 * Create a new playlist object with a name
	 *
	 * @param name The playlist's name
	 */
	public Playlist(String name) {
		this.name = name;
		uris = new ArrayList<>();
		loadedTracks = new ArrayList<>();
		executor = Executors.newSingleThreadExecutor();
	}
	
	/**
	 * Loads this playlist. This will go through all uris and search with {@link ITrackSearch} for the {@link IAudioTrack}
	 * and {@link IAudioTrackList} for {@link LoadedTracks}. This method is async.
	 */
	public void load() {
		load(() -> {
		});
	}
	
	/**
	 * Loads this playlist. This will go through all uris and search with {@link ITrackSearch} for the {@link IAudioTrack}
	 * and {@link IAudioTrackList} for {@link LoadedTracks}. This method is async. This method calls the
	 * {@link Runnable#run()} method when everything is loaded and the playlist was not loaded before.
	 *
	 * @param runnable A runnable that should be executed when the playlist is loaded
	 */
	public void load(Runnable runnable) {
		if (loaded) {
			return;
		}
		executor.execute(() -> {
			unload(); // Unload everything before, because of the threaded executor this method might pass the check before
			
			if (uris.isEmpty()) {
				loaded = true;
				runnable.run();
				return;
			}
			
			final ITrackSearch search = MusicPlayerManager.getPlayer().getTrackSearch();
			
			uris.forEach(uri -> loadedTracks.add(new LoadedTracks(uri))); // Add dummy elements
			
			final AtomicInteger counterIfReady = new AtomicInteger();
			
			for (int index = 0; index < uris.size(); index++) {
				final int immutableIndex = index; // Little workaround for using the index in closure
				final WrappedObject<String> uri = uris.get(immutableIndex);
				search.getTracks(uri.get(), result -> {
					final LoadedTracks loadedTrack = new LoadedTracks(uri, result);
					loadedTracks.set(immutableIndex, loadedTrack);
					if (counterIfReady.incrementAndGet() == loadedTracks.size()) { // Count up for every replaced track in loadedTracks. When its the last one it sets the loaded flag and runs the
																					// runnable
						loaded = true;
						runnable.run();
					}
				});
			}
		});
	}
	
	/**
	 * Unloads this playlist and removes all loaded tracks.
	 */
	public void unload() {
		loadedTracks.clear();
		loaded = false;
	}
	
	/**
	 * Is this playlist loaded
	 *
	 * @return Playlist loaded
	 */
	public boolean isLoaded() {
		return loaded;
	}
	
	/**
	 * Adds an {@link IAudioTrack} to the uri list and the loaded tracks. This playlist must be loaded.
	 *
	 * @param track The track that should be added
	 * @return The {@link WrappedObject} with the uri as a string
	 */
	public WrappedObject<String> add(IAudioTrack track) {
		if (!loaded) {
			return null;
		}
		final WrappedObject<String> uri = new WrappedObject<>(track.getInfo().getURI());
		final int index = uris.size();
		uris.add(index, uri);
		loadedTracks.add(index, new LoadedTracks(uri, track));
		save();
		return uri;
	}
	
	/**
	 * Adds an {@link IAudioTrackList} to the uri list and the loaded tracks if it has a valid uri and is not a search
	 * result. This playlist must be loaded.
	 *
	 * @param trackList The tracklist that should be added
	 * @return The {@link WrappedObject} with the uri as a string
	 */
	public WrappedObject<String> add(IAudioTrackList trackList) {
		if (!loaded) {
			return null;
		}
		if (!trackList.isSearch() && trackList.hasUri()) {
			final WrappedObject<String> uri = new WrappedObject<>(trackList.getUri());
			final int index = uris.size();
			uris.add(index, uri);
			loadedTracks.add(index, new LoadedTracks(uri, trackList));
			save();
			return uri;
		}
		return null;
	}
	
	/**
	 * Removes an uri from the uri list and the loaded tracks. This playlist must be loaded.
	 *
	 * @param uri The {@link WrappedObject} with the uri as a string
	 * @return If the uri was removed
	 */
	public boolean remove(WrappedObject<String> uri) {
		if (!loaded) {
			return false;
		}
		final int index = uris.indexOf(uri);
		if (index >= 0) {
			uris.remove(index);
			loadedTracks.remove(index);
			save();
			return true;
		}
		return false;
	}
	
	/**
	 * Move the uri and loaded track in the list up or down. This playlist must be loaded.
	 *
	 * @param uri The {@link WrappedObject} with the uri as a string
	 * @param value Positive value to move the uri up the value, and the other way around for a negative value
	 * @return If move was successful
	 */
	public boolean move(WrappedObject<String> uri, int value) {
		if (!loaded) {
			return false;
		}
		final int oldIndex = uris.indexOf(uri);
		final int newIndex = oldIndex - value;
		if (newIndex >= 0 && newIndex < uris.size()) {
			uris.add(newIndex, uris.remove(oldIndex));
			loadedTracks.add(newIndex, loadedTracks.remove(oldIndex));
			save();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Sets the name of this playlist
	 *
	 * @param name Name
	 */
	public void setName(String name) {
		this.name = name;
		save();
	}
	
	/**
	 * Gets the name of this playlist
	 *
	 * @return Name of this playlist
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the size of uri entries
	 *
	 * @return Size of uri entries
	 */
	public int getEntrySize() {
		return uris.size();
	}
	
	/**
	 * Gets a {@link Collection} of {@link LoadedTracks}. Should only be used if this playlist is already loaded. This
	 * collection is immutable
	 *
	 * @return Collection with all loaded tracks
	 */
	public Collection<LoadedTracks> getLoadedTracks() {
		return Collections.unmodifiableCollection(loadedTracks);
	}
	
	/**
	 * Returns true if the playlist is empty and don't contain any uris.
	 *
	 * @return true if empty
	 */
	public boolean isEmpty() {
		return uris.isEmpty();
	}
	
	private void save() {
		MusicPlayerManager.getPlaylistManager().writeToFile();
	}
	
	// -------------------------------------------------------------------------------------------------
	// Start of implementation for playing this playlist. Nothing here is serializable.
	// -------------------------------------------------------------------------------------------------
	
	private transient LoadedTracks nextLoadedTrack;
	private transient IAudioTrack next;
	
	private transient boolean first;
	
	private transient Random random;
	
	@Override
	public boolean calculateNext() {
		final Settings settings = MusicPlayerManager.getSettingsManager().getSettings();
		if (nextLoadedTrack == null || next == null) {
			return false;
		} else if (first) {
			first = false;
			return true;
		} else if (settings.isSingleRepeat()) {
			return true;
		} else if (settings.isShuffle()) {
			return selectRandomTrack();
		} else {
			return findNextSong(settings, Skip.FORWARD);
		}
	}
	
	@Override
	public IAudioTrack getNext() {
		return next;
	}
	
	/**
	 * Skip the current song in the {@link Skip} direction
	 *
	 * @param skip Should be skipped forward or backward
	 * @return If skip was executed
	 */
	public boolean skip(Skip skip) {
		final Settings settings = MusicPlayerManager.getSettingsManager().getSettings();
		return first = settings.isShuffle() ? selectRandomTrack() : findNextSong(settings, skip);
	}
	
	/**
	 * Gets the first track {@link Pair} with {@link LoadedTracks} and {@link IAudioTrack} in this playlist. Values in the
	 * pair might be null if there are no tracks.
	 *
	 * @return Pair of {@link LoadedTracks} and {@link IAudioTrack}. Can't be null, but elements can be null.
	 */
	public Pair<LoadedTracks, IAudioTrack> getFirstTrack() {
		return getTrackAtIndex(0, LoadedTracks::getFirstTrack);
	}
	
	/**
	 * Gets the last track {@link Pair} with {@link LoadedTracks} and {@link IAudioTrack} in this playlist. Values in the
	 * pair might be null if there are no tracks.
	 *
	 * @return Pair of {@link LoadedTracks} and {@link IAudioTrack}. Can't be null, but elements can be null.
	 */
	public Pair<LoadedTracks, IAudioTrack> getLastTrack() {
		return getTrackAtIndex(loadedTracks.size() - 1, LoadedTracks::getLastTrack);
	}
	
	/**
	 * Gets a {@link LoadedTracks} at the index of the loaded tracks list in this playlist. The supplied function must then
	 * select the right {@link IAudioTrack}
	 *
	 * @param index The index of the {@link LoadedTracks} entry. Must be in bound
	 * @param function A function that returns an {@link IAudioTrack} based on the passed {@link LoadedTracks}
	 * @return Pair of {@link LoadedTracks} and {@link IAudioTrack}. Can't be null, but elements can be null.
	 */
	private Pair<LoadedTracks, IAudioTrack> getTrackAtIndex(int index, Function<LoadedTracks, IAudioTrack> function) {
		if (loadedTracks.isEmpty()) {
			return Pair.of(null, null);
		}
		final LoadedTracks loadedTrack = loadedTracks.get(index);
		if (loadedTrack == null) {
			return Pair.of(null, null);
		} else {
			return Pair.of(loadedTrack, function.apply(loadedTrack));
		}
	}
	
	/**
	 * Sets the start {@link LoadedTracks} with the contained {@link IAudioTrack}
	 *
	 * @param loadedTrack {@link LoadedTracks} which must be in this playlist
	 * @param track {@link IAudioTrack} which must be in the passed loadedTrack
	 */
	public void setPlayable(LoadedTracks loadedTrack, IAudioTrack track) {
		setTracks(loadedTrack, track);
		first = true;
	}
	
	/**
	 * Sets the next track to null. So the queue if playing will then be stopped.
	 */
	public void setStopable() {
		nextLoadedTrack = null;
		next = null;
	}
	
	/**
	 * Returns a pair of calculated songs. This pair is either one after the current song if {@link Skip} is
	 * {@link Skip#FORWARD} or one behind. If the next song is invalid which is tested with
	 * {@link #getTrackAndValidate(int)} then the next valid song is chosen. If the end or start of the playlist is reached
	 * the pair contains null values.
	 *
	 * @param loadedTrack The currently loaded track {@link LoadedTracks}
	 * @param track The currently playing {@link IAudioTrack}
	 * @param skip In which direction we want to skip
	 * @return Pair of {@link LoadedTracks} and {@link IAudioTrack}. Can't be null, but elements can be null.
	 */
	private Pair<LoadedTracks, IAudioTrack> getOtherTrack(LoadedTracks loadedTrack, IAudioTrack track, Skip skip) {
		if (loadedTrack == null) {
			return Pair.of(null, null);
		}
		final IAudioTrack nextTrack = loadedTrack.getOtherTrack(track, skip);
		if (nextTrack != null) {
			return Pair.of(loadedTrack, nextTrack);
		} else {
			final int index = loadedTracks.indexOf(loadedTrack);
			if (index == -1) {
				return Pair.of(null, null);
			}
			
			final IntPredicate testIndex = newIndex -> skip == Skip.FORWARD ? newIndex < loadedTracks.size() : newIndex >= 0;
			
			for (int newIndex = index + skip.getValue(); testIndex.test(newIndex); newIndex += skip.getValue()) {
				final LoadedTracks nextValidLoadedTrack = getTrackAndValidate(newIndex);
				if (nextValidLoadedTrack != null) {
					return Pair.of(nextValidLoadedTrack, skip == Skip.FORWARD ? nextValidLoadedTrack.getFirstTrack() : nextValidLoadedTrack.getLastTrack());
				}
			}
			return Pair.of(null, null);
			
		}
	}
	
	/**
	 * Get the index of a {@link LoadedTracks} in the playlist and tests if the index is valid and the {@link LoadedTracks}
	 * has no errors and contains a valid {@link IAudioTrack} or a valid {@link IAudioTrackList}. Returns null if the test
	 * above failed.
	 *
	 * @param index The index to search for in the playlist
	 * @return The loaded track or null if the index is out of bounds or the {@link LoadedTracks} has an error
	 */
	private LoadedTracks getTrackAndValidate(int index) {
		if (index <= 0 && index >= loadedTracks.size()) {
			return null;
		}
		final LoadedTracks loadedTrack = loadedTracks.get(index);
		if (loadedTrack.hasError() || (!loadedTrack.isTrack() && !loadedTrack.isTrackList())) {
			return null;
		}
		return loadedTrack;
	}
	
	/**
	 * Sets the {@link #loadedTracks} and {@link #next} variable to the passed arguments
	 *
	 * @param loadedTrack {@link LoadedTracks} which must be in this playlist
	 * @param track {@link IAudioTrack} which must be in the passed loadedTrack
	 */
	private void setTracks(LoadedTracks loadedTrack, IAudioTrack track) {
		nextLoadedTrack = loadedTrack;
		next = track;
	}
	
	/**
	 * Find a next song and set it to the {@link #nextLoadedTrack} and {@link #next} track variable
	 *
	 * @param settings The current settings
	 * @param skip In which direction we want to find the song
	 * @return Return true if a valid next song could be found. Otherwise return false
	 */
	private boolean findNextSong(Settings settings, Skip skip) {
		final Pair<LoadedTracks, IAudioTrack> pair = getOtherTrack(nextLoadedTrack, next, skip);
		final LoadedTracks loadedTrack = pair.getLeft();
		final IAudioTrack track = pair.getRight();
		
		if (loadedTrack == null || track == null) {
			if (settings.isFinite()) {
				return false;
			} else if (loadedTracks.size() > 0) {
				final Pair<LoadedTracks, IAudioTrack> sidePair = skip == Skip.FORWARD ? getFirstTrack() : getLastTrack();
				final LoadedTracks sideLoadedTrack = sidePair.getLeft();
				final IAudioTrack sideTrack = sidePair.getRight();
				if (sideLoadedTrack != null && sideTrack != null) {
					setTracks(sideLoadedTrack, sideTrack);
					return true;
				} else if (sideLoadedTrack != null) {
					final Pair<LoadedTracks, IAudioTrack> nextValidPair = getOtherTrack(sideLoadedTrack, null, skip);
					final LoadedTracks nextValidLoadedTrack = nextValidPair.getLeft();
					final IAudioTrack nextValidTrack = nextValidPair.getRight();
					if (nextValidLoadedTrack != null && nextValidTrack != null) {
						setTracks(nextValidLoadedTrack, nextValidTrack);
						return true;
					}
				}
			}
		} else if (loadedTrack != null && track != null) {
			setTracks(loadedTrack, track);
			return true;
		}
		return false;
	}
	
	/**
	 * Select a random track
	 *
	 * @return If a new random track was found
	 */
	private boolean selectRandomTrack() {
		final List<Pair<LoadedTracks, IAudioTrack>> shuffleEntries = new ArrayList<>();
		loadedTracks.forEach(loadedTrack -> {
			if (loadedTrack.isTrack()) {
				shuffleEntries.add(Pair.of(loadedTrack, loadedTrack.getTrack()));
			} else if (loadedTrack.isTrackList()) {
				loadedTrack.getTrackList().getTracks().forEach(track -> {
					shuffleEntries.add(Pair.of(loadedTrack, track));
				});
			}
		});
		if (shuffleEntries.isEmpty()) {
			return false;
		}
		if (random == null) {
			random = new Random();
		}
		Collections.shuffle(shuffleEntries, random);
		final Pair<LoadedTracks, IAudioTrack> pair = shuffleEntries.get(random.nextInt(shuffleEntries.size()));
		nextLoadedTrack = pair.getLeft();
		next = pair.getRight();
		return true;
	}
}
