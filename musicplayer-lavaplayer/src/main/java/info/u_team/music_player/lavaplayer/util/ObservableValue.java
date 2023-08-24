package info.u_team.music_player.lavaplayer.util;

import java.util.ArrayList;
import java.util.List;

public class ObservableValue<T> {
	
	private T value;
	private final List<ChangeListener<T>> changeListener;
	
	public ObservableValue(T value) {
		this.value = value;
		changeListener = new ArrayList<>();
	}
	
	public void registerListener(ChangeListener<T> listener) {
		changeListener.add(listener);
	}
	
	public void setValue(T value) {
		this.value = value;
		changeListener.forEach(listener -> listener.update(value));
	}
	
	public T getValue() {
		return value;
	}
	
	@FunctionalInterface
	public interface ChangeListener<T> {
		
		void update(T value);
	}
	
}
