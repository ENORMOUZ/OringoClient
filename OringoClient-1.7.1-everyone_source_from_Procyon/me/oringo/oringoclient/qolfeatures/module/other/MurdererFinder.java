//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.other;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import me.oringo.oringoclient.utils.RenderUtils;
import java.awt.Color;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.scoreboard.ScoreObjective;
import me.oringo.oringoclient.utils.Notifications;
import me.oringo.oringoclient.utils.SkyblockUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.oringo.oringoclient.OringoClient;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import java.util.Collection;
import java.util.Arrays;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import java.util.ArrayList;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class MurdererFinder extends Module
{
    private ArrayList<Item> knives;
    public static ArrayList<EntityPlayer> murderers;
    public static ArrayList<EntityPlayer> detectives;
    private BooleanSetting autoSay;
    private BooleanSetting ingotESP;
    private BooleanSetting bowESP;
    private boolean inMurder;
    
    public MurdererFinder() {
        super("Murder Mystery", Category.OTHER);
        this.knives = new ArrayList<Item>(Arrays.asList(Items.IRON_SWORD, Items.STONE_SWORD, Items.IRON_SHOVEL, Items.STICK, Items.WOODEN_AXE, Items.WOODEN_SWORD, Blocks.DEADBUSH.func_180665_b((World)null, (BlockPos)null), Items.STONE_SHOVEL, Items.DIAMOND_SHOVEL, Items.QUARTZ, Items.PUMPKIN_PIE, Items.GOLDEN_PICKAXE, Items.APPLE, Items.NAME_TAG, Blocks.SPONGE.func_180665_b((World)null, (BlockPos)null), Items.CARROT_ON_A_STICK, Items.BONE, Items.CARROT, Items.GOLDEN_CARROT, Items.COOKIE, Items.DIAMOND_AXE, Blocks.RED_FLOWER.func_180665_b((World)null, (BlockPos)null), Items.PRISMARINE_SHARD, Items.COOKED_BEEF, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.DIAMOND_HOE, (Item)Items.SHEARS, Items.FISH, Items.DYE, Items.BOAT, Items.SPECKLED_MELON, Items.BLAZE_ROD, Items.FISH));
        this.autoSay = new BooleanSetting("Say murderer", false);
        this.ingotESP = new BooleanSetting("Ingot ESP", true);
        this.bowESP = new BooleanSetting("Bow esp", true);
        this.addSettings(this.autoSay, this.ingotESP, this.bowESP);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (!this.isToggled() || OringoClient.mc.player == null || OringoClient.mc.world == null) {
            return;
        }
        try {
            if (OringoClient.mc.player.getWorldScoreboard() != null) {
                final ScoreObjective objective = OringoClient.mc.player.getWorldScoreboard().getObjectiveInDisplaySlot(1);
                if (objective != null && ChatFormatting.stripFormatting(objective.getDisplayName()).equals("MURDER MYSTERY") && SkyblockUtils.hasLine("Innocents Left:")) {
                    this.inMurder = true;
                    for (final EntityPlayer player : OringoClient.mc.world.playerEntities) {
                        player.field_70153_n = null;
                        if (!MurdererFinder.murderers.contains(player)) {
                            if (MurdererFinder.detectives.contains(player)) {
                                continue;
                            }
                            if (player.func_70694_bm() == null) {
                                continue;
                            }
                            if (MurdererFinder.detectives.size() < 2 && player.func_70694_bm().getItem().equals(Items.BOW)) {
                                MurdererFinder.detectives.add(player);
                                Notifications.showNotification("Oringo Client", String.format("§b%s is detective!", player.getName()), 2500);
                            }
                            if (!this.knives.contains(player.func_70694_bm().getItem())) {
                                continue;
                            }
                            MurdererFinder.murderers.add(player);
                            Notifications.showNotification("Oringo Client", String.format("§c%s is murderer!", player.getName()), 2500);
                            if (!this.autoSay.isEnabled() || player == OringoClient.mc.player) {
                                continue;
                            }
                            OringoClient.mc.player.sendChatMessage(String.format("%s is murderer!", ChatFormatting.stripFormatting(player.getName())));
                        }
                    }
                    return;
                }
                this.inMurder = false;
                MurdererFinder.murderers.clear();
                MurdererFinder.detectives.clear();
            }
        }
        catch (Exception ex) {}
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent e) {
        if (!this.isToggled()) {
            return;
        }
        if (this.inMurder) {
            for (final Entity entity : OringoClient.mc.world.loadedEntityList) {
                if (entity instanceof EntityPlayer) {
                    if (((EntityPlayer)entity).isPlayerSleeping()) {
                        continue;
                    }
                    if (entity == OringoClient.mc.player) {
                        continue;
                    }
                    if (MurdererFinder.murderers.contains(entity)) {
                        RenderUtils.draw2D(entity, e.partialTicks, 1.5f, Color.red);
                    }
                    else if (MurdererFinder.detectives.contains(entity)) {
                        RenderUtils.draw2D(entity, e.partialTicks, 1.5f, Color.blue);
                    }
                    else {
                        RenderUtils.draw2D(entity, e.partialTicks, 1.5f, Color.gray);
                    }
                }
                else if (entity instanceof EntityItem && ((EntityItem)entity).getItem().getItem() == Items.GOLD_INGOT && this.ingotESP.isEnabled()) {
                    RenderUtils.draw2D(entity, e.partialTicks, 1.0f, Color.yellow);
                }
                else {
                    if (!this.bowESP.isEnabled() || !(entity instanceof EntityArmorStand) || ((EntityArmorStand)entity).func_71124_b(0) == null || ((EntityArmorStand)entity).func_71124_b(0).getItem() != Items.BOW) {
                        continue;
                    }
                    RenderUtils.tracerLine(entity, e.partialTicks, 1.5f, Color.CYAN);
                }
            }
        }
    }
    
    static {
        MurdererFinder.murderers = new ArrayList<EntityPlayer>();
        MurdererFinder.detectives = new ArrayList<EntityPlayer>();
    }
}
