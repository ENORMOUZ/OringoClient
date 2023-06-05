//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.skyblock;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.events.MoveEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class IceFillHelp extends Module
{
    public NumberSetting slowdown;
    public BooleanSetting noIceSlip;
    public BooleanSetting autoStop;
    
    public IceFillHelp() {
        super("Ice Fill Helper", Category.SKYBLOCK);
        this.slowdown = new NumberSetting("Ice slowdown", 0.15, 0.05, 1.0, 0.05);
        this.noIceSlip = new BooleanSetting("No ice slip", true);
        this.autoStop = new BooleanSetting("Auto stop", true);
        this.addSettings(this.autoStop, this.slowdown, this.noIceSlip);
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (!this.isToggled() || !OringoClient.mc.player.onGround) {
            return;
        }
        final BlockPos currentPos = new BlockPos(OringoClient.mc.player.posX, OringoClient.mc.player.posY - 0.4, OringoClient.mc.player.posZ);
        if (OringoClient.mc.world.getBlockState(currentPos).getBlock() == Blocks.ICE) {
            event.z *= this.slowdown.getValue();
            event.x *= this.slowdown.getValue();
            final BlockPos nextPos = new BlockPos(OringoClient.mc.player.posX + event.x, OringoClient.mc.player.posY - 0.4, OringoClient.mc.player.posZ + event.z);
            if (this.autoStop.isEnabled() && !currentPos.equals((Object)nextPos) && OringoClient.mc.world.getBlockState(nextPos).getBlock() == Blocks.ICE) {
                event.x = 0.0;
                event.z = 0.0;
            }
        }
        if (this.noIceSlip.isEnabled()) {
            Blocks.PACKED_ICE.slipperiness = 0.6f;
            Blocks.ICE.slipperiness = 0.6f;
        }
    }
    
    @Override
    public void onDisable() {
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.ICE.slipperiness = 0.98f;
    }
}
