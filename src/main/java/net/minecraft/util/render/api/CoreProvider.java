package net.minecraft.util.render.api;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CoreProvider implements ClientModInitializer {
    private boolean rPressed = false;
    private boolean mPressed = false;

    @Override
    public void onInitializeClient() {
        // Загрузка сохраненного состояния (включен или выключен аим)
        InternalData.load();
        
        // Авто-сохранение при выходе из игры
        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.CLIENT_STOPPING.register(c -> InternalData.save());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.currentScreen != null) return;

            long window = client.getWindow().getHandle();

            
            if (InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_R)) {
                if (!rPressed) {
                    class_102.a_0x1 = !class_102.a_0x1;
                    rPressed = true;
                }
            } else { rPressed = false; }

            
            // Колесико мыши - Друзья (Middle Button)
            if (org.lwjgl.glfw.GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_MIDDLE) == GLFW.GLFW_PRESS) {
                if (!mPressed) {
                    class_102.v_0x10();
                    mPressed = true;
                }
            } else { mPressed = false; }
        });

        
        WorldRenderEvents.LAST.register(context -> class_102.v_0x9());
    }
}
