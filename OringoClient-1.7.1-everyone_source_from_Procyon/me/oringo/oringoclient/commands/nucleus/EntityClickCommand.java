//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands.nucleus;

import org.jetbrains.annotations.NotNull;
import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.network.Packet;
import me.oringo.oringoclient.utils.ReflectionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import me.oringo.oringoclient.OringoClient;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ICommand;

public class EntityClickCommand implements ICommand
{
    public String getName() {
        return "entity";
    }
    
    public String getUsage(final ICommandSender iCommandSender) {
        return "/entity";
    }
    
    public List<String> getAliases() {
        return new ArrayList<String>();
    }
    
    public void func_71515_b(final ICommandSender iCommandSender, final String[] strings) throws CommandException {
        final C02PacketUseEntity packet = new C02PacketUseEntity((Entity)OringoClient.mc.player, C02PacketUseEntity.Action.INTERACT);
        ReflectionUtils.setFieldByIndex(packet, 0, Integer.parseInt(strings[0]));
        OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)packet);
    }
    
    public boolean func_71519_b(final ICommandSender iCommandSender) {
        return true;
    }
    
    public List<String> func_180525_a(final ICommandSender iCommandSender, final String[] strings, final BlockPos blockPos) {
        return new ArrayList<String>();
    }
    
    public boolean isUsernameIndex(final String[] strings, final int i) {
        return false;
    }
    
    public int compareTo(@NotNull final ICommand o) {
        return 0;
    }
}
