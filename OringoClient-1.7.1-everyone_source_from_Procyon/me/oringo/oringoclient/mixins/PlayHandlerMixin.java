//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.entity.EntityPlayerSP;
import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.util.IThreadListener;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import me.oringo.oringoclient.OringoClient;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.network.NetworkManager;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ NetHandlerPlayClient.class })
public abstract class PlayHandlerMixin
{
    @Shadow
    private Minecraft client;
    @Shadow
    private WorldClient world;
    @Shadow
    private boolean doneLoadingTerrain;
    @Shadow
    @Final
    private NetworkManager netManager;
    
    @Inject(method = { "handleExplosion" }, at = { @At("HEAD") }, cancellable = true)
    private void handleExplosion(final S27PacketExplosion packetIn, final CallbackInfo ci) {
        if (OringoClient.velocity.isToggled()) {
            PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)OringoClient.mc.getConnection(), (IThreadListener)this.client);
            final Explosion explosion = new Explosion((World)this.client.world, (Entity)null, packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getStrength(), packetIn.getAffectedBlockPositions());
            explosion.doExplosionB(true);
            final boolean shouldTakeKB = OringoClient.velocity.skyblockKB.isEnabled() && (Minecraft.getMinecraft().player.isInLava() || SkyblockUtils.getDisplayName(Minecraft.getMinecraft().player.func_70694_bm()).contains("Bonzo's Staff") || SkyblockUtils.getDisplayName(Minecraft.getMinecraft().player.func_70694_bm()).contains("Jerry-chine Gun"));
            if (shouldTakeKB || OringoClient.velocity.hModifier.getValue() != 0.0 || OringoClient.velocity.vModifier.getValue() != 0.0) {
                final EntityPlayerSP player = this.client.player;
                player.motionX += packetIn.getMotionX() * (shouldTakeKB ? 1.0 : OringoClient.velocity.hModifier.getValue());
                final EntityPlayerSP player2 = this.client.player;
                player2.motionY += packetIn.getMotionY() * (shouldTakeKB ? 1.0 : OringoClient.velocity.vModifier.getValue());
                final EntityPlayerSP player3 = this.client.player;
                player3.motionZ += packetIn.getMotionZ() * (shouldTakeKB ? 1.0 : OringoClient.velocity.hModifier.getValue());
            }
            ci.cancel();
        }
    }
    
    @Inject(method = { "handleEntityVelocity" }, at = { @At("HEAD") }, cancellable = true)
    public void handleEntityVelocity(final S12PacketEntityVelocity packetIn, final CallbackInfo ci) {
        if (OringoClient.velocity.isToggled()) {
            PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)OringoClient.mc.getConnection(), (IThreadListener)this.client);
            final Entity entity = this.world.getEntityByID(packetIn.getEntityID());
            if (entity != null) {
                if (entity.equals((Object)OringoClient.mc.player)) {
                    final boolean shouldTakeKB = OringoClient.velocity.skyblockKB.isEnabled() && (Minecraft.getMinecraft().player.isInLava() || SkyblockUtils.getDisplayName(Minecraft.getMinecraft().player.func_70694_bm()).contains("Bonzo's Staff") || SkyblockUtils.getDisplayName(Minecraft.getMinecraft().player.func_70694_bm()).contains("Jerry-chine Gun"));
                    if (shouldTakeKB || OringoClient.velocity.hModifier.getValue() != 0.0 || OringoClient.velocity.vModifier.getValue() != 0.0) {
                        entity.setVelocity(packetIn.getMotionX() * (shouldTakeKB ? 1.0 : OringoClient.velocity.hModifier.getValue()) / 8000.0, packetIn.getMotionY() * (shouldTakeKB ? 1.0 : OringoClient.velocity.vModifier.getValue()) / 8000.0, packetIn.getMotionZ() * (shouldTakeKB ? 1.0 : OringoClient.velocity.hModifier.getValue()) / 8000.0);
                    }
                }
                else {
                    entity.setVelocity(packetIn.getMotionX() / 8000.0, packetIn.getMotionY() / 8000.0, packetIn.getMotionZ() / 8000.0);
                }
            }
            ci.cancel();
        }
    }
}
