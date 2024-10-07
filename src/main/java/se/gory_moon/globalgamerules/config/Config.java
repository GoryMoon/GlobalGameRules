package se.gory_moon.globalgamerules.config;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import se.gory_moon.globalgamerules.GlobalGR;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Config extends Configuration {

    public static final String CATEGORY_GAMERULES = "gamerules";
    public HashMap<String, Boolean> rules = new HashMap<String, Boolean>();
    public HashMap<String, Boolean> defaults;
    public HashMap<String, String> comments = new HashMap<String, String>();

    public Config(File name) {
        super(name);
        rules.put("commandBlockOutput", true);
        rules.put("doDaylightCycle", true);
        rules.put("doFireTick", true);
        rules.put("doMobLoot", true);
        rules.put("doMobSpawning", true);
        rules.put("keepInventory", false);
        rules.put("mobGriefing", true);
        rules.put("naturalRegeneration", true);
        rules.put("tfEnforcedProgression", true);

        comments.put("commandBlockOutput", "Whether command blocks should notify admins when they perform commands");
        comments.put("doDaylightCycle", "Whether time progresses");
        comments.put("doFireTick", "Whether fire should spread and naturally extinguish");
        comments.put("doMobLoot", "Whether mobs should drop items");
        comments.put("doMobSpawning", "Whether mobs should naturally spawn");
        comments.put("keepInventory", "Whether the player should keep items in their inventory after death");
        comments.put("mobGriefing", "Whether creepers, zombies, endermen, ghasts, withers, rabbits, sheep, and villagers should be able to change blocks and whether villagers, zombies, skeletons, and zombie pigmen can pick up items");
        comments.put("naturalRegeneration", "Whether the player can regenerate health naturally if their hunger is full enough (doesn't affect external healing, such as golden apples, the Regeneration effect, etc.)");
        comments.put("tfEnforcedProgression", "Whether the Twilight Forest mod's progression system is enabled or not");

        addCustomCategoryComment(CATEGORY_GAMERULES, "Set the values to 'true' or 'false' depending if you want to have the GameRule enabled or disabled");
        defaults = (HashMap<String, Boolean>) rules.clone();
    }

    public Config loadConfig() {
        load();
        syncConfigs();
        return this;
    }

    public void saveConfig() {
        ConfigCategory cat = getCategory(CATEGORY_GAMERULES);
        for (Map.Entry<String, Boolean> entry : rules.entrySet()) {
            String rule = entry.getKey();
            Boolean state = entry.getValue();

            Property prop = cat.get(rule);
            prop.setValue(state);
            cat.put(rule, prop);
        }

        if (hasChanged())
            save();
    }

    public void syncConfigs() {
        for (Map.Entry<String, Boolean> entry : rules.entrySet()) {
            String rule = entry.getKey();
            Boolean state = get(CATEGORY_GAMERULES, rule, defaults.get(rule), comments.get(rule)).getBoolean();
            rules.put(rule, state);
        }

        if (hasChanged())
            save();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(GlobalGR.MODID))
            syncConfigs();
    }

}
