package info.u_team.music_player.lavaplayer.sources;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterface;

import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.YoutubeSourceOptions;
import net.hycrafthd.youtube_source_services.LocalService;
import net.hycrafthd.youtube_source_services.YoutubeSourceServices;

/**
 * Youtube source that runs the cipher and poToken services locally. They are installed once at startup and then only
 * run while youtube is used. After {@link #IDLE_TIMEOUT_MS} without youtube requests and without a playing track they
 * are stopped again.
 */
public class YoutubeServicesAudioSourceManager extends YoutubeAudioSourceManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(YoutubeServicesAudioSourceManager.class);

	private static final long IDLE_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(5);

	private final YoutubeSourceServices services;
	private final BooleanSupplier playing;

	/** When youtube was last used, 0 while the services are not in use. Guarded by this. */
	private long lastUsed;

	/**
	 * @param playing If a track is playing, which keeps the services running
	 */
	public static YoutubeAudioSourceManager create(BooleanSupplier playing) {
		try {
			final YoutubeSourceServices services = YoutubeSourceServices.builder(Paths.get(System.getProperty("java.io.tmpdir", "/tmp"), "musicplayer-youtube-services")) //
					.log(line -> LOGGER.debug(line)) //
					.build();
			return new YoutubeServicesAudioSourceManager(services, playing);
		} catch (final IOException ex) {
			LOGGER.warn("Cannot create the youtube services. Youtube tracks might not be playable", ex);
			return new YoutubeAudioSourceManager(new YoutubeSourceOptions(), DEFAULT_CLIENTS);
		}
	}

	private YoutubeServicesAudioSourceManager(YoutubeSourceServices services, BooleanSupplier playing) {
		super(createOptions(services), DEFAULT_CLIENTS);
		this.services = services;
		this.playing = playing;

		install();

		final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
			final Thread thread = new Thread(runnable, "musicplayer-youtube-services-idle");
			thread.setDaemon(true);
			return thread;
		});
		scheduler.scheduleWithFixedDelay(this::stopIfIdle, 30, 30, TimeUnit.SECONDS);
	}

	private static YoutubeSourceOptions createOptions(YoutubeSourceServices services) {
		final LocalService cipher = services.cipher();
		final LocalService poToken = services.poToken();
		return new YoutubeSourceOptions() //
				.setRemoteCipher(cipher.getUrl(), cipher.getToken(), "musicplayer") //
				.setRemotePoToken(poToken.getUrl(), poToken.getToken());
	}

	/**
	 * Starts the services once, which downloads and installs them, and stops them again if youtube was not used in the
	 * meantime.
	 */
	private void install() {
		services.start().whenComplete((unused, error) -> {
			if (error != null) {
				LOGGER.warn("Cannot start the youtube services. Youtube tracks might not be playable", error);
			}
			synchronized (this) {
				if (lastUsed == 0) {
					services.stop();
				}
			}
		});
	}

	/**
	 * Starts the services if needed and waits until they are ready. Only called on lavaplayer's loading and playback
	 * threads.
	 */
	private void use() {
		final CompletableFuture<Void> start;
		synchronized (this) {
			lastUsed = System.currentTimeMillis();
			start = services.start();
		}
		try {
			start.get();
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
		} catch (final Exception ex) {
			LOGGER.warn("Cannot start the youtube services. Youtube tracks might not be playable", ex);
		}
	}

	private synchronized void stopIfIdle() {
		if (lastUsed == 0) {
			return;
		}
		if (playing.getAsBoolean()) {
			lastUsed = System.currentTimeMillis();
		} else if (System.currentTimeMillis() - lastUsed >= IDLE_TIMEOUT_MS && services.isRunning()) {
			lastUsed = 0;
			services.stop();
			LOGGER.info("Stopped the youtube services because youtube was not used");
		}
	}

	/**
	 * Decides whether youtube handles an identifier, before any request to youtube. Searches and loads of youtube urls
	 * wait for the services here.
	 */
	@Override
	protected Router getRouter(HttpInterface httpInterface, String identifier) {
		final Router router = super.getRouter(httpInterface, identifier);
		if (router != null && router != Router.none) {
			use();
		}
		return router;
	}

	/**
	 * Used when a youtube track starts playing, which needs the cipher service.
	 */
	@Override
	public HttpInterface getInterface() {
		use();
		return super.getInterface();
	}
}
