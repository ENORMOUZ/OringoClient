//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.skyblock;

import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import java.util.List;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import me.oringo.oringoclient.utils.RotationUtils;
import me.oringo.oringoclient.utils.SkyblockUtils;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.qolfeatures.module.combat.KillAura;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import java.util.ArrayList;
import net.minecraft.entity.EntityLivingBase;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.StringSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class TerminatorAura extends Module
{
    public NumberSetting range;
    public NumberSetting delay;
    public ModeSetting mode;
    public ModeSetting button;
    public BooleanSetting bossLock;
    public BooleanSetting inDungeon;
    public BooleanSetting teamCheck;
    public StringSetting customItem;
    public static EntityLivingBase target;
    private static boolean attack;
    private static ArrayList<EntityLivingBase> attackedMobs;
    
    public TerminatorAura() {
        super("Terminator Aura", 0, Category.SKYBLOCK);
        this.range = new NumberSetting("Range", 15.0, 5.0, 30.0, 1.0);
        this.delay = new NumberSetting("Use delay", 3.0, 1.0, 10.0, 1.0);
        this.mode = new ModeSetting("Mode", "Swap", new String[] { "Swap", "Held" });
        this.button = new ModeSetting("Mouse", "Right", new String[] { "Left", "Right" });
        this.bossLock = new BooleanSetting("Boss Lock", true);
        this.inDungeon = new BooleanSetting("only Dungeon", true);
        this.teamCheck = new BooleanSetting("Teamcheck", false);
        this.customItem = new StringSetting("Custom Item");
        this.addSettings(this.delay, this.range, this.button, this.mode, this.customItem, this.bossLock, this.inDungeon, this.teamCheck);
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onUpdate(final MotionUpdateEvent.Pre event) {
        if (KillAura.target != null || Aimbot.attack || !this.isToggled() || OringoClient.mc.player.ticksExisted % this.delay.getValue() != 0.0 || (!SkyblockUtils.inDungeon && this.inDungeon.isEnabled())) {
            return;
        }
        boolean hasTerm = OringoClient.mc.player.func_70694_bm() != null && (OringoClient.mc.player.func_70694_bm().getDisplayName().contains("Juju") || OringoClient.mc.player.func_70694_bm().getDisplayName().contains("Terminator") || (!this.customItem.getValue().equals("") && OringoClient.mc.player.func_70694_bm().getDisplayName().contains(this.customItem.getValue())));
        if (this.mode.getSelected().equals("Swap")) {
            for (int i = 0; i < 9; ++i) {
                if (OringoClient.mc.player.inventory.getStackInSlot(i) != null && (OringoClient.mc.player.inventory.getStackInSlot(i).getDisplayName().contains("Juju") || OringoClient.mc.player.inventory.getStackInSlot(i).getDisplayName().contains("Terminator") || (!this.customItem.is("") && OringoClient.mc.player.inventory.getStackInSlot(i).getDisplayName().contains(this.customItem.getValue())))) {
                    hasTerm = true;
                    break;
                }
            }
        }
        if (!hasTerm) {
            return;
        }
        TerminatorAura.target = this.getTarget(TerminatorAura.target);
        if (TerminatorAura.target != null) {
            TerminatorAura.attack = true;
            final float[] angles = RotationUtils.getBowAngles((Entity)TerminatorAura.target);
            event.yaw = angles[0];
            event.pitch = angles[1];
        }
    }
    
    @SubscribeEvent
    public void onUpdatePost(final MotionUpdateEvent.Post event) {
        if (!TerminatorAura.attack) {
            return;
        }
        final int held = OringoClient.mc.player.inventory.currentItem;
        if (this.mode.getSelected().equals("Swap")) {
            for (int i = 0; i < 9; ++i) {
                if (OringoClient.mc.player.inventory.getStackInSlot(i) != null && (OringoClient.mc.player.inventory.getStackInSlot(i).getDisplayName().contains("Juju") || OringoClient.mc.player.inventory.getStackInSlot(i).getDisplayName().contains("Terminator") || (!this.customItem.is("") && OringoClient.mc.player.inventory.getStackInSlot(i).getDisplayName().contains(this.customItem.getValue())))) {
                    OringoClient.mc.player.inventory.currentItem = i;
                    break;
                }
            }
        }
        SkyblockUtils.updateItemNoEvent();
        this.click();
        OringoClient.mc.player.inventory.currentItem = held;
        SkyblockUtils.updateItemNoEvent();
        TerminatorAura.attack = false;
    }
    
    private EntityLivingBase getTarget(final EntityLivingBase lastTarget) {
        if (this.bossLock.isEnabled() && lastTarget != null && SkyblockUtils.isMiniboss((Entity)lastTarget) && lastTarget.getHealth() > 0.0f && !lastTarget.isDead && lastTarget.canEntityBeSeen((Entity)OringoClient.mc.player) && lastTarget.getDistance((Entity)OringoClient.mc.player) < this.range.getValue()) {
            return lastTarget;
        }
        final List<Entity> validTargets = (List<Entity>)OringoClient.mc.world.getLoadedEntityList().stream().filter(entity -> entity instanceof EntityLivingBase).filter(entity -> this.isValid(entity)).sorted(Comparator.comparingDouble((ToDoubleFunction<? super T>)OringoClient.mc.player::getDistance)).sorted(Comparator.comparing(entity -> RotationUtils.getYawDifference((lastTarget != null) ? lastTarget : entity, entity)).reversed()).collect(Collectors.toList());
        final Iterator<Entity> iterator = validTargets.iterator();
        if (iterator.hasNext()) {
            final Entity entity2 = iterator.next();
            TerminatorAura.attackedMobs.add((EntityLivingBase)entity2);
            final Object o;
            new Thread(() -> {
                try {
                    Thread.sleep(350L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TerminatorAura.attackedMobs.remove(o);
                return;
            }).start();
            return (EntityLivingBase)entity2;
        }
        return null;
    }
    
    private void click() {
        final String selected = this.button.getSelected();
        switch (selected) {
            case "Left": {
                OringoClient.mc.player.func_71038_i();
                break;
            }
            case "Right": {
                OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C08PacketPlayerBlockPlacement(OringoClient.mc.player.func_70694_bm()));
                break;
            }
        }
    }
    
    private boolean isValid(final EntityLivingBase entity) {
        return entity != OringoClient.mc.player && !(entity instanceof EntityArmorStand) && OringoClient.mc.player.canEntityBeSeen((Entity)entity) && entity.getHealth() > 0.0f && entity.getDistance((Entity)OringoClient.mc.player) <= this.range.getValue() && ((!(entity instanceof EntityPlayer) && !(entity instanceof EntityBat) && !(entity instanceof EntityZombie) && !(entity instanceof EntityGiantZombie)) || !entity.isInvisible()) && !entity.getName().equals("Dummy") && !entity.getName().startsWith("Decoy") && !TerminatorAura.attackedMobs.contains(entity) && !(entity instanceof EntityBlaze) && (!SkyblockUtils.isTeam((EntityLivingBase)OringoClient.mc.player, entity) || !this.teamCheck.isEnabled());
    }
    
    static {
        TerminatorAura.attackedMobs = new ArrayList<EntityLivingBase>();
    }
}
