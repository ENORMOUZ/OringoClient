//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import java.util.function.Supplier;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.utils.font.Fonts;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import java.util.ArrayList;

public class Notifications
{
    private static ArrayList<Notification> notifications;
    public static EntityLivingBase entity;
    protected static final ResourceLocation inventoryBackground;
    private static Minecraft mc;
    private float lastHp;
    
    public Notifications() {
        this.lastHp = 0.8f;
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (event.type.equals((Object)RenderGameOverlayEvent.ElementType.ALL)) {
            GL11.glPushMatrix();
            GL11.glScaled((double)(2.0f / Notifications.mc.gameSettings.guiScale), (double)(2.0f / Notifications.mc.gameSettings.guiScale), (double)(2.0f / Notifications.mc.gameSettings.guiScale));
            final int scale = Notifications.mc.gameSettings.guiScale;
            Notifications.mc.gameSettings.guiScale = 2;
            Notifications.notifications.removeIf(n -> n.end <= System.currentTimeMillis());
            if (Notifications.notifications.size() > 0) {
                GL11.glEnable(2848);
                GL11.glEnable(2832);
                int i = Notifications.notifications.size() - 1;
                for (final Notification notification : (ArrayList)Notifications.notifications.clone()) {
                    int t2 = 0;
                    if (notification.end - System.currentTimeMillis() < 250L) {
                        t2 = 250 - (int)(notification.end - System.currentTimeMillis());
                    }
                    t2 *= 2;
                    final int translateX = (int)Math.max(Math.max(0L, notification.end - (notification.time - 250) - System.currentTimeMillis()), t2);
                    GL11.glTranslated((double)translateX, 0.0, 0.0);
                    final ScaledResolution resolution = new ScaledResolution(Notifications.mc);
                    this.drawRectWithShadow((float)(resolution.getScaledWidth() - 170), resolution.getScaledHeight() - 50 * (i + 1), resolution.getScaledWidth() - 20, resolution.getScaledHeight() - 5 - 50 * i, 3.0f, new Color(20, 20, 20, 200).getRGB());
                    Fonts.fontBig.drawSmoothString(notification.getTitle(), resolution.getScaledWidth() - 165 + 0.4f, resolution.getScaledHeight() - 45 - 50 * i + 0.5f, new Color(20, 20, 20).getRGB());
                    Fonts.fontBig.drawSmoothString(notification.getTitle(), resolution.getScaledWidth() - 165, (float)(resolution.getScaledHeight() - 45 - 50 * i), OringoClient.clickGui.getColor().brighter().getRGB());
                    int x = 1;
                    for (final String string : Fonts.fontMedium.wrapWords(notification.getDescription(), 140.0)) {
                        Fonts.fontMedium.drawSmoothString(string, resolution.getScaledWidth() - 165 + 0.4f, resolution.getScaledHeight() - 40 - 50 * i + (Fonts.fontMedium.getHeight() + 2) * x + 0.5f, new Color(20, 20, 20).getRGB());
                        Fonts.fontMedium.drawSmoothString(string, resolution.getScaledWidth() - 165, (float)(resolution.getScaledHeight() - 40 - 50 * i + (Fonts.fontMedium.getHeight() + 2) * x), new Color(231, 231, 231).getRGB());
                        ++x;
                    }
                    final float time = (notification.getEnd() - System.currentTimeMillis()) / (float)notification.time;
                    drawRectWith2Colors(resolution.getScaledWidth() - 170, resolution.getScaledHeight() - 7 - 50 * i, (int)(resolution.getScaledWidth() - 170 + 150.0f * time), resolution.getScaledHeight() - 5 - 50 * i, OringoClient.clickGui.getColor().getRGB(), OringoClient.clickGui.getColor().darker().darker().getRGB());
                    --i;
                    GL11.glTranslated((double)(-translateX), 0.0, 0.0);
                }
                GL11.glDisable(2848);
                GL11.glDisable(2832);
            }
            Notifications.mc.gameSettings.guiScale = scale;
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
        }
    }
    
    private void drawRectWithShadow(final float x, final int y, final int x2, final int y2, final float radius, final int color) {
        GL11.glBlendFunc(770, 771);
        RenderUtils.drawRoundRect(x, (float)y, (float)x2, (float)y2, radius, color);
        final int r = color >> 16 & 0xFF;
        final int g = color >> 8 & 0xFF;
        final int b = color & 0xFF;
        for (int i = 0; i < 3; ++i) {
            RenderUtils.drawRoundRect(x - i, (float)(y - i), (float)(x2 + i), (float)(y2 + i), 3.0f, new Color(r, g, b, 30).getRGB());
        }
    }
    
    public static void drawRectWith2Colors(int left, int top, int right, int bottom, final int color, final int color2) {
        if (left < right) {
            final int i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final int j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final float ff3 = (color2 >> 24 & 0xFF) / 255.0f;
        final float ff4 = (color2 >> 16 & 0xFF) / 255.0f;
        final float ff5 = (color2 >> 8 & 0xFF) / 255.0f;
        final float ff6 = (color2 & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.color(f4, f5, f6, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)left, (double)bottom, 0.0).color(ff4, ff5, ff6, ff3).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0).color(f4, f5, f6, f3).endVertex();
        worldrenderer.pos((double)right, (double)top, 0.0).color(f4, f5, f6, f3).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0).color(ff4, ff5, ff6, ff3).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void showNotification(final String title, final Supplier<String> description, final int time) {
        Notifications.notifications.add(new Notification(title, description, time));
    }
    
    public static void showNotification(final String title, final String description, final int time) {
        Notifications.notifications.add(new Notification(title, description, time));
    }
    
    static {
        Notifications.notifications = new ArrayList<Notification>();
        inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
        Notifications.mc = Minecraft.getMinecraft();
    }
    
    private static class Notification
    {
        private String title;
        private String description;
        private Supplier<String> desc;
        private long end;
        private int time;
        
        public Notification(final String title, final String description, final int time) {
            this.desc = null;
            this.title = title;
            this.description = description;
            this.end = System.currentTimeMillis() + time;
            this.time = time;
        }
        
        public Notification(final String title, final Supplier<String> desc, final long end) {
            this.desc = null;
            this.title = title;
            this.desc = desc;
            this.end = end;
        }
        
        public int getTime() {
            return this.time;
        }
        
        public String getTitle() {
            return this.title;
        }
        
        public String getDescription() {
            return (this.desc == null) ? this.description : this.desc.get();
        }
        
        public long getEnd() {
            return this.end;
        }
    }
}
