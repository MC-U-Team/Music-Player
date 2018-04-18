package info.u_team.music_player.plugin;

import static info.u_team.music_player.MusicPlayerConstants.*;

import java.util.Map;

import info.u_team.music_player.plugin.dependecy.DependecyManager;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.*;

@Name(NAME)
@TransformerExclusions("info.u_team.music_player.plugin")
public class MusicPlayerPlugin implements IFMLLoadingPlugin, IFMLCallHook {
	
	@Override
	public Void call() throws Exception {
		if (FMLLaunchHandler.side() == Side.SERVER) {
			FMLLog.bigWarning("This mod (modid: %s, name: %s) can only be used on client side! Please remove it on server side!", MODID, NAME);
			FMLCommonHandler.instance().exitJava(0, false);
		}
		new DependecyManager().execute();
		return null;
	}
	
	@Override
	public String[] getASMTransformerClass() {
		return null;
	}
	
	@Override
	public String getModContainerClass() {
		return null;
	}
	
	@Override
	public String getSetupClass() {
		return this.getClass().getName();
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
	}
	
	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
