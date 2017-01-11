package se.gory_moon.globalgamerules.reference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reference {

    public static final String MODID = "globalgamerules";
    public static final String NAME = "Global GameRules";
    public static final String VERSION = "@MOD_VERSION@";

    public static final String PROXY_CLIENT = "se.gory_moon.globalgamerules.proxy.ClientProxy";
    public static final String PROXY_SERVER = "se.gory_moon.globalgamerules.proxy.ServerProxy";

    public static final String GUI_FACTORY = "se.gory_moon.globalgamerules.config.GGRGuiFactory";

    public static Logger logger = LogManager.getLogger(NAME.replace(" ", ""));

}
