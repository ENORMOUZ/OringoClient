//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.other;

import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import io.netty.channel.ChannelHandlerContext;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Arrays;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class TntRunPing extends Module
{
    NumberSetting ping;
    
    public TntRunPing() {
        super("TNT Run ping", 0, Category.OTHER);
        this.ping = new NumberSetting("Ping", 2000.0, 1.0, 2000.0, 1.0);
        this.addSettings(this.ping);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketReceivedEvent event) {
        if (this.isToggled()) {
            return;
        }
        try {
            final ScoreObjective objective = OringoClient.mc.player.getWorldScoreboard().getObjectiveInDisplaySlot(1);
            if (!Arrays.asList("TNT RUN", "PVP RUN").contains(ChatFormatting.stripFormatting(objective.getDisplayName()))) {
                return;
            }
        }
        catch (Exception e) {
            return;
        }
        if (event.packet instanceof S22PacketMultiBlockChange && ((S22PacketMultiBlockChange)event.packet).getChangedBlocks().length <= 10) {
            event.setCanceled(true);
            for (final S22PacketMultiBlockChange.BlockUpdateData changedBlock : ((S22PacketMultiBlockChange)event.packet).getChangedBlocks()) {
                this.threadBreak(event.context, changedBlock.getPos(), changedBlock.getBlockState());
            }
        }
        if (event.packet instanceof S23PacketBlockChange) {
            if (OringoClient.stop.contains(((S23PacketBlockChange)event.packet).getBlockPosition())) {
                event.setCanceled(true);
            }
            if (!Minecraft.getMinecraft().world.getBlockState(((S23PacketBlockChange)event.packet).getBlockPosition()).getBlock().equals(Blocks.WOOL) && ((S23PacketBlockChange)event.packet).getBlockState().getBlock().equals(Blocks.AIR)) {
                event.setCanceled(true);
                this.threadBreak(event.context, ((S23PacketBlockChange)event.packet).getBlockPosition(), ((S23PacketBlockChange)event.packet).getBlockState());
            }
        }
    }
    
    private void threadBreak(final ChannelHandlerContext context, final BlockPos pos, final IBlockState state) {
        if (this.isToggled()) {
            return;
        }
        Minecraft.getMinecraft().world.setBlockState(pos, Blocks.WOOL.getDefaultState());
        int i;
        new Thread(() -> {
            OringoClient.stop.add(pos);
            for (i = 0; i < 10; ++i) {
                try {
                    Thread.sleep((long)((long)this.ping.getValue() / 10.0));
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    OringoClient.mc.getConnection().handleBlockBreakAnim(new S25PacketBlockBreakAnim(pos.hashCode(), pos, i));
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            OringoClient.stop.remove(pos);
            Minecraft.getMinecraft().world.setBlockState(pos, state);
        }).start();
    }
}
