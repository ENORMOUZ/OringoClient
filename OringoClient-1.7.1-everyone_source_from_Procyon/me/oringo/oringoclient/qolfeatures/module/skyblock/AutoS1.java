//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.skyblock;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.world.WorldEvent;
import me.oringo.oringoclient.events.BlockChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import me.oringo.oringoclient.utils.SkyblockUtils;
import me.oringo.oringoclient.OringoClient;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.util.BlockPos;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AutoS1 extends Module
{
    private boolean clicked;
    private boolean clickedButton;
    private static BlockPos clickPos;
    
    public AutoS1() {
        super("Auto SS", 0, Category.SKYBLOCK);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (OringoClient.mc.player == null || !SkyblockUtils.inDungeon || !this.isToggled() || !SkyblockUtils.inP3) {
            return;
        }
        if (OringoClient.mc.player.getPositionEyes(0.0f).distanceTo(new Vec3(309.0, 121.0, 290.0)) < 5.5 && !this.clicked && OringoClient.mc.world.getBlockState(new BlockPos(309, 121, 290)).getBlock() == Blocks.STONE_BUTTON) {
            this.clickBlock(new BlockPos(309, 121, 290));
            this.clicked = true;
            this.clickedButton = false;
        }
        if (AutoS1.clickPos != null && OringoClient.mc.player.getDistance((double)AutoS1.clickPos.getX(), (double)(AutoS1.clickPos.getY() - OringoClient.mc.player.getEyeHeight()), (double)AutoS1.clickPos.getZ()) < 5.5 && !this.clickedButton && OringoClient.mc.world.getBlockState(AutoS1.clickPos).getBlock() == Blocks.STONE_BUTTON) {
            for (int i = 0; i < 20; ++i) {
                this.clickBlock(AutoS1.clickPos);
            }
            AutoS1.clickPos = null;
            this.clickedButton = true;
            OringoClient.sendMessageWithPrefix("Clicked!");
        }
    }
    
    @SubscribeEvent
    public void onPacket(final BlockChangeEvent event) {
        if (this.clicked && !this.clickedButton && SkyblockUtils.inP3 && event.state.getBlock() == Blocks.SEA_LANTERN && event.pos.getX() == 310 && event.pos.getY() >= 120 && event.pos.getY() <= 123 && event.pos.getZ() >= 291 && event.pos.getZ() <= 294) {
            AutoS1.clickPos = new BlockPos(event.pos.getX() - 1, event.pos.getY(), event.pos.getZ());
        }
    }
    
    @SubscribeEvent
    public void onWorldChange(final WorldEvent.Load event) {
        this.clicked = false;
        AutoS1.clickPos = null;
        this.clickedButton = false;
    }
    
    private void clickBlock(final BlockPos hitPos) {
        final Vec3 hitVec = new Vec3(0.0, 0.0, 0.0);
        final float f = (float)(hitVec.x - hitPos.getX());
        final float f2 = (float)(hitVec.y - hitPos.getY());
        final float f3 = (float)(hitVec.z - hitPos.getZ());
        OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C08PacketPlayerBlockPlacement(hitPos, EnumFacing.fromAngle((double)OringoClient.mc.player.rotationYaw).getIndex(), OringoClient.mc.player.inventory.getCurrentItem(), f, f2, f3));
    }
}
