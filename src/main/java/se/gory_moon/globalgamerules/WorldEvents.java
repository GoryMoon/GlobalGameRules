package se.gory_moon.globalgamerules;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedArgument;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class WorldEvents {

    public static final Logger LOGGER = LogManager.getLogger("GlobalGameRules");

    @SubscribeEvent
    public static void onWorldJoin(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && event.getEntity() instanceof PlayerEntity) {
            World world = event.getWorld();
            MinecraftServer server = world.getServer();
            PlayerEntity player = (PlayerEntity) event.getEntity();

            if (server != null) {
                GGRConfig.COMMON.defaultCommands.get().forEach((s) -> {
                    String command = s.replaceAll("@p", player.getGameProfile().getName());
                    server.getCommandManager().handleCommand(server.getCommandSource(), command);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if (event.getWorld().isRemote()) return;
        IWorld world = event.getWorld();
        WorldInfo info = world.getWorldInfo();
        GameRules rules = info.getGameRulesInstance();

        LOGGER.info("Applying config gamerules to dimension {} ({})", world.getDimension().getType().getId(), info.getWorldName());
        HashMap<String, ParsedArgument<CommandSource, ?>> arguments = new HashMap<>();
        GGRConfig.COMMON.gameRules.forEach((ruleKey, configValue) -> arguments.put(ruleKey.func_223576_a(), new ParsedArgument<CommandSource, Object>(0, 0, configValue.get())));
        CommandContext<CommandSource> context = new CommandContext<>(world.getWorld().getServer().getCommandSource(), null, arguments, null, null, null, null, null, null, false);
        GGRConfig.COMMON.gameRules.forEach((ruleKey, configValue) -> rules.get(ruleKey).func_223554_b(context, ruleKey.func_223576_a()));

        if (!info.isDifficultyLocked() && GGRConfig.COMMON.setDifficulty.get()) {
            Difficulty diff = GGRConfig.COMMON.difficulty.get();
            info.setDifficulty(diff);
            LOGGER.info("Setting difficulty of dimension {} ({}) to {}", world.getDimension().getType().getId(), info.getWorldName(), diff.toString());
        }

        if (!info.isDifficultyLocked()) {
            Boolean hardcore = GGRConfig.COMMON.hardcore.get();
            info.setHardcore(hardcore);
            if (hardcore && info.getDifficulty() != Difficulty.HARD) {
                info.setDifficulty(Difficulty.HARD);
            }
            if (hardcore) {
                LOGGER.info("Setting dimension {} ({}) to hardcore", world.getDimension().getType().getId(), info.getWorldName());
            }
        }

        if (GGRConfig.COMMON.lockDifficulty.get()) {
            info.setDifficultyLocked(true);
            LOGGER.info("Locking difficulty of dimension {} ({})", world.getDimension().getType().getId(), info.getWorldName());
        }
    }

    @SubscribeEvent
    public static void onWorldUnLoad(WorldEvent.Unload event) {
        if (event.getWorld().isRemote()) return;
        IWorld world = event.getWorld();
        WorldInfo info = world.getWorldInfo();
        GameRules rules = info.getGameRulesInstance();

        if (GGRConfig.COMMON.saveGameRules.get()) {
            GGRConfig.COMMON.gameRules.forEach((ruleKey, configValue) -> {
                GameRules.RuleValue<?> val = rules.get(ruleKey);
                if (val instanceof GameRules.BooleanValue) {
                    ((BooleanValue)configValue).set(((GameRules.BooleanValue) val).get());
                } else if (val instanceof GameRules.IntegerValue) {
                    ((IntValue)configValue).set(((GameRules.IntegerValue) val).get());
                }
            });
        }

        if (GGRConfig.COMMON.setDifficulty.get() && !event.getWorld().getWorldInfo().isDifficultyLocked()) {
            GGRConfig.COMMON.difficulty.set(info.getDifficulty());
        }

        GGRConfig.commonSpec.save();
    }
}
