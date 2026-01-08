package com.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import java.util.Comparator;

public class AimLogic {
    public static void onRender() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!Config.aimEnabled || mc.player == null || mc.world == null) return;

        PlayerEntity target = mc.world.getPlayers().stream()
            .filter(p -> p != mc.player && p.isAlive() && !p.isSpectator())
            .filter(p -> !Config.isFriend(p.getGameProfile().getName()))
            .filter(p -> {
                if (Config.antiBot) return p.age > 100 && !p.isInvisible();
                return true;
            })
            .filter(p -> mc.player.distanceTo(p) < Config.range)
            .filter(p -> isInFov(mc.player, p, Config.fov)) // ПРОВЕРКА FOV
            .min(Comparator.comparingDouble(p -> mc.player.distanceTo(p)))
            .orElse(null);

        if (target != null) {
            // Наводимся на шею (0.85 от высоты глаз)
            double targetX = target.getX();
            double targetY = target.getY() + (target.getEyeHeight(target.getPose()) * 0.85);
            double targetZ = target.getZ();

            double dx = targetX - mc.player.getX();
            double dy = targetY - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
            double dz = targetZ - mc.player.getZ();
            double dist = Math.sqrt(dx * dx + dz * dz);

            float targetYaw = (float) (Math.atan2(dz, dx) * 180 / Math.PI) - 90;
            float targetPitch = (float) -(Math.atan2(dy, dist) * 180 / Math.PI);

            // Плавное движение к цели
            mc.player.setYaw(lerp(mc.player.getYaw(), targetYaw, Config.smoothness));
            mc.player.setPitch(lerp(mc.player.getPitch(), targetPitch, Config.smoothness));
        }
    }

    // Метод проверки: находится ли цель в поле зрения
    private static boolean isInFov(PlayerEntity player, PlayerEntity target, float fov) {
        double dx = target.getX() - player.getX();
        double dz = target.getZ() - player.getZ();
        float yaw = (float) (Math.atan2(dz, dx) * 180 / Math.PI) - 90;
        float angleDiff = Math.abs(MathHelper.wrapDegrees(yaw - player.getYaw()));
        return angleDiff <= fov / 2f;
    }

    private static float lerp(float start, float end, float factor) {
        float delta = ((end - start + 180) % 360 + 360) % 360 - 180;
        return start + delta * factor;
    }
}
