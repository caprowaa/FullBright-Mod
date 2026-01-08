package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
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
        Config.load(); // Загружаем при старте

        // Сохранение при выходе из игры
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            Config.save();
        });

        aimToggle = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "FullBright Toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "FullBright Mod"));
        
        addFriendKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Add/Remove Friend", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "FullBright Mod"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (aimToggle.wasPressed()) {
                Config.aimEnabled = !Config.aimEnabled;
                client.player.sendMessage(Text.literal(Config.aimEnabled ? "§b[FB] ON" : "§7[FB] OFF"), true);
                Config.save(); // Сохраняем сразу при переключении
            }

            while (addFriendKey.wasPressed()) {
                HitResult hit = client.crosshairTarget;
                if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
                    Entity entity = ((EntityHitResult) hit).getEntity();
                    if (entity instanceof PlayerEntity targetPlayer) {
                        String name = targetPlayer.getGameProfile().getName().toLowerCase();
                        if (Config.friends.contains(name)) {
                            Config.friends.remove(name);
                            client.player.sendMessage(Text.literal("§cRemoved: " + targetPlayer.getGameProfile().getName()), true);
                        } else {
                            Config.friends.add(name);
                            client.player.sendMessage(Text.literal("§aAdded: " + targetPlayer.getGameProfile().getName()), true);
                        }
                        Config.save(); // Сохраняем список друзей
                    }
                }
            }
        });

        WorldRenderEvents.LAST.register(context -> AimLogic.onRender());
    }
}
