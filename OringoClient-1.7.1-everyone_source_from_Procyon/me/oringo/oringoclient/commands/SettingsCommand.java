//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands;

import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.scoreboard.ScorePlayerTeam;
import java.util.Iterator;
import net.minecraft.scoreboard.Scoreboard;
import java.util.Collection;
import net.minecraft.scoreboard.Score;
import net.minecraft.client.Minecraft;
import me.oringo.oringoclient.OringoClient;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ICommand;

public class SettingsCommand implements ICommand
{
    public static boolean openSettings;
    
    public String getName() {
        return "oringo";
    }
    
    public String getUsage(final ICommandSender sender) {
        return "/oringo";
    }
    
    public List<String> getAliases() {
        return new ArrayList<String>();
    }
    
    public void func_71515_b(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length != 0 && args[0].equalsIgnoreCase("scoreboard") && OringoClient.mc.player.getWorldScoreboard() != null) {
            final StringBuilder builder = new StringBuilder();
            final Scoreboard sb = Minecraft.getMinecraft().player.getWorldScoreboard();
            final List<Score> list = new ArrayList<Score>(sb.getSortedScores(sb.getObjectiveInDisplaySlot(1)));
            for (final Score score : list) {
                final ScorePlayerTeam team = sb.getPlayersTeam(score.getPlayerName());
                final String s = team.getPrefix() + score.getPlayerName() + team.getSuffix();
                for (final char c : s.toCharArray()) {
                    if (c < '\u0100') {
                        builder.append(c);
                    }
                }
                builder.append("\n");
            }
            builder.append(OringoClient.mc.player.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName());
            System.out.println(builder);
            return;
        }
        OringoClient.clickGui.toggle();
    }
    
    public boolean func_71519_b(final ICommandSender sender) {
        return true;
    }
    
    public List<String> func_180525_a(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return new ArrayList<String>();
    }
    
    public boolean isUsernameIndex(final String[] args, final int index) {
        return false;
    }
    
    public int compareTo(final ICommand o) {
        return 0;
    }
    
    static {
        SettingsCommand.openSettings = false;
    }
}
