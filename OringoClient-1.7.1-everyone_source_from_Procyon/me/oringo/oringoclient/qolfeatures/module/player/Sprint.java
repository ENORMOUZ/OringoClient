//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.player;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Sprint extends Module
{
    public BooleanSetting omni;
    public BooleanSetting keep;
    
    public Sprint() {
        super("Sprint", 0, Category.COMBAT);
        this.omni = new BooleanSetting("OmniSprint", false);
        this.keep = new BooleanSetting("KeepSprint", true);
        this.addSettings(this.keep, this.omni);
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void niceOmniSprintCheck(final MotionUpdateEvent.Pre event) {
        if (((this.omni.isEnabled() && this.isToggled()) || OringoClient.derp.isToggled()) && !OringoClient.mc.player.isSneaking()) {
            OringoClient.mc.getConnection().sendPacket((Packet)new C0BPacketEntityAction((Entity)OringoClient.mc.player, C0BPacketEntityAction.Action.START_SNEAKING));
            OringoClient.mc.getConnection().sendPacket((Packet)new C0BPacketEntityAction((Entity)OringoClient.mc.player, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
}
