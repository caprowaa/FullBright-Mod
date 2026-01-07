package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

public class ExampleMod implements ClientModInitializer {
    public static KeyBinding aimToggle;
    public static KeyBinding addFriendKey;

    @Override
    public void onInitializeClient() {
        Config.load(); // Загружаем друзей при старте

        aimToggle = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Toggle FullBright (Aim)", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "FullBright Mod"));
        
        addFriendKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Add/Remove Friend", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "FullBright Mod"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (aimToggle.wasPressed()) {
                Config.aimEnabled = !Config.aimEnabled;
                client.player.sendMessage(Text.literal(Config.aimEnabled ? "§b[FB] ON" : "§7[FB] OFF"), true);
            }

            while (addFriendKey.wasPressed()) {
                // Используем crosshairTarget для точного определения игрока под прицелом
                HitResult hit = client.crosshairTarget;
                if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
                    Entity entity = ((EntityHitResult) hit).getEntity();
                    if (entity instanceof PlayerEntity friend) {
                        String name = friend.getGameProfile().getName().toLowerCase();
                        if (Config.friends.contains(name)) {
                            Config.friends.remove(name);
                            client.player.sendMessage(Text.literal("§cRemoved: " + friend.getGameProfile().getName()), true);
                        } else {
                            Config.friends.add(name);
                            client.player.sendMessage(Text.literal("§aAdded: " + friend.getGameProfile().getName()), true);
                        }
                        Config.save(); // Сохраняем после изменений
                    }
                }
            }
        });

        WorldRenderEvents.LAST.register(context -> AimLogic.onRender());
    }
}
