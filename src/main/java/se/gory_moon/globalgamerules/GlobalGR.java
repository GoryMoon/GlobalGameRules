package se.gory_moon.globalgamerules;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * The main mod class used for config setup and event-listener registering
 */
@Mod(GlobalGR.MOD_ID)
public class GlobalGR {

    /**
     * The mod id
     */
    public static final String MOD_ID = "globalgamerules";

    /**
     * Registers the construction method for the config and registers the event-listener for the world events
     */
    public GlobalGR() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::constructMod);
        MinecraftForge.EVENT_BUS.register(WorldEvents.class);
    }

    private void constructMod(FMLConstructModEvent event) {
        ModContainer container = ModLoadingContext.get().getActiveContainer();
        event.enqueueWork(() -> container.addConfig(new ModConfig(ModConfig.Type.COMMON, GGRConfig.commonSpec, container)));
    }
}
