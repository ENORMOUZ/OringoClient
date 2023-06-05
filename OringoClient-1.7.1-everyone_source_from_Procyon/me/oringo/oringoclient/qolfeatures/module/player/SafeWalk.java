//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.settings.GameSettings;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class SafeWalk extends Module
{
    public SafeWalk() {
        super("Eagle", 0, Category.PLAYER);
    }
    
    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(OringoClient.mc.gameSettings.keyBindSneak.getKeyCode(), GameSettings.isKeyDown(OringoClient.mc.gameSettings.keyBindSneak));
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (OringoClient.mc.player == null || OringoClient.mc.world == null || !this.isToggled() || OringoClient.mc.currentScreen != null) {
            return;
        }
        final BlockPos BP = new BlockPos(OringoClient.mc.player.posX, OringoClient.mc.player.posY - 0.5, OringoClient.mc.player.posZ);
        if (OringoClient.mc.world.getBlockState(BP).getBlock() == Blocks.AIR && OringoClient.mc.world.getBlockState(BP.down()).getBlock() == Blocks.AIR && OringoClient.mc.player.onGround && OringoClient.mc.player.movementInput.field_78900_b < 0.1f) {
            KeyBinding.setKeyBindState(OringoClient.mc.gameSettings.keyBindSneak.getKeyCode(), true);
        }
        else {
            KeyBinding.setKeyBindState(OringoClient.mc.gameSettings.keyBindSneak.getKeyCode(), GameSettings.isKeyDown(OringoClient.mc.gameSettings.keyBindSneak));
        }
    }
}
