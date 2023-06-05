//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import me.oringo.oringoclient.mixins.entity.PlayerSPAccessor;
import net.minecraft.util.MathHelper;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.util.Vec3;

public class RotationUtils
{
    public static float lastReportedPitch;
    
    private RotationUtils() {
    }
    
    public static float[] getAngles(final Vec3 vec) {
        final double diffX = vec.x - OringoClient.mc.player.posX;
        final double diffY = vec.y - (OringoClient.mc.player.posY + OringoClient.mc.player.getEyeHeight());
        final double diffZ = vec.z - OringoClient.mc.player.posZ;
        final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { OringoClient.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - OringoClient.mc.player.rotationYaw), OringoClient.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - OringoClient.mc.player.rotationPitch) };
    }
    
    public static float[] getServerAngles(final Vec3 vec) {
        final double diffX = vec.x - OringoClient.mc.player.posX;
        final double diffY = vec.y - (OringoClient.mc.player.posY + OringoClient.mc.player.getEyeHeight());
        final double diffZ = vec.z - OringoClient.mc.player.posZ;
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { ((PlayerSPAccessor)OringoClient.mc.player).getLastReportedYaw() + MathHelper.wrapDegrees(yaw - ((PlayerSPAccessor)OringoClient.mc.player).getLastReportedYaw()), ((PlayerSPAccessor)OringoClient.mc.player).getLastReportedPitch() + MathHelper.wrapDegrees(pitch - ((PlayerSPAccessor)OringoClient.mc.player).getLastReportedPitch()) };
    }
    
    public static float[] getBowAngles(final Entity entity) {
        final double xDelta = (entity.posX - entity.lastTickPosX) * 0.4;
        final double zDelta = (entity.posZ - entity.lastTickPosZ) * 0.4;
        double d = OringoClient.mc.player.getDistance(entity);
        d -= d % 0.8;
        final double xMulti = d / 0.8 * xDelta;
        final double zMulti = d / 0.8 * zDelta;
        final double x = entity.posX + xMulti - OringoClient.mc.player.posX;
        final double z = entity.posZ + zMulti - OringoClient.mc.player.posZ;
        final double y = OringoClient.mc.player.posY + OringoClient.mc.player.getEyeHeight() - (entity.posY + entity.getEyeHeight());
        final double dist = OringoClient.mc.player.getDistance(entity);
        final float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        final double d2 = MathHelper.sqrt(x * x + z * z);
        final float pitch = (float)(-(Math.atan2(y, d2) * 180.0 / 3.141592653589793)) + (float)dist * 0.11f;
        return new float[] { yaw, -pitch };
    }
    
    public static boolean isWithinFOV(final EntityLivingBase entity, final double fov) {
        final float yawDifference = Math.abs(getAngles((Entity)entity)[0] - OringoClient.mc.player.rotationYaw);
        return yawDifference < fov && yawDifference > -fov;
    }
    
    public static float getYawDifference(final EntityLivingBase entity1, final EntityLivingBase entity2) {
        return Math.abs(getAngles((Entity)entity1)[0] - getAngles((Entity)entity2)[0]);
    }
    
    public static float getYawDifference(final EntityLivingBase entity1) {
        return Math.abs(OringoClient.mc.player.rotationYaw - getAngles((Entity)entity1)[0]);
    }
    
    public static boolean isWithinPitch(final EntityLivingBase entity, final double pitch) {
        final float pitchDifference = Math.abs(getAngles((Entity)entity)[1] - OringoClient.mc.player.rotationPitch);
        return pitchDifference < pitch && pitchDifference > -pitch;
    }
    
    public static float[] getAngles(final Entity en) {
        return getAngles(new Vec3(en.posX, en.posY + (en.getEyeHeight() - en.height / 1.5) + 0.5, en.posZ));
    }
    
    public static float[] getServerAngles(final Entity en) {
        return getServerAngles(new Vec3(en.posX, en.posY + (en.getEyeHeight() - en.height / 1.5) + 0.5, en.posZ));
    }
}
