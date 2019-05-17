package info.u_team.music_player.dependency.url;

import java.io.*;
import java.net.*;
import java.util.Optional;

public class UrlStreamHandlerMusicPlayer extends URLStreamHandler {

	@Override
	protected URLConnection openConnection(URL url) throws IOException {
		return new URLConnection(url) {

			@Override
			public void connect() throws IOException {
			}

			public InputStream getInputStream() throws IOException {
				String file = URLDecoder.decode(url.getFile(), "UTF-8");
				return Optional.ofNullable(getClass().getClassLoader().getResourceAsStream(file)).orElseThrow(MalformedURLException::new);
			}
		};
	}
}
