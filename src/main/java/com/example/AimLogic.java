package com.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import java.util.Comparator;

public class AimLogic {

    public static void onRender() {
        MinecraftClient mc = MinecraftClient.getInstance();
        
        // Если аим выключен или мы не в игре — ничего не делаем
        if (!Config.aimEnabled || mc.player == null || mc.world == null) return;

        // Ищем цель среди игроков
        PlayerEntity target = mc.world.getPlayers().stream()
            .filter(p -> p != mc.player) // Игнорируем себя
            .filter(p -> p.isAlive() && !p.isSpectator()) // Только живые и не в наблюдении
            .filter(p -> !Config.isFriend(p.getEntityName())) // Проверка списка друзей
            .filter(p -> {
                if (Config.antiBot) {
                    // АНТИ-БОТ ЛОГИКА:
                    // 1. Игнорируем тех, кто живет меньше 5 секунд (боты анти-читов спавнятся быстро)
                    // 2. Игнорируем невидимых (админы или скрытые боты)
                    return p.age > 100 && !p.isInvisible(); 
                }
                return true;
            })
            .filter(p -> mc.player.distanceTo(p) < Config.range) // Проверка дистанции
            .min(Comparator.comparingDouble(p -> mc.player.distanceTo(p))) // Берем ближайшего
            .orElse(null);

        if (target != null) {
            // Координаты цели (наводимся чуть ниже глаз — в шею/грудь для беспалевности)
            double targetX = target.getX();
            double targetY = target.getY() + (target.getEyeHeight(target.getPose()) * 0.85);
            double targetZ = target.getZ();

            // Вектор направления
            double dx = targetX - mc.player.getX();
            double dy = targetY - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
            double dz = targetZ - mc.player.getZ();
            double dist = Math.sqrt(dx * dx + dz * dz);

            // Рассчитываем углы поворота
            float targetYaw = (float) (Math.atan2(dz, dx) * 180 / Math.PI) - 90;
            float targetPitch = (float) -(Math.atan2(dy, dist) * 180 / Math.PI);

            // ПЛАВНАЯ ДОВОДКА (Interpolation)
            // Вместо мгновенного поворота, мы двигаем камеру на маленький шаг (Config.smoothness)
            // Это убирает рывки и "разрывы" картинки
            mc.player.setYaw(updateAngle(mc.player.getYaw(), targetYaw, Config.smoothness));
            mc.player.setPitch(updateAngle(mc.player.getPitch(), targetPitch, Config.smoothness));
        }
    }

    // Вспомогательный метод для мягкого поворота угла
    private static float updateAngle(float current, float target, float factor) {
        float delta = ((target - current + 180) % 360 + 360) % 360 - 180;
        return current + delta * factor;
    }
}
