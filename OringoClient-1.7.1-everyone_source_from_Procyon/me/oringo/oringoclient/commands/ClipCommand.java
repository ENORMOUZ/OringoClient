//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands;

import org.jetbrains.annotations.NotNull;
import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import me.oringo.oringoclient.utils.Notifications;
import net.minecraft.util.MathHelper;
import me.oringo.oringoclient.OringoClient;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ICommand;

public class ClipCommand implements ICommand
{
    public String getName() {
        return "clip";
    }
    
    public String getUsage(final ICommandSender sender) {
        return "/clip";
    }
    
    public List<String> getAliases() {
        return new ArrayList<String>();
    }
    
    public void func_71515_b(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length == 1) {
            OringoClient.mc.player.setPosition(MathHelper.floor(OringoClient.mc.player.posX) + 0.5, OringoClient.mc.player.posY + Double.parseDouble(args[0]), MathHelper.floor(OringoClient.mc.player.posZ) + 0.5);
        }
        else {
            Notifications.showNotification("Oringo Client", "/clip distance", 1500);
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
    
    public int compareTo(@NotNull final ICommand o) {
        return 0;
    }
}
