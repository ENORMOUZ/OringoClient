//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.skyblock;

import java.util.ArrayList;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import me.oringo.oringoclient.utils.RotationUtils;
import net.minecraft.util.Vec3;
import net.minecraft.entity.player.EntityPlayer;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.utils.SkyblockUtils;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import net.minecraft.entity.Entity;
import java.util.List;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Aimbot extends Module
{
    public NumberSetting yOffset;
    private static List<Entity> killed;
    public static boolean attack;
    
    public Aimbot() {
        super("Blood aimbot", 0, Category.SKYBLOCK);
        this.addSetting(this.yOffset = new NumberSetting("Y offset", 0.0, -2.0, 2.0, 0.1));
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onMove(final MotionUpdateEvent.Pre event) {
        if (!this.isToggled() || !SkyblockUtils.inDungeon || !SkyblockUtils.inBlood || OringoClient.mc.world == null) {
            return;
        }
        for (final Entity entity : OringoClient.mc.world.playerEntities) {
            if (entity.getDistance((Entity)OringoClient.mc.player) < 20.0f && entity instanceof EntityPlayer && !entity.isDead && !Aimbot.killed.contains(entity)) {
                for (final String name : new String[] { "Revoker", "Psycho", "Reaper", "Cannibal", "Mute", "Ooze", "Putrid", "Freak", "Leech", "Tear", "Parasite", "Flamer", "Skull", "Mr. Dead", "Vader", "Frost", "Walker", "WanderingSoul" }) {
                    if (entity.getName().contains(name)) {
                        Aimbot.attack = true;
                        final float[] angles = RotationUtils.getAngles(new Vec3(entity.posX, entity.posY + this.yOffset.getValue(), entity.posZ));
                        event.yaw = angles[0];
                        event.pitch = angles[1];
                        Aimbot.killed.add(entity);
                        break;
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onMovePost(final MotionUpdateEvent.Post event) {
        if (!Aimbot.attack) {
            return;
        }
        OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C0APacketAnimation());
        Aimbot.attack = false;
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load event) {
        Aimbot.killed.clear();
    }
    
    static {
        Aimbot.killed = new ArrayList<Entity>();
    }
}
