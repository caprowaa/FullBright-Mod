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
                    .setTitle(Text.literal("FullBright & Aim Settings"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // КАТЕГОРИЯ: Основные настройки
            ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));

            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Aim Assist Enabled"), Config.aimEnabled)
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> Config.aimEnabled = newValue)
                    .build());

            general.addEntry(entryBuilder.startFloatField(Text.literal("Smoothness (0.01 - 1.0)"), Config.smoothness)
                    .setDefaultValue(0.10f)
                    .setTooltip(Text.literal("Lower = Smoother, Higher = Snappier"))
                    .setSaveConsumer(newValue -> Config.smoothness = newValue)
                    .build());

            general.addEntry(entryBuilder.startDoubleField(Text.literal("Range (Blocks)"), Config.range)
                    .setDefaultValue(5.5)
                    .setSaveConsumer(newValue -> Config.range = newValue)
                    .build());

            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Anti-Bot (NPC Check)"), Config.antiBot)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> Config.antiBot = newValue)
                    .build());

            // КАТЕГОРИЯ: Друзья
            ConfigCategory friendsCat = builder.getOrCreateCategory(Text.literal("Friends"));

            friendsCat.addEntry(entryBuilder.startStrList(Text.literal("Friends List"), Config.friends)
                    .setDefaultValue(new ArrayList<>())
                    .setTooltip(Text.literal("Nicknames of people to ignore (lowercase)"))
                    .setSaveConsumer(newValue -> Config.friends = newValue)
                    .build());

            return builder.build();
        };
    }
}
