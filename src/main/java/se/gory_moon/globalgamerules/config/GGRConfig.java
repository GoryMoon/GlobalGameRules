package se.gory_moon.globalgamerules.config;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import se.gory_moon.globalgamerules.reference.Reference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GGRConfig extends Configuration {

    public static final String CATEGORY_GAMERULES = "gamerules";
    public static final String CATEGORY_MISC = "misc";

    public static final String MISC_WORLDDIFFICULTY = "worldDifficulty";
    public static final String MISC_WORLDDIFFICULTYLOCK = "worldDifficultyLocked";

    public HashMap<String, Value> rules = new HashMap<>();
    public HashMap<String, Value> misc = new HashMap<>();
    public HashMap<String, Value> defaults;
    public HashMap<String, String> comments = new HashMap<>();

    public GGRConfig(File name) {
        super(name);
        rules.put("commandBlockOutput",         defaultValue(true));
        rules.put("disableElytraMovementCheck", defaultValue(false));
        rules.put("doDaylightCycle",            defaultValue(true));
        rules.put("doEntityDrops",              defaultValue(true));
        rules.put("doFireTick",                 defaultValue(true));
        rules.put("doLimitedCrafting",          defaultValue(false));
        rules.put("doMobLoot",                  defaultValue(true));
        rules.put("doMobSpawning",              defaultValue(true));
        rules.put("doTileDrops",                defaultValue(true));
        rules.put("doWeatherCycle",             defaultValue(true));
        rules.put("gameLoopFunction",           defaultValue("-"));
        rules.put("keepInventory",              defaultValue(false));
        rules.put("logAdminCommands",           defaultValue(true));
        rules.put("maxCommandChainLength",      defaultValue(65536));
        rules.put("maxEntityCramming",          defaultValue(24));
        rules.put("mobGriefing",                defaultValue(true));
        rules.put("naturalRegeneration",        defaultValue(true));
        rules.put("randomTickSpeed",            defaultValue(3));
        rules.put("reducedDebugInfo",           defaultValue(false));
        rules.put("sendCommandFeedback",        defaultValue(true));
        rules.put("showDeathMessages",          defaultValue(true));
        rules.put("spawnRadius",                defaultValue(10));
        rules.put("spectatorsGenerateChunks",   defaultValue(true));

        rules.put("announceAdvancements",       defaultValue(true));

        misc.put(MISC_WORLDDIFFICULTY,          defaultValue(-1));
        misc.put(MISC_WORLDDIFFICULTYLOCK,      defaultValue(false));


        comments.put("commandBlockOutput",          "Whether command blocks should notify admins when they perform commands");
        comments.put("disableElytraMovementCheck",  "Whether the server should skip checking player speed when the player is wearing elytra.");
        comments.put("doDaylightCycle",             "Whether the day-night cycle and moon phases progress");
        comments.put("doEntityDrops",               "Whether entities that are not mobs should have drops");
        comments.put("doFireTick",                  "Whether fire should spread and naturally extinguish");
        comments.put("doLimitedCrafting",           "Whether players should only be able to craft recipes that they've unlocked first");
        comments.put("doMobLoot",                   "Whether mobs should drop items");
        comments.put("doMobSpawning",               "Whether mobs should naturally spawn");
        comments.put("doTileDrops",                 "Whether blocks should have drops");
        comments.put("doWeatherCycle",              "Whether the weather will change");
        comments.put("gameLoopFunction",            "The function to run every game tick");
        comments.put("keepInventory",               "Whether the player should keep items in their inventory after death");
        comments.put("logAdminCommands",            "Whether to log admin commands to server log");
        comments.put("maxCommandCainLength",        "Determines the number at which the chain command block acts as a \"chain\".");
        comments.put("maxEntityCramming",           "The maximum number of other pushable entities a mob or player can push, before taking 3 suffocation damage\nper half-second. Setting to 0 disables the rule. Damage affects survival-mode "+
                                                    "or adventure-mode players, and all mobs but bats.\nPushable entities include non-spectator-mode players, any mob except bats, as well as boats and minecarts.");
        comments.put("mobGriefing",                 "Whether creepers, zombies, endermen, ghasts, withers, ender dragons, rabbits, sheep, and villagers should be able to change blocks\nand whether villagers, zombies, skeletons, and zombie pigmen can pick up items");
        comments.put("naturalRegeneration",         "Whether the player can regenerate health naturally if their hunger is full enough (doesn't affect external healing, such as\ngolden apples, the Regeneration effect, etc.)");
        comments.put("randomTickSpeed",             "How often a random block tick occurs (such as plant growth, leaf decay, etc.) per chunk section per game tick. 0 will disable random\nticks, higher numbers will increase random ticks");
        comments.put("reducedDebugInfo",            "Whether the debug screen shows all or reduced information; and whether the effects of F3+B (entity hitboxes) and F3+G (chunk boundaries) are shown.");
        comments.put("sendCommandFeedback",         "Whether the feedback from commands executed by a player should show up in chat. Also affects the default behavior of whether command blocks store their output text");
        comments.put("showDeathMessages",           "Whether a message appears in chat when a player dies");
        comments.put("spawnRadius",                 "The number of blocks outward from the world spawn coordinates that a player will spawn in when first joining a server or when dying without a spawnpoint.");
        comments.put("spectatorsGenerateChunks",    "Whether players in spectator mode can generate chunks");

        comments.put("announceAdvancements",        "If an announcement when a player gets an advancement should be done");

        comments.put(MISC_WORLDDIFFICULTY,          "Sets the difficulty of a world when loaded, respects it the difficulty is locked or not for the world\n-1: Disabled\n0: Peaceful\n1: Easy\n2: Normal\n3: Hard");
        comments.put(MISC_WORLDDIFFICULTYLOCK,      "If a world's difficulty should be locked when loaded, if world already is locked it can't be change\nIf the global world difficulty is enabled it's set first");


        addCustomCategoryComment(CATEGORY_GAMERULES,    "Set the values to ('true'/'false'/an integer or a string defaultValue) depending if you want to have the GameRule (enabled/disabled or have a different defaultValue)");
        addCustomCategoryComment(CATEGORY_MISC,         "A collection of misc configs");
        defaults = (HashMap<String, Value>) rules.clone();
        defaults.putAll((Map<? extends String, ? extends Value>) misc.clone());
    }

    private Value defaultValue(boolean val) {
        return new Value(Boolean.toString(val), ValueType.BOOLEAN);
    }

    private Value defaultValue(int val) {
        return new Value(Integer.toString(val), ValueType.INTEGER);
    }

    private Value defaultValue(String val) {
        return new Value(val, ValueType.STRING);
    }

    public GGRConfig loadConfig() {
        load();
        syncConfigs();
        saveConfig();
        save();
        return this;
    }

    public void saveConfig() {
        ConfigCategory ruleCat = getCategory(CATEGORY_GAMERULES);
        setValueToProp(ruleCat, rules);
        ConfigCategory miscCat = getCategory(CATEGORY_MISC);
        setValueToProp(miscCat, misc);

        if (hasChanged())
            save();
    }

    private void setValueToProp(ConfigCategory cat, HashMap<String, Value> list) {
        list.forEach((s, value) ->
            cat.put(s,
                value.getType().equals(ValueType.BOOLEAN) ?
                    cat.get(s).setValue(value.getBooleanValue())
                            .setRequiresWorldRestart(true)
                            .setShowInGui(value.getShowInGui()) :
                    value.getType().equals(ValueType.INTEGER) ?
                        cat.get(s).setValue(value.getIntegerValue())
                                .setRequiresWorldRestart(true)
                                .setShowInGui(value.getShowInGui()) :
                        cat.get(s).setValue(value.getStringValue())
                                .setRequiresWorldRestart(true)
                                .setShowInGui(value.getShowInGui())
            )
        );
    }


    public void syncConfigs() {
        syncConfigs(rules, CATEGORY_GAMERULES);
        syncConfigs(misc, CATEGORY_MISC);

        if (hasChanged())
            save();
    }

    private void syncConfigs(HashMap<String, Value> list, String cat) {
        list.forEach((s, value) -> list.put(s,
            new Value(
                    value.getType().equals(ValueType.BOOLEAN) ?
                            String.valueOf(get(cat, s, defaults.get(s).getBooleanValue(), comments.get(s)).getBoolean()):
                            value.getType().equals(ValueType.INTEGER) ?
                                String.valueOf(get(cat, s, defaults.get(s).getIntegerValue(), comments.get(s)).getInt()):
                                get(cat, s, defaults.get(s).getStringValue(), comments.get(s)).getString(),
                    value.getType(),
                    value.getShowInGui()
            )
        ));
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MODID))
            syncConfigs();
    }

    public enum ValueType {
        BOOLEAN,
        INTEGER,
        STRING;
    }

    public static class Value implements Cloneable {

        private String stringValue;
        private int integerValue;
        private boolean booleanValue;
        private boolean showInGui;
        private ValueType type;

        public Value(String s, ValueType type) {
            this(s, type, true);
        }

        public Value(String s, ValueType type, boolean showInGui) {
            this.type = type;
            this.showInGui = showInGui;
            setValue(s);
        }

        public Value setValue(String s) {
            this.stringValue = s;
            this.booleanValue = Boolean.parseBoolean(s);
            this.integerValue = this.booleanValue ? 1 : 0;

            try {
                this.integerValue = Integer.parseInt(s);
            } catch (NumberFormatException ignored) {
            }

            return this;
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

        public boolean getShowInGui() {
            return showInGui;
        }

        public ValueType getType() {
            return type;
        }

        @Override
        protected Value clone() throws CloneNotSupportedException {
            return (Value) super.clone();
        }
    }

}
