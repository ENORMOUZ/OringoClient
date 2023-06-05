//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class NoRotate extends Module
{
    public BooleanSetting keepMotion;
    public BooleanSetting pitch;
    private boolean doneLoadingTerrain;
    
    public NoRotate() {
        super("No Rotate", 0, Category.PLAYER);
        this.keepMotion = new BooleanSetting("Keep motion", true);
        this.pitch = new BooleanSetting("0 pitch", false);
        this.addSettings(this.keepMotion, this.pitch);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketReceivedEvent event) {
        if (event.packet instanceof S08PacketPlayerPosLook) {
            if (this.isToggled() && OringoClient.mc.player != null && (((S08PacketPlayerPosLook)event.packet).getPitch() != 0.0 || this.pitch.isEnabled())) {
                event.setCanceled(true);
                final EntityPlayer entityplayer = (EntityPlayer)OringoClient.mc.player;
                double d0 = ((S08PacketPlayerPosLook)event.packet).getX();
                double d2 = ((S08PacketPlayerPosLook)event.packet).getY();
                double d3 = ((S08PacketPlayerPosLook)event.packet).getZ();
                float f = ((S08PacketPlayerPosLook)event.packet).getYaw();
                float f2 = ((S08PacketPlayerPosLook)event.packet).getPitch();
                if (((S08PacketPlayerPosLook)event.packet).getFlags().contains(S08PacketPlayerPosLook.EnumFlags.X)) {
                    d0 += entityplayer.posX;
                }
                else if (!this.keepMotion.isEnabled()) {
                    entityplayer.motionX = 0.0;
                }
                if (((S08PacketPlayerPosLook)event.packet).getFlags().contains(S08PacketPlayerPosLook.EnumFlags.Y)) {
                    d2 += entityplayer.posY;
                }
                else {
                    entityplayer.motionY = 0.0;
                }
                if (((S08PacketPlayerPosLook)event.packet).getFlags().contains(S08PacketPlayerPosLook.EnumFlags.Z)) {
                    d3 += entityplayer.posZ;
                }
                else if (!this.keepMotion.isEnabled()) {
                    entityplayer.motionZ = 0.0;
                }
                if (((S08PacketPlayerPosLook)event.packet).getFlags().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT)) {
                    f2 += entityplayer.rotationPitch;
                }
                if (((S08PacketPlayerPosLook)event.packet).getFlags().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT)) {
                    f += entityplayer.rotationYaw;
                }
                entityplayer.setPositionAndRotation(d0, d2, d3, entityplayer.rotationYaw, entityplayer.rotationPitch);
                OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(entityplayer.posX, entityplayer.getEntityBoundingBox().minY, entityplayer.posZ, f, f2, false));
                if (!this.doneLoadingTerrain) {
                    OringoClient.mc.player.prevPosX = OringoClient.mc.player.posX;
                    OringoClient.mc.player.prevPosY = OringoClient.mc.player.posY;
                    OringoClient.mc.player.prevPosZ = OringoClient.mc.player.posZ;
                    OringoClient.mc.displayGuiScreen((GuiScreen)null);
                }
            }
            this.doneLoadingTerrain = true;
        }
        if (event.packet instanceof S07PacketRespawn) {
            this.doneLoadingTerrain = false;
        }
    }
}
