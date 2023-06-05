//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.oringo.oringoclient.OringoClient;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ FontRenderer.class })
public abstract class NickHider
{
    @Shadow
    protected abstract void renderStringAtPos(final String p0, final boolean p1);
    
    @Shadow
    public abstract int getStringWidth(final String p0);
    
    @Shadow
    public abstract int getCharWidth(final char p0);
    
    @Inject(method = { "renderStringAtPos" }, at = { @At("HEAD") }, cancellable = true)
    private void renderString(final String text, final boolean shadow, final CallbackInfo ci) {
        if (OringoClient.nickHider.isToggled() && text.contains(OringoClient.mc.getSession().getUsername())) {
            ci.cancel();
            this.renderStringAtPos(text.replaceAll(OringoClient.mc.getSession().getUsername(), OringoClient.nickHider.name.getValue()), shadow);
        }
    }
    
    @Inject(method = { "getStringWidth" }, at = { @At("RETURN") }, cancellable = true)
    private void getStringWidth(final String text, final CallbackInfoReturnable<Integer> cir) {
        if (text != null && OringoClient.nickHider.isToggled() && text.contains(OringoClient.mc.getSession().getUsername())) {
            cir.setReturnValue(this.getStringWidth(text.replaceAll(OringoClient.mc.getSession().getUsername(), OringoClient.nickHider.name.getValue())));
        }
    }
}
