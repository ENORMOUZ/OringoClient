//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.other;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.scoreboard.ScoreObjective;
import java.util.List;
import java.util.Collections;
import me.oringo.oringoclient.utils.Notifications;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import java.util.ArrayList;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class GuessTheBuildAFK extends Module
{
    private ArrayList<String> wordList;
    private int tips;
    private Thread guesses;
    private int period;
    private long lastGuess;
    
    public GuessTheBuildAFK() {
        super("Auto GTB", 0, Category.OTHER);
        this.wordList = new ArrayList<String>();
        this.tips = 0;
        this.guesses = null;
        this.period = 3200;
        this.lastGuess = 0L;
    }
    
    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent event) {
        if (!this.isToggled()) {
            return;
        }
        try {
            final ScoreObjective o = Minecraft.getMinecraft().player.getWorldScoreboard().getObjectiveInDisplaySlot(0);
            if (ChatFormatting.stripFormatting(((o == null) ? Minecraft.getMinecraft().player.getWorldScoreboard().getObjectiveInDisplaySlot(1) : o).getDisplayName()).contains("GUESS THE BUILD") && ChatFormatting.stripFormatting(event.message.getFormattedText()).startsWith("This game has been recorded")) {
                Minecraft.getMinecraft().player.sendChatMessage("/play build_battle_guess_the_build");
            }
        }
        catch (Exception ex) {}
        if (event.message.getFormattedText().startsWith("§eThe theme was") && this.guesses != null) {
            this.guesses.stop();
            this.guesses = null;
        }
        if (ChatFormatting.stripFormatting(event.message.getFormattedText()).startsWith(Minecraft.getMinecraft().player.getName() + " correctly guessed the theme!") && this.guesses != null) {
            this.guesses.stop();
            this.guesses = null;
        }
        if (event.type == 2) {
            if (event.message.getFormattedText().contains("The theme is") && event.message.getFormattedText().contains("_")) {
                if (this.wordList.isEmpty()) {
                    this.loadWords();
                }
                final int newTips = this.getTips(event.message.getFormattedText());
                if (newTips != this.tips) {
                    this.tips = newTips;
                    final String tip = ChatFormatting.stripFormatting(event.message.getFormattedText()).replaceFirst("The theme is ", "");
                    final ArrayList<String> matchingWords = this.getMatchingWords(tip);
                    if (matchingWords.size() == 1) {
                        Notifications.showNotification("Oringo Client", "Found 1 matching word! \nSending: §f" + matchingWords.get(0), 2000);
                        if (this.guesses != null) {
                            this.guesses.stop();
                            this.guesses = null;
                            final ArrayList<String> list;
                            new Thread(() -> {
                                try {
                                    Thread.sleep(this.period - (System.currentTimeMillis() - this.lastGuess));
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Minecraft.getMinecraft().player.sendChatMessage("/ac " + list.get(0).toLowerCase());
                            }).start();
                            return;
                        }
                        Minecraft.getMinecraft().player.sendChatMessage("/ac " + matchingWords.get(0).toLowerCase());
                    }
                    else {
                        Notifications.showNotification("Oringo Client", String.format("Found %s matching words!", matchingWords.size()), 1500);
                        if (matchingWords.size() <= 15) {
                            Collections.shuffle(matchingWords);
                            final Iterator<String> iterator;
                            String matchingWord;
                            (this.guesses = new Thread(() -> {
                                matchingWords.iterator();
                                while (iterator.hasNext()) {
                                    matchingWord = iterator.next();
                                    this.lastGuess = System.currentTimeMillis();
                                    Minecraft.getMinecraft().player.sendChatMessage("/ac " + matchingWord.toLowerCase());
                                    Notifications.showNotification("Oringo Client", "Trying: §f" + matchingWord, 2000);
                                    try {
                                        Thread.sleep(this.period);
                                    }
                                    catch (Exception e2) {
                                        e2.printStackTrace();
                                    }
                                }
                            })).start();
                        }
                    }
                }
            }
            else {
                this.tips = 0;
            }
        }
    }
    
    @SubscribeEvent
    public void onGuiOpen(final GuiOpenEvent event) {
        if (!this.isToggled()) {
            return;
        }
        Minecraft mc;
        ScoreObjective o;
        new Thread(() -> {
            try {
                Thread.sleep(1000L);
                if (event.gui instanceof GuiChest) {
                    mc = Minecraft.getMinecraft();
                    o = mc.player.getWorldScoreboard().getObjectiveInDisplaySlot(0);
                    if (ChatFormatting.stripFormatting(((o == null) ? mc.player.getWorldScoreboard().getObjectiveInDisplaySlot(1) : o).getDisplayName()).contains("GUESS THE BUILD")) {
                        mc.playerController.func_78753_a(((GuiChest)event.gui).inventorySlots.windowId, 15, 0, 0, (EntityPlayer)mc.player);
                        Thread.sleep(2000L);
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
                        Thread.sleep(2000L);
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
                        mc.player.inventory.currentItem = 1;
                        mc.player.rotationPitch = 40.0f;
                        Thread.sleep(500L);
                        SkyblockUtils.rightClick();
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    private int getTips(final String text) {
        return text.replaceAll(" ", "").replaceAll("_", "").length();
    }
    
    private void loadWords() {
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(OringoClient.class.getClassLoader().getResourceAsStream("words.txt")));
            String line;
            while ((line = br.readLine()) != null) {
                this.wordList.add(line);
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            OringoClient.sendMessageWithPrefix("Couldn't load word list!");
        }
    }
    
    public ArrayList<String> getMatchingWords(final String tip) {
        final ArrayList<String> list = new ArrayList<String>();
        for (final String word : this.wordList) {
            if (word.length() == tip.length()) {
                boolean matching = true;
                for (int i = 0; i < word.length(); ++i) {
                    if (tip.charAt(i) == '_') {
                        if (word.charAt(i) != ' ') {
                            continue;
                        }
                        matching = false;
                    }
                    if (tip.charAt(i) != word.charAt(i)) {
                        matching = false;
                    }
                    if (!matching) {
                        break;
                    }
                }
                if (!matching) {
                    continue;
                }
                list.add(word);
            }
        }
        return list;
    }
}
