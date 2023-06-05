//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.Item;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemEnderPearl;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.client.event.GuiScreenEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class ChestStealer extends Module
{
    private MilliTimer timer;
    public NumberSetting delay;
    public BooleanSetting close;
    public BooleanSetting nameCheck;
    public BooleanSetting stealTrash;
    
    public ChestStealer() {
        super("Chest stealer", 0, Category.PLAYER);
        this.timer = new MilliTimer();
        this.delay = new NumberSetting("Delay", 100.0, 30.0, 200.0, 1.0);
        this.close = new BooleanSetting("Auto close", true);
        this.nameCheck = new BooleanSetting("Name check", true);
        this.stealTrash = new BooleanSetting("Steal trash", false);
        this.addSettings(this.delay, this.nameCheck, this.stealTrash, this.close);
    }
    
    @SubscribeEvent
    public void onGui(final GuiScreenEvent.BackgroundDrawnEvent event) {
        if (event.gui instanceof GuiChest && this.isToggled()) {
            final Container container = ((GuiChest)event.gui).inventorySlots;
            if (container instanceof ContainerChest && (!this.nameCheck.isEnabled() || ChatFormatting.stripFormatting(((ContainerChest)container).getLowerChestInventory().getDisplayName().getFormattedText()).equals("Chest") || ChatFormatting.stripFormatting(((ContainerChest)container).getLowerChestInventory().getDisplayName().getFormattedText()).equals("LOW"))) {
                for (int i = 0; i < ((ContainerChest)container).getLowerChestInventory().getSizeInventory(); ++i) {
                    if (container.getSlot(i).getHasStack() && this.timer.hasTimePassed((long)this.delay.getValue())) {
                        final Item item = container.getSlot(i).getStack().getItem();
                        if (this.stealTrash.isEnabled() || item instanceof ItemEnderPearl || item instanceof ItemTool || item instanceof ItemArmor || item instanceof ItemBow || item instanceof ItemPotion || item == Items.ARROW || item instanceof ItemAppleGold || item instanceof ItemSword || item instanceof ItemBlock) {
                            OringoClient.mc.playerController.func_78753_a(container.windowId, i, 0, 1, (EntityPlayer)OringoClient.mc.player);
                            this.timer.updateTime();
                            return;
                        }
                    }
                }
                for (int i = 0; i < ((ContainerChest)container).getLowerChestInventory().getSizeInventory(); ++i) {
                    if (container.getSlot(i).getHasStack()) {
                        final Item item = container.getSlot(i).getStack().getItem();
                        if (this.stealTrash.isEnabled() || item instanceof ItemEnderPearl || item instanceof ItemTool || item instanceof ItemArmor || item instanceof ItemBow || item instanceof ItemPotion || item == Items.ARROW || item instanceof ItemAppleGold || item instanceof ItemSword || item instanceof ItemBlock) {
                            return;
                        }
                    }
                }
                if (this.close.isEnabled()) {
                    OringoClient.mc.player.closeScreen();
                }
            }
        }
    }
}
