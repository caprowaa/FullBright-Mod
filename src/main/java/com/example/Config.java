package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    // Параметры, которые будут сохраняться
    public static boolean aimEnabled = false;
    public static float smoothness = 0.10f;
    public static double range = 5.5;
    public static float fov = 90.0f;
    public static boolean antiBot = true;
    public static List<String> friends = new ArrayList<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = MinecraftClient.getInstance().runDirectory.toPath().resolve("config/fullbright_ultimate.json");

    // Класс-обертка для JSON
    private static class ConfigData {
        boolean aimEnabled = Config.aimEnabled;
        float smoothness = Config.smoothness;
        double range = Config.range;
        float fov = Config.fov;
        boolean antiBot = Config.antiBot;
        List<String> friends = Config.friends;
    }

    public static void save() {
        try {
            if (!Files.exists(CONFIG_PATH.getParent())) Files.createDirectories(CONFIG_PATH.getParent());
            ConfigData data = new ConfigData();
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                ConfigData data = GSON.fromJson(reader, ConfigData.class);
                if (data != null) {
                    aimEnabled = data.aimEnabled;
                    smoothness = data.smoothness;
                    range = data.range;
                    fov = data.fov;
                    antiBot = data.antiBot;
                    friends = data.friends != null ? data.friends : new ArrayList<>();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isFriend(String name) {
        return name != null && friends.contains(name.toLowerCase());
    }
}
