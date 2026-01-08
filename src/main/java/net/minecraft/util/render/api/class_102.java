package net.minecraft.util.render.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import java.util.*;

public class class_102 {
    public static boolean a_0x1 = false; 
    public static Set<UUID> f_list = new HashSet<>(); 

    // === НАСТРОЙКИ ===
    private static final float FOV = 100.0f;       
    private static final double RANGE = 3.4;      
    private static final float SMOOTH = 0.04f;    

    public static void v_0x10() {
        MinecraftClient c = MinecraftClient.getInstance();
        if (c.targetedEntity instanceof PlayerEntity) {
            UUID id = c.targetedEntity.getUuid();
            if (f_list.contains(id)) f_list.remove(id);
            else f_list.add(id);
        }
    }

    public static void v_0x9() {
        MinecraftClient c = MinecraftClient.getInstance();
        if (!a_0x1 || c.player == null || c.currentScreen != null) return;

        PlayerEntity t = c.world.getPlayers().stream()
            .filter(p -> p != c.player && p.isAlive())
            .filter(p -> !f_list.contains(p.getUuid())) 
            .filter(p -> c.player.distanceTo(p) < RANGE) 
            .filter(p -> {
                // РАСЧЕТ FOV
                double dX = p.getX() - c.player.getX();
                double dZ = p.getZ() - c.player.getZ();
                float yaw = (float) (Math.atan2(dZ, dX) * 180 / Math.PI) - 90;
                float angleDiff = Math.abs(MathHelper.wrapDegrees(yaw - c.player.getYaw()));
                return angleDiff <= FOV / 2f;
            })
            .findFirst().orElse(null);

        if (t != null) {
            double d0 = t.getX() - c.player.getX();
            double d1 = (t.getY() + t.getEyeHeight(t.getPose()) * 0.7) - (c.player.getY() + c.player.getEyeHeight(c.player.getPose()));
            double d2 = t.getZ() - c.player.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);

            float f0 = (float) (Math.atan2(d2, d0) * 180 / Math.PI) - 90;
            float f1 = (float) -(Math.atan2(d1, d3) * 180 / Math.PI);

            c.player.setYaw(c.player.getYaw() + MathHelper.wrapDegrees(f0 - c.player.getYaw()) * SMOOTH);
            c.player.setPitch(c.player.getPitch() + MathHelper.wrapDegrees(f1 - c.player.getPitch()) * SMOOTH);
        }
    }
}
