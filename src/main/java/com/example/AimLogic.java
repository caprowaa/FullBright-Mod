package com.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import java.util.Comparator;

public class AimLogic {
    // Настройки для максимальной плавности
    private static final double RANGE = 6.0;      
    private static final float SMOOTHNESS = 0.12f; // Чем меньше, тем плавнее (0.05 - очень мягко)

    public static void renderTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!ExampleMod.aimEnabled || mc.player == null || mc.world == null) return;

        // Ищем ближайшую цель
        PlayerEntity target = mc.world.getPlayers().stream()
            .filter(p -> p != mc.player && p.isAlive() && !p.isSpectator())
            .filter(p -> mc.player.distanceTo(p) < RANGE)
            .min(Comparator.comparingDouble(p -> mc.player.distanceTo(p)))
            .orElse(null);

        if (target != null) {
            // Рассчитываем позицию цели с учетом её движения (интерполяция)
            double x = target.getX();
            double y = target.getY() + (target.getEyeHeight(target.getPose()) * 0.85);
            double z = target.getZ();

            double dx = x - mc.player.getX();
            double dy = y - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
            double dz = z - mc.player.getZ();
            double distXZ = Math.sqrt(dx * dx + dz * dz);

            float targetYaw = (float) (Math.atan2(dz, dx) * 180 / Math.PI) - 90;
            float targetPitch = (float) -(Math.atan2(dy, distXZ) * 180 / Math.PI);

            // Плавное изменение углов (Lerp) без резких скачков
            float newYaw = updateAngle(mc.player.getYaw(), targetYaw, SMOOTHNESS);
            float newPitch = updateAngle(mc.player.getPitch(), targetPitch, SMOOTHNESS);

            mc.player.setYaw(newYaw);
            mc.player.setPitch(newPitch);
        }
    }

    private static float updateAngle(float current, float target, float factor) {
        float delta = ((target - current + 180) % 360 + 360) % 360 - 180;
        return current + delta * factor;
    }
}
