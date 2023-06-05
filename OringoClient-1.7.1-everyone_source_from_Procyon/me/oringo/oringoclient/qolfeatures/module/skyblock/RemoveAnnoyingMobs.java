//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.skyblock;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import me.oringo.oringoclient.OringoClient;
import net.minecraftforge.event.entity.living.LivingEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class RemoveAnnoyingMobs extends Module
{
    private Entity golemEntity;
    public static ArrayList<Entity> seraphs;
    public BooleanSetting hidePlayers;
    
    public RemoveAnnoyingMobs() {
        super("Remove Mobs", 0, Category.SKYBLOCK);
        this.hidePlayers = new BooleanSetting("Hide players", false);
        this.addSettings(this.hidePlayers);
    }
    
    @SubscribeEvent
    public void onUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (OringoClient.mc.world == null || OringoClient.mc.player == null) {
            return;
        }
        if (this.isToggled()) {
            if (event.entity instanceof EntityPlayer && !event.entity.equals((Object)Minecraft.getMinecraft().player) && this.golemEntity != null && !this.golemEntity.isDead && this.golemEntity.getDistance(event.entity) < 9.0f) {
                event.entity.posY = 999999.0;
                event.entity.lastTickPosY = 999999.0;
                event.setCanceled(true);
            }
            if (!(event.entity instanceof EntityArmorStand) && !(event.entity instanceof EntityEnderman) && !(event.entity instanceof EntityGuardian) && !(event.entity instanceof EntityFallingBlock) && !event.entity.equals((Object)Minecraft.getMinecraft().player)) {
                for (final Entity seraph : RemoveAnnoyingMobs.seraphs) {
                    if (event.entity.getDistance(seraph) < 5.0f) {
                        event.entity.posY = 999999.0;
                        event.entity.lastTickPosY = 999999.0;
                        event.setCanceled(true);
                    }
                }
            }
            if (event.entity instanceof EntityOtherPlayerMP && this.hidePlayers.isEnabled()) {
                event.entity.posY = 999999.0;
                event.entity.lastTickPosY = 999999.0;
                event.setCanceled(true);
            }
            if (event.entity.getDisplayName().getFormattedText().contains("Endstone Protector")) {
                this.golemEntity = event.entity;
            }
            if (event.entity instanceof EntityCreeper && event.entity.isInvisible() && ((EntityCreeper)event.entity).getHealth() == 20.0f) {
                OringoClient.mc.world.removeEntity(event.entity);
            }
            if (event.entity instanceof EntityCreeper && event.entity.isInvisible() && ((EntityCreeper)event.entity).getHealth() != 20.0f) {
                event.entity.setInvisible(false);
            }
        }
    }
    
    @SubscribeEvent
    public void onWorldRender(final TickEvent.ClientTickEvent event) {
        RemoveAnnoyingMobs.seraphs.clear();
        if (!this.isToggled() || OringoClient.mc.world == null) {
            return;
        }
        for (final Entity entity : OringoClient.mc.world.getLoadedEntityList()) {
            if (entity.getDisplayName().getFormattedText().contains("Voidgloom Seraph")) {
                RemoveAnnoyingMobs.seraphs.add(entity);
            }
            if (entity instanceof EntityFireworkRocket) {
                OringoClient.mc.world.removeEntity(entity);
            }
            if (entity instanceof EntityHorse) {
                OringoClient.mc.world.removeEntity(entity);
            }
        }
    }
    
    static {
        RemoveAnnoyingMobs.seraphs = new ArrayList<Entity>();
    }
}
