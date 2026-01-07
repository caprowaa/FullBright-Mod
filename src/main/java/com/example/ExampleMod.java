package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class ExampleMod implements ClientModInitializer {
    private static KeyBinding aimToggle;
    private static KeyBinding addFriendKey;

    @Override
    public void onInitializeClient() {
        aimToggle = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fb.toggle", Config.aimKey, "FullBright"));
        addFriendKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fb.friend", Config.friendKey, "FullBright"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (aimToggle.wasPressed()) {
                Config.aimEnabled = !Config.aimEnabled;
                client.player.sendMessage(Text.literal(Config.aimEnabled ? "§b[FB] Vivid" : "§7[FB] Standard"), true);
            }

            while (addFriendKey.wasPressed()) {
                // Прямая проверка того, на кого ты смотришь
                if (client.crosshairTarget != null && client.targetedEntity instanceof PlayerEntity friend) {
                    String name = friend.getGameProfile().getName().toLowerCase();
                    if (Config.friends.contains(name)) {
                        Config.friends.remove(name);
                        client.player.sendMessage(Text.literal("§cRemoved: " + friend.getGameProfile().getName()), true);
                    } else {
                        Config.friends.add(name);
                        client.player.sendMessage(Text.literal("§aAdded: " + friend.getGameProfile().getName()), true);
                    }
                }
            }
        });

        WorldRenderEvents.LAST.register(context -> {
            AimLogic.onRender();
        });
    }
}
