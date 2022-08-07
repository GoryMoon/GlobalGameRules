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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.storage.LevelData;
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
 * Handles events for when the world is loaded/unloaded and when a plyer joins the world
 */
public class WorldEvents {

    private static final Logger LOGGER = LogManager.getLogger("GlobalGameRules");

    /**
     * Runs the default command on users when they join a world.
     * This should not be called, it's automatically called by the {@link SubscribeEvent} annotation
     *
     * @see EntityJoinLevelEvent
     * @param event The data for the {@link EntityJoinLevelEvent} event
     */
    @SubscribeEvent
    public static void onWorldJoin(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof Player player) {
            Level world = event.getLevel();
            MinecraftServer server = world.getServer();

            if (server != null) {
                GGRConfig.COMMON.defaultCommands.get().forEach((s) -> {
                    String command = s.replaceAll("@p", player.getGameProfile().getName());
                    server.getCommands().performCommand(server.createCommandSourceStack(), command);
                });
            }
        }
    }

    /**
     * Applies all the gamerules and any configured difficultly configs from the config.
     * This should not be called, it's automatically called by the {@link SubscribeEvent} annotation
     *
     * @see LevelEvent.Load
     * @param event The data for the {@link LevelEvent.Load} event
     */
    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getLevel() instanceof ServerLevel world)) return;
        if (!(world.getLevelData() instanceof PrimaryLevelData info)) return;
        GameRules rules = info.getGameRules();

        LOGGER.info("Applying config gamerules to level {}", info.getLevelName());
        HashMap<String, ParsedArgument<CommandSourceStack, ?>> arguments = new HashMap<>();
        GGRConfig.COMMON.gameRules.forEach((ruleKey, configValue) -> arguments.put(ruleKey.getId(), new ParsedArgument<CommandSourceStack, Object>(0, 0, configValue.get())));
        CommandContext<CommandSourceStack> context = new CommandContext<>(world.getServer().createCommandSourceStack(), null, arguments, null, null, null, null, null, null, false);
        GGRConfig.COMMON.gameRules.forEach((ruleKey, configValue) -> rules.getRule(ruleKey).setFromArgument(context, ruleKey.getId()));

        if (!info.isDifficultyLocked()) {
            Boolean hardcore = GGRConfig.COMMON.hardcore.get();
            if (info.isHardcore() != hardcore) {
                LevelSettings settings = info.settings;
                info.settings = new LevelSettings(settings.levelName(), settings.gameType(), hardcore, settings.difficulty(), settings.allowCommands(), settings.gameRules(), settings.getDataPackConfig());
                if (hardcore && info.getDifficulty() != Difficulty.HARD) {
                    world.getServer().setDifficulty(Difficulty.HARD, false);
                }
                if (hardcore) {
                    LOGGER.info("Enabling hardcore in level {}", info.getLevelName());
                } else {
                    LOGGER.info("Disabling hardcore in level {}", info.getLevelName());
                }
            }

            if (GGRConfig.COMMON.setDifficulty.get()) {
                Difficulty diff = GGRConfig.COMMON.difficulty.get();
                world.getServer().setDifficulty(diff, false);
                LOGGER.info("Setting difficulty of level {} to {}", info.getLevelName(), diff.toString());
            }
        }

        if (GGRConfig.COMMON.lockDifficulty.get()) {
            world.getServer().setDifficultyLocked(true);
            LOGGER.info("Locking difficulty of level {}", info.getLevelName());
        }
    }

    /**
     * Saves all the gamerules and any changed difficultly settings to the config.
     * This should not be called, it's automatically called by the {@link SubscribeEvent} annotation
     *
     * @see LevelEvent.Unload
     * @param event The data for the {@link LevelEvent.Unload} event
     */
    @SubscribeEvent
    public static void onWorldUnLoad(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) return;
        LevelAccessor world = event.getLevel();
        LevelData info = world.getLevelData();
        GameRules rules = info.getGameRules();

        AtomicBoolean dirty = new AtomicBoolean(false);
        if (GGRConfig.COMMON.saveGameRules.get()) {
            GGRConfig.COMMON.gameRules.forEach((ruleKey, configValue) -> {
                GameRules.Value<?> val = rules.getRule(ruleKey);
                if (val instanceof GameRules.BooleanValue && ((BooleanValue)configValue).get() != ((GameRules.BooleanValue) val).get()) {
                    ((BooleanValue)configValue).set(((GameRules.BooleanValue) val).get());
                    dirty.set(true);
                } else if (val instanceof GameRules.IntegerValue && ((IntValue)configValue).get() != ((GameRules.IntegerValue) val).get()) {
                    ((IntValue)configValue).set(((GameRules.IntegerValue) val).get());
                    dirty.set(true);
                }
            });
        }

        if (GGRConfig.COMMON.setDifficulty.get() && !event.getLevel().getLevelData().isDifficultyLocked()) {
            if (GGRConfig.COMMON.difficulty.get() != info.getDifficulty()) {
                GGRConfig.COMMON.difficulty.set(info.getDifficulty());
                dirty.set(true);
            }
        }

        if (dirty.get()) {
            GGRConfig.commonSpec.save();
        }
    }
}
