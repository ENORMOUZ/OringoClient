//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.other;

import net.minecraft.client.entity.EntityPlayerSP;
import me.oringo.oringoclient.utils.MovementUtils;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class TaraFly extends Module
{
    public NumberSetting setting;
    public NumberSetting time;
    private BooleanSetting dev;
    public MilliTimer disablerTimer;
    private boolean isFlying;
    
    public TaraFly() {
        super("Slime fly", 0, Category.OTHER);
        this.setting = new NumberSetting("Speed", 1.0, 0.1, 3.0, 0.01);
        this.time = new NumberSetting("Disabler timer", 1200.0, 250.0, 2500.0, 1.0);
        this.dev = new BooleanSetting("Dev", false) {
            @Override
            public boolean isHidden() {
                return !OringoClient.devMode;
            }
        };
        this.disablerTimer = new MilliTimer();
        this.isFlying = false;
        this.addSettings(this.setting, this.time, this.dev);
    }
    
    @Override
    public void onDisable() {
        OringoClient.mc.player.setVelocity(0.0, 0.0, 0.0);
        this.isFlying = false;
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load event) {
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(final ClientChatReceivedEvent event) {
        if (ChatFormatting.stripFormatting(event.message.getFormattedText()).contains("Double-jump boots are temporarily disabled!")) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onUpdate(final MotionUpdateEvent.Pre pre) {
        if (this.isFlying && this.disablerTimer.hasTimePassed((long)this.time.getValue())) {
            OringoClient.mc.player.setVelocity(0.0, 0.0, 0.0);
            this.isFlying = false;
        }
        if (!this.isToggled() || (!OringoClient.mc.player.capabilities.allowFlying && this.disablerTimer.hasTimePassed((long)this.time.getValue()) && !this.dev.isEnabled())) {
            return;
        }
        OringoClient.mc.player.fallDistance = 0.0f;
        OringoClient.mc.player.onGround = false;
        OringoClient.mc.player.capabilities.isFlying = false;
        OringoClient.mc.player.motionY = 0.0;
        if (OringoClient.mc.player.capabilities.allowFlying && (OringoClient.mc.player.ticksExisted % 6 == 0 || !this.isFlying)) {
            OringoClient.mc.player.capabilities.isFlying = true;
            OringoClient.mc.player.sendPlayerAbilities();
            OringoClient.mc.player.capabilities.isFlying = false;
            OringoClient.mc.player.sendPlayerAbilities();
            this.isFlying = true;
            this.disablerTimer.updateTime();
        }
        if (!MovementUtils.isMoving()) {
            OringoClient.mc.player.motionZ = 0.0;
            OringoClient.mc.player.motionX = 0.0;
        }
        final double speed = this.setting.getValue();
        OringoClient.mc.player.jumpMovementFactor = (float)speed;
        if (OringoClient.mc.gameSettings.keyBindJump.isKeyDown()) {
            final EntityPlayerSP player = OringoClient.mc.player;
            player.motionY += speed * 3.0;
        }
        if (OringoClient.mc.gameSettings.keyBindSneak.isKeyDown()) {
            final EntityPlayerSP player2 = OringoClient.mc.player;
            player2.motionY -= speed * 3.0;
        }
    }
    
    private boolean hasBoots() {
        return OringoClient.mc.player.func_82169_q(0) != null && (OringoClient.mc.player.func_82169_q(0).getDisplayName().contains("Tarantula Boots") || OringoClient.mc.player.func_82169_q(0).getDisplayName().contains("Spider's Boots"));
    }
}
