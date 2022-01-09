package info.u_team.music_player.dependency.urlprovider;

import cpw.mods.cl.ModularURLHandler;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.function.Function;

/**
 * Copied method from DependencyManager#setupURLStreamHack and implement IURLProvider.
 * Loading from ServiceLoader.
 * (See {@link ModularURLHandler#initFrom(ModuleLayer) ModularURLHander.initFrom(ModuleLayer)},
 * {@link java.util.ServiceLoader#load(ModuleLayer, Class) ServiceLoader.load(ModuleLayer, Class)},
 * src/main/resources/META-INF/services/cpw.mods.cl.ModularURLHandler$IURLProvider)
 */
public class DependencyURLProvider implements ModularURLHandler.IURLProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public String protocol() {
        return "jarlookup";
    }

    @Override
    public Function<URL, InputStream> inputStreamFunction() {
        return url -> {
            try {
                final var info = FMLLoader.getLoadingModList().getModFileById(url.getHost());
                if (info == null) {
                    throw new IOException("Modid " + url.getHost() + " does not exists");
                }
                final var path = info.getFile().findResource(url.getPath().substring(1));
                return Files.newInputStream(path);
            } catch (IOException ex) {
                LOGGER.error("Could not find resource {}", url);
                throw new UncheckedIOException(ex);
            }
        };
    }
}
