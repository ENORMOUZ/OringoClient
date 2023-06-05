//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands;

import org.jetbrains.annotations.NotNull;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import net.minecraft.command.CommandException;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import me.oringo.oringoclient.OringoClient;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import me.oringo.oringoclient.utils.MilliTimer;
import net.minecraft.command.ICommand;

public class WardrobeCommand implements ICommand
{
    private int slot;
    private MilliTimer timeout;
    
    public WardrobeCommand() {
        this.slot = -1;
        this.timeout = new MilliTimer();
    }
    
    public String getName() {
        return "wd";
    }
    
    public String getUsage(final ICommandSender sender) {
        return "/wd";
    }
    
    public List<String> getAliases() {
        return new ArrayList<String>();
    }
    
    public void func_71515_b(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length > 0) {
            this.slot = Math.min(Math.max(Integer.parseInt(args[0]), 1), 18);
            OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C01PacketChatMessage("/pets"));
            this.timeout.updateTime();
        }
        else {
            OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C01PacketChatMessage("/wd"));
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketReceivedEvent event) {
        if (this.slot != -1 && event.packet instanceof S2DPacketOpenWindow) {
            if (this.timeout.hasTimePassed(2500L)) {
                this.slot = -1;
                return;
            }
            if (((S2DPacketOpenWindow)event.packet).getWindowTitle().getFormattedText().startsWith("Pets")) {
                final int windowId = ((S2DPacketOpenWindow)event.packet).getWindowId();
                OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C0EPacketClickWindow(windowId, 48, 0, 3, (ItemStack)null, (short)0));
                OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C0EPacketClickWindow(windowId + 1, 32, 0, 3, (ItemStack)null, (short)0));
                if (this.slot <= 9) {
                    OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C0EPacketClickWindow(windowId + 2, 35 + this.slot, 0, 0, (ItemStack)null, (short)0));
                    OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C0DPacketCloseWindow(windowId + 2));
                }
                else {
                    OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C0EPacketClickWindow(windowId + 2, 53, 0, 3, (ItemStack)null, (short)0));
                    OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C0EPacketClickWindow(windowId + 3, 35 + this.slot, 0, 0, (ItemStack)null, (short)0));
                    OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C0DPacketCloseWindow(windowId + 3));
                }
                event.setCanceled(true);
            }
            else if (((S2DPacketOpenWindow)event.packet).getWindowTitle().getFormattedText().startsWith("SkyBlock Menu")) {
                event.setCanceled(true);
            }
            else if (((S2DPacketOpenWindow)event.packet).getWindowTitle().getFormattedText().startsWith("Wardrobe")) {
                event.setCanceled(true);
                this.slot = -1;
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
    
    public int compareTo(@NotNull final ICommand o) {
        return 0;
    }
}
