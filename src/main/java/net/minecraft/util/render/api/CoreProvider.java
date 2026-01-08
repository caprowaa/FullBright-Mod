package net.minecraft.util.render.api;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CoreProvider implements ClientModInitializer {
    public static KeyBinding k_0x1;

    @Override
    public void onInitializeClient() {
        InternalData.load();

        ClientLifecycleEvents.CLIENT_STOPPING.register(c -> InternalData.save());

        k_0x1 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.system.render.fb", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "Controls"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (k_0x1.wasPressed()) {
                class_102.a_0x1 = !class_102.a_0x1;
            }
        });

        WorldRenderEvents.LAST.register(context -> class_102.v_0x9());
    }
}
