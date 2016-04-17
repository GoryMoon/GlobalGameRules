package se.gory_moon.globalgamerules.config;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import se.gory_moon.globalgamerules.GlobalGR;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Config extends Configuration {

    public static final String CATEGORY_GAMERULES = "gamerules";
    public HashMap<String, Value> rules = new HashMap<String, Value>();
    public HashMap<String, Value> defaults;
    public HashMap<String, String> comments = new HashMap<String, String>();

    public Config(File name) {
        super(name);
        rules.put("doFireTick", new Value("true", ValueType.BOOLEAN));
        rules.put("mobGriefing", new Value("true", ValueType.BOOLEAN));
        rules.put("keepInventory", new Value("false", ValueType.BOOLEAN));
        rules.put("doMobSpawning", new Value("true", ValueType.BOOLEAN));
        rules.put("doMobLoot", new Value("true", ValueType.BOOLEAN));
        rules.put("doTileDrops", new Value("true", ValueType.BOOLEAN));
        rules.put("commandBlockOutput", new Value("true", ValueType.BOOLEAN));
        rules.put("naturalRegeneration", new Value("true", ValueType.BOOLEAN));
        rules.put("doDaylightCycle", new Value("true", ValueType.BOOLEAN));
        rules.put("logAdminCommands", new Value("true", ValueType.BOOLEAN));
        rules.put("showDeathMessages", new Value("true", ValueType.BOOLEAN));
        rules.put("randomTickSpeed", new Value("3", ValueType.INTEGER));
        rules.put("sendCommandFeedback", new Value("true", ValueType.BOOLEAN));
        rules.put("reducedDebugInfo", new Value("false", ValueType.BOOLEAN));
        rules.put("spectatorsGenerateChunks", new Value("true", ValueType.BOOLEAN));
        rules.put("spawnRadius", new Value("10", ValueType.INTEGER));
        rules.put("disableElytraMovementCheck", new Value("false", ValueType.BOOLEAN));


        comments.put("doFireTick", "Whether fire should spread and naturally extinguish");
        comments.put("mobGriefing", "Whether creepers, zombies, endermen, ghasts, withers, rabbits, sheep, and villagers should be able to change blocks and whether villagers, zombies, skeletons, and zombie pigmen can pick up items");
        comments.put("keepInventory", "Whether the player should keep items in their inventory after death");
        comments.put("doMobSpawning", "Whether mobs should naturally spawn");
        comments.put("doMobLoot", "Whether mobs should drop items");
        comments.put("doTileDrops", "Whether blocks should have drops");
        comments.put("commandBlockOutput", "Whether command blocks should notify admins when they perform commands");
        comments.put("naturalRegeneration", "Whether the player can regenerate health naturally if their hunger is full enough (doesn't affect external healing, such as golden apples, the Regeneration effect, etc.)");
        comments.put("doDaylightCycle", "Whether time progresses");
        comments.put("logAdminCommands", "Whether to log admin commands to server log");
        comments.put("showDeathMessages", "Whether a message appears in chat when a player dies");
        comments.put("randomTickSpeed", "How often a random block tick occurs (such as plant growth, leaf decay, etc.) per chunk section per game tick. 0 will disable random ticks, higher numbers will increase random ticks");
        comments.put("sendCommandFeedback", "Whether the feedback from commands executed by a player should show up in chat. Also affects the default behavior of whether command blocks store their output text");
        comments.put("reducedDebugInfo", "Whether the debug screen shows all or reduced infomation");
        comments.put("spectatorsGenerateChunks", "Whether players in spectator mode can generate chunks");
        comments.put("spawnRadius", "The number of blocks outward from the world spawn coordinates that a player will spawn in when first joining a server or when dying without a spawnpoint.");
        comments.put("disableElytraMovementCheck", "Whether the server should skip checking player speed when the player is wearing elytra.");

        addCustomCategoryComment(CATEGORY_GAMERULES, "Set the values to 'true' or 'false' depending if you want to have the GameRule enabled or disabled");
        defaults = (HashMap<String, Value>) rules.clone();
    }

    public Config loadConfig() {
        load();
        syncConfigs();
        save();
        return this;
    }

    public void saveConfig() {
        ConfigCategory cat = getCategory(CATEGORY_GAMERULES);
        for (Map.Entry<String, Value> entry : rules.entrySet()) {
            String rule = entry.getKey();
            Config.Value state = entry.getValue();

            Property prop = cat.get(rule);
            if (state.getType().equals(ValueType.BOOLEAN))
                prop.setValue(state.getBooleanValue());
            else
                prop.setValue(state.getIntegerValue());
            cat.put(rule, prop);
        }

        if (hasChanged())
            save();
    }

    public void syncConfigs() {
        for (Map.Entry<String, Value> entry : rules.entrySet()) {
            String rule = entry.getKey();
            Value state = entry.getValue();
            String val;
            if (state.getType().equals(ValueType.BOOLEAN))
                val = String.valueOf(get(CATEGORY_GAMERULES, rule, defaults.get(rule).getBooleanValue(), comments.get(rule)).getBoolean());
            else
                val = String.valueOf(get(CATEGORY_GAMERULES, rule, defaults.get(rule).getIntegerValue(), comments.get(rule)).getInt());

            rules.put(rule, new Value(val, state.getType()));
        }

        if (hasChanged())
            save();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(GlobalGR.MODID))
            syncConfigs();
    }

    public enum ValueType {
        BOOLEAN,
        INTEGER;
    }

    public static class Value {

        private String stringValue;
        private int integerValue;
        private boolean booleanValue;
        private ValueType type;

        public Value(String s, ValueType type) {
            this.type = type;
            setValue(s);
        }

        public void setValue(String s) {

            this.stringValue = s;
            this.booleanValue = Boolean.parseBoolean(s);
            this.integerValue = this.booleanValue ? 1 : 0;

            try {
                this.integerValue = Integer.parseInt(s);
            } catch (NumberFormatException ignored) {
            }
        }

        public int getIntegerValue() {
            return integerValue;
        }

        public boolean getBooleanValue() {
            return booleanValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public ValueType getType() {
            return type;
        }
    }

}
