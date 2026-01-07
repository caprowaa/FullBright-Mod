package com.example;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.GLFW;

public class Config {
    public static float smoothness = 0.10f;
    public static double range = 5.5;
    public static boolean aimEnabled = false;
    public static boolean antiBot = true; // Теперь всегда true по умолчанию
    
    // Бинды
    public static int aimKey = GLFW.GLFW_KEY_R;
    public static int friendKey = GLFW.GLFW_KEY_M;
    
    public static List<String> friends = new ArrayList<>();

    public static boolean isFriend(String name) {
        return name != null && friends.contains(name.toLowerCase());
    }
}
