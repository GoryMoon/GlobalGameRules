package se.gory_moon.globalgamerules;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.base.Strings;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GGRConfig {

    public static final Common COMMON;
    public static final ForgeConfigSpec commonSpec;
    private static final HashMap<GameRules.RuleKey<?>, String> COMMENTS = new HashMap<>();

    static {
        COMMENTS.put(GameRules.DO_FIRE_TICK,                  "Whether fire should spread and naturally extinguish");
        COMMENTS.put(GameRules.MOB_GRIEFING,                  "Whether creepers, zombies, endermen, ghasts, withers, ender dragons, rabbits, sheep, villagers, and snow golems should be able to change blocks and whether mobs can pick up items. This also affects the capability of zombie-like creatures like zombie pigmen and drowned to pathfind to turtle eggs. This will also prevent villagers from breeding.");
        COMMENTS.put(GameRules.KEEP_INVENTORY,                "Whether the player should keep items in their inventory after death");
        COMMENTS.put(GameRules.DO_MOB_SPAWNING,               "Whether mobs should naturally spawn. Does not affect monster spawners.");
        COMMENTS.put(GameRules.DO_MOB_LOOT,                   "Whether mobs should drop items");
        COMMENTS.put(GameRules.DO_TILE_DROPS,                 "Whether blocks should have drops");
        COMMENTS.put(GameRules.DO_ENTITY_DROPS,               "Whether entities that are not mobs should have drops");
        COMMENTS.put(GameRules.COMMAND_BLOCK_OUTPUT,          "Whether command blocks should notify admins when they perform commands\t");
        COMMENTS.put(GameRules.NATURAL_REGENERATION,          "Whether the player can regenerate health naturally if their hunger is full enough (doesn't affect external healing, such as golden apples, the Regeneration effect, etc.)");
        COMMENTS.put(GameRules.DO_DAYLIGHT_CYCLE,             "Whether the day-night cycle and moon phases progress");
        COMMENTS.put(GameRules.LOG_ADMIN_COMMANDS,            "Whether to log admin commands to server log");
        COMMENTS.put(GameRules.SHOW_DEATH_MESSAGES,           "Whether death messages are put into chat when a player dies. Also affects whether a message is sent to the pet's owner when the pet dies.");
        COMMENTS.put(GameRules.RANDOM_TICK_SPEED,             "How often a random block tick occurs (such as plant growth, leaf decay, etc.) per chunk section per game tick. 0 disables random ticks, higher numbers increase random ticks. Setting to a high integer results in high speeds of decay and growth");
        COMMENTS.put(GameRules.SEND_COMMAND_FEEDBACK,         "Whether the feedback from commands executed by a player should show up in chat. Also affects the default behavior of whether command blocks store their output text");
        COMMENTS.put(GameRules.REDUCED_DEBUG_INFO,            "Whether the debug screen shows all or reduced information; and whether the effects of F3+B (entity hitboxes) and F3+G (chunk boundaries) are shown.");
        COMMENTS.put(GameRules.SPECTATORS_GENERATE_CHUNKS,    "Whether players in spectator mode can generate chunks");
        COMMENTS.put(GameRules.SPAWN_RADIUS,                  "The number of blocks outward from the world spawn coordinates that a player spawns in when first joining a server or when dying without a personal spawnpoint.");
        COMMENTS.put(GameRules.DISABLE_ELYTRA_MOVEMENT_CHECK, "Whether the server should skip checking player speed when the player is wearing elytra. Often helps with jittering due to lag in multiplayer, but may also be used to travel unfairly long distances in survival mode (cheating).");
        COMMENTS.put(GameRules.MAX_ENTITY_CRAMMING,           "The maximum number of other pushable entities a mob or player can push, before taking 3 suffocation damage per half-second. Setting to 0 disables the rule. Damage affects survival-mode or adventure-mode players, and all mobs but bats. Pushable entities include non-spectator-mode players, any mob except bats, as well as boats and minecarts.");
        COMMENTS.put(GameRules.DO_WEATHER_CYCLE,              "Whether the weather can change");
        COMMENTS.put(GameRules.DO_LIMITED_CRAFTING,           "Whether players should be able to craft only those recipes that they've unlocked first");
        COMMENTS.put(GameRules.MAX_COMMAND_CHAIN_LENGTH,      "Determines the number at which the chain command block acts as a \"chain\"");
        COMMENTS.put(GameRules.ANNOUNCE_ADVANCEMENTS,         "Whether advancements should be announced in chat");
        COMMENTS.put(GameRules.DISABLE_RAIDS,                 "Whether raids are disabled.");
        COMMENTS.put(GameRules.DO_INSOMNIA,                   "Whether phantoms can spawn in the nighttime");
        COMMENTS.put(GameRules.DO_IMMEDIATE_RESPAWN,          "Players respawn immediately without showing the death screen");
        COMMENTS.put(GameRules.DROWNING_DAMAGE,               "Whether the player should take damage when drowning");
        COMMENTS.put(GameRules.FALL_DAMAGE,                   "Whether the player should take fall damage");
        COMMENTS.put(GameRules.FIRE_DAMAGE,                   "Whether the player should take fire damage");
        COMMENTS.put(GameRules.DO_PATROL_SPAWNING,            "Whether patrols can spawn.");
        COMMENTS.put(GameRules.DO_TRADER_SPAWNING,            "Whether wandering traders can spawn.");
        COMMENTS.put(GameRules.FORGIVE_DEAD_PLAYERS,          "Makes angered neutral mobs stop being angry when the targeted player dies nearby.");
        COMMENTS.put(GameRules.UNIVERSAL_ANGER,               "Makes angered neutral mobs attack any nearby player, not just the player that angered them. Works best if forgiveDeadPlayers is disabled.");


        Pair<Common, ForgeConfigSpec> configSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = configSpecPair.getRight();
        COMMON = configSpecPair.getLeft();
    }

    public static class Common {

        public final BooleanValue setDifficulty;
        public final EnumValue<Difficulty> difficulty;
        public final BooleanValue hardcore;
        public final BooleanValue lockDifficulty;
        public final ConfigValue<List<? extends String>> defaultCommands;
        public final BooleanValue saveGameRules;
        public final Map<GameRules.RuleKey<?>, ConfigValue<?>> gameRules;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Gamerules that are set when a world is loaded")
                    .push("gamerules");

            gameRules = new HashMap<>();
            Converter<String, String> converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE);
            GameRules.visitAll(new GameRules.IRuleEntryVisitor() {
                @Override
                public <T extends GameRules.RuleValue<T>> void visit(GameRules.RuleKey<T> ruleKey, GameRules.RuleType<T> ruleType) {
                    T t = ruleType.createValue();
                    String name = converter.convert(ruleKey.getName());
                    if (name != null) {
                        if (t instanceof GameRules.BooleanValue) {
                            BooleanValue value = builder
                                    .comment(COMMENTS.get(ruleKey))
                                    .define(name, ((GameRules.BooleanValue) t).get());
                            gameRules.put(ruleKey, value);
                        } else if (t instanceof GameRules.IntegerValue) {
                            IntValue value = builder
                                    .comment(COMMENTS.get(ruleKey))
                                    .defineInRange(name, ((GameRules.IntegerValue) t).get(), 0, Integer.MAX_VALUE);
                            gameRules.put(ruleKey, value);
                        }
                    }
                }
            });

            builder.pop()
                    .comment("Configs related to difficult changes")
                    .push("difficulty");

            setDifficulty = builder
                    .comment("If the difficulty should be set on world load", "If true difficulty changes in game will also be saved to the config")
                    .define("set_difficulty", false);

            difficulty = builder
                    .comment("The difficulty to set if 'set_difficulty' is true, respects if the difficulty is locked or not for the world")
                    .defineEnum("difficulty", Difficulty.NORMAL);

            hardcore = builder
                    .comment("If true the world will be set to hardcore, difficultly will be set to hard independent to the 'difficulty' config", "Setting it to hardcore auto locks the difficulty while this is true")
                    .define("hardcore", false);

            lockDifficulty = builder
                    .comment("If a world's difficulty should be locked when loaded, if world already is locked it can't be changed", "If the global world difficulty is enabled it's set first")
                    .define("lock_difficulty", false);

            builder.pop()
                    .comment("A collection of misc configs")
                    .push("misc");

            saveGameRules = builder
                    .comment("If gamerules and world difficulty should be saved to config on world unload")
                    .define("save_gamerules", true);

            defaultCommands = builder
                    .comment("A comma separated list of commands to run on world join, @p is replaced with joining player name, command is run by the server", "Example: default_commands = [\"/tellraw @p [\\\"\\\",{\\\"text\\\":\\\"Hi \\\"},{\\\"text\\\":\\\"@p\\\",\\\"color\\\":\\\"aqua\\\"}]\"]")
                    .defineList("default_commands", ArrayList::new, o -> !Strings.isNullOrEmpty(String.valueOf(o)) && String.valueOf(o).startsWith("/"));

            builder.pop();
        }
    }

}
