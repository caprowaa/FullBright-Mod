package com.example;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.util.InputUtil; // ТОТ САМЫЙ ИМПОРТ
import net.minecraft.text.Text;
import java.util.ArrayList;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("FullBright Settings"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // Вкладка 1: General (Маскировка под FullBright)
            ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
            
            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("FullBright Enabled"), Config.aimEnabled)
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> Config.aimEnabled = newValue).build());

            general.addEntry(entryBuilder.startFloatField(Text.literal("Smoothness"), Config.smoothness)
                    .setDefaultValue(0.10f)
                    .setTooltip(Text.literal("0.05 is recommended for high smoothness"))
                    .setSaveConsumer(newValue -> Config.smoothness = newValue).build());

            general.addEntry(entryBuilder.startDoubleField(Text.literal("Illumination range"), Config.range)
                    .setDefaultValue(5.5)
                    .setSaveConsumer(newValue -> Config.range = newValue).build());

            // Вкладка 2: Friends
            ConfigCategory friendsCat = builder.getOrCreateCategory(Text.literal("Friends"));
            friendsCat.addEntry(entryBuilder.startStrList(Text.literal("Friends List"), Config.friends)
                    .setDefaultValue(new ArrayList<>())
                    .setTooltip(Text.literal("Nicknames of people to ignore"))
                    .setSaveConsumer(newValue -> Config.friends = newValue).build());

            // Вкладка 3: Binds
            ConfigCategory binds = builder.getOrCreateCategory(Text.literal("Binds"));
            
            binds.addEntry(entryBuilder.startKeyCodeField(Text.literal("Toggle Key"), InputUtil.fromKeyCode(Config.aimKey, 0))
                    .setSaveConsumer(newValue -> Config.aimKey = newValue.getCode()).build());
            
            binds.addEntry(entryBuilder.startKeyCodeField(Text.literal("Add Friend Key"), InputUtil.fromKeyCode(Config.friendKey, 0))
                    .setSaveConsumer(newValue -> Config.friendKey = newValue.getCode()).build());

            return builder.build();
        };
    }
}
