package se.gory_moon.globalgamerules;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.world.WorldEvent;

import java.util.Map;

public class WorldEvents {

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        GameRules gRules = event.world.getGameRules();

        for (Map.Entry<String, Boolean> entry : GlobalGR.instance.config.rules.entrySet()) {
            String rule = entry.getKey();
            Boolean state = entry.getValue();
            gRules.setOrCreateGameRule(rule, state ? "true" : "false");
        }

    }

    @SubscribeEvent
    public void onWorldUnLoad(WorldEvent.Unload event) {
        GameRules gRules = event.world.getGameRules();

        for (Map.Entry<String, Boolean> entry : GlobalGR.instance.config.rules.entrySet()) {
            String rule = entry.getKey();
            Boolean state = gRules.getGameRuleBooleanValue(rule);

            GlobalGR.instance.config.rules.put(rule, state);
        }

        GlobalGR.instance.config.saveConfig();

    }

}
