//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands;

import org.jetbrains.annotations.NotNull;
import net.minecraft.command.CommandException;
import me.oringo.oringoclient.utils.Notifications;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraft.block.properties.IProperty;
import me.oringo.oringoclient.events.BlockChangeEvent;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.server.S02PacketChat;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemHoe;
import net.minecraft.client.settings.KeyBinding;
import me.oringo.oringoclient.OringoClient;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.command.ICommand;

public class FarmingMacro implements ICommand
{
    private boolean toggled;
    private boolean left;
    private boolean direction;
    private boolean cage;
    private int ticksStanding;
    private int susPackets;
    private int pause;
    private int nukerCheck;
    
    public FarmingMacro() {
        this.ticksStanding = 0;
        this.susPackets = 0;
        this.pause = 0;
        this.nukerCheck = 0;
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent e) {
        if (OringoClient.mc.player == null || OringoClient.mc.world == null || !this.toggled || e.phase == TickEvent.Phase.END || this.cage) {
            return;
        }
        --this.pause;
        --this.nukerCheck;
        KeyBinding.setKeyBindState(OringoClient.mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(OringoClient.mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(OringoClient.mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(OringoClient.mc.gameSettings.keyBindSprint.getKeyCode(), false);
        if (OringoClient.mc.player.ticksExisted % 600 == 0) {
            this.susPackets = 0;
            OringoClient.mc.player.sendChatMessage("/setspawn");
        }
        if (OringoClient.mc.player.func_70694_bm() == null || !(OringoClient.mc.player.func_70694_bm().getItem() instanceof ItemHoe)) {
            if (this.pause < 5) {
                this.pause = 10;
            }
            else {
                if (OringoClient.mc.player.func_70694_bm() != null && OringoClient.mc.player.func_70694_bm().getItem() instanceof ItemMap) {
                    return;
                }
                if (this.pause == 5) {
                    for (final Slot slot : OringoClient.mc.player.openContainer.inventorySlots) {
                        if (slot.getHasStack() && slot.getStack().getItem() instanceof ItemHoe) {
                            this.numberClick(slot.slotNumber, OringoClient.mc.player.inventory.currentItem);
                            OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C08PacketPlayerBlockPlacement(OringoClient.mc.player.func_70694_bm()));
                        }
                    }
                }
                return;
            }
        }
        if (OringoClient.mc.world.getBlockState(new BlockPos(OringoClient.mc.player.posX, OringoClient.mc.player.posY - 0.5, OringoClient.mc.player.posZ)).getBlock() == Blocks.BEDROCK && this.pause < 1) {
            this.pause = 600;
            OringoClient.mc.player.sendChatMessage("/setguestspawn");
            new Thread(() -> {
                try {
                    Thread.sleep(1800L);
                    OringoClient.mc.player.sendChatMessage("/ac wtf");
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
            return;
        }
        if (this.pause > 0 || this.susPackets > 7 || OringoClient.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (Math.abs(OringoClient.mc.player.posX - OringoClient.mc.player.lastTickPosX) < 0.15 && Math.abs(OringoClient.mc.player.posZ - OringoClient.mc.player.lastTickPosZ) < 0.15) {
            ++this.ticksStanding;
            if (this.ticksStanding > 10) {
                this.left = !this.left;
                this.ticksStanding = 0;
            }
        }
        else if (this.ticksStanding > 0) {
            --this.ticksStanding;
        }
        if (OringoClient.mc.objectMouseOver != null && OringoClient.mc.objectMouseOver.getBlockPos() != null && (OringoClient.mc.world.getBlockState(OringoClient.mc.objectMouseOver.getBlockPos()).getBlock() instanceof BlockCrops || OringoClient.mc.world.getBlockState(OringoClient.mc.objectMouseOver.getBlockPos()).getBlock() instanceof BlockNetherWart)) {
            OringoClient.mc.player.func_71038_i();
            OringoClient.mc.playerController.clickBlock(OringoClient.mc.objectMouseOver.getBlockPos(), OringoClient.mc.objectMouseOver.sideHit);
        }
        KeyBinding.setKeyBindState(OringoClient.mc.gameSettings.keyBindForward.getKeyCode(), true);
        KeyBinding.setKeyBindState(OringoClient.mc.gameSettings.keyBindLeft.getKeyCode(), this.left);
        KeyBinding.setKeyBindState(OringoClient.mc.gameSettings.keyBindRight.getKeyCode(), !this.left);
        KeyBinding.setKeyBindState(OringoClient.mc.gameSettings.keyBindSprint.getKeyCode(), true);
    }
    
    public void numberClick(final int slot, final int button) {
        OringoClient.mc.playerController.func_78753_a(OringoClient.mc.player.inventoryContainer.windowId, slot, button, 2, (EntityPlayer)OringoClient.mc.player);
    }
    
    @SubscribeEvent(receiveCanceled = true)
    public void onPacket(final PacketReceivedEvent event) {
        if (this.toggled) {
            if (event.packet instanceof S02PacketChat && ChatFormatting.stripFormatting(((S02PacketChat)event.packet).getChatComponent().getFormattedText()).startsWith("Warped from the ")) {
                this.direction = !this.direction;
            }
            if (event.packet instanceof S27PacketExplosion || (event.packet instanceof S12PacketEntityVelocity && OringoClient.mc.world.getEntityByID(((S12PacketEntityVelocity)event.packet).getEntityID()) == OringoClient.mc.player)) {
                ++this.susPackets;
            }
            if (event.packet instanceof S08PacketPlayerPosLook) {
                this.pause = 20;
            }
        }
    }
    
    @SubscribeEvent
    public void onBlock(final BlockChangeEvent event) {
        if (this.toggled && this.pause < 1 && OringoClient.mc.world.getChunk(event.pos) != null && !(OringoClient.mc.world.getBlockState(event.pos).getBlock() instanceof BlockCrops) && !(OringoClient.mc.world.getBlockState(event.pos).getBlock() instanceof BlockNetherWart) && OringoClient.mc.player.getDistance((double)event.pos.getX(), (double)event.pos.getY(), (double)event.pos.getZ()) < 7.0 && (event.state.getBlock() instanceof BlockCrops || event.state.getBlock() instanceof BlockNetherWart) && ((event.state.getBlock() instanceof BlockCrops) ? event.state.getValue((IProperty)BlockCrops.AGE) : ((Integer)event.state.getValue((IProperty)BlockNetherWart.AGE))) == 7 && this.nukerCheck++ > 20) {
            OringoClient.sendMessageWithPrefix("Nuker check");
            this.pause = 60;
        }
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load e) {
        if (!this.toggled) {
            return;
        }
        this.toggled = false;
        KeyBinding.setKeyBindState(OringoClient.mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(OringoClient.mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(OringoClient.mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(OringoClient.mc.gameSettings.keyBindSprint.getKeyCode(), false);
        int i;
        new Thread(() -> {
            try {
                for (i = 1; i < 4; ++i) {
                    Thread.sleep(5000L);
                    if (OringoClient.mc.player != null) {
                        OringoClient.mc.player.sendChatMessage((i == 1) ? "/l" : ((i == 2) ? "/play skyblock" : "/is"));
                    }
                }
                Thread.sleep(10000L);
                this.toggled = true;
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
    
    public String getName() {
        return "farm";
    }
    
    public String getUsage(final ICommandSender sender) {
        return "/farm";
    }
    
    public List<String> getAliases() {
        return new ArrayList<String>();
    }
    
    public void func_71515_b(final ICommandSender sender, final String[] args) throws CommandException {
        this.toggled = !this.toggled;
        Notifications.showNotification("Oringo Client", this.toggled ? "Started farm!" : "Stopped farm!", 1500);
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
