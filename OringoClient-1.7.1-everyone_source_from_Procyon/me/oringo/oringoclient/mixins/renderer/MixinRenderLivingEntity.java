//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.renderer;

import me.oringo.oringoclient.events.RenderLayersEvent;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import me.oringo.oringoclient.events.RenderModelEvent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Final;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RendererLivingEntity.class })
public abstract class MixinRenderLivingEntity extends RenderMixin
{
    @Shadow
    protected ModelBase mainModel;
    @Shadow
    protected boolean field_177098_i;
    @Shadow
    @Final
    private static Logger LOGGER;
    
    @Shadow
    protected abstract <T extends EntityLivingBase> float getSwingProgress(final T p0, final float p1);
    
    @Shadow
    protected abstract float interpolateRotation(final float p0, final float p1, final float p2);
    
    @Shadow
    protected abstract <T extends EntityLivingBase> void renderLivingAt(final T p0, final double p1, final double p2, final double p3);
    
    @Shadow
    protected abstract <T extends EntityLivingBase> float handleRotationFloat(final T p0, final float p1);
    
    @Shadow
    protected abstract <T extends EntityLivingBase> void applyRotations(final T p0, final float p1, final float p2, final float p3);
    
    @Shadow
    protected abstract <T extends EntityLivingBase> void preRenderCallback(final T p0, final float p1);
    
    @Shadow
    protected abstract <T extends EntityLivingBase> boolean setScoreTeamColor(final T p0);
    
    @Shadow
    protected abstract void unsetScoreTeamColor();
    
    @Shadow
    protected abstract <T extends EntityLivingBase> boolean setDoRenderBrightness(final T p0, final float p1);
    
    @Shadow
    @Override
    public abstract void doRender(final Entity p0, final double p1, final double p2, final double p3, final float p4, final float p5);
    
    @Shadow
    protected abstract void unsetBrightness();
    
    @Shadow
    protected abstract <T extends EntityLivingBase> void renderModel(final T p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6);
    
    @Shadow
    protected abstract <T extends EntityLivingBase> void renderLayers(final T p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7);
    
    @Inject(method = { "renderModel" }, at = { @At("HEAD") }, cancellable = true)
    private <T extends EntityLivingBase> void renderModelPre(final T entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post((Event)new RenderModelEvent(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, this.mainModel))) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "renderLayers" }, at = { @At("RETURN") }, cancellable = true)
    protected void renderLayersPost(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleIn, final CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post((Event)new RenderLayersEvent(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleIn, this.mainModel))) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "renderModel" }, at = { @At("RETURN") }, cancellable = true)
    private <T extends EntityLivingBase> void renderModelPost(final T entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post((Event)new RenderModelEvent.Post(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, this.mainModel))) {
            ci.cancel();
        }
    }
}
