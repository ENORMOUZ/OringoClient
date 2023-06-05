//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.player;

import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor;
import java.util.Iterator;
import net.minecraft.inventory.Slot;
import me.oringo.oringoclient.utils.Notifications;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.oringo.oringoclient.OringoClient;
import org.lwjgl.input.Mouse;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiScreenEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import java.util.Arrays;
import java.util.List;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class InvManager extends Module
{
    public NumberSetting delay;
    public BooleanSetting dropTrash;
    public BooleanSetting autoArmor;
    public BooleanSetting middleClick;
    public ModeSetting mode;
    private MilliTimer timer;
    private boolean wasPressed;
    public NumberSetting swordSlot;
    public NumberSetting blockSlot;
    public NumberSetting gappleSlot;
    public NumberSetting pickaxeSlot;
    public NumberSetting axeSlot;
    public NumberSetting shovelSlot;
    public NumberSetting bowSlot;
    private List<String> dropSkyblock;
    private List<String> dropSkywars;
    public static List<String> dropCustom;
    
    public InvManager() {
        super("Inventory Manager", 0, Category.PLAYER);
        this.delay = new NumberSetting("Delay", 30.0, 0.0, 300.0, 1.0);
        this.dropTrash = new BooleanSetting("Drop trash", true);
        this.autoArmor = new BooleanSetting("Auto Armor", false);
        this.middleClick = new BooleanSetting("Middle click to drop", false);
        this.mode = new ModeSetting("Trash items", "Skyblock", new String[] { "Skyblock", "Skywars", "Custom" });
        this.timer = new MilliTimer();
        this.swordSlot = new NumberSetting("Sword slot", 0.0, 0.0, 9.0, 1.0);
        this.blockSlot = new NumberSetting("Block slot", 0.0, 0.0, 9.0, 1.0);
        this.gappleSlot = new NumberSetting("Gapple slot", 0.0, 0.0, 9.0, 1.0);
        this.pickaxeSlot = new NumberSetting("Pickaxe slot", 0.0, 0.0, 9.0, 1.0);
        this.axeSlot = new NumberSetting("Axe slot", 0.0, 0.0, 9.0, 1.0);
        this.shovelSlot = new NumberSetting("Shovel slot", 0.0, 0.0, 9.0, 1.0);
        this.bowSlot = new NumberSetting("Bow slot", 0.0, 0.0, 9.0, 1.0);
        this.dropSkyblock = Arrays.asList("Training Weight", "Healing Potion", "Beating Heart", "Premium Flesh", "Mimic Fragment", "Enchanted Rotten Flesh", "Machine Gun Bow", "Enchanted Bone", "Defuse Kit", "Enchanted Ice", "Diamond Atom", "Silent Death", "Cutlass", "Soulstealer Bow", "Sniper Bow", "Optical Lens", "Tripwire Hook", "Button", "Carpet", "Lever", "Journal Entry", "Sign", "Zombie Commander", "Zombie Lord", "Skeleton Master, Skeleton Grunt, Skeleton Lord, Zombie Soldier", "Zombie Knight", "Heavy", "Super Heavy", "Undead", "Bouncy", "Skeletor", "Trap", "Inflatable Jerry");
        this.dropSkywars = Arrays.asList("Egg", "Snowball", "Poison", "Lava", "Steak", "Enchanting");
        this.addSettings(this.delay, this.dropTrash, this.mode, this.middleClick, this.autoArmor);
    }
    
    @SubscribeEvent
    public void onGui(final GuiScreenEvent.BackgroundDrawnEvent event) {
        if (event.gui instanceof GuiInventory && this.isToggled()) {
            if (this.autoArmor.isEnabled() && !SkyblockUtils.onSkyblock) {
                this.getBestArmor();
            }
            if (this.dropTrash.isEnabled()) {
                this.dropTrash();
            }
        }
    }
    
    @SubscribeEvent
    public void onTooltip(final ItemTooltipEvent event) {
        if (Mouse.isButtonDown(2) && OringoClient.mc.currentScreen instanceof GuiInventory && this.middleClick.isEnabled()) {
            if (!this.wasPressed) {
                this.wasPressed = true;
                final String name = ChatFormatting.stripFormatting(event.itemStack.getDisplayName());
                if (InvManager.dropCustom.contains(name)) {
                    InvManager.dropCustom.remove(name);
                    Notifications.showNotification("Oringo Client", "Removed " + name + " from custom drop list", 2000);
                }
                else {
                    InvManager.dropCustom.add(name);
                    Notifications.showNotification("Oringo Client", "Added " + ChatFormatting.AQUA + name + ChatFormatting.RESET + " to custom drop list", 2000);
                }
                save();
            }
        }
        else {
            this.wasPressed = false;
        }
    }
    
    public void dropTrash() {
        for (final Slot slot : OringoClient.mc.player.inventoryContainer.inventorySlots) {
            if (slot.getHasStack() && this.canInteract()) {
                if (this.mode.getSelected().equals("Custom")) {
                    if (!InvManager.dropCustom.contains(ChatFormatting.stripFormatting(slot.getStack().getDisplayName()))) {
                        continue;
                    }
                    this.drop(slot.slotNumber);
                    this.pause();
                }
                else if (this.mode.getSelected().equals("Skyblock") && this.dropSkyblock.stream().anyMatch(a -> a.contains(ChatFormatting.stripFormatting(slot.getStack().getDisplayName())))) {
                    this.drop(slot.slotNumber);
                    this.pause();
                }
                else {
                    if (!this.mode.getSelected().equals("Skywars") || !this.dropSkywars.stream().anyMatch(a -> a.contains(ChatFormatting.stripFormatting(slot.getStack().getDisplayName())))) {
                        continue;
                    }
                    this.drop(slot.slotNumber);
                    this.pause();
                }
            }
        }
    }
    
    public void getBestArmor() {
        for (int i = 5; i < 9; ++i) {
            if (OringoClient.mc.player.inventoryContainer.getSlot(i).getHasStack() && this.canInteract()) {
                final ItemStack armor = OringoClient.mc.player.inventoryContainer.getSlot(i).getStack();
                if (!isBestArmor(armor, i)) {
                    this.drop(i);
                    this.pause();
                }
            }
        }
        for (int i = 9; i < 45; ++i) {
            if (OringoClient.mc.player.inventoryContainer.getSlot(i).getHasStack() && this.canInteract()) {
                final ItemStack stack = OringoClient.mc.player.inventoryContainer.getSlot(i).getStack();
                if (stack.getItem() instanceof ItemArmor) {
                    if (isBestArmor(stack, i)) {
                        this.shiftClick(i);
                    }
                    else {
                        this.drop(i);
                    }
                    this.pause();
                }
            }
        }
    }
    
    public static boolean isBestArmor(final ItemStack armor, final int slot) {
        if (!(armor.getItem() instanceof ItemArmor)) {
            return false;
        }
        for (int i = 5; i < 45; ++i) {
            if (OringoClient.mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = OringoClient.mc.player.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemArmor && ((getProtection(is) > getProtection(armor) && slot < 9) || (slot >= 9 && getProtection(is) >= getProtection(armor) && slot != i)) && ((ItemArmor)is.getItem()).armorType == ((ItemArmor)armor.getItem()).armorType) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static float getProtection(final ItemStack stack) {
        float prot = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            final ItemArmor armor = (ItemArmor)stack.getItem();
            prot += (float)(armor.damageReduceAmount + (100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.PROTECTION.field_77352_x, stack) * 0.0075);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.field_77327_f.field_77352_x, stack) / 100.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.FIRE_PROTECTION.field_77352_x, stack) / 100.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.THORNS.field_77352_x, stack) / 100.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.field_77347_r.field_77352_x, stack) / 50.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.PROJECTILE_PROTECTION.field_77352_x, stack) / 100.0);
            prot += (float)(stack.getMaxDamage() / 1000.0);
        }
        return prot;
    }
    
    public static float getMaterial(final ItemStack item) {
        if (item.getItem() instanceof ItemTool) {
            return (float)(((ItemTool)item.getItem()).func_150913_i().getHarvestLevel() + EnchantmentHelper.getEnchantmentLevel(Enchantment.field_77349_p.field_77352_x, item) * 0.75);
        }
        return 0.0f;
    }
    
    public static int getBowDamage(final ItemStack bow) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.field_77345_t.field_77352_x, bow) + EnchantmentHelper.getEnchantmentLevel(Enchantment.field_77343_v.field_77352_x, bow) * 2;
    }
    
    public static float getSwordDamage(final ItemStack sword) {
        float damage = 0.0f;
        if (sword.getItem() instanceof ItemSword) {
            damage += 4.0f;
            damage += ((ItemSword)sword.getItem()).getAttackDamage();
            damage += (float)(1.25 * EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.field_77352_x, sword));
        }
        return damage;
    }
    
    private void pause() {
        this.timer.updateTime();
    }
    
    private boolean canInteract() {
        return this.timer.hasTimePassed((long)this.delay.getValue());
    }
    
    private static void save() {
        try {
            final DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("config/OringoClient/InventoryManager.cfg"));
            dataOutputStream.writeInt(InvManager.dropCustom.size());
            for (final String s : InvManager.dropCustom) {
                dataOutputStream.writeUTF(s);
            }
            dataOutputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void shiftClick(final int slot) {
        OringoClient.mc.playerController.func_78753_a(OringoClient.mc.player.inventoryContainer.windowId, slot, 0, 1, (EntityPlayer)OringoClient.mc.player);
    }
    
    public void numberClick(final int slot, final int button) {
        OringoClient.mc.playerController.func_78753_a(OringoClient.mc.player.inventoryContainer.windowId, slot, button, 2, (EntityPlayer)OringoClient.mc.player);
    }
    
    public void drop(final int slot) {
        OringoClient.mc.playerController.func_78753_a(OringoClient.mc.player.inventoryContainer.windowId, slot, 1, 4, (EntityPlayer)OringoClient.mc.player);
    }
    
    static {
        InvManager.dropCustom = new ArrayList<String>();
    }
}
