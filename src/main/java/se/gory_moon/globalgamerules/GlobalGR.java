package se.gory_moon.globalgamerules;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(GlobalGR.MOD_ID)
public class GlobalGR {

    public static final String MOD_ID = "globalgamerules";

    public GlobalGR() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GGRConfig.commonSpec);
        MinecraftForge.EVENT_BUS.register(WorldEvents.class);
    }
}
