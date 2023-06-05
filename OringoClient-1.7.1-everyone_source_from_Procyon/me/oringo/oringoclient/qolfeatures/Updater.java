//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures;

import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URI;
import java.awt.Desktop;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiButton;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;

public class Updater
{
    @SubscribeEvent
    public void onGuiCreate(final GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiMainMenu && OringoClient.shouldUpdate) {
            event.buttonList.add(new GuiButton(-2137, 5, 50, 150, 20, "Update Oringo Client"));
        }
    }
    
    @SubscribeEvent
    public void onClick(final GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.gui instanceof GuiMainMenu && event.button.id == -2137) {
            try {
                Desktop.getDesktop().browse(new URI(OringoClient.vers[1]));
                OringoClient.mc.shutdown();
            }
            catch (IOException | URISyntaxException ex2) {
                final Exception ex;
                final Exception e = ex;
                e.printStackTrace();
            }
        }
    }
}
