//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.settings.GameSettings;
import me.oringo.oringoclient.gui.ClickGUI;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AttackQueue
{
    public static boolean attack;
    private static int ticks;
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (AttackQueue.ticks != 0) {
            --AttackQueue.ticks;
        }
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null && AttackQueue.attack && (AttackQueue.ticks == 0 || (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit.equals((Object)MovingObjectPosition.MovingObjectType.ENTITY)))) {
            mc.player.func_71038_i();
            Label_0201: {
                if (mc.objectMouseOver != null) {
                    switch (mc.objectMouseOver.typeOfHit) {
                        case ENTITY: {
                            mc.playerController.attackEntity((EntityPlayer)mc.player, mc.objectMouseOver.entityHit);
                            break Label_0201;
                        }
                        case BLOCK: {
                            final BlockPos blockpos = mc.objectMouseOver.getBlockPos();
                            if (mc.world.getBlockState(blockpos).getBlock().getMaterial() != Material.AIR) {
                                mc.playerController.clickBlock(blockpos, mc.objectMouseOver.sideHit);
                                break Label_0201;
                            }
                            break;
                        }
                    }
                    if (mc.playerController.isNotCreative()) {
                        AttackQueue.ticks = 10;
                    }
                }
            }
            AttackQueue.attack = false;
        }
        if (mc.currentScreen instanceof ClickGUI) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindForward));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindBack));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindLeft));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindRight));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindJump));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindSprint));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindSneak));
        }
    }
    
    static {
        AttackQueue.attack = false;
        AttackQueue.ticks = 0;
    }
}
