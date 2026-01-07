package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ExampleMod implements ClientModInitializer {
    private static KeyBinding aimKey;
    public static boolean aimEnabled = false;

    @Override
    public void onInitializeClient() {
        // Регистрация кнопки R (секретный триггер)
        aimKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fullbright.vivid", 
                InputUtil.Type.KEYSYM, 
                GLFW.GLFW_KEY_R, 
                "category.fullbright"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // ЧАСТЬ 1: Постоянный FullBright
            client.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1000, 0, false, false));

            // ЧАСТЬ 2: Переключатель режима
            while (aimKey.wasPressed()) {
                aimEnabled = !aimEnabled;
                // Маскировочное сообщение над хотбаром
                client.player.sendMessage(Text.literal(
                    aimEnabled ? "§b[FullBright] Mode: Vivid (Enhanced)" : "§b[FullBright] Mode: Standard"
                ), true);
            }
            
            // Запуск логики аима
            AimLogic.tick(client);
        });
    }
}
