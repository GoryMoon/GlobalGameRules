package se.gory_moon.globalgamerules.proxy;

import se.gory_moon.globalgamerules.GlobalGR;
import se.gory_moon.globalgamerules.config.GGRConfig;
import se.gory_moon.globalgamerules.config.GGRConfigGUI;

import static se.gory_moon.globalgamerules.config.GGRConfig.MISC_WORLDDIFFICULTY;

public class ClientProxy extends CommonProxy {

    @Override
    public void setConfigEntryClasses() {
        GlobalGR.getConfig().get(GGRConfig.CATEGORY_MISC, MISC_WORLDDIFFICULTY,
                GlobalGR.getConfig().defaults.get(MISC_WORLDDIFFICULTY).getIntegerValue()).setConfigEntryClass(GGRConfigGUI.DifficultyEntry.class);
    }

    @Override
    public void registerEvents() {
        super.registerEvents();
    }
}
