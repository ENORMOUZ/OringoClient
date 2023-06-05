//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands.nucleus;

import org.jetbrains.annotations.NotNull;
import net.minecraft.command.CommandException;
import net.minecraft.init.Blocks;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.utils.Notifications;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import java.util.HashMap;
import net.minecraft.command.ICommand;

public class SaveCommand implements ICommand
{
    public static HashMap<String, BlockPos> posHashMap;
    public static HashMap<String, Integer> idHashMap;
    
    public String getName() {
        return "save";
    }
    
    public String getUsage(final ICommandSender iCommandSender) {
        return "/save";
    }
    
    public List<String> getAliases() {
        return new ArrayList<String>();
    }
    
    public void func_71515_b(final ICommandSender iCommandSender, final String[] args) throws CommandException {
        if (args.length < 1) {
            Notifications.showNotification("Oringo Client", "/save name", 2500);
            return;
        }
        if (args[0].toLowerCase().equals("list")) {
            SaveCommand.idHashMap.forEach((key, value) -> OringoClient.sendMessageWithPrefix(key + " " + value.toString()));
            SaveCommand.posHashMap.forEach((key, value) -> OringoClient.sendMessageWithPrefix(key + " " + value.toString()));
        }
        else if (OringoClient.mc.objectMouseOver != null) {
            switch (OringoClient.mc.objectMouseOver.typeOfHit) {
                case ENTITY: {
                    if (OringoClient.mc.objectMouseOver.entityHit != null) {
                        if (!SaveCommand.posHashMap.containsKey(args[0])) {
                            SaveCommand.idHashMap.put(args[0], OringoClient.mc.objectMouseOver.entityHit.getEntityId());
                        }
                        else {
                            SaveCommand.idHashMap.replace(args[0], OringoClient.mc.objectMouseOver.entityHit.getEntityId());
                        }
                        Notifications.showNotification("Oringo Client", "Added " + OringoClient.mc.objectMouseOver.entityHit.getEntityId() + " to list", 2000);
                        break;
                    }
                    break;
                }
                case BLOCK: {
                    if (OringoClient.mc.objectMouseOver.getBlockPos() != null && OringoClient.mc.world.getBlockState(OringoClient.mc.objectMouseOver.getBlockPos()).getBlock() != Blocks.AIR) {
                        if (!SaveCommand.posHashMap.containsKey(args[0])) {
                            SaveCommand.posHashMap.put(args[0], OringoClient.mc.objectMouseOver.getBlockPos());
                        }
                        else {
                            SaveCommand.posHashMap.replace(args[0], OringoClient.mc.objectMouseOver.getBlockPos());
                        }
                        Notifications.showNotification("Oringo Client", "Added " + OringoClient.mc.objectMouseOver.getBlockPos() + " to list", 2000);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    public boolean func_71519_b(final ICommandSender iCommandSender) {
        return true;
    }
    
    public List<String> func_180525_a(final ICommandSender iCommandSender, final String[] strings, final BlockPos blockPos) {
        return new ArrayList<String>();
    }
    
    public boolean isUsernameIndex(final String[] strings, final int i) {
        return false;
    }
    
    public int compareTo(@NotNull final ICommand o) {
        return 0;
    }
    
    static {
        SaveCommand.posHashMap = new HashMap<String, BlockPos>();
        SaveCommand.idHashMap = new HashMap<String, Integer>();
    }
}
