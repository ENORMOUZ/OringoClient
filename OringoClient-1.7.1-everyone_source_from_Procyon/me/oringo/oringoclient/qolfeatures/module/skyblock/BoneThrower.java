//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.skyblock;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import me.oringo.oringoclient.utils.SkyblockUtils;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.events.PlayerUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class BoneThrower extends Module
{
    public ModeSetting mode;
    public BooleanSetting autoDisable;
    private int ticks;
    
    public BoneThrower() {
        super("BoneThrower", Category.SKYBLOCK);
        this.mode = new ModeSetting("Mode", "Hotbar", new String[] { "Hotbar" });
        this.autoDisable = new BooleanSetting("Disable", true);
        this.addSettings(this.mode, this.autoDisable);
    }
    
    @Override
    public void onEnable() {
        this.ticks = 6;
    }
    
    @SubscribeEvent
    public void onUpdate(final PlayerUpdateEvent event) {
        --this.ticks;
        if (this.isToggled()) {
            final String selected = this.mode.getSelected();
            switch (selected) {
                case "Hotbar": {
                    final int last = OringoClient.mc.player.inventory.currentItem;
                    for (int i = 0; i < 9; ++i) {
                        final ItemStack stack = OringoClient.mc.player.inventory.getStackInSlot(i);
                        if (stack != null && stack.getDisplayName().contains("Bonemerang")) {
                            OringoClient.mc.player.inventory.currentItem = i;
                            SkyblockUtils.updateItem();
                            OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C08PacketPlayerBlockPlacement(OringoClient.mc.player.func_70694_bm()));
                        }
                    }
                    OringoClient.mc.player.inventory.currentItem = last;
                    SkyblockUtils.updateItem();
                    if (this.autoDisable.isEnabled()) {
                        this.toggle();
                        break;
                    }
                    break;
                }
            }
        }
    }
}
