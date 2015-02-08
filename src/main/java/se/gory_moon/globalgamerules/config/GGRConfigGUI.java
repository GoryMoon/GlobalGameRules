package se.gory_moon.globalgamerules.config;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import se.gory_moon.globalgamerules.GlobalGR;


public class GGRConfigGUI extends GuiConfig {

    public GGRConfigGUI(GuiScreen parentScreen) {
        super(parentScreen,
                new ConfigElement(GlobalGR.instance.config.getCategory(GlobalGR.instance.config.CATEGORY_GAMERULES)).getChildElements(),
                GlobalGR.MODID, true, false, GuiConfig.getAbridgedConfigPath(GlobalGR.instance.config.toString()));
    }
}
