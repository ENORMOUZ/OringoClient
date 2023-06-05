//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.skyblock;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemSnowball;
import me.oringo.oringoclient.OringoClient;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Snowballs extends Module
{
    private boolean wasPressed;
    public BooleanSetting pickupstash;
    
    public Snowballs() {
        super("Snowballs", Category.SKYBLOCK);
        this.pickupstash = new BooleanSetting("Pick up stash", true);
        this.addSettings(this.pickupstash);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (OringoClient.mc.currentScreen != null) {
            return;
        }
        if (this.isPressed() && !this.wasPressed) {
            final int holding = OringoClient.mc.player.inventory.currentItem;
            for (int x = 0; x < 9; ++x) {
                if (OringoClient.mc.player.inventory.getStackInSlot(x) != null && (OringoClient.mc.player.inventory.getStackInSlot(x).getItem() instanceof ItemSnowball || OringoClient.mc.player.inventory.getStackInSlot(x).getItem() instanceof ItemEgg || OringoClient.mc.player.inventory.getStackInSlot(x).getItem() instanceof ItemEnderPearl || OringoClient.mc.player.inventory.getStackInSlot(x).getDisplayName().contains("Bonemerang"))) {
                    OringoClient.mc.player.inventory.currentItem = x;
                    SkyblockUtils.updateItem();
                    for (int e = 0; e < 16; ++e) {
                        OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C08PacketPlayerBlockPlacement(OringoClient.mc.player.func_70694_bm()));
                    }
                }
            }
            OringoClient.mc.player.inventory.currentItem = holding;
            SkyblockUtils.updateItem();
            if (this.pickupstash.isEnabled()) {
                OringoClient.mc.player.sendChatMessage("/pickupstash");
            }
        }
        this.wasPressed = this.isPressed();
    }
    
    @Override
    public boolean isKeybind() {
        return true;
    }
}
