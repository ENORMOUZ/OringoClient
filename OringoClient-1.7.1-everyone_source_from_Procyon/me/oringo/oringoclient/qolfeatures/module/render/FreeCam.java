//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.render;

import net.minecraft.network.Packet;
import me.oringo.oringoclient.utils.PacketUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.oringo.oringoclient.events.PacketSentEvent;
import net.minecraftforge.event.world.WorldEvent;
import me.oringo.oringoclient.utils.RenderUtils;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import me.oringo.oringoclient.utils.MovementUtils;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class FreeCam extends Module
{
    private EntityOtherPlayerMP playerEntity;
    public NumberSetting speed;
    public BooleanSetting tracer;
    
    public FreeCam() {
        super("FreeCam", Category.RENDER);
        this.speed = new NumberSetting("Speed", 3.0, 0.1, 5.0, 0.1);
        this.tracer = new BooleanSetting("Show tracer", false);
        this.addSettings(this.speed, this.tracer);
    }
    
    @Override
    public void onEnable() {
        (this.playerEntity = new EntityOtherPlayerMP((World)OringoClient.mc.world, OringoClient.mc.player.getGameProfile())).copyLocationAndAnglesFrom((Entity)OringoClient.mc.player);
        this.playerEntity.onGround = OringoClient.mc.player.onGround;
        OringoClient.mc.world.addEntityToWorld(-2137, (Entity)this.playerEntity);
    }
    
    @Override
    public void onDisable() {
        if (OringoClient.mc.player == null || OringoClient.mc.world == null || this.playerEntity == null) {
            return;
        }
        OringoClient.mc.player.noClip = false;
        OringoClient.mc.player.setPosition(this.playerEntity.posX, this.playerEntity.posY, this.playerEntity.posZ);
        OringoClient.mc.world.removeEntityFromWorld(-2137);
        this.playerEntity = null;
        OringoClient.mc.player.setVelocity(0.0, 0.0, 0.0);
    }
    
    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (this.isToggled()) {
            OringoClient.mc.player.noClip = true;
            OringoClient.mc.player.fallDistance = 0.0f;
            OringoClient.mc.player.onGround = false;
            OringoClient.mc.player.capabilities.isFlying = false;
            OringoClient.mc.player.motionY = 0.0;
            if (!MovementUtils.isMoving()) {
                OringoClient.mc.player.motionZ = 0.0;
                OringoClient.mc.player.motionX = 0.0;
            }
            final double speed = this.speed.getValue() * 0.1;
            OringoClient.mc.player.jumpMovementFactor = (float)speed;
            if (OringoClient.mc.gameSettings.keyBindJump.isKeyDown()) {
                final EntityPlayerSP player = OringoClient.mc.player;
                player.motionY += speed * 3.0;
            }
            if (OringoClient.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final EntityPlayerSP player2 = OringoClient.mc.player;
                player2.motionY -= speed * 3.0;
            }
        }
    }
    
    @SubscribeEvent
    public void onRenderWorld(final RenderWorldLastEvent event) {
        if (this.isToggled() && this.playerEntity != null && this.tracer.isEnabled()) {
            RenderUtils.tracerLine((Entity)this.playerEntity, event.partialTicks, 1.0f, OringoClient.clickGui.getColor());
        }
    }
    
    @SubscribeEvent
    public void onWorldChange(final WorldEvent.Load event) {
        if (this.isToggled()) {
            this.toggle();
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
        if (this.isToggled() && event.packet instanceof C03PacketPlayer) {
            event.setCanceled(true);
            PacketUtils.sendPacketNoEvent((Packet<?>)new C03PacketPlayer(this.playerEntity.onGround));
        }
    }
}
