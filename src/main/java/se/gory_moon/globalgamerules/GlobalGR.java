package se.gory_moon.globalgamerules;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import se.gory_moon.globalgamerules.config.Config;

@Mod(modid = GlobalGR.MODID, version = GlobalGR.VERSION, guiFactory = "se.gory_moon.globalgamerules.config.GGRGuiFactory", acceptableRemoteVersions = "*")
public class GlobalGR {
    public static final String MODID = "GlobalGameRules";
    public static final String VERSION = "@MOD_VERSION@";

    @Instance
    public static GlobalGR instance;
    public Config config;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Config(event.getSuggestedConfigurationFile()).loadConfig();
        MinecraftForge.EVENT_BUS.register(config);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new WorldEvents());

    }
}
