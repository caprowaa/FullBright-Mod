package com.example;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    public static float smoothness = 0.10f;
    public static double range = 5.5;
    public static float fov = 90.0f; // Поле зрения аима (в градусах)
    public static boolean aimEnabled = false;
    public static boolean antiBot = true; 
    public static List<String> friends = new ArrayList<>();
    public static int friendKey = GLFW.GLFW_KEY_M;

    private static final Path CONFIG_PATH = MinecraftClient.getInstance().runDirectory.toPath().resolve("config/fullbright_friends.txt");

    public static void save() {
        try {
            if (!Files.exists(CONFIG_PATH.getParent())) Files.createDirectories(CONFIG_PATH.getParent());
            Files.write(CONFIG_PATH, friends);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                friends = new ArrayList<>(Files.readAllLines(CONFIG_PATH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isFriend(String name) {
        return name != null && friends.contains(name.toLowerCase());
    }
}
