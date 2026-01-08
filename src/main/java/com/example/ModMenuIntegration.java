package com.example;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;
import java.util.ArrayList;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("FullBright Settings"))
                    .setSavingRunnable(Config::save);

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));

            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("FullBright Enabled"), Config.aimEnabled)
                    .setSaveConsumer(v -> Config.aimEnabled = v).build());

            general.addEntry(entryBuilder.startFloatField(Text.literal("Smoothness"), Config.smoothness)
                    .setSaveConsumer(v -> Config.smoothness = v).build());

            general.addEntry(entryBuilder.startDoubleField(Text.literal("Illumination range"), Config.range)
                    .setSaveConsumer(v -> Config.range = v).build());

            // НОВЫЙ ПОЛЗУНОК FOV
            general.addEntry(entryBuilder.startFloatField(Text.literal("Aim FOV"), Config.fov)
                    .setDefaultValue(90.0f)
                    .setSaveConsumer(v -> Config.fov = v).build());

            ConfigCategory friendsCat = builder.getOrCreateCategory(Text.literal("Friends"));
            friendsCat.addEntry(entryBuilder.startStrList(Text.literal("Friends List"), Config.friends)
                    .setDefaultValue(new ArrayList<>())
                    .setSaveConsumer(v -> Config.friends = v).build());

            return builder.build();
        };
    }
}
