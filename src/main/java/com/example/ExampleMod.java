package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
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
        aimKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fullbright.vivid", 
                InputUtil.Type.KEYSYM, 
                GLFW.GLFW_KEY_R, 
                "category.fullbright"
        ));

        // Тик для логики эффектов и кнопок
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            client.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1000, 0, false, false));

            while (aimKey.wasPressed()) {
                aimEnabled = !aimEnabled;
                client.player.sendMessage(Text.literal(
                    aimEnabled ? "§b[Aim] ALWAYS ACTIVE" : "§7[Aim] DISABLED"
                ), true);
            }
        });

        // HudRenderCallback срабатывает каждый кадр — это уберет тряску
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            AimLogic.renderTick();
        });
    }
}
