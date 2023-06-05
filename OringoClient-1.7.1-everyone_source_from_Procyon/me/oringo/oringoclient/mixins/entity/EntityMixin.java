//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.entity;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import java.util.Random;
import org.spongepowered.asm.mixin.Shadow;
import java.util.UUID;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Entity.class })
public abstract class EntityMixin
{
    @Shadow
    protected UUID entityUniqueID;
    @Shadow
    public double lastTickPosX;
    @Shadow
    protected Random rand;
    @Shadow
    public int field_70174_ab;
    @Shadow
    public World world;
    @Shadow
    protected boolean inPortal;
    @Shadow
    public float entityCollisionReduction;
    @Shadow
    public float rotationPitch;
    @Shadow
    public int ticksExisted;
    @Shadow
    public boolean noClip;
    @Shadow
    public Entity field_70154_o;
    @Shadow
    public boolean onGround;
    @Shadow
    public float fallDistance;
    @Shadow
    public float rotationYaw;
    @Shadow
    public boolean isAirBorne;
    @Shadow
    public double motionX;
    @Shadow
    public double motionY;
    @Shadow
    public double motionZ;
    @Shadow
    private int field_70151_c;
    @Shadow
    public float distanceWalkedModified;
    @Shadow
    public float distanceWalkedOnStepModified;
    @Shadow
    private int nextStepDistance;
    @Shadow
    public double posX;
    @Shadow
    public double posY;
    @Shadow
    public double posZ;
    @Shadow
    public boolean collided;
    @Shadow
    public boolean collidedHorizontally;
    @Shadow
    public boolean collidedVertically;
    @Shadow
    public float stepHeight;
    @Shadow
    protected boolean isInWeb;
    
    @Shadow
    public abstract boolean isEntityInsideOpaqueBlock();
    
    @Shadow
    public abstract void extinguish();
    
    @Shadow
    public abstract void setFire(final int p0);
    
    @Shadow
    public abstract boolean isWet();
    
    @Shadow
    protected abstract void dealFireDamage(final int p0);
    
    @Shadow
    public abstract AxisAlignedBB getEntityBoundingBox();
    
    @Shadow
    public abstract void func_70060_a(final float p0, final float p1, final float p2);
    
    @Shadow
    public abstract UUID getUniqueID();
    
    @Shadow
    @Override
    public abstract boolean equals(final Object p0);
    
    @Shadow
    public abstract boolean isInWater();
    
    @Shadow
    public void move(final double x, final double y, final double z) {
    }
    
    @Shadow
    public abstract boolean isSprinting();
    
    @Shadow
    protected abstract boolean getFlag(final int p0);
    
    @Shadow
    public abstract void addEntityCrashInfo(final CrashReportCategory p0);
    
    @Shadow
    protected abstract void doBlockCollisions();
    
    @Shadow
    protected abstract void playStepSound(final BlockPos p0, final Block p1);
    
    @Shadow
    public abstract void setEntityBoundingBox(final AxisAlignedBB p0);
}
