package com.example;

import java.util.ArrayList;
import java.util.List;

public class Config {
    // Стандартные настройки
    public static float smoothness = 0.10f; // Плавность (0.01 - 1.0)
    public static double range = 5.5;      // Дистанция
    public static boolean aimEnabled = false;
    public static boolean antiBot = true;  // Проверка на ботов
    
    // Список друзей (ники в нижнем регистре)
    public static List<String> friends = new ArrayList<>();

    public static boolean isFriend(String name) {
        return friends.contains(name.toLowerCase());
    }
}
