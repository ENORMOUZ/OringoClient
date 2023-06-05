//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.FoodStats;
import net.minecraft.stats.StatBase;
import net.minecraft.entity.player.InventoryPlayer;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityPlayer.class })
public abstract class PlayerMixin extends EntityLivingBaseMixin
{
    @Shadow
    public PlayerCapabilities capabilities;
    @Shadow
    private int field_71072_f;
    @Shadow
    public InventoryPlayer inventory;
    @Shadow
    protected float speedInAir;
    @Shadow
    public float experience;
    @Shadow
    public int experienceLevel;
    @Shadow
    public int experienceTotal;
    @Shadow
    public float eyeHeight;
    private boolean wasSprinting;
    
    @Shadow
    public abstract void addStat(final StatBase p0);
    
    @Shadow
    public abstract void addExhaustion(final float p0);
    
    @Shadow
    public abstract FoodStats getFoodStats();
    
    @Shadow
    public abstract void attackTargetEntityWithCurrentItem(final Entity p0);
    
    @Shadow
    public abstract ItemStack func_70694_bm();
    
    @Shadow
    public abstract ItemStack func_71045_bC();
    
    @Shadow
    public abstract void func_71028_bD();
    
    @Shadow
    protected void updateEntityActionState() {
    }
    
    @Shadow
    public abstract boolean func_71039_bw();
    
    @Shadow
    public abstract ItemStack func_71011_bu();
    
    @Shadow
    protected abstract String func_145776_H();
    
    @Shadow
    protected abstract boolean canTriggerWalking();
    
    @Shadow
    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }
}
