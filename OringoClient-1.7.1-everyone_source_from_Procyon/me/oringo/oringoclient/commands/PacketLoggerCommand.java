//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands;

import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.io.FileOutputStream;
import java.io.IOException;
import me.oringo.oringoclient.OringoClient;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import java.io.BufferedWriter;
import net.minecraft.command.ICommand;

public class PacketLoggerCommand implements ICommand
{
    public static BufferedWriter writer;
    
    public String getName() {
        return "packetlog";
    }
    
    public String getUsage(final ICommandSender sender) {
        return "/packetlog";
    }
    
    public List<String> getAliases() {
        return new ArrayList<String>();
    }
    
    public void func_71515_b(final ICommandSender sender, final String[] args) throws CommandException {
        if (PacketLoggerCommand.writer != null) {
            OringoClient.sendMessageWithPrefix("Packet logger disabled.");
            try {
                PacketLoggerCommand.writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            PacketLoggerCommand.writer = null;
        }
        else {
            OringoClient.sendMessageWithPrefix("Packet logger enabled.");
            try {
                PacketLoggerCommand.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("packetlog.txt"), StandardCharsets.UTF_8));
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
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
    
    static {
        PacketLoggerCommand.writer = null;
    }
}
