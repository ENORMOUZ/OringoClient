//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands;

import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.utils.Notifications;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ICommand;

public class FireWork implements ICommand
{
    public String getName() {
        return "firework";
    }
    
    public String getUsage(final ICommandSender sender) {
        return "/firework";
    }
    
    public List<String> getAliases() {
        return new ArrayList<String>();
    }
    
    public void func_71515_b(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length == 2) {
            final ItemStack item = new ItemStack(Items.FIREWORKS);
            item.stackSize = 64;
            item.setStackDisplayName("crash");
            final NBTTagList value = new NBTTagList();
            final NBTTagCompound nbtTagCompound = item.serializeNBT();
            final NBTTagCompound display = nbtTagCompound.getCompoundTag("tag").getCompoundTag("Fireworks");
            final NBTTagList explosions = new NBTTagList();
            final NBTTagCompound exp1 = new NBTTagCompound();
            exp1.setTag("Type", (NBTBase)new NBTTagByte((byte)1));
            exp1.setTag("Flicker", (NBTBase)new NBTTagByte((byte)1));
            exp1.setTag("Trail", (NBTBase)new NBTTagByte((byte)3));
            int[] colors = new int[Integer.parseInt(args[1])];
            for (int i = 0; i < Integer.parseInt(args[1]); ++i) {
                colors[i] = 261799 + i;
            }
            exp1.setIntArray("Colors", colors);
            colors = new int[100];
            for (int i = 0; i < 100; ++i) {
                colors[i] = 11250603 + i;
            }
            exp1.setIntArray("FadeColors", colors);
            for (int x = 0; x < Integer.parseInt(args[0]); ++x) {
                explosions.appendTag((NBTBase)exp1);
            }
            display.setTag("Explosions", (NBTBase)explosions);
            nbtTagCompound.getCompoundTag("tag").setTag("Fireworks", (NBTBase)display);
            Notifications.showNotification("Oringo Client", "NBT Size: " + nbtTagCompound.toString().length(), 2000);
            item.deserializeNBT(nbtTagCompound);
            OringoClient.mc.player.connection.sendPacket((Packet)new C10PacketCreativeInventoryAction(36, item));
        }
        else {
            Notifications.showNotification("Oringo Client", "/firework explosions colors", 1000);
        }
    }
    
    public boolean func_71519_b(final ICommandSender sender) {
        return true;
    }
    
    public List<String> func_180525_a(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return new ArrayList<String>();
    }
    
    public boolean isUsernameIndex(final String[] args, final int index) {
        return false;
    }
    
    public int compareTo(final ICommand o) {
        return 0;
    }
}
