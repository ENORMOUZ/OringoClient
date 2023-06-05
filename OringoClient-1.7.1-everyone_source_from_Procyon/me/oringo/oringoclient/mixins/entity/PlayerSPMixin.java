//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.entity;

import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.stats.AchievementList;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import me.oringo.oringoclient.events.MoveEvent;
import me.oringo.oringoclient.events.PlayerUpdateEvent;
import net.minecraft.item.EnumAction;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.stats.StatList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.ForgeHooks;
import net.minecraft.util.MathHelper;
import me.oringo.oringoclient.qolfeatures.module.combat.KillAura;
import me.oringo.oringoclient.utils.MovementUtils;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Overwrite;
import me.oringo.oringoclient.utils.RotationUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import net.minecraft.stats.StatBase;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.util.MovementInput;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { EntityPlayerSP.class }, priority = 1)
public abstract class PlayerSPMixin extends AbstractClientPlayerMixin
{
    @Shadow
    public MovementInput movementInput;
    @Shadow
    @Final
    public NetHandlerPlayClient connection;
    @Shadow
    public float timeInPortal;
    @Shadow
    public float prevRenderArmPitch;
    @Shadow
    public float prevRenderArmYaw;
    @Shadow
    public float renderArmPitch;
    @Shadow
    public float renderArmYaw;
    @Shadow
    private boolean serverSprintState;
    @Shadow
    private float lastReportedPitch;
    @Shadow
    private double lastReportedPosX;
    @Shadow
    private double lastReportedPosY;
    @Shadow
    private double lastReportedPosZ;
    @Shadow
    private float lastReportedYaw;
    @Shadow
    private int positionUpdateTicks;
    @Shadow
    private boolean serverSneakState;
    
    @Shadow
    public abstract void setSprinting(final boolean p0);
    
    @Shadow
    public abstract boolean isSneaking();
    
    @Shadow
    public abstract void onCriticalHit(final Entity p0);
    
    @Shadow
    public abstract void onEnchantmentCritical(final Entity p0);
    
    @Shadow
    public abstract void addStat(final StatBase p0, final int p1);
    
    @Shadow
    protected abstract boolean isCurrentViewEntity();
    
    @Shadow
    public abstract void func_85030_a(final String p0, final float p1, final float p2);
    
    @Overwrite
    public void onUpdateWalkingPlayer() {
        final MotionUpdateEvent event = new MotionUpdateEvent.Pre(this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround, this.isSprinting(), this.isSneaking());
        if (MinecraftForge.EVENT_BUS.post((Event)event)) {
            return;
        }
        final boolean flag = event.sprinting;
        if (flag != this.serverSprintState) {
            if (flag) {
                this.connection.sendPacket((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.START_SPRINTING));
            }
            else {
                this.connection.sendPacket((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            this.serverSprintState = flag;
        }
        final boolean flag2 = event.sneaking;
        if (flag2 != this.serverSneakState) {
            if (flag2) {
                this.connection.sendPacket((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            else {
                this.connection.sendPacket((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
            this.serverSneakState = flag2;
        }
        if (this.isCurrentViewEntity()) {
            final double d0 = event.x - this.lastReportedPosX;
            final double d2 = event.y - this.lastReportedPosY;
            final double d3 = event.z - this.lastReportedPosZ;
            final double d4 = event.yaw - this.lastReportedYaw;
            final double d5 = event.pitch - this.lastReportedPitch;
            boolean flag3 = d0 * d0 + d2 * d2 + d3 * d3 > 9.0E-4 || this.positionUpdateTicks >= 20;
            final boolean flag4 = d4 != 0.0 || d5 != 0.0;
            if (this.field_70154_o == null) {
                if (flag3 && flag4) {
                    this.connection.sendPacket((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(event.x, event.y, event.z, event.yaw, event.pitch, event.onGround));
                }
                else if (flag3) {
                    this.connection.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(event.x, event.y, event.z, event.onGround));
                }
                else if (flag4) {
                    this.connection.sendPacket((Packet)new C03PacketPlayer.C05PacketPlayerLook(event.yaw, event.pitch, event.onGround));
                }
                else {
                    this.connection.sendPacket((Packet)new C03PacketPlayer(event.onGround));
                }
            }
            else {
                this.connection.sendPacket((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0, this.motionZ, event.yaw, event.pitch, event.onGround));
                flag3 = false;
            }
            ++this.positionUpdateTicks;
            if (flag3) {
                this.lastReportedPosX = event.x;
                this.lastReportedPosY = event.y;
                this.lastReportedPosZ = event.z;
                this.positionUpdateTicks = 0;
            }
            RotationUtils.lastReportedPitch = this.lastReportedPitch;
            if (flag4) {
                this.lastReportedYaw = event.yaw;
                this.lastReportedPitch = event.pitch;
            }
        }
        MinecraftForge.EVENT_BUS.post((Event)new MotionUpdateEvent.Post(event));
    }
    
    public void jump() {
        this.motionY = this.getJumpUpwardsMotion();
        if (this.func_82165_m(Potion.JUMP_BOOST.field_76415_H)) {
            this.motionY += (this.getActivePotionEffect(Potion.JUMP_BOOST).getAmplifier() + 1) * 0.1f;
        }
        if (this.isSprinting()) {
            final float f = ((OringoClient.sprint.isToggled() && OringoClient.sprint.omni.isEnabled()) ? MovementUtils.getYaw() : ((OringoClient.killAura.isToggled() && KillAura.target != null && OringoClient.killAura.movementFix.isEnabled()) ? RotationUtils.getAngles((Entity)KillAura.target)[0] : this.rotationYaw)) * 0.017453292f;
            this.motionX -= MathHelper.sin(f) * 0.2f;
            this.motionZ += MathHelper.cos(f) * 0.2f;
        }
        this.isAirBorne = true;
        ForgeHooks.onLivingJump((EntityLivingBase)this);
        this.addStat(StatList.JUMP);
        if (this.isSprinting()) {
            this.addExhaustion(0.8f);
        }
        else {
            this.addExhaustion(0.2f);
        }
    }
    
    @Override
    public void func_70060_a(float strafe, float forward, final float friction) {
        float f = strafe * strafe + forward * forward;
        if (f >= 1.0E-4f) {
            f = MathHelper.sqrt(f);
            if (f < 1.0f) {
                f = 1.0f;
            }
            f = friction / f;
            strafe *= f;
            forward *= f;
            final float yaw = (OringoClient.killAura.isToggled() && KillAura.target != null && OringoClient.killAura.movementFix.isEnabled()) ? RotationUtils.getAngles((Entity)KillAura.target)[0] : this.rotationYaw;
            final float f2 = MathHelper.sin(yaw * 3.1415927f / 180.0f);
            final float f3 = MathHelper.cos(yaw * 3.1415927f / 180.0f);
            this.motionX += strafe * f3 - forward * f2;
            this.motionZ += forward * f3 + strafe * f2;
        }
    }
    
    @Inject(method = { "pushOutOfBlocks" }, at = { @At("HEAD") }, cancellable = true)
    public void pushOutOfBlocks(final double d2, final double f, final double blockpos, final CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
    
    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isUsingItem()Z"))
    public boolean isUsingItem(final EntityPlayerSP instance) {
        return (!OringoClient.noSlow.isToggled() || !instance.func_71039_bw()) && instance.func_71039_bw();
    }
    
    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }
    
    @Inject(method = { "onLivingUpdate" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;onLivingUpdate()V") }, cancellable = true)
    public void onLivingUpdate(final CallbackInfo ci) {
        if (OringoClient.sprint.omni.isEnabled() && OringoClient.sprint.isToggled()) {
            if (!MovementUtils.isMoving() || this.isSneaking() || (this.getFoodStats().getFoodLevel() <= 6.0f && !this.capabilities.allowFlying)) {
                if (this.isSprinting()) {
                    this.setSprinting(false);
                }
                return;
            }
            if (!this.isSprinting()) {
                this.setSprinting(true);
            }
        }
        if (OringoClient.noSlow.isToggled() && this.func_71039_bw()) {
            if (this.func_71011_bu().getItem().getItemUseAction(this.func_71011_bu()) == EnumAction.BLOCK) {
                final MovementInput movementInput = this.movementInput;
                movementInput.field_78900_b *= (float)OringoClient.noSlow.swordSlowdown.getValue();
                final MovementInput movementInput2 = this.movementInput;
                movementInput2.moveStrafe *= (float)OringoClient.noSlow.swordSlowdown.getValue();
            }
            else if (this.func_71011_bu().getItem().getItemUseAction(this.func_71011_bu()) == EnumAction.BOW) {
                final MovementInput movementInput3 = this.movementInput;
                movementInput3.field_78900_b *= (float)OringoClient.noSlow.bowSlowdown.getValue();
                final MovementInput movementInput4 = this.movementInput;
                movementInput4.moveStrafe *= (float)OringoClient.noSlow.bowSlowdown.getValue();
            }
            else if (this.func_71011_bu().getItem().getItemUseAction(this.func_71011_bu()) != EnumAction.NONE) {
                final MovementInput movementInput5 = this.movementInput;
                movementInput5.field_78900_b *= (float)OringoClient.noSlow.eatingSlowdown.getValue();
                final MovementInput movementInput6 = this.movementInput;
                movementInput6.moveStrafe *= (float)OringoClient.noSlow.eatingSlowdown.getValue();
            }
        }
        if (OringoClient.freeCam.isToggled()) {
            this.noClip = true;
        }
        if (MinecraftForge.EVENT_BUS.post((Event)new PlayerUpdateEvent())) {
            ci.cancel();
        }
    }
    
    @Override
    public void move(double x, double y, double z) {
        final MoveEvent event = new MoveEvent(x, y, z);
        if (MinecraftForge.EVENT_BUS.post((Event)event)) {
            return;
        }
        x = event.x;
        y = event.y;
        z = event.z;
        super.move(x, y, z);
    }
    
    @Override
    public void attackTargetEntityWithCurrentItem(final Entity targetEntity) {
        if (ForgeHooks.onPlayerAttackTarget((EntityPlayer)this, targetEntity) && targetEntity.canBeAttackedWithItem() && !targetEntity.hitByEntity((Entity)this)) {
            float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
            int i = 0;
            float f2 = 0.0f;
            if (targetEntity instanceof EntityLivingBase) {
                f2 = EnchantmentHelper.getModifierForCreature(this.func_70694_bm(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
            }
            else {
                f2 = EnchantmentHelper.getModifierForCreature(this.func_70694_bm(), EnumCreatureAttribute.UNDEFINED);
            }
            i += EnchantmentHelper.getKnockbackModifier((EntityLivingBase)this);
            if (this.isSprinting()) {
                ++i;
            }
            if (f > 0.0f || f2 > 0.0f) {
                final boolean flag = this.fallDistance > 0.0f && !this.onGround && !this.isOnLadder() && !this.isInWater() && !this.isPotionActive(Potion.BLINDNESS) && this.field_70154_o == null && targetEntity instanceof EntityLivingBase;
                if (flag && f > 0.0f) {
                    f *= 1.5f;
                }
                f += f2;
                boolean flag2 = false;
                final int j = EnchantmentHelper.getFireAspectModifier((EntityLivingBase)this);
                if (targetEntity instanceof EntityLivingBase && j > 0 && !targetEntity.isBurning()) {
                    flag2 = true;
                    targetEntity.setFire(1);
                }
                final double d0 = targetEntity.motionX;
                final double d2 = targetEntity.motionY;
                final double d3 = targetEntity.motionZ;
                final boolean flag3 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)this), f);
                if (flag3) {
                    if (i > 0) {
                        targetEntity.addVelocity((double)(-MathHelper.sin(this.rotationYaw * 3.1415927f / 180.0f) * i * 0.5f), 0.1, (double)(MathHelper.cos(this.rotationYaw * 3.1415927f / 180.0f) * i * 0.5f));
                        if (OringoClient.sprint.isToggled() && OringoClient.sprint.keep.isEnabled()) {
                            if (this.isSprinting()) {
                                OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C0BPacketEntityAction((Entity)OringoClient.mc.player, C0BPacketEntityAction.Action.STOP_SPRINTING));
                                OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C0BPacketEntityAction((Entity)OringoClient.mc.player, C0BPacketEntityAction.Action.START_SPRINTING));
                            }
                        }
                        else {
                            this.motionX *= 0.6;
                            this.motionZ *= 0.6;
                            this.setSprinting(false);
                        }
                    }
                    if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
                        ((EntityPlayerMP)targetEntity).connection.sendPacket((Packet)new S12PacketEntityVelocity(targetEntity));
                        targetEntity.velocityChanged = false;
                        targetEntity.motionX = d0;
                        targetEntity.motionY = d2;
                        targetEntity.motionZ = d3;
                    }
                    if (flag) {
                        this.onCriticalHit(targetEntity);
                    }
                    if (f2 > 0.0f) {
                        this.onEnchantmentCritical(targetEntity);
                    }
                    if (f >= 18.0f) {
                        this.addStat((StatBase)AchievementList.field_75999_E);
                    }
                    this.setLastAttackedEntity(targetEntity);
                    if (targetEntity instanceof EntityLivingBase) {
                        EnchantmentHelper.applyThornEnchantments((EntityLivingBase)targetEntity, (Entity)this);
                    }
                    EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)this, targetEntity);
                    final ItemStack itemstack = this.func_71045_bC();
                    Entity entity = targetEntity;
                    if (targetEntity instanceof EntityDragonPart) {
                        final IEntityMultiPart ientitymultipart = ((EntityDragonPart)targetEntity).parent;
                        if (ientitymultipart instanceof EntityLivingBase) {
                            entity = (Entity)ientitymultipart;
                        }
                    }
                    if (itemstack != null && entity instanceof EntityLivingBase) {
                        itemstack.hitEntity((EntityLivingBase)entity, (EntityPlayer)this);
                        if (itemstack.stackSize <= 0) {
                            this.func_71028_bD();
                        }
                    }
                    if (targetEntity instanceof EntityLivingBase) {
                        this.addStat(StatList.field_75951_w, Math.round(f * 10.0f));
                        if (j > 0) {
                            targetEntity.setFire(j * 4);
                        }
                    }
                    this.addExhaustion(0.3f);
                }
                else if (flag2) {
                    targetEntity.extinguish();
                }
            }
        }
    }
}
