//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.renderer;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.MathHelper;
import net.minecraft.item.EnumAction;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.qolfeatures.module.combat.KillAura;
import net.minecraft.item.ItemMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { ItemRenderer.class }, priority = 1)
public abstract class AnimationsMixin
{
    @Shadow
    private float field_78451_d;
    @Shadow
    private float field_78454_c;
    @Shadow
    @Final
    private Minecraft mc;
    @Shadow
    private ItemStack field_78453_b;
    
    @Shadow
    protected abstract void rotateArroundXAndY(final float p0, final float p1);
    
    @Shadow
    protected abstract void func_178109_a(final AbstractClientPlayer p0);
    
    @Shadow
    protected abstract void func_178110_a(final EntityPlayerSP p0, final float p1);
    
    @Shadow
    protected abstract void func_178097_a(final AbstractClientPlayer p0, final float p1, final float p2, final float p3);
    
    @Shadow
    protected abstract void func_178104_a(final AbstractClientPlayer p0, final float p1);
    
    @Shadow
    protected abstract void func_178105_d(final float p0);
    
    @Shadow
    public abstract void renderItem(final EntityLivingBase p0, final ItemStack p1, final ItemCameraTransforms.TransformType p2);
    
    @Shadow
    protected abstract void func_178095_a(final AbstractClientPlayer p0, final float p1, final float p2);
    
    @Shadow
    protected abstract void func_178098_a(final float p0, final AbstractClientPlayer p1);
    
    @Overwrite
    public void renderItemInFirstPerson(final float partialTicks) {
        final float f = 1.0f - (this.field_78451_d + (this.field_78454_c - this.field_78451_d) * partialTicks);
        final AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)this.mc.player;
        final float f2 = abstractclientplayer.getSwingProgress(partialTicks);
        final float f3 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        final float f4 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        this.rotateArroundXAndY(f3, f4);
        this.func_178109_a(abstractclientplayer);
        this.func_178110_a((EntityPlayerSP)abstractclientplayer, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        if (this.field_78453_b != null) {
            if (this.field_78453_b.getItem() instanceof ItemMap) {
                this.func_178097_a(abstractclientplayer, f3, f, f2);
            }
            else if (abstractclientplayer.func_71052_bv() > 0 || (KillAura.target != null && !OringoClient.killAura.blockMode.getSelected().equals("None"))) {
                EnumAction enumaction = this.field_78453_b.getItemUseAction();
                if (KillAura.target != null && !OringoClient.killAura.blockMode.getSelected().equals("None")) {
                    enumaction = EnumAction.BLOCK;
                }
                switch (enumaction) {
                    case NONE: {
                        this.func_178096_b(f, 0.0f);
                        break;
                    }
                    case EAT:
                    case DRINK: {
                        this.func_178104_a(abstractclientplayer, partialTicks);
                        this.func_178096_b(f, 0.0f);
                        break;
                    }
                    case BLOCK: {
                        if (OringoClient.animations.isToggled()) {
                            final String selected = OringoClient.animations.mode.getSelected();
                            switch (selected) {
                                case "1.7": {
                                    this.func_178096_b(f, f2);
                                    this.func_178103_d();
                                    break;
                                }
                                case "spin":
                                case "vertical spin": {
                                    this.func_178096_b(f, OringoClient.animations.showSwing.isEnabled() ? f2 : 0.0f);
                                    this.func_178103_d();
                                    break;
                                }
                                case "long hit": {
                                    this.func_178096_b(f, 0.0f);
                                    this.func_178103_d();
                                    final float var19 = MathHelper.sin(MathHelper.sqrt(f2) * 3.1415927f);
                                    GlStateManager.translate(-0.05f, 0.6f, 0.3f);
                                    GlStateManager.rotate(-var19 * 70.0f / 2.0f, -8.0f, -0.0f, 9.0f);
                                    GlStateManager.rotate(-var19 * 70.0f, 1.5f, -0.4f, -0.0f);
                                    break;
                                }
                                case "chill": {
                                    final float f5 = MathHelper.sin(MathHelper.sqrt(f2) * 3.1415927f);
                                    this.func_178096_b(f / 2.0f - 0.18f, 0.0f);
                                    GL11.glRotatef(f5 * 60.0f / 2.0f, -f5 / 2.0f, -0.0f, -16.0f);
                                    GL11.glRotatef(-f5 * 30.0f, 1.0f, f5 / 2.0f, -1.0f);
                                    this.func_178103_d();
                                    break;
                                }
                                case "push": {
                                    this.func_178096_b(f, -f2);
                                    this.func_178103_d();
                                    break;
                                }
                                case "custom": {
                                    this.func_178096_b(OringoClient.animationCreator.blockProgress.isEnabled() ? f : 0.0f, OringoClient.animationCreator.swingProgress.isEnabled() ? f2 : 0.0f);
                                    this.func_178103_d();
                                    break;
                                }
                                case "helicopter": {
                                    GlStateManager.rotate((float)(System.currentTimeMillis() / 3L % 360L), 0.0f, 0.0f, -0.1f);
                                    this.func_178096_b(f / 1.6f, 0.0f);
                                    this.func_178103_d();
                                    break;
                                }
                            }
                            break;
                        }
                        this.func_178096_b(f, 0.0f);
                        this.func_178103_d();
                        break;
                    }
                    case BOW: {
                        this.func_178096_b(f, 0.0f);
                        this.func_178098_a(partialTicks, abstractclientplayer);
                        break;
                    }
                }
            }
            else {
                this.func_178105_d(f2);
                this.func_178096_b(f, f2);
            }
            this.renderItem((EntityLivingBase)abstractclientplayer, this.field_78453_b, ItemCameraTransforms.TransformType.FIRST_PERSON);
        }
        else if (!abstractclientplayer.isInvisible()) {
            this.func_178095_a(abstractclientplayer, f, f2);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }
    
    @Overwrite
    private void func_178096_b(final float equipProgress, final float swingProgress) {
        final float size = (float)OringoClient.animations.size.getValue();
        final float x = (float)OringoClient.animations.x.getValue();
        final float y = (float)OringoClient.animations.y.getValue();
        final float z = (float)OringoClient.animations.z.getValue();
        GlStateManager.translate(0.56f * x, -0.52f * y, -0.71999997f * z);
        GlStateManager.translate(0.0f, equipProgress * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        final float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927f);
        final float f2 = MathHelper.sin(MathHelper.sqrt(swingProgress) * 3.1415927f);
        GlStateManager.rotate(f * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f2 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f2 * -80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.4f * size, 0.4f * size, 0.4f * size);
    }
    
    @Overwrite
    private void func_178103_d() {
        float angle1 = 30.0f;
        float angle2 = -80.0f;
        float angle3 = 60.0f;
        float translateX = -0.5f;
        float translateY = 0.2f;
        float translateZ = 0.0f;
        float rotation1x = 0.0f;
        float rotation1y = 1.0f;
        float rotation1z = 0.0f;
        float rotation2x = 1.0f;
        float rotation2y = 0.0f;
        float rotation2z = 0.0f;
        final String selected = OringoClient.animations.mode.getSelected();
        switch (selected) {
            case "custom": {
                angle1 = (float)OringoClient.animationCreator.angle1.getValue();
                angle2 = (float)OringoClient.animationCreator.angle2.getValue();
                angle3 = (float)OringoClient.animationCreator.angle3.getValue();
                translateX = (float)OringoClient.animationCreator.translateX.getValue();
                translateY = (float)OringoClient.animationCreator.translateY.getValue();
                translateZ = (float)OringoClient.animationCreator.translateZ.getValue();
                rotation1x = (float)OringoClient.animationCreator.rotation1x.getValue();
                rotation1y = (float)OringoClient.animationCreator.rotation1y.getValue();
                rotation1z = (float)OringoClient.animationCreator.rotation1z.getValue();
                rotation2x = (float)OringoClient.animationCreator.rotation2x.getValue();
                rotation2y = (float)OringoClient.animationCreator.rotation2y.getValue();
                rotation2z = (float)OringoClient.animationCreator.rotation2z.getValue();
                break;
            }
            case "vertical spin": {
                angle1 = (float)(System.currentTimeMillis() % 720L);
                angle1 /= 2.0f;
                rotation2x = 0.0f;
                angle2 = 0.0f;
                break;
            }
            case "spin": {
                translateY = 0.8f;
                angle1 = 60.0f;
                angle2 = (float)(-System.currentTimeMillis() % 720L);
                angle2 /= 2.0f;
                rotation2z = 0.8f;
                angle3 = 30.0f;
                break;
            }
        }
        GlStateManager.translate(translateX, translateY, translateZ);
        GlStateManager.rotate(angle1, rotation1x, rotation1y, rotation1z);
        GlStateManager.rotate(angle2, rotation2x, rotation2y, rotation2z);
        GlStateManager.rotate(angle3, 0.0f, 1.0f, 0.0f);
    }
}
