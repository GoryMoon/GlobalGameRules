package se.gory_moon.globalgamerules.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import se.gory_moon.globalgamerules.GlobalGR;
import se.gory_moon.globalgamerules.reference.Reference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GGRConfigGUI extends GuiConfig {

    public GGRConfigGUI(GuiScreen parentScreen) {
        super(parentScreen,
                getConfigElements(),
                Reference.MODID, true, false, GuiConfig.getAbridgedConfigPath(GlobalGR.getConfig().toString()));
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        list.add(new DummyConfigElement.DummyCategoryElement(GGRConfig.CATEGORY_GAMERULES, "ggr.configgui.rules",
                (new ConfigElement(GlobalGR.getConfig().getCategory(GGRConfig.CATEGORY_GAMERULES))).getChildElements(), GuiConfigEntries.CategoryEntry.class));
        list.add(new DummyConfigElement.DummyCategoryElement(GGRConfig.CATEGORY_MISC, "ggr.configgui.misc",
                (new ConfigElement(GlobalGR.getConfig().getCategory(GGRConfig.CATEGORY_MISC))).getChildElements(), GuiConfigEntries.CategoryEntry.class));
        return list;
    }

    public static class DifficultyEntry extends GuiConfigEntries.SelectValueEntry {
        public DifficultyEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
            super(owningScreen, owningEntryList, prop, getSelectableValues());
        }

        private static Map<Object, String> getSelectableValues() {
            Map<Object, String> selectableValues = new HashMap<Object, String>();

            selectableValues.put("-1", I18n.format("ggr.configgui.diffDisabled"));
            for (EnumDifficulty ed: EnumDifficulty.values())
                selectableValues.put(String.valueOf(ed.getDifficultyId()), I18n.format(ed.getDifficultyResourceKey()));

            return selectableValues;
        }

        @Override
        public void updateValueButtonText() {
            int val = Integer.parseInt(String.valueOf(currentValue));
            if (val > -1)
                this.btnValue.displayString = I18n.format(EnumDifficulty.getDifficultyEnum(val).getDifficultyResourceKey());
            else
                this.btnValue.displayString = I18n.format("ggr.configgui.diffDisabled");
        }
    }

}
