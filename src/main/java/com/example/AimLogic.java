package com.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import java.util.Comparator;

public class AimLogic {
    // Храним предыдущие углы для стабилизации
    private static float lastYaw;
    private static float lastPitch;

    public static void onRender() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!Config.aimEnabled || mc.player == null || mc.world == null) return;

        PlayerEntity target = mc.world.getPlayers().stream()
            .filter(p -> p != mc.player && p.isAlive() && !p.isSpectator())
            .filter(p -> !Config.isFriend(p.getGameProfile().getName()))
            .filter(p -> Config.antiBot ? (p.age > 100 && !p.isInvisible()) : true)
            .filter(p -> mc.player.distanceTo(p) < Config.range)
            .filter(p -> isInFov(mc.player, p, Config.fov))
            .min(Comparator.comparingDouble(p -> mc.player.distanceTo(p)))
            .orElse(null);

        if (target != null) {
            // Наводимся на хитбокс (чуть ниже головы, чтобы не дергалось при прыжках)
            double dx = target.getX() - mc.player.getX();
            double dy = (target.getY() + target.getEyeHeight(target.getPose()) * 0.70) - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
            double dz = target.getZ() - mc.player.getZ();
            double dist = Math.sqrt(dx * dx + dz * dz);

            float targetYaw = (float) (Math.atan2(dz, dx) * 180 / Math.PI) - 90;
            float targetPitch = (float) -(Math.atan2(dy, dist) * 180 / Math.PI);

            // РАСЧЕТ ПЛАВНОСТИ (Убираем тряску)
            float yawDiff = MathHelper.wrapDegrees(targetYaw - mc.player.getYaw());
            float pitchDiff = MathHelper.wrapDegrees(targetPitch - mc.player.getPitch());

            // Если разница очень мала (меньше 0.1 градуса), не двигаем камеру (Deadzone)
            if (Math.abs(yawDiff) < 0.1f && Math.abs(pitchDiff) < 0.1f) return;

            // Динамический коэффициент: чем ближе к цели, тем медленнее доводка
            float dynamicSmoothness = Config.smoothness * 0.6f; 

            // Устанавливаем новые углы с использованием lerp
            mc.player.setYaw(mc.player.getYaw() + (yawDiff * dynamicSmoothness));
            mc.player.setPitch(mc.player.getPitch() + (pitchDiff * dynamicSmoothness));
        }
    }

    private static boolean isInFov(PlayerEntity player, PlayerEntity target, float fov) {
        double dx = target.getX() - player.getX();
        double dz = target.getZ() - player.getZ();
        float yaw = (float) (Math.atan2(dz, dx) * 180 / Math.PI) - 90;
        float angleDiff = Math.abs(MathHelper.wrapDegrees(yaw - player.getYaw()));
        return angleDiff <= fov / 2f;
    }
}
