package se.gory_moon.globalgamerules;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.MutablePair;
import se.gory_moon.globalgamerules.config.GGRConfig;
import se.gory_moon.globalgamerules.reference.Reference;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class WorldEvents {

    @SubscribeEvent
    public static void onWorldJoin(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayer) {
            World world = event.getWorld();
            GameRules gRules = world.getGameRules();
            EntityPlayer playerMP = (EntityPlayer) event.getEntity();

            GlobalGR.getConfig().custom.forEach((s, stringValuePair) -> {
                if (gRules.getBoolean(s)) {
                    String command = stringValuePair.getKey();
                    command = command.replaceAll("@p", playerMP.getName());
                    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                    server.commandManager.executeCommand(server, command);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        WorldInfo info = world.getWorldInfo();
        GameRules gRules = world.getGameRules();

        Reference.logger.info("Applying config gamerules to dimension {} ({})", world.provider.getDimension(), info.getWorldName());
        GlobalGR.getConfig().rules.forEach((s, value) -> gRules.setOrCreateGameRule(s, value.getStringValue()));
        GlobalGR.getConfig().custom.forEach((s, stringValuePair) -> gRules.setOrCreateGameRule(s, stringValuePair.getValue().getStringValue()));

        if (!event.getWorld().isRemote && !info.isDifficultyLocked()) {
            int diff = GlobalGR.getConfig().misc.get(GGRConfig.MISC_WORLDDIFFICULTY).getIntegerValue();
            if (diff != -1) {
                info.setDifficulty(EnumDifficulty.byId(diff));
                Reference.logger.info("Setting difficulty of dimension {} ({}) to {}", world.provider.getDimension(), info.getWorldName(),
                        EnumDifficulty.byId(diff).toString());
            }

            if (GlobalGR.getConfig().misc.get(GGRConfig.MISC_WORLDDIFFICULTYLOCK).getBooleanValue()) {
                info.setDifficultyLocked(true);
                Reference.logger.info("Locking difficulty of dimension {} ({})", world.provider.getDimension(), info.getWorldName());
            }
        }
    }

    @SubscribeEvent
    public static void onWorldUnLoad(WorldEvent.Unload event) {
        World world = event.getWorld();
        WorldInfo info = world.getWorldInfo();
        GameRules gRules = event.getWorld().getGameRules();

        if (GlobalGR.getConfig().misc.get(GGRConfig.MISC_SAVEGAMRULES).getBooleanValue()) {
            Reference.logger.info("Saving gamerules of dimension {} ({}) to config", world.provider.getDimension(), info.getWorldName());
            Arrays.stream(gRules.getRules()).filter(s -> !GlobalGR.getConfig().custom.containsKey(s)).forEach(s -> GlobalGR.getConfig().rules.put(s, new GGRConfig.Value(gRules.getString(s), getType(gRules, s))));
        }
        GlobalGR.getConfig().custom.forEach((s, stringValuePair) -> GlobalGR.getConfig().custom.put(s, MutablePair.of(stringValuePair.getLeft(), new GGRConfig.Value(gRules.getString(s), stringValuePair.getRight().getType()))));

        if (!event.getWorld().isRemote && GlobalGR.getConfig().misc.get(GGRConfig.MISC_WORLDDIFFICULTY).getIntegerValue() != -1 && !event.getWorld().getWorldInfo().isDifficultyLocked()) {
            GGRConfig.Value val = GlobalGR.getConfig().misc.get(GGRConfig.MISC_WORLDDIFFICULTY);
            GGRConfig.Value newVal = new GGRConfig.Value(String.valueOf(info.getDifficulty().getId()), val.getType(), val.getShowInGui());
            GlobalGR.getConfig().misc.put(GGRConfig.MISC_WORLDDIFFICULTY, newVal);
        }

        if (!GlobalGR.getConfig().misc.get(GGRConfig.MISC_SAVEGAMRULES).getBooleanValue()) {
            Arrays.stream(gRules.getRules()).filter(s -> !GlobalGR.getConfig().rules.containsKey(s)).forEach(s -> GlobalGR.getConfig().rules.put(s, new GGRConfig.Value(gRules.getString(s), getType(gRules, s))));
        }

        GlobalGR.getConfig().saveConfig();
    }

    private static GGRConfig.ValueType getType(GameRules rules, String s) {
        if (rules.areSameType(s, GameRules.ValueType.BOOLEAN_VALUE))
            return GGRConfig.ValueType.BOOLEAN;
        else if (rules.areSameType(s, GameRules.ValueType.NUMERICAL_VALUE))
            return GGRConfig.ValueType.INTEGER;
        else
            return GGRConfig.ValueType.STRING;
    }
}
