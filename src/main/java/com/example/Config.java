package com.example;

import net.minecraft.client.MinecraftClient;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    public static float smoothness = 0.10f;
    public static double range = 5.5;
    public static boolean aimEnabled = false;
    public static List<String> friends = new ArrayList<>();
    
    private static final Path CONFIG_PATH = MinecraftClient.getInstance().runDirectory.toPath().resolve("config/fullbright_friends.txt");

    public static void save() {
        try {
            Files.write(CONFIG_PATH, friends);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                friends = Files.readAllLines(CONFIG_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isFriend(String name) {
        return name != null && friends.contains(name.toLowerCase());
    }
}
