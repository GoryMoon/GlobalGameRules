package se.gory_moon.globalgamerules;


import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import se.gory_moon.globalgamerules.config.GGRConfig;
import se.gory_moon.globalgamerules.proxy.CommonProxy;
import se.gory_moon.globalgamerules.reference.Reference;

@Mod(modid = Reference.MODID, name = Reference.NAME,version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY, acceptableRemoteVersions = "*")
public class GlobalGR {

    @Instance
    private static GlobalGR instance;

    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_SERVER)
    private static CommonProxy proxy;

    private GGRConfig GGRConfig;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Reference.logger = event.getModLog();
        GGRConfig = new GGRConfig(event.getSuggestedConfigurationFile()).loadConfig();
        proxy.setConfigEntryClasses();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerEvents();
    }

    public static GlobalGR getInstance() {
        return instance;
    }

    public static GGRConfig getConfig() {
        return instance.GGRConfig;
    }
}
