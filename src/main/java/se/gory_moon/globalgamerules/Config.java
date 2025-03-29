package se.gory_moon.globalgamerules;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.base.Strings;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to hold and manage config options
 */
public class Config {

    /**
     * The instance for that holds the common configs
     */
    public static final Common COMMON;

    /**
     * The config spec for the common config
     */
    public static final ForgeConfigSpec commonSpec;
    private static final HashMap<GameRules.Key<?>, String> COMMENTS = new HashMap<>();

    static {
        COMMENTS.put(GameRules.RULE_DOFIRETICK,                         "Whether fire should spread and naturally extinguish");
        COMMENTS.put(GameRules.RULE_MOBGRIEFING,                        "Whether creepers, zombies, endermen, ghasts, withers, ender dragons, rabbits, sheep, villagers, silverfish, snow golems, and end crystals should be able to change blocks, and whether mobs can pick up items. When mobGriefing is disabled, piglins do not pick up gold ingots, but a player can still barter with them by using the item on the mob. Similarly, villagers do not pick up food items but can still breed until they run out of any food already in their inventory. This also affects the capability of zombie-like creatures like zombified piglins and drowned to pathfind to turtle eggs.");
        COMMENTS.put(GameRules.RULE_KEEPINVENTORY,                      "Whether the player should keep items and experience in their inventory after death .");
        COMMENTS.put(GameRules.RULE_DOMOBSPAWNING,                      "Whether mobs should naturally spawn. Does not affect monster spawners.");
        COMMENTS.put(GameRules.RULE_DOMOBLOOT,                          "Whether mobs should drop items and experience orbs.");
        COMMENTS.put(GameRules.RULE_DOBLOCKDROPS,                       "Whether blocks should have drops.");
        COMMENTS.put(GameRules.RULE_DOENTITYDROPS,                      "Whether entities that are not mobs should have drops.");
        COMMENTS.put(GameRules.RULE_COMMANDBLOCKOUTPUT,                 "Whether command blocks should notify admins when they perform commands.");
        COMMENTS.put(GameRules.RULE_NATURAL_REGENERATION,               "Whether the player can regenerate health naturally if their hunger is full enough (doesn't affect external healing, such as golden apples, the Regeneration effect, etc.)");
        COMMENTS.put(GameRules.RULE_DAYLIGHT,                           "Whether the daylight cycle and moon phases progress.");
        COMMENTS.put(GameRules.RULE_LOGADMINCOMMANDS,                   "Whether to log admin commands to server log.");
        COMMENTS.put(GameRules.RULE_SHOWDEATHMESSAGES,                  "Whether death messages are put into chat when a player dies. Also affects whether a message is sent to the pet's owner when the pet dies.");
        COMMENTS.put(GameRules.RULE_RANDOMTICKING,                      "How often a random block tick occurs (such as plant growth, leaf decay, etc.) per chunk section per game tick. 0 and negative values disables random ticks, higher numbers increase random ticks. Setting to a high integer results in high speeds of decay and growth. Numbers over 4096 make plant growth or leaf decay instantaneous.");
        COMMENTS.put(GameRules.RULE_SENDCOMMANDFEEDBACK,                "Whether the feedback from commands executed by a player should show up in chat. Also affects the default behavior of whether command blocks store their output text.");
        COMMENTS.put(GameRules.RULE_REDUCEDDEBUGINFO,                   "Whether the debug screen shows all or reduced information; and whether the effects of F3+B (entity hitboxes) and F3+G (chunk boundaries) are shown.");
        COMMENTS.put(GameRules.RULE_SPECTATORSGENERATECHUNKS,           "Whether players in spectator mode can generate chunks");
        COMMENTS.put(GameRules.RULE_SPAWN_RADIUS,                       "The number of blocks outward from the world spawn coordinates that a player spawns in when first joining a server or when dying without a personal spawnpoint. Has no effect on servers where the default game mode is Adventure.");
        COMMENTS.put(GameRules.RULE_DISABLE_ELYTRA_MOVEMENT_CHECK,      "Whether the server should skip checking player speed when the player is wearing elytra. Often helps with jittering due to lag in multiplayer.");
        COMMENTS.put(GameRules.RULE_MAX_ENTITY_CRAMMING,                "The maximum number of pushable entities a mob or player can push, before taking 6 (♥♥♥) entity cramming damage per half-second. Setting to 0 or lower disables the rule. Damage affects Survival-mode or Adventure-mode players, and all mobs but bats. Pushable entities include non-Spectator-mode players, any mob except bats, as well as boats and minecarts.");
        COMMENTS.put(GameRules.RULE_WEATHER_CYCLE,                      "Whether the weather can change naturally. The /weather command can still change weather.");
        COMMENTS.put(GameRules.RULE_LIMITED_CRAFTING,                   "Whether players can craft only those recipes that they have unlocked.");
        COMMENTS.put(GameRules.RULE_MAX_COMMAND_CHAIN_LENGTH,           "The maximum length of a chain of commands that can be executed during one tick. Applies to command blocks and functions.");
        COMMENTS.put(GameRules.RULE_COMMAND_MODIFICATION_BLOCK_LIMIT,   "Controls the maximum number of blocks changed when using /clone, /fill, or /fillbiome.");
        COMMENTS.put(GameRules.RULE_ANNOUNCE_ADVANCEMENTS,              "Whether advancements should be announced in chat.");
        COMMENTS.put(GameRules.RULE_DISABLE_RAIDS,                      "Whether raids are disabled.");
        COMMENTS.put(GameRules.RULE_DOINSOMNIA,                         "Whether phantoms can spawn in the nighttime.");
        COMMENTS.put(GameRules.RULE_DO_IMMEDIATE_RESPAWN,               "Players respawn immediately without showing the death screen.");
        COMMENTS.put(GameRules.RULE_DROWNING_DAMAGE,                    "Whether the player should take damage when drowning.");
        COMMENTS.put(GameRules.RULE_FALL_DAMAGE,                        "Whether the player should take fall damage.");
        COMMENTS.put(GameRules.RULE_FIRE_DAMAGE,                        "Whether the player should take damage in fire, lava, campfires, or on magma blocks.");
        COMMENTS.put(GameRules.RULE_FREEZE_DAMAGE,                      "Whether the player should take damage when inside powder snow.");
        COMMENTS.put(GameRules.RULE_DO_PATROL_SPAWNING,                 "Whether patrols can spawn.");
        COMMENTS.put(GameRules.RULE_DO_TRADER_SPAWNING,                 "Whether wandering traders can spawn.");
        COMMENTS.put(GameRules.RULE_DO_WARDEN_SPAWNING,                 "Whether wardens can spawn.");
        COMMENTS.put(GameRules.RULE_FORGIVE_DEAD_PLAYERS,               "Makes angered neutral mobs stop being angry when the targeted player dies nearby.");
        COMMENTS.put(GameRules.RULE_UNIVERSAL_ANGER,                    "Makes angered neutral mobs attack any nearby player, not just the player that angered them. Works best if forgiveDeadPlayers is disabled.");
        COMMENTS.put(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE,        "What percentage of players must sleep to skip the night.");
        COMMENTS.put(GameRules.RULE_BLOCK_EXPLOSION_DROP_DECAY,         "Whether block loot is dropped by all blocks (false) or randomly (true) depending on how far the block is from the center of a block explosion (e.g. clicking a bed in dimensions other than the Overworld).");
        COMMENTS.put(GameRules.RULE_MOB_EXPLOSION_DROP_DECAY,           "Whether block loot is dropped by all blocks (false) or randomly (true) depending on how far the block is from the center of a mob explosion (e.g. Creeper explosion).");
        COMMENTS.put(GameRules.RULE_TNT_EXPLOSION_DROP_DECAY,           "Whether block loot is dropped by all blocks (false) or randomly (true) depending on how far the block is from the center of a TNT explosion.");
        COMMENTS.put(GameRules.RULE_SNOW_ACCUMULATION_HEIGHT,           "The maximum number of snow layers that can be accumulated on each block.");
        COMMENTS.put(GameRules.RULE_WATER_SOURCE_CONVERSION,            "Whether new sources of water are allowed to form.");
        COMMENTS.put(GameRules.RULE_LAVA_SOURCE_CONVERSION,             "Whether new sources of lava are allowed to form.");
        COMMENTS.put(GameRules.RULE_GLOBAL_SOUND_EVENTS,                "Whether certain sound events are heard by all players regardless of location.");
        COMMENTS.put(GameRules.RULE_DO_VINES_SPREAD,                    "Whether vines can spread to other blocks. Cave vines, weeping vines, and twisting vines are not affected.");

        Pair<Common, ForgeConfigSpec> configSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = configSpecPair.getRight();
        COMMON = configSpecPair.getLeft();
    }

    /**
     * Common config options for all sides
     */
    public static class Common {

        /**
         * If the difficulty should be set on world load and saved on world unload
         */
        public final BooleanValue setDifficulty;

        /**
         * The difficulty to get/set on world load/unload
         */
        public final EnumValue<Difficulty> difficulty;

        /**
         * If we should enforce the hardcore setting
         */
        public final BooleanValue enforceHardcore;

        /**
         * If the world should be set to hardcore on load
         */
        public final BooleanValue hardcore;

        /**
         * If the world difficulty should be locked on word load
         */
        public final BooleanValue lockDifficulty;

        /**
         * A list of raw commands to run on world join
         */
        public final ConfigValue<List<? extends String>> defaultCommands;

        /**
         * If gamerules should be saved then the world is unloaded
         */
        public final BooleanValue saveGameRules;

        /**
         * A map of gamerules and their config values
         */
        public final Map<GameRules.Key<?>, ConfigValue<?>> gameRules;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Gamerules that are set when a world is loaded")
                    .push("gamerules");

            gameRules = new HashMap<>();
            Converter<String, String> ruleConverter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE);
            GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
                @Override
                public <T extends GameRules.Value<T>> void visit(GameRules.@NotNull Key<T> ruleKey, GameRules.@NotNull Type<T> ruleType) {
                    T t = ruleType.createRule();
                    String name = ruleConverter.convert(ruleKey.getId());

                    // Only allow boolean and int types
                    if (name != null && (t instanceof GameRules.BooleanValue || t instanceof GameRules.IntegerValue)) {
                        // Push the category
                        String category = ruleKey.getCategory().toString().toLowerCase();
                        builder.push(category);

                        // Create comment, add note if it's not a vanilla rule
                        var ruleBuilder = builder.comment(COMMENTS.getOrDefault(ruleKey, "Non Vanilla gamerule: '%s'".formatted(ruleKey.getId())));

                        if (t instanceof GameRules.BooleanValue booleanValue) {
                            gameRules.put(ruleKey, ruleBuilder
                                    .define(name, booleanValue.get()));
                        } else {
                            gameRules.put(ruleKey, ruleBuilder
                                    .defineInRange(name, ((GameRules.IntegerValue) t).get(), Integer.MIN_VALUE, Integer.MAX_VALUE));
                        }

                        // Pop the category
                        builder.pop();
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

            enforceHardcore = builder
                    .comment("If true the 'hardcore' setting is enforced", "If false the 'hardcore' setting isn't checked against the setting of a world")
                    .define("enforce_hardcore", true);

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
                    .comment("If gamerules should be saved to config on world unload")
                    .define("save_gamerules", true);

            defaultCommands = builder
                    .comment("A comma separated list of commands to run on world join, @p is replaced with joining player name, command is run by the server", "Example: default_commands = [\"/tellraw @p [\\\"\\\",{\\\"text\\\":\\\"Hi \\\"},{\\\"text\\\":\\\"@p\\\",\\\"color\\\":\\\"aqua\\\"}]\"]")
                    .defineList("default_commands", ArrayList::new, o -> !Strings.isNullOrEmpty(String.valueOf(o)) && String.valueOf(o).startsWith("/"));

            builder.pop();
        }
    }

}
