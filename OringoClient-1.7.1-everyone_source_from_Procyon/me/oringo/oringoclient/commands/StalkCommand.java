//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands;

import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import java.util.Iterator;
import me.oringo.oringoclient.utils.Notifications;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import java.util.UUID;
import net.minecraft.command.ICommand;

public class StalkCommand implements ICommand
{
    public static UUID stalking;
    
    public String getName() {
        return "stalk";
    }
    
    public String getUsage(final ICommandSender sender) {
        return "/stalk";
    }
    
    public List<String> getAliases() {
        return new ArrayList<String>();
    }
    
    public void func_71515_b(final ICommandSender sender, final String[] args) throws CommandException {
        StalkCommand.stalking = null;
        if (args.length == 1) {
            for (final EntityPlayer player : Minecraft.getMinecraft().world.playerEntities) {
                if (player.getName().equalsIgnoreCase(args[0])) {
                    StalkCommand.stalking = player.getUniqueID();
                    Notifications.showNotification("Oringo Client", "Enabled stalking!", 1000);
                    return;
                }
            }
            Notifications.showNotification("Oringo Client", "Player not found!", 1000);
            return;
        }
        Notifications.showNotification("Oringo Client", "Disabled Stalking!", 1000);
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
