package se.gory_moon.globalgamerules;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Handles events for when the world is loaded/unloaded and when a player joins the world
 */
public class WorldEvents {

    private static final Logger LOGGER = LogManager.getLogger("GlobalGameRules");

    /**
     * Runs the default command on users when they join a world.
     * This should not be called, it's automatically called by the {@link SubscribeEvent} annotation
     *
     * @param event The data for the {@link EntityJoinLevelEvent} event
     * @see EntityJoinLevelEvent
     */
    @SubscribeEvent
    public static void onWorldJoin(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof Player player) {
            Level world = event.getLevel();
            MinecraftServer server = world.getServer();

            if (server != null) {
                Config.COMMON.defaultCommands.get().forEach((s) -> {
                    String command = s.replaceAll("@p", player.getGameProfile().getName());
                    server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), command);
                });
            }
        }
    }

    /**
     * Applies all the gamerules and any configured difficultly configs from the config.
     * This should not be called, it's automatically called by the {@link SubscribeEvent} annotation
     *
     * @param event The data for the {@link LevelEvent.Load} event
     * @see LevelEvent.Load
     */
    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getLevel() instanceof ServerLevel world)) return;

        // Only apply on the primary level data as all dimensions share settings
        if (!(world.getLevelData() instanceof PrimaryLevelData info)) return;

        // Check if the settings only should be used as a default when creating a new world
        if (Config.COMMON.useAsDefaultsOnly.get() && info.isInitialized()) return;

        GameRules rules = info.getGameRules();
        MinecraftServer server = world.getServer();

        LOGGER.info("Applying config gamerules to level {}", info.getLevelName());
        HashMap<String, ParsedArgument<CommandSourceStack, ?>> arguments = new HashMap<>();
        Config.COMMON.gameRules.forEach((ruleKey, configValue) -> arguments.put(ruleKey.getId(), new ParsedArgument<CommandSourceStack, Object>(0, 0, configValue.get())));
        CommandContext<CommandSourceStack> context = new CommandContext<>(server.createCommandSourceStack(), null, arguments, null, null, null, null, null, null, false);
        Config.COMMON.gameRules.forEach((ruleKey, configValue) -> rules.getRule(ruleKey).setFromArgument(context, ruleKey.getId()));

        if (!info.isDifficultyLocked()) {

            var hardcore = Config.COMMON.hardcore.get();
            var enforceHardcore = Config.COMMON.enforceHardcore.get();

            // Only change the hardcore setting if we are enforcing it
            if (enforceHardcore && info.isHardcore() != hardcore) {
                LevelSettings settings = info.settings;
                info.settings = new LevelSettings(settings.levelName(), settings.gameType(), hardcore, settings.difficulty(), settings.allowCommands(), settings.gameRules(), settings.getDataConfiguration());
                if (hardcore && info.getDifficulty() != Difficulty.HARD) {
                    server.setDifficulty(Difficulty.HARD, false);
                }
                if (hardcore) {
                    LOGGER.info("Enabling hardcore in level {}", info.getLevelName());
                } else {
                    LOGGER.info("Disabling hardcore in level {}", info.getLevelName());
                }
            }

            if (Config.COMMON.setDifficulty.get()) {
                Difficulty diff = Config.COMMON.difficulty.get();
                server.setDifficulty(diff, false);
                LOGGER.info("Setting difficulty of level {} to {}", info.getLevelName(), diff.toString());
            }
        }

        if (Config.COMMON.lockDifficulty.get()) {
            server.setDifficultyLocked(true);
            LOGGER.info("Locking difficulty of level {}", info.getLevelName());
        }
    }

    /**
     * Saves all the gamerules and any changed difficultly settings to the config.
     * This should not be called, it's automatically called by the {@link SubscribeEvent} annotation
     *
     * @param event The data for the {@link LevelEvent.Unload} event
     * @see LevelEvent.Unload
     */
    @SubscribeEvent
    public static void onWorldUnLoad(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getLevel() instanceof ServerLevel world)) return;

        // Only apply on the primary level data as all dimensions share settings
        if (!(world.getLevelData() instanceof PrimaryLevelData info)) return;

        // Check if the settings only should be used as a default we never save changes
        if (Config.COMMON.useAsDefaultsOnly.get()) return;

        GameRules rules = info.getGameRules();

        AtomicBoolean dirty = new AtomicBoolean(false);
        if (Config.COMMON.saveGameRules.get()) {
            Config.COMMON.gameRules.forEach((ruleKey, configValue) -> {
                GameRules.Value<?> val = rules.getRule(ruleKey);
                if (val instanceof GameRules.BooleanValue booleanValue) {
                    var config = (BooleanValue) configValue;
                    if (config.get() != booleanValue.get()) {
                        config.set(booleanValue.get());
                        dirty.set(true);
                    }
                } else if (val instanceof GameRules.IntegerValue intValue) {
                    var config = (IntValue) configValue;
                    if (config.get() != intValue.get()) {
                        config.set(intValue.get());
                        dirty.set(true);
                    }
                }
            });
        }

        if (Config.COMMON.setDifficulty.get() && !info.isDifficultyLocked()) {
            if (Config.COMMON.difficulty.get() != info.getDifficulty()) {
                Config.COMMON.difficulty.set(info.getDifficulty());
                dirty.set(true);
            }
        }

        if (dirty.get()) {
            Config.commonSpec.save();
        }
    }
}
