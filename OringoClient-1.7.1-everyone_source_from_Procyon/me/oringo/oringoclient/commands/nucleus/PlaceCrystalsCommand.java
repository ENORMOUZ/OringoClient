//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands.nucleus;

import org.jetbrains.annotations.NotNull;
import net.minecraft.command.CommandException;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import me.oringo.oringoclient.OringoClient;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.command.ICommand;

public class PlaceCrystalsCommand implements ICommand
{
    public static BlockPos[] nucleus;
    
    public String getName() {
        return "placecrystals";
    }
    
    public String getUsage(final ICommandSender iCommandSender) {
        return "/placecrystals";
    }
    
    public List<String> getAliases() {
        return new ArrayList<String>();
    }
    
    public void func_71515_b(final ICommandSender iCommandSender, final String[] strings) throws CommandException {
        final BlockPos[] nucleus;
        int length;
        int i = 0;
        BlockPos crystal;
        new Thread(() -> {
            nucleus = PlaceCrystalsCommand.nucleus;
            for (length = nucleus.length; i < length; ++i) {
                crystal = nucleus[i];
                OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, crystal, EnumFacing.fromAngle((double)OringoClient.mc.player.rotationYaw)));
                try {
                    Thread.sleep(500L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
    
    static {
        PlaceCrystalsCommand.nucleus = new BlockPos[] { new BlockPos(543, 111, 499), new BlockPos(544, 111, 527), new BlockPos(482, 111, 525), new BlockPos(483, 111, 500), new BlockPos(513, 111, 483) };
    }
}
