//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.oringo.oringoclient.mixins.entity.PlayerSPAccessor;
import me.oringo.oringoclient.utils.RotationUtils;
import me.oringo.oringoclient.utils.ReflectionUtils;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.util.Timer;
import net.minecraft.client.Minecraft;
import me.oringo.oringoclient.qolfeatures.module.combat.KillAura;
import me.oringo.oringoclient.qolfeatures.module.render.ServerRotations;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBiped;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ModelBiped.class })
public class MixinModelBiped
{
    @Shadow
    public ModelRenderer bipedRightArm;
    @Shadow
    public int field_78120_m;
    @Shadow
    public ModelRenderer bipedHead;
    @Shadow
    public ModelRenderer bipedBody;
    
    @Inject(method = { "setRotationAngles" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelBiped;swingProgress:F") })
    private void setRotationAngles(final float p_setRotationAngles_1_, final float p_setRotationAngles_2_, final float p_setRotationAngles_3_, final float p_setRotationAngles_4_, final float p_setRotationAngles_5_, final float p_setRotationAngles_6_, final Entity p_setRotationAngles_7_, final CallbackInfo callbackInfo) {
        if (!ServerRotations.getInstance().isToggled()) {
            return;
        }
        if ((!ServerRotations.getInstance().onlyKillAura.isEnabled() || KillAura.target != null) && p_setRotationAngles_7_ != null && p_setRotationAngles_7_.equals((Object)Minecraft.getMinecraft().player)) {
            final Timer timer = (Timer)ReflectionUtils.getFieldByName(Minecraft.class, "timer", OringoClient.mc);
            if (timer != null) {
                this.bipedHead.rotateAngleX = (RotationUtils.lastReportedPitch + (((PlayerSPAccessor)p_setRotationAngles_7_).getLastReportedPitch() - RotationUtils.lastReportedPitch) * ((MinecraftAccessor)OringoClient.mc).getTimer().field_74281_c) / 57.295776f;
            }
        }
    }
}
