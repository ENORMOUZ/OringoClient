//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins;

import net.minecraft.util.BlockPos;
import net.minecraft.block.material.Material;
import me.oringo.oringoclient.events.LeftClickEvent;
import me.oringo.oringoclient.qolfeatures.module.player.FastPlace;
import net.minecraftforge.fml.common.eventhandler.Event;
import me.oringo.oringoclient.events.RightClickEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.oringo.oringoclient.mixins.entity.PlayerSPAccessor;
import net.minecraft.entity.EntityLivingBase;
import me.oringo.oringoclient.qolfeatures.module.combat.KillAura;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.qolfeatures.module.render.ServerRotations;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.util.Timer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MovingObjectPosition;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Minecraft.class })
public abstract class MinecraftMixin
{
    @Shadow
    private int leftClickCounter;
    @Shadow
    public MovingObjectPosition objectMouseOver;
    @Shadow
    public WorldClient world;
    @Shadow
    public PlayerControllerMP playerController;
    @Shadow
    private Entity renderViewEntity;
    @Shadow
    public EntityPlayerSP player;
    @Shadow
    public GuiScreen currentScreen;
    @Shadow
    public GameSettings gameSettings;
    @Shadow
    public boolean inGameHasFocus;
    @Shadow
    public boolean renderChunksMany;
    @Shadow
    private Timer timer;
    @Shadow
    private int rightClickDelayTimer;
    
    @Shadow
    public abstract PropertyMap getProfileProperties();
    
    @Inject(method = { "getRenderViewEntity" }, at = { @At("HEAD") })
    public void getRenderViewEntity(final CallbackInfoReturnable<Entity> cir) {
        if (!ServerRotations.getInstance().isToggled() || this.renderViewEntity == null || this.renderViewEntity != OringoClient.mc.player) {
            return;
        }
        if (!ServerRotations.getInstance().onlyKillAura.isEnabled() || KillAura.target != null) {
            ((EntityLivingBase)this.renderViewEntity).rotationYawHead = ((PlayerSPAccessor)this.renderViewEntity).getLastReportedYaw();
            ((EntityLivingBase)this.renderViewEntity).renderYawOffset = ((PlayerSPAccessor)this.renderViewEntity).getLastReportedYaw();
        }
    }
    
    @Inject(method = { "runTick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V") })
    public void keyPresses(final CallbackInfo ci) {
        final int k = (Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + '\u0100') : Keyboard.getEventKey();
        if (OringoClient.mc.currentScreen == null && Keyboard.getEventKeyState()) {
            OringoClient.handleKeypress(k);
        }
    }
    
    @Inject(method = { "rightClickMouse" }, at = { @At("HEAD") }, cancellable = true)
    public void onRightClick(final CallbackInfo callbackInfo) {
        if (MinecraftForge.EVENT_BUS.post((Event)new RightClickEvent())) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "rightClickMouse" }, at = { @At("RETURN") }, cancellable = true)
    public void onRightClickPost(final CallbackInfo callbackInfo) {
        if (FastPlace.getInstance().isToggled()) {
            this.rightClickDelayTimer = (int)FastPlace.getInstance().placeDelay.getValue();
        }
    }
    
    @Inject(method = { "clickMouse" }, at = { @At("HEAD") }, cancellable = true)
    public void onClick(final CallbackInfo callbackInfo) {
        if (MinecraftForge.EVENT_BUS.post((Event)new LeftClickEvent())) {
            callbackInfo.cancel();
        }
        if (OringoClient.noHitDelay.isToggled() || OringoClient.mithrilMacro.isToggled()) {
            this.leftClickCounter = 0;
        }
    }
    
    @Inject(method = { "sendClickBlockToController" }, at = { @At("RETURN") })
    public void sendClickBlock(final CallbackInfo callbackInfo) {
        final boolean click = this.currentScreen == null && this.gameSettings.keyBindAttack.isKeyDown() && this.inGameHasFocus;
        if (OringoClient.fastBreak.isToggled() && click && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            for (int i = 0; i < OringoClient.fastBreak.maxBlocks.getValue(); ++i) {
                final BlockPos prevBlockPos = this.objectMouseOver.getBlockPos();
                this.objectMouseOver = this.renderViewEntity.rayTrace((double)this.playerController.getBlockReachDistance(), 1.0f);
                final BlockPos blockpos = this.objectMouseOver.getBlockPos();
                if (this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || blockpos == prevBlockPos || this.world.getBlockState(blockpos).getBlock().getMaterial() == Material.AIR) {
                    break;
                }
                this.player.func_71038_i();
                this.playerController.clickBlock(blockpos, this.objectMouseOver.sideHit);
            }
        }
    }
}
