//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.skyblock;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.server.S02PacketChat;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import com.mojang.authlib.properties.Property;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import me.oringo.oringoclient.utils.RotationUtils;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import me.oringo.oringoclient.utils.SkyblockUtils;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import net.minecraft.util.BlockPos;
import java.util.ArrayList;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.StringSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class SecretAura extends Module
{
    public NumberSetting reach;
    public StringSetting item;
    public BooleanSetting cancelChest;
    public BooleanSetting clickedCheck;
    public BooleanSetting rotation;
    public static ArrayList<BlockPos> clicked;
    public static boolean inBoss;
    private boolean sent;
    
    public SecretAura() {
        super("Secret Aura", 0, Category.SKYBLOCK);
        this.reach = new NumberSetting("Reach", 5.0, 2.0, 6.0, 0.1);
        this.item = new StringSetting("Item");
        this.cancelChest = new BooleanSetting("Cancel chests", true);
        this.clickedCheck = new BooleanSetting("Clicked check", true);
        this.rotation = new BooleanSetting("Rotation", false);
        this.addSettings(this.reach, this.item, this.rotation, this.cancelChest, this.clickedCheck);
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onUpdatePre(final MotionUpdateEvent.Pre event) {
        if (OringoClient.mc.player != null && this.isToggled() && SkyblockUtils.inDungeon && this.rotation.isEnabled()) {
            final Vec3i vec3i = new Vec3i(10, 10, 10);
            for (final BlockPos blockPos : BlockPos.getAllInBox(new BlockPos((Vec3i)OringoClient.mc.player.getPosition()).add(vec3i), new BlockPos((Vec3i)OringoClient.mc.player.getPosition().subtract(vec3i)))) {
                if (this.isValidBlock(blockPos) && OringoClient.mc.player.getDistance((double)blockPos.getX(), (double)(blockPos.getY() - OringoClient.mc.player.getEyeHeight()), (double)blockPos.getZ()) < this.reach.getValue()) {
                    final float[] floats = RotationUtils.getAngles(new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5));
                    event.pitch = floats[1];
                    event.yaw = floats[0];
                    break;
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onTick(final MotionUpdateEvent.Post event) {
        if (OringoClient.mc.player != null && this.isToggled() && SkyblockUtils.inDungeon) {
            final Vec3i vec3i = new Vec3i(10, 10, 10);
            for (final BlockPos blockPos : BlockPos.getAllInBox(new BlockPos((Vec3i)OringoClient.mc.player.getPosition()).add(vec3i), new BlockPos((Vec3i)OringoClient.mc.player.getPosition().subtract(vec3i)))) {
                if (this.isValidBlock(blockPos) && OringoClient.mc.player.getDistance((double)blockPos.getX(), (double)(blockPos.getY() - OringoClient.mc.player.getEyeHeight()), (double)blockPos.getZ()) < this.reach.getValue()) {
                    this.interactWithBlock(blockPos);
                    if (this.rotation.isEnabled()) {
                        break;
                    }
                    continue;
                }
            }
        }
    }
    
    private boolean isValidBlock(final BlockPos blockPos) {
        final Block block = OringoClient.mc.world.getBlockState(blockPos).getBlock();
        if (block == Blocks.SKULL) {
            final TileEntitySkull tileEntity = (TileEntitySkull)OringoClient.mc.world.getTileEntity(blockPos);
            if (tileEntity.getSkullType() == 3 && tileEntity.getPlayerProfile() != null && tileEntity.getPlayerProfile().getProperties() != null) {
                final Property property = SkyblockUtils.firstOrNull((Iterable<Property>)tileEntity.getPlayerProfile().getProperties().get((Object)"textures"));
                return property != null && property.getValue().equals("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRkYjRhZGZhOWJmNDhmZjVkNDE3MDdhZTM0ZWE3OGJkMjM3MTY1OWZjZDhjZDg5MzQ3NDlhZjRjY2U5YiJ9fX0=") && (!SecretAura.clicked.contains(blockPos) || !this.clickedCheck.isEnabled());
            }
        }
        return (block == Blocks.LEVER || block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST) && (!SecretAura.clicked.contains(blockPos) || !this.clickedCheck.isEnabled());
    }
    
    private void interactWithBlock(final BlockPos pos) {
        for (int i = 0; i < 9; ++i) {
            if (OringoClient.mc.player.inventory.getStackInSlot(i) != null && OringoClient.mc.player.inventory.getStackInSlot(i).getDisplayName().toLowerCase().contains(this.item.getValue().toLowerCase())) {
                final int holding = OringoClient.mc.player.inventory.currentItem;
                OringoClient.mc.player.inventory.currentItem = i;
                if (OringoClient.mc.world.getBlockState(pos).getBlock() == Blocks.LEVER && !SecretAura.inBoss) {
                    OringoClient.mc.playerController.func_178890_a(OringoClient.mc.player, OringoClient.mc.world, OringoClient.mc.player.inventory.getCurrentItem(), pos, EnumFacing.fromAngle((double)OringoClient.mc.player.rotationYaw), new Vec3(0.0, 0.0, 0.0));
                }
                OringoClient.mc.playerController.func_178890_a(OringoClient.mc.player, OringoClient.mc.world, OringoClient.mc.player.inventory.getCurrentItem(), pos, EnumFacing.fromAngle((double)OringoClient.mc.player.rotationYaw), new Vec3(0.0, 0.0, 0.0));
                OringoClient.mc.player.inventory.currentItem = holding;
                SecretAura.clicked.add(pos);
                return;
            }
        }
        if (!this.sent) {
            OringoClient.sendMessageWithPrefix("You don't have a required item in your hotbar!");
            this.sent = true;
        }
    }
    
    @SubscribeEvent
    public void onChat(final PacketReceivedEvent event) {
        if (event.packet instanceof S02PacketChat && ChatFormatting.stripFormatting(((S02PacketChat)event.packet).getChatComponent().getFormattedText()).startsWith("[BOSS] Necron")) {
            SecretAura.inBoss = true;
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketReceivedEvent event) {
        if (event.packet instanceof S2DPacketOpenWindow && ChatFormatting.stripFormatting(((S2DPacketOpenWindow)event.packet).getWindowTitle().getFormattedText()).equals("Chest") && SkyblockUtils.inDungeon && this.cancelChest.isEnabled()) {
            event.setCanceled(true);
            OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C0DPacketCloseWindow(((S2DPacketOpenWindow)event.packet).getWindowId()));
        }
    }
    
    @SubscribeEvent
    public void clear(final WorldEvent.Load event) {
        SecretAura.inBoss = false;
        SecretAura.clicked.clear();
    }
    
    static {
        SecretAura.clicked = new ArrayList<BlockPos>();
    }
}
