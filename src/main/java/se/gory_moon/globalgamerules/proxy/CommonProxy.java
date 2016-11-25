package se.gory_moon.globalgamerules.proxy;

import net.minecraftforge.common.MinecraftForge;
import se.gory_moon.globalgamerules.GlobalGR;
import se.gory_moon.globalgamerules.WorldEvents;

public abstract class CommonProxy {

    public abstract void setConfigEntryClasses();

    public void registerEvents() {
        MinecraftForge.EVENT_BUS.register(GlobalGR.getConfig());
        MinecraftForge.EVENT_BUS.register(new WorldEvents());
    }
}
