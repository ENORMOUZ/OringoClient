//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.player;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C02PacketUseEntity;
import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.events.PacketSentEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AutoTool extends Module
{
    public BooleanSetting tools;
    public BooleanSetting swords;
    private MilliTimer delay;
    
    public AutoTool() {
        super("Auto Tool", Category.PLAYER);
        this.tools = new BooleanSetting("Tools", true);
        this.swords = new BooleanSetting("Swords", true);
        this.delay = new MilliTimer();
        this.addSettings(this.tools, this.swords);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
        if (!this.isToggled() || OringoClient.mc.player == null) {
            return;
        }
        if (this.tools.isEnabled() && !OringoClient.mc.player.func_71039_bw() && event.packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging)event.packet).getAction() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = OringoClient.mc.player.inventory.getStackInSlot(i);
                final Block block = OringoClient.mc.world.getBlockState(((C07PacketPlayerDigging)event.packet).getPosition()).getBlock();
                if (stack != null && block != null && stack.getDestroySpeed(block) > ((OringoClient.mc.player.inventory.getCurrentItem() == null) ? 1.0f : OringoClient.mc.player.inventory.getCurrentItem().getDestroySpeed(block))) {
                    OringoClient.mc.player.inventory.currentItem = i;
                }
            }
            SkyblockUtils.updateItemNoEvent();
        }
        else if (this.delay.hasTimePassed(500L) && !OringoClient.mc.player.func_71039_bw() && this.swords.isEnabled() && event.packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.packet).getAction() == C02PacketUseEntity.Action.ATTACK) {
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = OringoClient.mc.player.inventory.getStackInSlot(i);
                if (stack != null && getToolDamage(stack) > ((OringoClient.mc.player.inventory.getCurrentItem() == null) ? 0.0f : getToolDamage(OringoClient.mc.player.inventory.getCurrentItem()))) {
                    OringoClient.mc.player.inventory.currentItem = i;
                }
            }
            SkyblockUtils.updateItemNoEvent();
        }
        if ((event.packet instanceof C09PacketHeldItemChange && OringoClient.mc.player.inventory.getStackInSlot(((C09PacketHeldItemChange)event.packet).getSlotId()) != null) || (event.packet instanceof C08PacketPlayerBlockPlacement && ((C08PacketPlayerBlockPlacement)event.packet).func_149574_g() != null)) {
            this.delay.updateTime();
        }
    }
    
    public static float getToolDamage(final ItemStack tool) {
        float damage = 0.0f;
        if (tool != null && (tool.getItem() instanceof ItemTool || tool.getItem() instanceof ItemSword)) {
            if (tool.getItem() instanceof ItemSword) {
                damage += 4.0f;
            }
            else if (tool.getItem() instanceof ItemAxe) {
                damage += 3.0f;
            }
            else if (tool.getItem() instanceof ItemPickaxe) {
                damage += 2.0f;
            }
            else if (tool.getItem() instanceof ItemSpade) {
                ++damage;
            }
            damage += ((tool.getItem() instanceof ItemTool) ? ((ItemTool)tool.getItem()).func_150913_i().getAttackDamage() : ((ItemSword)tool.getItem()).getAttackDamage());
            damage += (float)(1.25 * EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.field_77352_x, tool));
            damage += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.field_77352_x, tool) * 0.5);
        }
        return damage;
    }
}
