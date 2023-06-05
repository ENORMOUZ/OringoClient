//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.skyblock;

import net.minecraft.block.Block;
import me.oringo.oringoclient.utils.SkyblockUtils;
import com.mojang.authlib.properties.Property;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import me.oringo.oringoclient.OringoClient;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class GhostBlocks extends Module
{
    public NumberSetting range;
    public BooleanSetting cordGhostBlocks;
    public ModeSetting mode;
    private boolean wasPressed;
    private final int[][] cords;
    
    public GhostBlocks() {
        super("Ghost Blocks", 0, Category.SKYBLOCK);
        this.range = new NumberSetting("Range", 10.0, 1.0, 100.0, 1.0);
        this.cordGhostBlocks = new BooleanSetting("Cord blocks", true);
        this.mode = new ModeSetting("Speed", "Fast", new String[] { "Slow", "Fast" });
        this.cords = new int[][] { { 275, 220, 231 }, { 275, 220, 232 }, { 299, 168, 243 }, { 299, 168, 244 }, { 299, 168, 246 }, { 299, 168, 247 }, { 299, 168, 247 }, { 300, 168, 247 }, { 300, 168, 246 }, { 300, 168, 244 }, { 300, 168, 243 }, { 298, 168, 247 }, { 298, 168, 246 }, { 298, 168, 244 }, { 298, 168, 243 }, { 287, 167, 240 }, { 288, 167, 240 }, { 289, 167, 240 }, { 290, 167, 240 }, { 291, 167, 240 }, { 292, 167, 240 }, { 293, 167, 240 }, { 294, 167, 240 }, { 295, 167, 240 }, { 290, 167, 239 }, { 291, 167, 239 }, { 292, 167, 239 }, { 293, 167, 239 }, { 294, 167, 239 }, { 295, 167, 239 }, { 290, 166, 239 }, { 291, 166, 239 }, { 292, 166, 239 }, { 293, 166, 239 }, { 294, 166, 239 }, { 295, 166, 239 }, { 290, 166, 240 }, { 291, 166, 240 }, { 292, 166, 240 }, { 293, 166, 240 }, { 294, 166, 240 }, { 295, 166, 240 } };
        this.addSettings(this.range, this.mode, this.cordGhostBlocks);
    }
    
    @SubscribeEvent
    public void onKey(final TickEvent.ClientTickEvent event) {
        if (OringoClient.mc.currentScreen != null || OringoClient.mc.world == null || !this.isToggled()) {
            return;
        }
        if (this.cordGhostBlocks.isEnabled() && SecretAura.inBoss) {
            for (final int[] i : this.cords) {
                OringoClient.mc.world.setBlockToAir(new BlockPos(i[0], i[1], i[2]));
            }
        }
        if (this.isPressed() && ((this.mode.getSelected().equals("Slow") && !this.wasPressed) || this.mode.getSelected().equals("Fast"))) {
            final Vec3 vec3 = OringoClient.mc.player.getPositionEyes(0.0f);
            final Vec3 vec4 = OringoClient.mc.player.getLook(0.0f);
            final Vec3 vec5 = vec3.add(vec4.x * this.range.getValue(), vec4.y * this.range.getValue(), vec4.z * this.range.getValue());
            final BlockPos obj = OringoClient.mc.world.rayTraceBlocks(vec3, vec5, true, false, true).getBlockPos();
            if (this.isValidBlock(obj)) {
                return;
            }
            OringoClient.mc.world.setBlockToAir(obj);
        }
        this.wasPressed = this.isPressed();
    }
    
    @Override
    public boolean isKeybind() {
        return true;
    }
    
    private boolean isValidBlock(final BlockPos blockPos) {
        final Block block = OringoClient.mc.world.getBlockState(blockPos).getBlock();
        if (block == Blocks.SKULL) {
            final TileEntitySkull tileEntity = (TileEntitySkull)OringoClient.mc.world.getTileEntity(blockPos);
            if (tileEntity.getSkullType() == 3 && tileEntity.getPlayerProfile() != null && tileEntity.getPlayerProfile().getProperties() != null) {
                final Property property = SkyblockUtils.firstOrNull((Iterable<Property>)tileEntity.getPlayerProfile().getProperties().get((Object)"textures"));
                return property != null && property.getValue().equals("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRkYjRhZGZhOWJmNDhmZjVkNDE3MDdhZTM0ZWE3OGJkMjM3MTY1OWZjZDhjZDg5MzQ3NDlhZjRjY2U5YiJ9fX0=");
            }
        }
        return block == Blocks.LEVER || block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST;
    }
}
