//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import me.oringo.oringoclient.utils.Notifications;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ICommand;

public class CheckNameCommand implements ICommand
{
    public static String profileView;
    
    public String getName() {
        return "checkname";
    }
    
    public String getUsage(final ICommandSender sender) {
        return "/checkname";
    }
    
    public List<String> getAliases() {
        return new ArrayList<String>();
    }
    
    public void func_71515_b(final ICommandSender sender, final String[] args) throws CommandException {
        final Minecraft mc = Minecraft.getMinecraft();
        if (args.length != 1) {
            OringoClient.sendMessageWithPrefix("/checkname [IGN]");
            return;
        }
        for (final EntityPlayer entity : mc.world.playerEntities) {
            if (entity.getName().equalsIgnoreCase(args[0])) {
                if (entity.getDistance((Entity)mc.player) > 6.0f) {
                    OringoClient.sendMessageWithPrefix("You are too far away!");
                    return;
                }
                if (mc.player.func_70694_bm() != null) {
                    OringoClient.sendMessageWithPrefix("You can't hold anything in your hand!");
                    return;
                }
                mc.playerController.func_78768_b((EntityPlayer)mc.player, (Entity)entity);
                CheckNameCommand.profileView = args[0];
                return;
            }
        }
        OringoClient.sendMessageWithPrefix("That Player isn't loaded!");
    }
    
    public boolean func_71519_b(final ICommandSender sender) {
        return true;
    }
    
    public List<String> func_180525_a(final ICommandSender sender, final String[] args, final BlockPos pos) {
        final ArrayList<String> matching = new ArrayList<String>();
        for (final EntityPlayer playerEntity : Minecraft.getMinecraft().world.playerEntities) {
            if (playerEntity.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                matching.add(playerEntity.getName());
            }
        }
        return matching;
    }
    
    public boolean isUsernameIndex(final String[] args, final int index) {
        return false;
    }
    
    public int compareTo(final ICommand o) {
        return 0;
    }
    
    @SubscribeEvent
    public void onGui(final GuiOpenEvent event) {
        if (event.gui instanceof GuiChest && CheckNameCommand.profileView != null && ((ContainerChest)((GuiChest)event.gui).inventorySlots).getLowerChestInventory().getName().toLowerCase().startsWith(CheckNameCommand.profileView.toLowerCase())) {
            final ItemStack is;
            String name;
            new Thread(() -> {
                try {
                    Thread.sleep(100L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                is = Minecraft.getMinecraft().player.openContainer.getSlot(22).getStack();
                if (is != null && is.getItem().equals(Items.SKULL)) {
                    name = is.serializeNBT().getCompoundTag("tag").getCompoundTag("SkullOwner").getString("Name");
                    Minecraft.getMinecraft().player.closeScreen();
                    Notifications.showNotification("Oringo Client", "Real name: " + ChatFormatting.GOLD + name.replaceFirst("§", ""), 4000);
                }
                CheckNameCommand.profileView = null;
            }).start();
        }
    }
}
