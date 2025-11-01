package se.gory_moon.globalgamerules;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The main mod class used for config setup and event-listener registering
 */
@Mod(GlobalGR.MOD_ID)
public class GlobalGR {

    private static final Logger LOGGER = LogManager.getLogger("GlobalGameRules");

    /**
     * The mod id
     */
    public static final String MOD_ID = "globalgamerules";

    /**
     * Registers the construction method for the config and registers the event-listener for the world events
     */
    public GlobalGR(FMLJavaModLoadingContext context) {
        MinecraftForge.EVENT_BUS.register(WorldEvents.class);

        context.getContainer().addConfig(new ModConfig(ModConfig.Type.COMMON, Config.commonSpec, context.getContainer()));

        var modEventBus = context.getModEventBus();
        modEventBus.addListener(this::loadComplete);
    }

    public void loadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> {
            // Check gamerules and update non vanilla ones in the config
            Config.COMMON.updateModdedGamerules(null);
        });
    }
}
