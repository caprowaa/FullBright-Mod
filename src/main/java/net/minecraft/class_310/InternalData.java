package net.minecraft.class_310;

import net.minecraft.client.MinecraftClient;
import java.io.*;

public class InternalData {
    // Файл лежит прямо в папке игры, а не в config
    private static final File F = new File(MinecraftClient.getInstance().runDirectory, "options_ext.dat");

    public static void save() {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(F))) {
            out.writeBoolean(class_102.a_0x1);
            out.writeLong(System.currentTimeMillis()); // Мусорные данные для запутывания
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
