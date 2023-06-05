//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import net.minecraft.client.network.NetworkPlayerInfo;
import java.awt.Color;
import net.minecraft.item.ItemStack;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import java.util.Locale;
import java.lang.reflect.Method;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreObjective;
import java.util.Iterator;
import java.util.List;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import java.util.Collection;
import net.minecraft.scoreboard.Score;
import java.util.ArrayList;
import com.google.common.collect.Iterables;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import me.oringo.oringoclient.OringoClient;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.server.S02PacketChat;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import me.oringo.oringoclient.events.PacketSentEvent;
import net.minecraft.client.Minecraft;

public class SkyblockUtils
{
    private static final Minecraft mc;
    public static boolean inDungeon;
    public static boolean isInOtherGame;
    public static boolean onSkyblock;
    public static boolean onBedwars;
    public static boolean onSkywars;
    public static boolean inBlood;
    public static boolean inP3;
    public static int lastReportedSlot;
    
    @SubscribeEvent
    public void onPacketSent(final PacketSentEvent event) {
        if (event.packet instanceof C09PacketHeldItemChange) {
            SkyblockUtils.lastReportedSlot = ((C09PacketHeldItemChange)event.packet).getSlotId();
        }
    }
    
    public static void updateItem() {
        if (SkyblockUtils.lastReportedSlot != SkyblockUtils.mc.player.inventory.currentItem) {
            SkyblockUtils.mc.getConnection().getNetworkManager().sendPacket((Packet)new C09PacketHeldItemChange(SkyblockUtils.mc.player.inventory.currentItem));
        }
    }
    
    public static void updateItemNoEvent() {
        if (SkyblockUtils.lastReportedSlot != SkyblockUtils.mc.player.inventory.currentItem) {
            PacketUtils.sendPacketNoEvent((Packet<?>)new C09PacketHeldItemChange(SkyblockUtils.mc.player.inventory.currentItem));
            SkyblockUtils.lastReportedSlot = SkyblockUtils.mc.player.inventory.currentItem;
        }
    }
    
    @SubscribeEvent
    public void onChat(final PacketReceivedEvent event) {
        if (event.packet instanceof S02PacketChat) {
            if (ChatFormatting.stripFormatting(((S02PacketChat)event.packet).getChatComponent().getFormattedText()).startsWith("[BOSS] The Watcher: ") && !SkyblockUtils.inBlood) {
                SkyblockUtils.inBlood = true;
                if (OringoClient.bloodAimbot.isToggled()) {
                    Notifications.showNotification("Oringo Client", "Started Camp", 1000);
                }
            }
            if (ChatFormatting.stripFormatting(((S02PacketChat)event.packet).getChatComponent().getFormattedText()).equals("[BOSS] The Watcher: You have proven yourself. You may pass.")) {
                SkyblockUtils.inBlood = false;
                if (OringoClient.bloodAimbot.isToggled()) {
                    Notifications.showNotification("Oringo Client", "Stopped camp", 1000);
                }
            }
            if (ChatFormatting.stripFormatting(((S02PacketChat)event.packet).getChatComponent().getFormattedText()).startsWith("[BOSS] Necron: I hope you're in shape. BETTER GET RUNNING!")) {
                SkyblockUtils.inP3 = true;
            }
            if (ChatFormatting.stripFormatting(((S02PacketChat)event.packet).getChatComponent().getFormattedText()).startsWith("[BOSS] Necron: THAT'S IT YOU HAVE DONE IT! MY ENTIRE FACTORY IS RUINED! ARE YOU HAPPY?!")) {
                SkyblockUtils.inP3 = false;
            }
        }
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load event) {
        SkyblockUtils.inBlood = false;
        SkyblockUtils.inP3 = false;
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (SkyblockUtils.mc.world != null) {
            SkyblockUtils.inDungeon = (hasLine("Cleared:") || hasLine("Start"));
        }
        SkyblockUtils.isInOtherGame = isInOtherGame();
        SkyblockUtils.onSkyblock = isOnSkyBlock();
        SkyblockUtils.onBedwars = hasScoreboardTitle("bed wars");
        SkyblockUtils.onSkywars = hasScoreboardTitle("SKYWARS");
    }
    
    public static boolean isTeam(final EntityLivingBase e, final EntityLivingBase e2) {
        return e.getDisplayName().getUnformattedText().length() >= 4 && (e.getDisplayName().getFormattedText().charAt(2) == '§' && e2.getDisplayName().getFormattedText().charAt(2) == '§') && (SkyblockUtils.onSkyblock || e.getDisplayName().getFormattedText().charAt(3) == e2.getDisplayName().getFormattedText().charAt(3));
    }
    
    public static <T> T firstOrNull(final Iterable<T> iterable) {
        return (T)Iterables.getFirst((Iterable)iterable, (Object)null);
    }
    
    public static boolean hasScoreboardTitle(final String title) {
        return SkyblockUtils.mc.player != null && SkyblockUtils.mc.player.getWorldScoreboard() != null && SkyblockUtils.mc.player.getWorldScoreboard().getObjectiveInDisplaySlot(1) != null && ChatFormatting.stripFormatting(SkyblockUtils.mc.player.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName()).equalsIgnoreCase(title);
    }
    
    public static boolean isInOtherGame() {
        try {
            final Scoreboard sb = SkyblockUtils.mc.player.getWorldScoreboard();
            final List<Score> list = new ArrayList<Score>(sb.getSortedScores(sb.getObjectiveInDisplaySlot(1)));
            for (final Score score : list) {
                final ScorePlayerTeam team = sb.getPlayersTeam(score.getPlayerName());
                final String s = ChatFormatting.stripFormatting(ScorePlayerTeam.formatPlayerName((Team)team, score.getPlayerName()));
                if (s.contains("Map")) {
                    return true;
                }
            }
        }
        catch (Exception ex) {}
        return false;
    }
    
    public static boolean isOnSkyBlock() {
        try {
            final ScoreObjective titleObjective = SkyblockUtils.mc.player.getWorldScoreboard().getObjectiveInDisplaySlot(1);
            if (SkyblockUtils.mc.player.getWorldScoreboard().getObjectiveInDisplaySlot(0) != null) {
                return ChatFormatting.stripFormatting(SkyblockUtils.mc.player.getWorldScoreboard().getObjectiveInDisplaySlot(0).getDisplayName()).contains("SKYBLOCK");
            }
            return ChatFormatting.stripFormatting(SkyblockUtils.mc.player.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName()).contains("SKYBLOCK");
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public static boolean hasLine(final String line) {
        try {
            final Scoreboard sb = Minecraft.getMinecraft().player.getWorldScoreboard();
            final List<Score> list = new ArrayList<Score>(sb.getSortedScores(sb.getObjectiveInDisplaySlot(1)));
            for (final Score score : list) {
                final ScorePlayerTeam team = sb.getPlayersTeam(score.getPlayerName());
                String s;
                try {
                    s = ChatFormatting.stripFormatting(team.getPrefix() + score.getPlayerName() + team.getSuffix());
                }
                catch (Exception e) {
                    return false;
                }
                StringBuilder builder = new StringBuilder();
                for (final char c : s.toCharArray()) {
                    if (c < '\u0100') {
                        builder.append(c);
                    }
                }
                if (builder.toString().toLowerCase().contains(line.toLowerCase())) {
                    return true;
                }
                try {
                    s = ChatFormatting.stripFormatting(team.getPrefix() + team.getSuffix());
                }
                catch (Exception e2) {
                    return false;
                }
                builder = new StringBuilder();
                for (final char c : s.toCharArray()) {
                    if (c < '\u0100') {
                        builder.append(c);
                    }
                }
                if (builder.toString().toLowerCase().contains(line.toLowerCase())) {
                    return true;
                }
            }
        }
        catch (Exception e3) {
            return false;
        }
        return false;
    }
    
    public static boolean isMiniboss(final Entity entity) {
        return entity.getName().equals("Shadow Assassin") || entity.getName().equals("Lost Adventurer") || entity.getName().equals("Diamond Guy");
    }
    
    public static void click() {
        try {
            Method clickMouse;
            try {
                clickMouse = Minecraft.class.getDeclaredMethod("clickMouse", (Class<?>[])new Class[0]);
            }
            catch (NoSuchMethodException e2) {
                clickMouse = Minecraft.class.getDeclaredMethod("clickMouse", (Class<?>[])new Class[0]);
            }
            clickMouse.setAccessible(true);
            clickMouse.invoke(Minecraft.getMinecraft(), new Object[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static boolean anyTab(final String s) {
        return Minecraft.getMinecraft().getConnection().getPlayerInfoMap().stream().anyMatch(player -> player.getDisplayName() != null && ChatFormatting.stripFormatting(player.getDisplayName().getFormattedText()).toLowerCase().contains(s.toLowerCase(Locale.ROOT)));
    }
    
    public static boolean isNPC(final Entity entity) {
        if (!(entity instanceof EntityOtherPlayerMP)) {
            return false;
        }
        final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
        return entity.getUniqueID().version() == 2 && entityLivingBase.getHealth() == 20.0f;
    }
    
    public static void rightClick() {
        try {
            Method rightClickMouse = null;
            try {
                rightClickMouse = Minecraft.class.getDeclaredMethod("rightClickMouse", (Class<?>[])new Class[0]);
            }
            catch (NoSuchMethodException e2) {
                rightClickMouse = Minecraft.class.getDeclaredMethod("rightClickMouse", (Class<?>[])new Class[0]);
            }
            rightClickMouse.setAccessible(true);
            rightClickMouse.invoke(Minecraft.getMinecraft(), new Object[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String getDisplayName(final ItemStack item) {
        if (item == null) {
            return "null";
        }
        return item.getDisplayName();
    }
    
    public static Color rainbow(final int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360.0;
        return Color.getHSBColor((float)(rainbowState / 360.0), 1.0f, 1.0f);
    }
    
    public static int getPing() {
        final NetworkPlayerInfo networkPlayerInfo = SkyblockUtils.mc.getConnection().getPlayerInfo(Minecraft.getMinecraft().player.getUniqueID());
        return (networkPlayerInfo == null) ? 0 : networkPlayerInfo.getResponseTime();
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
