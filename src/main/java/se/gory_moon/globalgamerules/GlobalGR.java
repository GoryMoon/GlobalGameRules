package se.gory_moon.globalgamerules;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import se.gory_moon.globalgamerules.config.Config;

@Mod(modid = GlobalGR.MODID, version = GlobalGR.VERSION, guiFactory = "se.gory_moon.globalgamerules.GGRGuiFactory", acceptableRemoteVersions = "*")
public class GlobalGR {
    public static final String MODID = "GlobalGameRules";
    public static final String VERSION = "@MOD_VERSION@";

    @Mod.Instance
    public static GlobalGR instance;
    public Config config;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Config(event.getSuggestedConfigurationFile()).loadConfig();
        FMLCommonHandler.instance().bus().register(config);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new WorldEvents());

    }
}
