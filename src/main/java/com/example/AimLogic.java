package com.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import java.util.Comparator;

public class AimLogic {
    public static void onRender() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!Config.aimEnabled || mc.player == null || mc.world == null) return;

        PlayerEntity target = mc.world.getPlayers().stream()
            .filter(p -> p != mc.player && p.isAlive() && !p.isSpectator())
            .filter(p -> !Config.isFriend(p.getGameProfile().getName()))
            .filter(p -> {
                if (Config.antiBot) {
                    return p.age > 100 && !p.isInvisible(); // Игнорим ботов
                }
                return true;
            })
            .filter(p -> mc.player.distanceTo(p) < Config.range)
            .min(Comparator.comparingDouble(p -> mc.player.distanceTo(p)))
            .orElse(null);

        if (target != null) {
            double dx = target.getX() - mc.player.getX();
            double dy = (target.getY() + target.getEyeHeight(target.getPose()) * 0.85) - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
            double dz = target.getZ() - mc.player.getZ();
            double dist = Math.sqrt(dx * dx + dz * dz);

            float targetYaw = (float) (Math.atan2(dz, dx) * 180 / Math.PI) - 90;
            float targetPitch = (float) -(Math.atan2(dy, dist) * 180 / Math.PI);

            mc.player.setYaw(lerp(mc.player.getYaw(), targetYaw, Config.smoothness));
            mc.player.setPitch(lerp(mc.player.getPitch(), targetPitch, Config.smoothness));
        }
    }

    private static float lerp(float start, float end, float factor) {
        float delta = ((end - start + 180) % 360 + 360) % 360 - 180;
        return start + delta * factor;
    }
}
