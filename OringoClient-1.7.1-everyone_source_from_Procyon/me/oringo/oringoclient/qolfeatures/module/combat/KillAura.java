//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.combat;

import java.util.ArrayList;
import net.minecraftforge.event.world.WorldEvent;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.boss.EntityWither;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraft.util.MathHelper;
import me.oringo.oringoclient.mixins.entity.PlayerSPAccessor;
import net.minecraft.entity.Entity;
import me.oringo.oringoclient.utils.RotationUtils;
import net.minecraft.item.ItemSword;
import me.oringo.oringoclient.qolfeatures.module.skyblock.Aimbot;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.Notifications;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.item.EntityArmorStand;
import org.lwjgl.input.Mouse;
import me.oringo.oringoclient.OringoClient;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import java.util.List;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import net.minecraft.entity.EntityLivingBase;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class KillAura extends Module
{
    public static EntityLivingBase target;
    public BooleanSetting namesOnly;
    public BooleanSetting middleClick;
    public BooleanSetting players;
    public BooleanSetting mobs;
    public BooleanSetting walls;
    public BooleanSetting teams;
    public BooleanSetting toggleOnLoad;
    public BooleanSetting toggleInGui;
    public BooleanSetting onlySword;
    public BooleanSetting antiNPC;
    public BooleanSetting movementFix;
    public NumberSetting range;
    public NumberSetting rotationRange;
    public NumberSetting fov;
    public NumberSetting maxRotationPitch;
    public NumberSetting maxRotationYaw;
    public ModeSetting mode;
    public ModeSetting blockMode;
    public ModeSetting namesonlyMode;
    public static List<String> names;
    private boolean wasDown;
    
    public KillAura() {
        super("Kill Aura", 0, Category.COMBAT);
        this.namesOnly = new BooleanSetting("Names only", false);
        this.middleClick = new BooleanSetting("Middle click to add", false);
        this.players = new BooleanSetting("Players", false);
        this.mobs = new BooleanSetting("Mobs", true);
        this.walls = new BooleanSetting("Through walls", true);
        this.teams = new BooleanSetting("Teams", true);
        this.toggleOnLoad = new BooleanSetting("Disable on join", true);
        this.toggleInGui = new BooleanSetting("No containers", true);
        this.onlySword = new BooleanSetting("Only swords", false);
        this.antiNPC = new BooleanSetting("Anti npc", true);
        this.movementFix = new BooleanSetting("Movement fix", false);
        this.range = new NumberSetting("Range", 4.2, 2.0, 6.0, 0.01);
        this.rotationRange = new NumberSetting("Rotation Range", 6.0, 2.0, 12.0, 0.01);
        this.fov = new NumberSetting("Fov", 180.0, 30.0, 180.0, 0.1);
        this.maxRotationPitch = new NumberSetting("Max pitch", 60.0, 10.0, 180.0, 0.1);
        this.maxRotationYaw = new NumberSetting("Max Yaw", 80.0, 10.0, 180.0, 0.1);
        this.mode = new ModeSetting("Sorting", "Distance", new String[] { "Distance", "Health", "Hurt", "Hp reverse" });
        this.blockMode = new ModeSetting("Autoblock", "None", new String[] { "Default", "Fake", "None" });
        this.namesonlyMode = new ModeSetting("Names mode", "Enemies", new String[] { "Friends", "Enemies" });
        this.addSettings(this.range, this.rotationRange, this.mode, this.maxRotationYaw, this.maxRotationPitch, this.fov, this.blockMode, this.players, this.mobs, this.teams, this.movementFix, this.namesOnly, this.namesonlyMode, this.middleClick, this.walls, this.toggleInGui, this.toggleOnLoad, this.onlySword, this.antiNPC);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (OringoClient.mc.player == null || OringoClient.mc.world == null || !this.middleClick.isEnabled()) {
            return;
        }
        if (Mouse.isButtonDown(2) && OringoClient.mc.currentScreen == null) {
            if (OringoClient.mc.pointedEntity != null && !this.wasDown && !(OringoClient.mc.pointedEntity instanceof EntityArmorStand) && OringoClient.mc.pointedEntity instanceof EntityLivingBase) {
                final String name = ChatFormatting.stripFormatting(OringoClient.mc.pointedEntity.getDisplayName().getFormattedText());
                if (!KillAura.names.contains(name)) {
                    KillAura.names.add(name);
                    save();
                    Notifications.showNotification("Oringo Client", "Added " + ChatFormatting.AQUA + name + ChatFormatting.RESET + " to name sorting", 2000);
                }
                else {
                    KillAura.names.remove(name);
                    save();
                    Notifications.showNotification("Oringo Client", "Removed " + ChatFormatting.AQUA + name + ChatFormatting.RESET + " from name sorting", 2000);
                }
            }
            this.wasDown = true;
        }
        else {
            this.wasDown = false;
        }
    }
    
    @Override
    public void onDisable() {
        KillAura.target = null;
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onMovePre(final MotionUpdateEvent.Pre event) {
        if (!this.isToggled() || Aimbot.attack || (this.onlySword.isEnabled() && (OringoClient.mc.player.func_70694_bm() == null || !(OringoClient.mc.player.func_70694_bm().getItem() instanceof ItemSword)))) {
            KillAura.target = null;
            return;
        }
        KillAura.target = this.getTarget();
        if (KillAura.target != null) {
            final float[] angles = RotationUtils.getServerAngles((Entity)KillAura.target);
            event.yaw = ((PlayerSPAccessor)OringoClient.mc.player).getLastReportedYaw() - MathHelper.wrapDegrees((float)Math.max(-this.maxRotationYaw.getValue(), Math.min(((PlayerSPAccessor)OringoClient.mc.player).getLastReportedYaw() - angles[0], this.maxRotationYaw.getValue())));
            event.pitch = ((PlayerSPAccessor)OringoClient.mc.player).getLastReportedPitch() - MathHelper.wrapDegrees((float)Math.max(-this.maxRotationPitch.getValue(), Math.min(((PlayerSPAccessor)OringoClient.mc.player).getLastReportedPitch() - angles[1], this.maxRotationPitch.getValue())));
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMovePost(final MotionUpdateEvent.Post event) {
        if (KillAura.target != null && OringoClient.mc.player.ticksExisted % 2 == 0) {
            SkyblockUtils.updateItemNoEvent();
            if (OringoClient.mc.player.getDistance((Entity)KillAura.target) < this.range.getValue()) {
                if (OringoClient.mc.player.func_71039_bw()) {
                    OringoClient.mc.playerController.onStoppedUsingItem((EntityPlayer)OringoClient.mc.player);
                }
                OringoClient.mc.player.func_71038_i();
                final float[] angles = RotationUtils.getServerAngles((Entity)KillAura.target);
                if (Math.abs(((PlayerSPAccessor)OringoClient.mc.player).getLastReportedPitch() - angles[1]) < 25.0f && Math.abs(((PlayerSPAccessor)OringoClient.mc.player).getLastReportedYaw() - angles[0]) < 15.0f) {
                    OringoClient.mc.playerController.attackEntity((EntityPlayer)OringoClient.mc.player, (Entity)KillAura.target);
                }
                if (!OringoClient.mc.player.func_71039_bw() && OringoClient.mc.player.func_70694_bm() != null && OringoClient.mc.player.func_70694_bm().getItem() instanceof ItemSword && this.blockMode.getSelected().equals("Default")) {
                    OringoClient.mc.playerController.func_78769_a((EntityPlayer)OringoClient.mc.player, (World)OringoClient.mc.world, OringoClient.mc.player.func_70694_bm());
                }
            }
        }
    }
    
    private EntityLivingBase getTarget() {
        if ((OringoClient.mc.currentScreen instanceof GuiContainer && this.toggleInGui.isEnabled()) || OringoClient.mc.world == null) {
            return null;
        }
        final List<Entity> validTargets = (List<Entity>)OringoClient.mc.world.getLoadedEntityList().stream().filter(entity -> entity instanceof EntityLivingBase).filter(entity -> this.isValid(entity)).sorted(Comparator.comparingDouble(e -> e.getDistance((Entity)OringoClient.mc.player))).collect(Collectors.toList());
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Health": {
                validTargets.sort(Comparator.comparingDouble(e -> e.getHealth()));
                break;
            }
            case "Hurt": {
                validTargets.sort(Comparator.comparing(e -> e.hurtTime));
                break;
            }
            case "Hp reverse": {
                validTargets.sort(Comparator.comparingDouble(e -> e.getHealth()).reversed());
                break;
            }
        }
        final Iterator<Entity> iterator = validTargets.iterator();
        if (iterator.hasNext()) {
            final Entity entity2 = iterator.next();
            return (EntityLivingBase)entity2;
        }
        return null;
    }
    
    private boolean isValid(final EntityLivingBase entity) {
        if (entity == OringoClient.mc.player || ((entity instanceof EntityPlayer || entity instanceof EntityWither || entity instanceof EntityBat) && entity.isInvisible()) || entity instanceof EntityArmorStand || (!OringoClient.mc.player.canEntityBeSeen((Entity)entity) && !this.walls.isEnabled()) || entity.getHealth() <= 0.0f || entity.getDistance((Entity)OringoClient.mc.player) > ((KillAura.target != null && KillAura.target != entity) ? this.range.getValue() : Math.max(this.rotationRange.getValue(), this.range.getValue())) || !RotationUtils.isWithinFOV(entity, this.fov.getValue() + 5.0) || !RotationUtils.isWithinPitch(entity, this.fov.getValue() + 5.0)) {
            return false;
        }
        if (this.namesOnly.isEnabled()) {
            final boolean flag = KillAura.names.contains(ChatFormatting.stripFormatting(entity.getDisplayName().getFormattedText()));
            if (this.namesonlyMode.is("Enemies") || flag) {
                return this.namesonlyMode.is("Enemies") && flag;
            }
        }
        return ((!(entity instanceof EntityMob) && !(entity instanceof EntityAmbientCreature) && !(entity instanceof EntityWaterMob) && !(entity instanceof EntityAnimal) && !(entity instanceof EntitySlime)) || this.mobs.isEnabled()) && (!(entity instanceof EntityPlayer) || ((!SkyblockUtils.isTeam(entity, (EntityLivingBase)OringoClient.mc.player) || !this.teams.isEnabled()) && (!SkyblockUtils.isNPC((Entity)entity) || !this.antiNPC.isEnabled()) && this.players.isEnabled())) && !(entity instanceof EntityVillager);
    }
    
    private static void save() {
        try {
            final DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("config/OringoClient/KillAura.cfg"));
            dataOutputStream.writeInt(KillAura.names.size());
            for (final String name : KillAura.names) {
                dataOutputStream.writeUTF(name);
            }
            dataOutputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load event) {
        if (this.isToggled() && this.toggleOnLoad.isEnabled()) {
            this.toggle();
        }
    }
    
    static {
        KillAura.names = new ArrayList<String>();
    }
}
