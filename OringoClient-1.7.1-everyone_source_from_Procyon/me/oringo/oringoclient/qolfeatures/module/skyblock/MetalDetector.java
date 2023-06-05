//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.skyblock;

import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraft.init.Blocks;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import net.minecraft.util.BlockPos;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class MetalDetector extends Module
{
    public ModeSetting mode;
    public NumberSetting tries;
    private static BlockPos[] chests;
    private double distances;
    private BlockPos coords;
    private BlockPos bestCoordinates;
    private int showChestLocation;
    private BlockPos baseCoordinates;
    private boolean searchingForBase;
    private int searchForBaseCooldown;
    
    public MetalDetector() {
        super("Metal Detector", Category.SKYBLOCK);
        this.mode = new ModeSetting("Mode", "Brute", new String[] { "Brute", "Smart" });
        this.tries = new NumberSetting("Tries", 4.0, 1.0, 10.0, 1.0) {
            @Override
            public boolean isHidden() {
                return !MetalDetector.this.mode.is("Brute");
            }
        };
        this.distances = 0.0;
        this.coords = null;
        this.showChestLocation = 0;
        this.searchingForBase = false;
        this.searchForBaseCooldown = 0;
        this.addSettings(this.mode, this.tries);
    }
    
    @SubscribeEvent
    public void onUpdate(final MotionUpdateEvent.Pre event) {
        if (!this.isToggled()) {
            return;
        }
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Smart": {
                if (this.searchForBaseCooldown > 0) {
                    --this.searchForBaseCooldown;
                }
                if (this.showChestLocation <= 0) {
                    return;
                }
                --this.showChestLocation;
                final BlockPos coordinates1 = this.getClosestChestLocation();
                if (coordinates1 == null) {
                    return;
                }
                this.clickBlock(this.bestCoordinates = coordinates1);
                break;
            }
            case "Brute": {
                if (this.baseCoordinates != null && OringoClient.mc.player.func_70694_bm() != null && OringoClient.mc.player.func_70694_bm().getDisplayName().contains("Metal Detector")) {
                    for (int i = 0; i < this.tries.getValue(); ++i) {
                        final BlockPos coordinates2 = MetalDetector.chests[(OringoClient.mc.player.ticksExisted + i) % MetalDetector.chests.length];
                        this.clickBlock(new BlockPos(this.baseCoordinates.getX() - coordinates2.getX(), this.baseCoordinates.getY() - coordinates2.getY(), this.baseCoordinates.getZ() - coordinates2.getZ()));
                    }
                    break;
                }
                break;
            }
        }
    }
    
    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent e) {
        if (e.type == 2 && e.message.getFormattedText().contains("§3§lTREASURE:")) {
            if (this.baseCoordinates == null) {
                this.findBaseCoordinates();
            }
            if (this.mode.is("Smart")) {
                this.showChestLocation = 10;
                this.distances = Double.parseDouble(ChatFormatting.stripFormatting(e.message.getFormattedText().substring(e.message.getFormattedText().length() - 3 - 6, e.message.getFormattedText().length() - 3)));
                this.coords = new BlockPos(OringoClient.mc.player.posX, OringoClient.mc.player.posY, OringoClient.mc.player.posZ);
            }
        }
        else if (e.type == 1 && e.message.getFormattedText().startsWith("&r&aYou found ") && e.message.getFormattedText().endsWith("with your &r&cMetal Detector&r&a!&r")) {
            this.reset();
        }
    }
    
    private void reset() {
        this.distances = 0.0;
        this.coords = null;
        this.bestCoordinates = null;
        this.showChestLocation = 0;
    }
    
    private void findBaseCoordinates() {
        if (this.searchForBaseCooldown > 0) {
            return;
        }
        final int x;
        final int y;
        final int z;
        int i;
        int j;
        int k;
        new Thread(() -> {
            this.searchingForBase = true;
            x = (int)OringoClient.mc.player.posX;
            y = (int)OringoClient.mc.player.posY;
            z = (int)OringoClient.mc.player.posZ;
            for (i = x - 50; i < x + 50; ++i) {
                for (j = y + 30; j >= y - 30; --j) {
                    for (k = z - 50; k < z + 50; ++k) {
                        if (OringoClient.mc.world.getBlockState(new BlockPos(i, j, k)).getBlock() == Blocks.QUARTZ_STAIRS) {
                            if (OringoClient.mc.world.getBlockState(new BlockPos(i, j + 13, k)).getBlock() == Blocks.BARRIER) {
                                this.baseCoordinates = this.getBaseCoordinates(i, j + 13, k);
                                break;
                            }
                        }
                    }
                }
            }
            this.searchingForBase = false;
            return;
        }).start();
        this.searchForBaseCooldown = 15;
    }
    
    private BlockPos getBaseCoordinates(final int x, final int y, final int z) {
        boolean loop = true;
        int posX = x;
        int posY = y;
        int posZ = z;
        if (OringoClient.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.BARRIER) {
            return new BlockPos(x, y, z);
        }
        while (loop) {
            loop = false;
            if (OringoClient.mc.world.getBlockState(new BlockPos(posX + 1, posY, posZ)).getBlock() == Blocks.BARRIER) {
                ++posX;
                loop = true;
            }
            if (OringoClient.mc.world.getBlockState(new BlockPos(posX, posY - 1, posZ)).getBlock() == Blocks.BARRIER) {
                --posY;
                loop = true;
            }
            if (OringoClient.mc.world.getBlockState(new BlockPos(posX, posY, posZ + 1)).getBlock() == Blocks.BARRIER) {
                ++posZ;
                loop = true;
            }
        }
        return new BlockPos(posX, posY, posZ);
    }
    
    double getDistance(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        return Math.sqrt(Math.pow(x1 - x2, 2.0) + Math.pow(y1 - y2, 2.0) + Math.pow(z1 - z2, 2.0));
    }
    
    private BlockPos getClosestChestLocation() {
        if (this.baseCoordinates == null) {
            return null;
        }
        BlockPos bestChestLocation = null;
        double minDistance = 100000.0;
        if (this.distances == 0.0) {
            return null;
        }
        for (final BlockPos coordinates : MetalDetector.chests) {
            final double currentDistance = Math.abs(this.getDistance(this.coords.getX(), this.coords.getY(), this.coords.getZ(), this.baseCoordinates.getX() - coordinates.getX(), this.baseCoordinates.getY() - coordinates.getY(), this.baseCoordinates.getZ() - coordinates.getZ()) - this.distances);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                bestChestLocation = new BlockPos(this.baseCoordinates.getX() - coordinates.getX(), this.baseCoordinates.getY() - coordinates.getY(), this.baseCoordinates.getZ() - coordinates.getZ());
            }
        }
        return bestChestLocation;
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load event) {
        this.reset();
        this.baseCoordinates = null;
    }
    
    private void clickBlock(final BlockPos hitPos) {
        OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, hitPos, EnumFacing.fromAngle((double)OringoClient.mc.player.rotationYaw)));
    }
    
    static {
        MetalDetector.chests = new BlockPos[] { new BlockPos(-7, 26, -2), new BlockPos(-15, 26, 31), new BlockPos(-17, 26, 19), new BlockPos(47, 25, 33), new BlockPos(36, 26, 45), new BlockPos(48, 27, 45), new BlockPos(45, 27, -13), new BlockPos(-38, 26, 21), new BlockPos(42, 26, 27), new BlockPos(29, 27, -7), new BlockPos(22, 26, -15), new BlockPos(-7, 27, -26), new BlockPos(-2, 26, -6), new BlockPos(43, 27, -21), new BlockPos(10, 26, -11), new BlockPos(17, 26, 49), new BlockPos(19, 26, -17), new BlockPos(-35, 27, 35), new BlockPos(25, 27, 5), new BlockPos(-37, 24, 46), new BlockPos(-24, 26, 49), new BlockPos(-7, 26, 48), new BlockPos(-14, 27, -24), new BlockPos(-18, 27, 44), new BlockPos(-1, 26, -23), new BlockPos(41, 25, -37), new BlockPos(19, 26, -38), new BlockPos(-7, 27, 27), new BlockPos(42, 26, 19), new BlockPos(42, 26, 19), new BlockPos(-33, 27, 31), new BlockPos(6, 27, 25), new BlockPos(-2, 26, -17), new BlockPos(-15, 27, 5), new BlockPos(-20, 27, -12), new BlockPos(-25, 26, 30), new BlockPos(28, 27, -35), new BlockPos(-19, 27, -22), new BlockPos(4, 26, -15), new BlockPos(36, 26, 17) };
    }
}
