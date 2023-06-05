//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import me.oringo.oringoclient.OringoClient;

public class MovementUtils
{
    public static MilliTimer strafeTimer;
    
    public static float getSpeed() {
        return (float)Math.sqrt(OringoClient.mc.player.motionX * OringoClient.mc.player.motionX + OringoClient.mc.player.motionZ * OringoClient.mc.player.motionZ);
    }
    
    public static float getSpeed(final double x, final double z) {
        return (float)Math.sqrt(x * x + z * z);
    }
    
    public static void strafe(final boolean ignoreDelay) {
        strafe(getSpeed(), ignoreDelay);
    }
    
    public static boolean isMoving() {
        return OringoClient.mc.player != null && (OringoClient.mc.player.moveVertical != 0.0f || OringoClient.mc.player.moveStrafing != 0.0f);
    }
    
    public static boolean hasMotion() {
        return OringoClient.mc.player.motionX != 0.0 && OringoClient.mc.player.motionZ != 0.0 && OringoClient.mc.player.motionY != 0.0;
    }
    
    public static void strafe(final float speed, final boolean ignoreDelay) {
        if (!isMoving() || (!MovementUtils.strafeTimer.hasTimePassed(200L) && !ignoreDelay)) {
            return;
        }
        final double yaw = getDirection();
        OringoClient.mc.player.motionX = -Math.sin(yaw) * speed;
        OringoClient.mc.player.motionZ = Math.cos(yaw) * speed;
        MovementUtils.strafeTimer.updateTime();
    }
    
    public static void strafe(final float speed, final float yaw) {
        if (!isMoving() || !MovementUtils.strafeTimer.hasTimePassed(150L)) {
            return;
        }
        OringoClient.mc.player.motionX = -Math.sin(Math.toRadians(yaw)) * speed;
        OringoClient.mc.player.motionZ = Math.cos(Math.toRadians(yaw)) * speed;
        MovementUtils.strafeTimer.updateTime();
    }
    
    public static void forward(final double length) {
        final double yaw = Math.toRadians(OringoClient.mc.player.rotationYaw);
        OringoClient.mc.player.setPosition(OringoClient.mc.player.posX + -Math.sin(yaw) * length, OringoClient.mc.player.posY, OringoClient.mc.player.posZ + Math.cos(yaw) * length);
    }
    
    public static double getDirection() {
        return Math.toRadians(getYaw());
    }
    
    public static float getYaw() {
        float rotationYaw = OringoClient.mc.player.rotationYaw;
        if (OringoClient.mc.player.moveVertical < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (OringoClient.mc.player.moveVertical < 0.0f) {
            forward = -0.5f;
        }
        else if (OringoClient.mc.player.moveVertical > 0.0f) {
            forward = 0.5f;
        }
        if (OringoClient.mc.player.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (OringoClient.mc.player.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return rotationYaw;
    }
    
    static {
        MovementUtils.strafeTimer = new MilliTimer();
    }
}
