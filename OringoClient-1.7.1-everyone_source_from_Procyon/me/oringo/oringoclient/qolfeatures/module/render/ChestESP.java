//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import me.oringo.oringoclient.utils.RenderUtils;
import net.minecraft.tileentity.TileEntity;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.tileentity.TileEntityChest;
import me.oringo.oringoclient.OringoClient;
import java.util.List;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class ChestESP extends Module
{
    public BooleanSetting tracer;
    
    public ChestESP() {
        super("ChestESP", Category.RENDER);
        this.tracer = new BooleanSetting("Tracer", true);
        this.addSettings(this.tracer);
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (!this.isToggled()) {
            return;
        }
        for (final TileEntity tileEntityChest : (List)OringoClient.mc.world.loadedTileEntityList.stream().filter(tileEntity -> tileEntity instanceof TileEntityChest).collect(Collectors.toList())) {
            RenderUtils.blockBox(tileEntityChest.getPos(), OringoClient.clickGui.getColor());
            if (this.tracer.isEnabled()) {
                RenderUtils.tracerLine(tileEntityChest.getPos().getX() + 0.5, tileEntityChest.getPos().getY() + 0.5, tileEntityChest.getPos().getZ() + 0.5, OringoClient.clickGui.getColor());
            }
        }
    }
}
