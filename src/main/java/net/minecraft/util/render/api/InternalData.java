package net.minecraft.util.render.api;

import net.minecraft.client.MinecraftClient;
import java.io.*;

public class InternalData {
    
    private static final File F = MinecraftClient.getInstance().runDirectory.toPath().resolve("options_ext.dat").toFile();

    public static void save() {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(F))) {
            out.writeBoolean(class_102.a_0x1);
        } catch (Exception ignored) {}
    }

    public static void load() {
        if (F.exists()) {
            try (DataInputStream in = new DataInputStream(new FileInputStream(F))) {
                class_102.a_0x1 = in.readBoolean();
            } catch (Exception ignored) {}
        }
    }
}
