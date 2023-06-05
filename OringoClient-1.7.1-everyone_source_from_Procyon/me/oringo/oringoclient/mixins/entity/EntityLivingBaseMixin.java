//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.entity;

import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityLivingBase.class })
public abstract class EntityLivingBaseMixin extends EntityMixin
{
    @Shadow
    public float rotationYawHead;
    @Shadow
    private int jumpTicks;
    @Shadow
    protected boolean isJumping;
    @Shadow
    public float jumpMovementFactor;
    @Shadow
    protected int newPosRotationIncrements;
    @Shadow
    public float moveVertical;
    @Shadow
    public float moveStrafing;
    @Shadow
    protected float movedDistance;
    @Shadow
    protected int idleTime;
    @Shadow
    protected double interpTargetPitch;
    
    @Shadow
    protected abstract float getJumpUpwardsMotion();
    
    @Shadow
    public abstract boolean func_82165_m(final int p0);
    
    @Shadow
    public abstract PotionEffect getActivePotionEffect(final Potion p0);
    
    @Shadow
    protected abstract void jump();
    
    @Shadow
    public abstract IAttributeInstance getEntityAttribute(final IAttribute p0);
    
    @Shadow
    public abstract float getHealth();
    
    @Shadow
    public abstract boolean isOnLadder();
    
    @Shadow
    public abstract boolean isPotionActive(final Potion p0);
    
    @Shadow
    public abstract void setLastAttackedEntity(final Entity p0);
    
    @Shadow
    public abstract float getSwingProgress(final float p0);
    
    @Shadow
    protected abstract void func_180433_a(final double p0, final boolean p1, final Block p2, final BlockPos p3);
    
    @Shadow
    protected abstract void resetPotionEffectMetadata();
    
    public void setJumpTicks(final int jumpTicks) {
        this.jumpTicks = jumpTicks;
    }
    
    public int getJumpTicks() {
        return this.jumpTicks;
    }
}
