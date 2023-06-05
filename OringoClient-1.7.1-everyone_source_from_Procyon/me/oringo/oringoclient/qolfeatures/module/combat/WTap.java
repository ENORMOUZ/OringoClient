//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.combat;

import me.oringo.oringoclient.mixins.entity.PlayerSPAccessor;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.item.ItemBow;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C02PacketUseEntity;
import me.oringo.oringoclient.events.PacketSentEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class WTap extends Module
{
    public ModeSetting mode;
    public BooleanSetting bow;
    
    public WTap() {
        super("WTap", Category.COMBAT);
        this.mode = new ModeSetting("mode", "Packet", new String[] { "Packet", "Extra Packet" });
        this.bow = new BooleanSetting("Bow", true);
        this.addSettings(this.mode, this.bow);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
        if (this.isToggled() && ((event.packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.packet).getAction() == C02PacketUseEntity.Action.ATTACK) || (this.bow.isEnabled() && event.packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging)event.packet).getAction() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM && OringoClient.mc.player.func_70694_bm() != null && OringoClient.mc.player.func_70694_bm().getItem() instanceof ItemBow))) {
            final String selected = this.mode.getSelected();
            switch (selected) {
                case "Extra Packet": {
                    for (int i = 0; i < 4; ++i) {
                        OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C0BPacketEntityAction((Entity)OringoClient.mc.player, C0BPacketEntityAction.Action.STOP_SPRINTING));
                        OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C0BPacketEntityAction((Entity)OringoClient.mc.player, C0BPacketEntityAction.Action.START_SPRINTING));
                    }
                    break;
                }
                default: {
                    if (OringoClient.mc.player.isSprinting()) {
                        OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C0BPacketEntityAction((Entity)OringoClient.mc.player, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    }
                    OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C0BPacketEntityAction((Entity)OringoClient.mc.player, C0BPacketEntityAction.Action.START_SPRINTING));
                    break;
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSentEvent.Post event) {
        if (this.isToggled() && ((event.packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.packet).getAction() == C02PacketUseEntity.Action.ATTACK) || (this.bow.isEnabled() && event.packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging)event.packet).getAction() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM && OringoClient.mc.player.func_70694_bm() != null && OringoClient.mc.player.func_70694_bm().getItem() instanceof ItemBow)) && !OringoClient.mc.player.isSprinting()) {
            ((PlayerSPAccessor)OringoClient.mc.player).setServerSprintState(false);
        }
    }
}
