//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands;

import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.command.CommandException;
import java.util.function.Consumer;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import me.oringo.oringoclient.utils.Notifications;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ICommand;

public class ArmorStandsCommand implements ICommand
{
    public String getName() {
        return "armorstands";
    }
    
    public String getUsage(final ICommandSender sender) {
        return "/armorstands";
    }
    
    public List<String> getAliases() {
        return new ArrayList<String>();
    }
    
    public void func_71515_b(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length == 1) {
            try {
                final Entity entityByID = Minecraft.getMinecraft().world.getEntityByID(Integer.parseInt(args[0]));
                Minecraft.getMinecraft().playerController.func_78768_b((EntityPlayer)Minecraft.getMinecraft().player, entityByID);
            }
            catch (Exception e) {
                Notifications.showNotification("Oringo Client", "This armor stand is too far away!", 1000);
            }
            return;
        }
        Minecraft.getMinecraft().world.loadedEntityList.stream().filter(entity -> entity instanceof EntityArmorStand).filter(entity -> entity.hasCustomName()).filter(entity -> entity.getDisplayName().getFormattedText().length() > 5).sorted(Comparator.comparingDouble(entity -> entity.getDistance((Entity)Minecraft.getMinecraft().player))).forEach(ArmorStandsCommand::sendEntityInteract);
    }
    
    private static void sendEntityInteract(final Entity entity) {
        final ChatComponentText chatComponentText = new ChatComponentText("Name: " + entity.getDisplayName().getFormattedText());
        final ChatStyle style = new ChatStyle();
        style.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/armorstands " + entity.getEntityId()));
        chatComponentText.setStyle(style);
        Minecraft.getMinecraft().player.sendMessage((IChatComponent)chatComponentText);
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
}
