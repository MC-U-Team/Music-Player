package info.u_team.music_player.musicplayer.playlist;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.music_player.lavaplayer.api.audio.*;
import info.u_team.music_player.lavaplayer.api.queue.ITrackQueue;
import info.u_team.music_player.lavaplayer.api.search.ITrackSearch;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.settings.Settings;
import info.u_team.music_player.util.WrappedObject;

/**
 * This class represents a playlist. This list can be serialized or deserialized. After a serialization the tracks must be loaded, because only the
 * uris are saved. {@link IAudioTrack} and {@link IAudioTrackList} can be added. Tracks can be removed. Tracks can be moved in the order. Any changes
 * to the serializable fields are saved
 * 
 * @author HyCraftHD
 *
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
	 * @param name
	 *            The playlist's name
	 */
	public Playlist(String name) {
		this.name = name;
		uris = new ArrayList<>();
		loadedTracks = new ArrayList<>();
		executor = Executors.newSingleThreadExecutor();
	}
	
	/**
	 * Loads this playlist. This will go through all uris and search with {@link ITrackSearch} for the {@link IAudioTrack} and {@link IAudioTrackList} for
	 * {@link LoadedTracks}. This method is async.
	 */
	public void load() {
		load(() -> {
		});
	}
	
	/**
	 * Loads this playlist. This will go through all uris and search with {@link ITrackSearch} for the {@link IAudioTrack} and {@link IAudioTrackList} for
	 * {@link LoadedTracks}. This method is async. This method calls the {@link Runnable#run()} method when everything is loaded.
	 * 
	 * @param runnable
	 *            A runnable that should be executed when the playlist is loaded
	 */
	public void load(Runnable runnable) {
		if (loaded) {
			return;
		}
		executor.execute(() -> {
			unload(); // Unload everything before, because of the threaded executor this method might pass the check before
			
			final ITrackSearch search = MusicPlayerManager.getPlayer().getTrackSearch();
			
			uris.forEach(uri -> loadedTracks.add(new LoadedTracks(uri))); // Add dummy elements
			
			AtomicInteger counterIfReady = new AtomicInteger();
			
			for (int index = 0; index < uris.size(); index++) {
				final int immutableIndex = index; // Little workaround for using the index in closure
				final WrappedObject<String> uri = uris.get(immutableIndex);
				search.getTracks(uri.get(), result -> {
					LoadedTracks loadedTrack = new LoadedTracks(uri, result);
					loadedTracks.set(immutableIndex, loadedTrack);
					if (counterIfReady.incrementAndGet() == loadedTracks.size()) { // Count up for every replaced track in loadedTracks. When its the last one it sets the loaded flag and runs the runnable
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
	 * @param track
	 *            The track that should be added
	 * @return The {@link WrappedObject} with the uri as a string
	 */
	public WrappedObject<String> add(IAudioTrack track) {
		if (!loaded) {
			return null;
		}
		WrappedObject<String> uri = new WrappedObject<>(track.getInfo().getURI());
		int index = uris.size();
		uris.add(index, uri);
		loadedTracks.add(index, new LoadedTracks(uri, track));
		save();
		return uri;
	}
	
	/**
	 * Adds an {@link IAudioTrackList} to the uri list and the loaded tracks if it has a valid uri and is not a search result. This playlist must be
	 * loaded.
	 * 
	 * @param trackList
	 *            The tracklist that should be added
	 * @return The {@link WrappedObject} with the uri as a string
	 */
	public WrappedObject<String> add(IAudioTrackList trackList) {
		if (!loaded) {
			return null;
		}
		if (!trackList.isSearch() && trackList.hasUri()) {
			WrappedObject<String> uri = new WrappedObject<>(trackList.getUri());
			int index = uris.size();
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
	 * @param uri
	 *            The {@link WrappedObject} with the uri as a string
	 * @return If the uri was removed
	 */
	public boolean remove(WrappedObject<String> uri) {
		if (!loaded) {
			return false;
		}
		int index = uris.indexOf(uri);
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
	 * @param uri
	 *            The {@link WrappedObject} with the uri as a string
	 * @param value
	 *            Positive value to move the uri up the value, and the other way around for a negative value
	 * 
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
	 * @param name
	 *            Name
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
	 * Gets a {@link Collection} of {@link LoadedTracks}. Should only be used if this playlist is already loaded. This collection is immutable
	 * 
	 * @return Collection with all loaded tracks
	 */
	public Collection<LoadedTracks> getLoadedTracks() {
		return Collections.unmodifiableCollection(loadedTracks);
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
		} else if (!settings.isShuffle()) {
			if (settings.isRepeat()) {
				return true;
			}
			final Pair<LoadedTracks, IAudioTrack> pair = getOtherTrack(nextLoadedTrack, next, Skip.FORWARD);
			if (pair.getLeft() == null || pair.getRight() == null) {
				if (settings.isFinite()) {
					return false;
				} else {
					nextLoadedTrack = loadedTracks.get(0);
					if (nextLoadedTrack != null) {
						next = nextLoadedTrack.getFirstTrack();
						if (next != null) {
							return true;
						}
					}
					return false;
				}
			} else {
				nextLoadedTrack = pair.getLeft();
				next = pair.getRight();
				return true;
			}
		} else {
			List<Pair<LoadedTracks, IAudioTrack>> shuffleEntries = new ArrayList<>();
			loadedTracks.forEach(loadedTrack -> {
				if (loadedTrack.isTrack()) {
					shuffleEntries.add(Pair.of(loadedTrack, loadedTrack.getTrack()));
				} else {
					loadedTrack.getTrackList().getTracks().forEach(track -> {
						shuffleEntries.add(Pair.of(loadedTrack, track));
					});
				}
			});
			if (random == null) {
				random = new Random();
			}
			Collections.shuffle(shuffleEntries, random);
			Pair<LoadedTracks, IAudioTrack> pair = shuffleEntries.get(new Random().nextInt(shuffleEntries.size()));
			nextLoadedTrack = pair.getLeft();
			next = pair.getRight();
			return true;
		}
	}
	
	@Override
	public IAudioTrack getNext() {
		return next;
	}
	
	/**
	 * Returns a pair of calculated songs. This pair is either on after the current song if {@link Skip} is {@link Skip#FORWARD} or one behind.
	 * 
	 * @param loadedTrack
	 *            The currently loaded track {@link LoadedTracks}
	 * @param track
	 *            The currently playing {@link IAudioTrack}
	 * @param skip
	 *            In which direction we wanna skip
	 * @return Pair of {@link LoadedTracks} and {@link IAudioTrack}. Can't be null, but elements can be null.
	 */
	private Pair<LoadedTracks, IAudioTrack> getOtherTrack(LoadedTracks loadedTrack, IAudioTrack track, Skip skip) {
		LoadedTracks nextLoadedTrack = loadedTrack;
		IAudioTrack nextTrack = loadedTrack.getOtherTrack(track, skip);
		if (nextTrack == null) {
			final int newIndex = loadedTracks.indexOf(loadedTrack) + skip.getValue();
			if (newIndex >= 0 && newIndex < loadedTracks.size()) {
				nextLoadedTrack = loadedTracks.get(newIndex);
				nextTrack = skip == Skip.FORWARD ? nextLoadedTrack.getFirstTrack() : nextLoadedTrack.getLastTrack();
			} else {
				nextLoadedTrack = null;
			}
		}
		return Pair.of(nextLoadedTrack, nextTrack);
	}
	
	/**
	 * Sets the start {@link LoadedTracks} with the contained {@link IAudioTrack}
	 * 
	 * @param loadedTrack
	 *            {@link LoadedTracks} which must be in this playlist
	 * @param track
	 *            {@link IAudioTrack} which must be in the passed loadedTrack
	 * 
	 */
	public void setPlayable(LoadedTracks loadedTrack, IAudioTrack track) {
		nextLoadedTrack = loadedTrack;
		next = track;
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
	 * Gets the first track {@link Pair} with {@link LoadedTracks} and {@link IAudioTrack} in this playlist. Might be null if there are no tracks.
	 * 
	 * @return {@link Pair} with {@link LoadedTracks} as key and {@link IAudioTrack} as value
	 */
	public Pair<LoadedTracks, IAudioTrack> getFirstTrack() {
		LoadedTracks loadedTrack = loadedTracks.get(0);
		if (loadedTrack == null) {
			return null;
		} else {
			return Pair.of(loadedTrack, loadedTrack.getFirstTrack());
		}
	}
	
	/**
	 * Skip the current song in the {@link Skip} direction
	 * 
	 * @param skip
	 *            Should be skipped forward or backward
	 * @return If skip was executed
	 */
	public boolean skip(Skip skip) {
		final Settings settings = MusicPlayerManager.getSettingsManager().getSettings();
		if (!settings.isShuffle()) {
			Pair<LoadedTracks, IAudioTrack> pair = getOtherTrack(nextLoadedTrack, next, skip);
			LoadedTracks loadedTrack = pair.getLeft();
			IAudioTrack track = pair.getRight();
			if (loadedTrack == null || track == null) {
				if (!settings.isFinite()) {
					nextLoadedTrack = loadedTracks.get(0);
					if (nextLoadedTrack != null) {
						next = nextLoadedTrack.getFirstTrack();
					}
				}
			}
			if (loadedTrack != null && track != null) {
				setPlayable(loadedTrack, track);
				return true;
			}
			return false;
		}
		return true;
	}
	
}
