package info.u_team.music_player.util;

import java.util.Collection;

import com.google.gson.annotations.SerializedName;

/**
 * This class holds an object. This class is immutable. To get the object use {@link #get()}. This method use the
 * default implementation of {@link #hashCode()} and {@link #equals(Object)}. This ensures there can't be
 * {@link WrappedObject} which are equals. This can be used in the Collection Framework {@link Collection} for some
 * special use.
 * 
 * @author HyCraftHD
 *
 * @param <T>
 */
public class WrappedObject<T> {

	/**
	 * Wrapped object <br>
	 * Annotation sets the gson default serialization to <code>o</code>.
	 */
	@SerializedName(value = "o", alternate = { "object" })
	private final T object;

	/**
	 * Creates a new {@link WrappedObject}
	 * 
	 * @param object The object you want to wrap. Must match the generic type
	 */
	public WrappedObject(T object) {
		this.object = object;
	}

	/**
	 * Retrieves the wrapped object
	 * 
	 * @return Wrapped object
	 */
	public T get() {
		return object;
	}

}
