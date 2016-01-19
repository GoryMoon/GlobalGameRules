package se.gory_moon.globalgamerules;

import net.minecraft.world.GameRules;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import se.gory_moon.globalgamerules.config.Config;

import java.util.Map;

public class WorldEvents {

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        GameRules gRules = event.world.getGameRules();

        for (Map.Entry<String, Config.Value> entry : GlobalGR.instance.config.rules.entrySet()) {
            String rule = entry.getKey();
            Config.Value state = entry.getValue();
            gRules.setOrCreateGameRule(rule, state.getStringValue());
        }

    }

    @SubscribeEvent
    public void onWorldUnLoad(WorldEvent.Unload event) {
        GameRules gRules = event.world.getGameRules();

        for (Map.Entry<String, Config.Value> entry : GlobalGR.instance.config.rules.entrySet()) {
            String rule = entry.getKey();
            Config.Value value = entry.getValue();
            String state = gRules.getString(rule);

            GlobalGR.instance.config.rules.put(rule, new Config.Value(state, value.getType()));
        }

        GlobalGR.instance.config.saveConfig();

    }

}
