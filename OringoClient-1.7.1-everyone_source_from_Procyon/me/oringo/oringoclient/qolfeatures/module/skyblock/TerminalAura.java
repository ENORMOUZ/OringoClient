//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.skyblock;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.entity.player.EntityPlayer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.List;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import me.oringo.oringoclient.events.PacketSentEvent;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import me.oringo.oringoclient.utils.SkyblockUtils;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.Entity;
import java.util.ArrayList;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class TerminalAura extends Module
{
    public static ArrayList<Entity> finishedTerms;
    public static EntityArmorStand currentTerminal;
    public static long termTime;
    public static long ping;
    public static long pingAt;
    public static boolean pinged;
    public BooleanSetting onGroud;
    public NumberSetting reach;
    public NumberSetting crystalReach;
    
    public TerminalAura() {
        super("Terminal Aura", 0, Category.SKYBLOCK);
        this.onGroud = new BooleanSetting("Only ground", true);
        this.reach = new NumberSetting("Terminal Reach", 6.0, 2.0, 6.0, 0.1);
        this.crystalReach = new NumberSetting("Crystal Reach", 64.0, 3.0, 64.0, 0.1);
        this.addSettings(this.reach, this.crystalReach, this.onGroud);
    }
    
    @SubscribeEvent
    public void onTick(final MotionUpdateEvent.Post event) {
        if (OringoClient.mc.player == null || !this.isToggled() || !SkyblockUtils.inDungeon) {
            return;
        }
        if (TerminalAura.currentTerminal != null && !this.isInTerminal() && System.currentTimeMillis() - TerminalAura.termTime > TerminalAura.ping * 2L) {
            TerminalAura.finishedTerms.add((Entity)TerminalAura.currentTerminal);
            TerminalAura.currentTerminal = null;
        }
        if (OringoClient.mc.player.ticksExisted % 20 == 0 && !TerminalAura.pinged) {
            OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
            TerminalAura.pinged = true;
            TerminalAura.pingAt = System.currentTimeMillis();
        }
        if (TerminalAura.currentTerminal == null && (OringoClient.mc.player.onGround || !this.onGroud.isEnabled()) && !this.isInTerminal() && !OringoClient.mc.player.isInLava()) {
            final Iterator<Entity> iterator = this.getValidTerminals().iterator();
            if (iterator.hasNext()) {
                final Entity entity = iterator.next();
                this.openTerminal((EntityArmorStand)entity);
            }
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketReceivedEvent event) {
        if (event.packet instanceof S2EPacketCloseWindow && this.isInTerminal() && TerminalAura.currentTerminal != null) {
            this.openTerminal(TerminalAura.currentTerminal);
        }
        if (event.packet instanceof S37PacketStatistics && TerminalAura.pinged) {
            TerminalAura.pinged = false;
            TerminalAura.ping = System.currentTimeMillis() - TerminalAura.pingAt;
        }
    }
    
    @SubscribeEvent
    public void onSent(final PacketSentEvent.Post event) {
        if (event.packet instanceof C0DPacketCloseWindow && this.isInTerminal() && TerminalAura.currentTerminal != null) {
            this.openTerminal(TerminalAura.currentTerminal);
        }
    }
    
    @SubscribeEvent
    public void onWorldChange(final WorldEvent.Load event) {
        TerminalAura.finishedTerms.clear();
        TerminalAura.currentTerminal = null;
        TerminalAura.pinged = false;
        TerminalAura.termTime = System.currentTimeMillis();
        TerminalAura.ping = 300L;
        TerminalAura.pingAt = -1L;
    }
    
    private List<Entity> getValidTerminals() {
        return (List<Entity>)OringoClient.mc.world.getLoadedEntityList().stream().filter(entity -> entity instanceof EntityArmorStand).filter(entity -> entity.getName().contains("CLICK HERE")).filter(entity -> entity.getDistance((Entity)OringoClient.mc.player) < (OringoClient.mc.world.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().grow(1.0, 2.0, 1.0)).stream().anyMatch(entity1 -> entity1 instanceof EntityEnderCrystal) ? this.crystalReach.getValue() : this.reach.getValue())).filter(entity -> !TerminalAura.finishedTerms.contains(entity)).sorted(Comparator.comparingDouble((ToDoubleFunction<? super T>)OringoClient.mc.player::getDistance)).collect(Collectors.toList());
    }
    
    private void openTerminal(final EntityArmorStand entity) {
        OringoClient.mc.playerController.func_78768_b((EntityPlayer)OringoClient.mc.player, (Entity)entity);
        TerminalAura.currentTerminal = entity;
        TerminalAura.termTime = System.currentTimeMillis();
    }
    
    private boolean isInTerminal() {
        final Container container = OringoClient.mc.player.openContainer;
        String name = "";
        if (container instanceof ContainerChest) {
            name = ((ContainerChest)container).getLowerChestInventory().getName();
        }
        return container instanceof ContainerChest && (name.contains("Correct all the panes!") || name.contains("Navigate the maze!") || name.contains("Click in order!") || name.contains("What starts with:") || name.contains("Select all the"));
    }
    
    static {
        TerminalAura.finishedTerms = new ArrayList<Entity>();
        TerminalAura.termTime = -1L;
        TerminalAura.ping = 300L;
        TerminalAura.pingAt = -1L;
    }
}
