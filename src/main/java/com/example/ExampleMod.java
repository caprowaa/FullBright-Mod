package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ExampleMod implements ClientModInitializer {
    private static KeyBinding aimToggle;
    private static KeyBinding addFriendKey;

    @Override
    public void onInitializeClient() {
        // Кнопка R для включения аима
        aimToggle = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fullbright.toggle", 
                InputUtil.Type.KEYSYM, 
                GLFW.GLFW_KEY_R, 
                "category.fullbright"
        ));

        // Кнопка M для быстрого добавления друга (на кого смотришь)
        addFriendKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fullbright.friend", 
                InputUtil.Type.KEYSYM, 
                GLFW.GLFW_KEY_M, 
                "category.fullbright"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // Логика переключения аима
            while (aimToggle.wasPressed()) {
                Config.aimEnabled = !Config.aimEnabled;
                client.player.sendMessage(Text.literal(
                    Config.aimEnabled ? "§b[Aim] ALWAYS ACTIVE" : "§7[Aim] DISABLED"
                ), true);
            }

            // Логика добавления друга по кнопке M
            while (addFriendKey.wasPressed()) {
                if (client.targetedEntity instanceof PlayerEntity friend) {
                    String name = friend.getEntityName().toLowerCase();
                    if (Config.friends.contains(name)) {
                        Config.friends.remove(name);
                        client.player.sendMessage(Text.literal("§cRemoved Friend: " + friend.getEntityName()), true);
                    } else {
                        Config.friends.add(name);
                        client.player.sendMessage(Text.literal("§aAdded Friend: " + friend.getEntityName()), true);
                    }
                }
            }
        });

        // Использование WorldRenderEvents.LAST — это секрет максимальной плавности.
        // Этот метод вызывается ПЕРЕД выводом кадра на экран, что убирает тряску.
        WorldRenderEvents.LAST.register(context -> {
            AimLogic.onRender();
        });
    }
}
