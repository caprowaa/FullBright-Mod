package com.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import java.util.Comparator;

public class AimLogic {
    // === НАСТРОЙКИ МОДА ===
    private static final double RANGE = 4.5;      // Радиус действия
    private static final float SENSITIVITY = 0.2f; // Плавность (0.1 - медленно, 0.4 - быстро)
    private static final String TARGET_PART = "mid"; // "head" (голова), "body" (тело), "mid" (грудь)

    public static void tick(MinecraftClient mc) {
        // Работает только если режим включен и зажата кнопка атаки
        if (!ExampleMod.aimEnabled || !mc.options.attackKey.isPressed()) return;

        PlayerEntity target = mc.world.getPlayers().stream()
            .filter(p -> p != mc.player && p.isAlive() && !p.isSpectator())
            .filter(p -> mc.player.distanceTo(p) < RANGE)
            .min(Comparator.comparingDouble(p -> mc.player.distanceTo(p)))
            .orElse(null);

        if (target != null) {
            double yOffset;
            if (TARGET_PART.equals("head")) {
                yOffset = target.getEyeHeight(target.getPose());
            } else if (TARGET_PART.equals("body")) {
                yOffset = target.getEyeHeight(target.getPose()) / 2;
            } else {
                yOffset = target.getEyeHeight(target.getPose()) * 0.85; // mid
            }

            double dx = target.getX() - mc.player.getX();
            double dy = (target.getY() + yOffset) - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
            double dz = target.getZ() - mc.player.getZ();
            double dist = Math.sqrt(dx * dx + dz * dz);

            float targetYaw = (float) (Math.atan2(dz, dx) * 180 / Math.PI) - 90;
            float targetPitch = (float) -(Math.atan2(dy, dist) * 180 / Math.PI);

            // Прямое наведение через движок Fabric
            mc.player.setYaw(lerpAngle(mc.player.getYaw(), targetYaw, SENSITIVITY));
            mc.player.setPitch(lerpAngle(mc.player.getPitch(), targetPitch, SENSITIVITY));
        }
    }

    private static float lerpAngle(float start, float end, float factor) {
        float delta = ((end - start + 180) % 360 + 360) % 360 - 180;
        return start + delta * factor;
    }
}
