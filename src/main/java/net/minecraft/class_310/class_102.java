package net.minecraft.class_310;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class class_102 {
    public static boolean a_0x1 = false; // Состояние аима
    
    public static void v_0x9() {
        MinecraftClient c = MinecraftClient.getInstance();
        if (!a_0x1 || c.player == null) return;

        PlayerEntity t = c.world.getPlayers().stream()
            .filter(p -> p != c.player && p.isAlive())
            .filter(p -> c.player.distanceTo(p) < 5.2)
            .findFirst().orElse(null);

        if (t != null) {
            double d0 = t.getX() - c.player.getX();
            double d1 = (t.getY() + t.getEyeHeight(t.getPose()) * 0.7) - (c.player.getY() + c.player.getEyeHeight(c.player.getPose()));
            double d2 = t.getZ() - c.player.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);

            float f0 = (float) (Math.atan2(d2, d0) * 180 / Math.PI) - 90;
            float f1 = (float) -(Math.atan2(d1, d3) * 180 / Math.PI);

            c.player.setYaw(c.player.getYaw() + MathHelper.wrapDegrees(f0 - c.player.getYaw()) * 0.07f);
            c.player.setPitch(c.player.getPitch() + MathHelper.wrapDegrees(f1 - c.player.getPitch()) * 0.07f);
        }
    }
}
