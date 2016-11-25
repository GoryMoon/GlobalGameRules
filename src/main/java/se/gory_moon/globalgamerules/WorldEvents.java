package se.gory_moon.globalgamerules;

import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import se.gory_moon.globalgamerules.config.Config;
import se.gory_moon.globalgamerules.reference.Reference;

import java.util.Map;

public class WorldEvents {

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        WorldInfo info = world.getWorldInfo();
        GameRules gRules = world.getGameRules();

        Reference.logger.info("Applying config gamerules to dimension {} ({})", getDimensionId(world), info.getWorldName());
        for (Map.Entry<String, Config.Value> entry : GlobalGR.getConfig().rules.entrySet()) {
            String rule = entry.getKey();
            Config.Value state = entry.getValue();
            gRules.setOrCreateGameRule(rule, state.getStringValue());
        }

        if (!event.getWorld().isRemote && !info.isDifficultyLocked()) {
            int diff = GlobalGR.getConfig().misc.get(Config.MISC_WORLDDIFFICULTY).getIntegerValue();
            if (diff != -1) {
                info.setDifficulty(EnumDifficulty.getDifficultyEnum(diff));
                Reference.logger.info("Setting difficulty of dimension {} ({}) to {}", getDimensionId(world), info.getWorldName(),
                        EnumDifficulty.getDifficultyEnum(diff).toString());
            }

            if (GlobalGR.getConfig().misc.get(Config.MISC_WORLDDIFFICULTYLOCK).getBooleanValue()) {
                info.setDifficultyLocked(true);
                Reference.logger.info("Locking difficulty of dimension {} ({})", getDimensionId(world), info.getWorldName());
            }
        }
    }

    private int getDimensionId(World world) {
        for (Integer i : DimensionManager.getIDs()) {
            if (DimensionManager.getWorld(i).equals(world))
                return i;
        }
        return 0;
    }

    @SubscribeEvent
    public void onWorldUnLoad(WorldEvent.Unload event) {
        World world = event.getWorld();
        WorldInfo info = world.getWorldInfo();
        GameRules gRules = event.getWorld().getGameRules();

        Reference.logger.info("Saving gamerules of dimension {} ({}) to config",
                getDimensionId(world), info.getWorldName());
        for (Map.Entry<String, Config.Value> entry : GlobalGR.getConfig().rules.entrySet()) {
            String rule = entry.getKey();
            Config.Value value = entry.getValue();
            String state = gRules.getString(rule);

            GlobalGR.getConfig().rules.put(rule, new Config.Value(state, value.getType()));
        }

        if (!event.getWorld().isRemote && GlobalGR.getConfig().misc.get(Config.MISC_WORLDDIFFICULTY).getIntegerValue() != -1) {
            Config.Value val = GlobalGR.getConfig().misc.get(Config.MISC_WORLDDIFFICULTY);
            Config.Value newVal = new Config.Value(String.valueOf(info.getDifficulty().getDifficultyId()), val.getType(), val.getShowInGui());
            GlobalGR.getConfig().misc.put(Config.MISC_WORLDDIFFICULTY, newVal);
        }

        GlobalGR.getConfig().saveConfig();
    }

}
