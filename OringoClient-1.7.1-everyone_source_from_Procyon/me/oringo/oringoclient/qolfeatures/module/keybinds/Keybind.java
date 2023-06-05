//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.keybinds;

import java.util.Iterator;
import java.util.List;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import me.oringo.oringoclient.utils.SkyblockUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import net.minecraftforge.common.MinecraftForge;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.RunnableSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.StringSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Keybind extends Module
{
    public ModeSetting button;
    public ModeSetting mode;
    public NumberSetting delay;
    public StringSetting item;
    public RunnableSetting remove;
    private boolean wasPressed;
    private MilliTimer delayTimer;
    private boolean isEnabled;
    
    public Keybind(final String name) {
        super(name, Category.KEYBINDS);
        this.button = new ModeSetting("Button", "Right", new String[] { "Right", "Left", "Swing" });
        this.mode = new ModeSetting("Mode", "Normal", new String[] { "Normal", "Rapid", "Toggle" });
        this.delay = new NumberSetting("Delay", 50.0, 1.0, 5000.0, 1.0);
        this.item = new StringSetting("Item");
        this.remove = new RunnableSetting("Remove keybinding", () -> {
            this.setToggled(false);
            OringoClient.modules.remove(this);
            MinecraftForge.EVENT_BUS.unregister((Object)this);
            return;
        });
        this.delayTimer = new MilliTimer();
        this.addSettings(this.item, this.button, this.mode, this.delay, this.remove);
    }
    
    @Override
    public String getName() {
        return this.item.getValue().equals("") ? ("Keybind " + (Module.getModulesByCategory(Category.KEYBINDS).indexOf(this) + 1)) : this.item.getValue();
    }
    
    @Override
    public boolean isKeybind() {
        return true;
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        final boolean keyPressed = this.isPressed();
        System.out.println(this.getName());
        if ((keyPressed || this.isEnabled) && this.isToggled() && !this.item.getValue().equals("") && OringoClient.mc.currentScreen == null && this.delayTimer.hasTimePassed((long)this.delay.getValue()) && (this.mode.is("Rapid") || !this.wasPressed || (this.mode.is("Toggle") && this.isEnabled))) {
            for (int i = 0; i < 9; ++i) {
                if (OringoClient.mc.player.inventory.getStackInSlot(i) != null && ChatFormatting.stripFormatting(OringoClient.mc.player.inventory.getStackInSlot(i).getDisplayName()).toLowerCase().contains(this.item.getValue().toLowerCase())) {
                    final int held = OringoClient.mc.player.inventory.currentItem;
                    OringoClient.mc.player.inventory.currentItem = i;
                    SkyblockUtils.updateItemNoEvent();
                    final String selected = this.button.getSelected();
                    switch (selected) {
                        case "Left": {
                            SkyblockUtils.click();
                            break;
                        }
                        case "Right": {
                            OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)new C08PacketPlayerBlockPlacement(OringoClient.mc.player.func_70694_bm()));
                            break;
                        }
                        case "Swing": {
                            OringoClient.mc.player.func_71038_i();
                            break;
                        }
                    }
                    OringoClient.mc.player.inventory.currentItem = held;
                    SkyblockUtils.updateItemNoEvent();
                    this.delayTimer.updateTime();
                    break;
                }
            }
        }
        if (this.mode.is("Toggle") && !this.wasPressed && keyPressed && this.isToggled()) {
            this.isEnabled = !this.isEnabled;
        }
        this.wasPressed = keyPressed;
    }
    
    public static void saveKeybinds() {
        try {
            final File oringoKeybinds = new File(OringoClient.mc.gameDir.getPath() + "/config/OringoClient/OringoKeybinds.cfg");
            if (!oringoKeybinds.exists()) {
                oringoKeybinds.createNewFile();
            }
            else {
                final DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(OringoClient.mc.gameDir.getPath() + "/config/OringoClient/OringoKeybinds.cfg"));
                final List<Module> keybinds = Module.getModulesByCategory(Category.KEYBINDS);
                dataOutputStream.writeInt(keybinds.size());
                for (final Module keybind : keybinds) {
                    dataOutputStream.writeUTF(keybind.name);
                }
                dataOutputStream.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
