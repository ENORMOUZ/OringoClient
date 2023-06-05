//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.skyblock;

import net.minecraft.block.BlockSkull;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.block.BlockAir;
import net.minecraft.util.MathHelper;
import java.awt.Color;
import me.oringo.oringoclient.utils.font.Fonts;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import me.oringo.oringoclient.events.BlockBoundsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.mixins.MinecraftAccessor;
import net.minecraft.util.BlockPos;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Phase extends Module
{
    private int ticks;
    public NumberSetting timer;
    public ModeSetting activate;
    public BooleanSetting clip;
    public boolean isPhasing;
    public boolean wasPressed;
    public boolean canPhase;
    
    public Phase() {
        super("Stair Phase", Category.SKYBLOCK);
        this.timer = new NumberSetting("Timer", 1.0, 0.1, 1.0, 0.1);
        this.activate = new ModeSetting("Activate", "on Key", new String[] { "Auto", "on Key" });
        this.clip = new BooleanSetting("Autoclip", true);
        this.addSettings(this.timer, this.clip, this.activate);
    }
    
    @Override
    public void onDisable() {
        this.isPhasing = false;
    }
    
    @SubscribeEvent
    public void onUpdate(final MotionUpdateEvent.Pre event) {
        if (OringoClient.mc.player == null || OringoClient.mc.world == null) {
            return;
        }
        --this.ticks;
        if (this.isToggled()) {
            this.canPhase = (OringoClient.mc.player.onGround && OringoClient.mc.player.collidedVertically && isValidBlock(OringoClient.mc.world.getBlockState(new BlockPos(OringoClient.mc.player.posX, OringoClient.mc.player.posY, OringoClient.mc.player.posZ)).getBlock()));
            if (!this.isPhasing && (!this.isKeybind() || (this.isPressed() && !this.wasPressed)) && OringoClient.mc.player.onGround && OringoClient.mc.player.collidedVertically && isValidBlock(OringoClient.mc.world.getBlockState(new BlockPos(OringoClient.mc.player.posX, OringoClient.mc.player.posY, OringoClient.mc.player.posZ)).getBlock())) {
                this.isPhasing = true;
                this.ticks = 8;
            }
            else if (this.isPhasing && ((!isInsideBlock() && this.ticks < 0) || (this.isPressed() && !this.wasPressed && this.isKeybind()))) {
                OringoClient.mc.player.setVelocity(0.0, 0.0, 0.0);
                this.isPhasing = false;
            }
            if (this.isPhasing && isInsideBlock()) {
                ((MinecraftAccessor)OringoClient.mc).getTimer().field_74278_d = (float)this.timer.getValue();
            }
            else {
                ((MinecraftAccessor)OringoClient.mc).getTimer().field_74278_d = 1.0f;
            }
        }
        this.wasPressed = this.isPressed();
        if (!this.isToggled() || !this.isPhasing) {
            ((MinecraftAccessor)OringoClient.mc).getTimer().field_74278_d = 1.0f;
        }
    }
    
    @SubscribeEvent
    public void onBlockBounds(final BlockBoundsEvent event) {
        if (!this.isPhasing || OringoClient.mc.player == null || !this.isToggled()) {
            return;
        }
        if (event.collidingEntity == OringoClient.mc.player && ((event.aabb != null && event.aabb.maxY > OringoClient.mc.player.getEntityBoundingBox().minY) || OringoClient.mc.gameSettings.keyBindSneak.isKeyDown() || (this.ticks == 7 && this.clip.isEnabled()))) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (OringoClient.mc.world == null || OringoClient.mc.player == null || !this.isToggled()) {
            return;
        }
        if (this.canPhase && this.activate.is("on Key") && event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
            final ScaledResolution resolution = new ScaledResolution(OringoClient.mc);
            Fonts.fontMediumBold.drawSmoothCenteredString("Phase usage detected", resolution.getScaledWidth() / 2.0f + 0.4f, resolution.getScaledHeight() - resolution.getScaledHeight() / 4.5f + 0.5f, new Color(20, 20, 20).getRGB());
            Fonts.fontMediumBold.drawSmoothCenteredString("Phase usage detected", resolution.getScaledWidth() / 2.0f, resolution.getScaledHeight() - resolution.getScaledHeight() / 4.5f, OringoClient.clickGui.getColor().getRGB());
        }
    }
    
    public static boolean isInsideBlock() {
        for (int x = MathHelper.floor(OringoClient.mc.player.getEntityBoundingBox().minX); x < MathHelper.floor(OringoClient.mc.player.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int y = MathHelper.floor(OringoClient.mc.player.getEntityBoundingBox().minY); y < MathHelper.floor(OringoClient.mc.player.getEntityBoundingBox().maxY) + 1; ++y) {
                for (int z = MathHelper.floor(OringoClient.mc.player.getEntityBoundingBox().minZ); z < MathHelper.floor(OringoClient.mc.player.getEntityBoundingBox().maxZ) + 1; ++z) {
                    final Block block = OringoClient.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        final AxisAlignedBB boundingBox = block.getSelectedBoundingBox((World)OringoClient.mc.world, new BlockPos(x, y, z), OringoClient.mc.world.getBlockState(new BlockPos(x, y, z)));
                        if (boundingBox != null && OringoClient.mc.player.getEntityBoundingBox().intersects(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean isKeybind() {
        return this.activate.is("on Key");
    }
    
    private static boolean isValidBlock(final Block block) {
        return block instanceof BlockStairs || block instanceof BlockFence || block instanceof BlockFenceGate || block instanceof BlockWall || block == Blocks.HOPPER || block instanceof BlockSkull;
    }
}
