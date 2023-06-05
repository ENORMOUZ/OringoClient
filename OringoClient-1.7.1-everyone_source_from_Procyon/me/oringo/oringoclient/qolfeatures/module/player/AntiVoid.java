//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AntiVoid extends Module
{
    public NumberSetting fallDistance;
    private boolean tp;
    private boolean canTp;
    private double x;
    private double y;
    private double z;
    
    public AntiVoid() {
        super("Anti Void", 0, Category.PLAYER);
        this.fallDistance = new NumberSetting("Fall distance", 1.0, 0.5, 5.0, 0.1);
        this.canTp = true;
        this.addSettings(this.fallDistance);
    }
    
    @SubscribeEvent
    public void onMovePre(final MotionUpdateEvent.Pre event) {
        if (!this.isToggled() || !this.canTp || OringoClient.mc.player.fallDistance < this.fallDistance.getValue() || OringoClient.mc.player.capabilities.allowFlying) {
            return;
        }
        BlockPos block = new BlockPos(OringoClient.mc.player.posX, OringoClient.mc.player.posY, OringoClient.mc.player.posZ);
        for (int i = (int)OringoClient.mc.player.posY; i > 0; --i) {
            if (!(OringoClient.mc.world.getBlockState(block).getBlock() instanceof BlockAir)) {
                return;
            }
            block = block.add(0, -1, 0);
        }
        this.tp = true;
        this.canTp = false;
        this.x = OringoClient.mc.player.posX;
        this.y = OringoClient.mc.player.posY;
        this.z = OringoClient.mc.player.posZ;
        OringoClient.mc.player.setPosition(this.x + 1000.0, this.y, this.z + 1000.0);
    }
    
    @SubscribeEvent
    public void onMovePost(final MotionUpdateEvent.Post event) {
        if (this.tp) {
            OringoClient.mc.player.setPosition(this.x, this.y, this.z);
            OringoClient.mc.player.setVelocity(0.0, 0.0, 0.0);
            this.tp = false;
            new Thread(() -> {
                try {
                    Thread.sleep(750L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.canTp = true;
            }).start();
        }
    }
}
