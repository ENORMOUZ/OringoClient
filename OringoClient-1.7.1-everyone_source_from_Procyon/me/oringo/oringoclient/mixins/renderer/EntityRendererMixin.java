//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.renderer;

import net.minecraft.entity.Entity;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.injection.Inject;
import me.oringo.oringoclient.OringoClient;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.potion.Potion;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityRenderer.class })
public class EntityRendererMixin
{
    @Shadow
    private float thirdPersonDistancePrev;
    @Shadow
    private float thirdPersonDistance;
    
    @Redirect(method = { "setupFog" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isPotionActive(Lnet/minecraft/potion/Potion;)Z"))
    public boolean removeBlindness(final EntityLivingBase instance, final Potion potionIn) {
        return false;
    }
    
    @Inject(method = { "hurtCameraEffect" }, at = { @At("HEAD") }, cancellable = true)
    public void hurtCam(final float entitylivingbase, final CallbackInfo ci) {
        if (OringoClient.camera.noHurtCam.isEnabled() && OringoClient.camera.isToggled()) {
            ci.cancel();
        }
    }
    
    @Redirect(method = { "orientCamera" }, at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;thirdPersonDistanceTemp:F"))
    public float thirdPersonDistanceTemp(final EntityRenderer instance) {
        return OringoClient.camera.isToggled() ? ((float)OringoClient.camera.cameraDistance.getValue()) : this.thirdPersonDistancePrev;
    }
    
    @Redirect(method = { "orientCamera" }, at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;thirdPersonDistance:F"))
    public float thirdPersonDistance(final EntityRenderer instance) {
        return OringoClient.camera.isToggled() ? ((float)OringoClient.camera.cameraDistance.getValue()) : this.thirdPersonDistance;
    }
    
    @Redirect(method = { "orientCamera" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D"))
    public double onCamera(final Vec3 instance, final Vec3 vec) {
        return (OringoClient.camera.isToggled() && OringoClient.camera.cameraClip.isEnabled()) ? OringoClient.camera.cameraDistance.getValue() : instance.distanceTo(vec);
    }
    
    @Redirect(method = { "getMouseOver" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getBlockReachDistance()F"))
    private float getBlockReachDistance(final PlayerControllerMP instance) {
        return OringoClient.reach.isToggled() ? ((float)OringoClient.reach.blockReach.getValue()) : OringoClient.mc.playerController.getBlockReachDistance();
    }
    
    @Redirect(method = { "getMouseOver" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D", ordinal = 2))
    private double distanceTo(final Vec3 instance, final Vec3 vec) {
        return (OringoClient.reach.isToggled() && instance.distanceTo(vec) + (OringoClient.hitboxes.isToggled() ? OringoClient.hitboxes.expand.getValue() : 0.0) <= OringoClient.reach.reach.getValue()) ? 2.9000000953674316 : (instance.distanceTo(vec) + (OringoClient.hitboxes.isToggled() ? OringoClient.hitboxes.expand.getValue() : 0.0));
    }
    
    @Redirect(method = { "getMouseOver" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getCollisionBorderSize()F"))
    private float getCollisionBorderSize(final Entity instance) {
        return OringoClient.hitboxes.isToggled() ? ((float)OringoClient.hitboxes.expand.getValue() + 0.1f) : 0.1f;
    }
}
