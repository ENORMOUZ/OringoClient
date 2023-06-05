//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.Vec3;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import org.lwjgl.util.vector.Vector2f;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import me.oringo.oringoclient.OringoClient;
import java.awt.Color;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

public class RenderUtils
{
    public static void drawRect(float left, float top, float right, float bottom, final int color) {
        if (left < right) {
            final float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final float j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f4, f5, f6, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)left, (double)bottom, 0.0).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0).endVertex();
        worldrenderer.pos((double)right, (double)top, 0.0).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawBorderedRect(final float x, final float y, final float x1, final float y1, final float width, final int internalColor, final int borderColor) {
        enableGL2D();
        drawRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        GL11.glPushMatrix();
        drawRect(x + width, y, x1 - width, y + width, borderColor);
        drawRect(x, y, x + width, y1, borderColor);
        drawRect(x1 - width, y, x1, y1, borderColor);
        drawRect(x + width, y1 - width, x1 - width, y1, borderColor);
        GL11.glPopMatrix();
        disableGL2D();
    }
    
    public static void draw2D(final Entity entityLiving, final float partialTicks, final float lineWidth, final Color color) {
        final Matrix4f mvMatrix = WorldToScreen.getMatrix(2982);
        final Matrix4f projectionMatrix = WorldToScreen.getMatrix(2983);
        GL11.glPushAttrib(8192);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glMatrixMode(5889);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, (double)OringoClient.mc.displayWidth, (double)OringoClient.mc.displayHeight, 0.0, -1.0, 1.0);
        GL11.glMatrixMode(5888);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glDisable(2929);
        GL11.glBlendFunc(770, 771);
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GL11.glLineWidth(lineWidth);
        final RenderManager renderManager = OringoClient.mc.getRenderManager();
        final AxisAlignedBB bb = entityLiving.getEntityBoundingBox().offset(-entityLiving.posX, -entityLiving.posY, -entityLiving.posZ).offset(entityLiving.lastTickPosX + (entityLiving.posX - entityLiving.lastTickPosX) * partialTicks, entityLiving.lastTickPosY + (entityLiving.posY - entityLiving.lastTickPosY) * partialTicks, entityLiving.lastTickPosZ + (entityLiving.posZ - entityLiving.lastTickPosZ) * partialTicks).offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ);
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
        final double[][] boxVertices = { { bb.minX, bb.minY, bb.minZ }, { bb.minX, bb.maxY, bb.minZ }, { bb.maxX, bb.maxY, bb.minZ }, { bb.maxX, bb.minY, bb.minZ }, { bb.minX, bb.minY, bb.maxZ }, { bb.minX, bb.maxY, bb.maxZ }, { bb.maxX, bb.maxY, bb.maxZ }, { bb.maxX, bb.minY, bb.maxZ } };
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = -1.0f;
        float maxY = -1.0f;
        for (final double[] boxVertex : boxVertices) {
            final Vector2f screenPos = WorldToScreen.worldToScreen(new Vector3f((float)boxVertex[0], (float)boxVertex[1], (float)boxVertex[2]), mvMatrix, projectionMatrix, OringoClient.mc.displayWidth, OringoClient.mc.displayHeight);
            if (screenPos != null) {
                minX = Math.min(screenPos.x, minX);
                minY = Math.min(screenPos.y, minY);
                maxX = Math.max(screenPos.x, maxX);
                maxY = Math.max(screenPos.y, maxY);
            }
        }
        if (minX > 0.0f || minY > 0.0f || maxX <= OringoClient.mc.displayWidth || maxY <= OringoClient.mc.displayWidth) {
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
            GL11.glBegin(2);
            GL11.glVertex2f(minX, minY);
            GL11.glVertex2f(minX, maxY);
            GL11.glVertex2f(maxX, maxY);
            GL11.glVertex2f(maxX, minY);
            GL11.glEnd();
        }
        GL11.glEnable(2929);
        GL11.glMatrixMode(5889);
        GL11.glPopMatrix();
        GL11.glMatrixMode(5888);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }
    
    public static void tracerLine(final Entity entity, final float partialTicks, final float lineWidth, final Color color) {
        final double x = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        final double y = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + entity.height / 2.0f - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        final double z = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glLineWidth(lineWidth);
        GL11.glDepthMask(false);
        setColor(color);
        GL11.glBegin(2);
        GL11.glVertex3d(0.0, (double)Minecraft.getMinecraft().player.getEyeHeight(), 0.0);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void drawTargetESP(final EntityLivingBase target, final Color color, final float partialTicks) {
        GL11.glPushMatrix();
        final float location = (float)((Math.sin(System.currentTimeMillis() * 0.005) + 1.0) * 0.5);
        GlStateManager.translate(target.lastTickPosX + (target.posX - target.lastTickPosX) * partialTicks - OringoClient.mc.getRenderManager().viewerPosX, target.lastTickPosY + (target.posY - target.lastTickPosY) * partialTicks - OringoClient.mc.getRenderManager().viewerPosY + target.height * location, target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * partialTicks - OringoClient.mc.getRenderManager().viewerPosZ);
        enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glDisable(2884);
        GL11.glLineWidth(3.0f);
        GL11.glBegin(3);
        final double cos = Math.cos(System.currentTimeMillis() * 0.005);
        for (int i = 0; i <= 120; ++i) {
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
            final double x = Math.cos(i * 3.141592653589793 / 60.0) * target.width;
            final double z = Math.sin(i * 3.141592653589793 / 60.0) * target.width;
            GL11.glVertex3d(x, 0.15000000596046448 * cos, z);
        }
        GL11.glEnd();
        GL11.glBegin(5);
        for (int i = 0; i <= 120; ++i) {
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
            final double x = Math.cos(i * 3.141592653589793 / 60.0) * target.width;
            final double z = Math.sin(i * 3.141592653589793 / 60.0) * target.width;
            GL11.glVertex3d(x, 0.15000000596046448 * cos, z);
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f);
            GL11.glVertex3d(x, -0.15000000596046448 * cos, z);
        }
        GL11.glEnd();
        GL11.glShadeModel(7424);
        GL11.glEnable(2884);
        disableGL2D();
        GL11.glPopMatrix();
    }
    
    public static void drawRoundRect(float left, float top, float right, float bottom, final float radius, final int color) {
        left += radius;
        right -= radius;
        if (left < right) {
            final float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final float j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f4, f5, f6, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)left, (double)bottom, 0.0).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0).endVertex();
        worldrenderer.pos((double)right, (double)top, 0.0).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0).endVertex();
        tessellator.draw();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)(right - radius), (double)(top - radius), 0.0).endVertex();
        worldrenderer.pos((double)right, (double)(top - radius), 0.0).endVertex();
        worldrenderer.pos((double)right, (double)(bottom + radius), 0.0).endVertex();
        worldrenderer.pos((double)(right - radius), (double)(bottom + radius), 0.0).endVertex();
        tessellator.draw();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)left, (double)(top - radius), 0.0).endVertex();
        worldrenderer.pos((double)(left + radius), (double)(top - radius), 0.0).endVertex();
        worldrenderer.pos((double)(left + radius), (double)(bottom + radius), 0.0).endVertex();
        worldrenderer.pos((double)left, (double)(bottom + radius), 0.0).endVertex();
        tessellator.draw();
        drawArc(right, bottom + radius, radius, 180);
        drawArc(left, bottom + radius, radius, 90);
        drawArc(right, top - radius, radius, 270);
        drawArc(left, top - radius, radius, 0);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static Color interpolateColor(final Color color1, final Color color2, final float value) {
        return new Color((int)(color1.getRed() + (color2.getRed() - color1.getRed()) * value), (int)(color1.getGreen() + (color2.getGreen() - color1.getGreen()) * value), (int)(color1.getBlue() + (color2.getBlue() - color1.getBlue()) * value));
    }
    
    public static Color applyOpacity(final Color color, final int opacity) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }
    
    public static void drawRoundRect2(float x, float y, float width, float height, final float radius, final int color) {
        width += x;
        x += radius;
        width -= radius;
        if (x < width) {
            final float i = x;
            x = width;
            width = i;
        }
        height += y;
        if (y < height) {
            final float j = y;
            y = height;
            height = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f4, f5, f6, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)x, (double)height, 0.0).endVertex();
        worldrenderer.pos((double)width, (double)height, 0.0).endVertex();
        worldrenderer.pos((double)width, (double)y, 0.0).endVertex();
        worldrenderer.pos((double)x, (double)y, 0.0).endVertex();
        tessellator.draw();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)(width - radius), (double)(y - radius), 0.0).endVertex();
        worldrenderer.pos((double)width, (double)(y - radius), 0.0).endVertex();
        worldrenderer.pos((double)width, (double)(height + radius), 0.0).endVertex();
        worldrenderer.pos((double)(width - radius), (double)(height + radius), 0.0).endVertex();
        tessellator.draw();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)x, (double)(y - radius), 0.0).endVertex();
        worldrenderer.pos((double)(x + radius), (double)(y - radius), 0.0).endVertex();
        worldrenderer.pos((double)(x + radius), (double)(height + radius), 0.0).endVertex();
        worldrenderer.pos((double)x, (double)(height + radius), 0.0).endVertex();
        tessellator.draw();
        drawArc(width, height + radius, radius, 180);
        drawArc(x, height + radius, radius, 90);
        drawArc(width, y - radius, radius, 270);
        drawArc(x, y - radius, radius, 0);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static void drawArc(final float x, final float y, final float radius, final int angleStart) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(6, DefaultVertexFormats.POSITION);
        GlStateManager.translate((double)x, (double)y, 0.0);
        worldrenderer.pos(0.0, 0.0, 0.0).endVertex();
        final int points = 21;
        for (double i = 0.0; i < points; ++i) {
            final double radians = Math.toRadians(i / points * 90.0 + angleStart);
            worldrenderer.pos(radius * Math.sin(radians), radius * Math.cos(radians), 0.0).endVertex();
        }
        tessellator.draw();
        GlStateManager.translate((double)(-x), (double)(-y), 0.0);
    }
    
    public static void tracerLine(double x, double y, double z, final Color color) {
        x -= Minecraft.getMinecraft().getRenderManager().viewerPosX;
        y -= Minecraft.getMinecraft().getRenderManager().viewerPosY;
        z -= Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.5f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        setColor(color);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0, (double)Minecraft.getMinecraft().player.getEyeHeight(), 0.0);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void drawLine(final Vec3 pos1, final Vec3 pos2, final Color color) {
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.5f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        setColor(color);
        GL11.glTranslated(-Minecraft.getMinecraft().getRenderManager().viewerPosX, -Minecraft.getMinecraft().getRenderManager().viewerPosY, -Minecraft.getMinecraft().getRenderManager().viewerPosZ);
        GL11.glBegin(1);
        GL11.glVertex3d(pos1.x, pos1.y, pos1.z);
        GL11.glVertex3d(pos2.x, pos2.y, pos2.z);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void setColor(final Color c) {
        GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, c.getAlpha() / 255.0f);
    }
    
    public static void entityESPBox(final Entity entity, final float partialTicks, final Color color) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.5f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        setColor(color);
        RenderGlobal.func_181561_a(new AxisAlignedBB(entity.getEntityBoundingBox().minX - entity.posX + (entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosX), entity.getEntityBoundingBox().minY - entity.posY + (entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosY), entity.getEntityBoundingBox().minZ - entity.posZ + (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosZ), entity.getEntityBoundingBox().maxX - entity.posX + (entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosX), entity.getEntityBoundingBox().maxY - entity.posY + (entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosY), entity.getEntityBoundingBox().maxZ - entity.posZ + (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosZ)));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void blockBox(final TileEntity block, final Color color) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        setColor(color);
        RenderGlobal.func_181561_a(block.getRenderBoundingBox().offset(-OringoClient.mc.getRenderManager().viewerPosX, -OringoClient.mc.getRenderManager().viewerPosY, -OringoClient.mc.getRenderManager().viewerPosZ));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void blockBox(final BlockPos block, final Color color) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        setColor(color);
        RenderGlobal.func_181561_a(new AxisAlignedBB(block.getX() - Minecraft.getMinecraft().getRenderManager().viewerPosX, block.getY() - Minecraft.getMinecraft().getRenderManager().viewerPosY, block.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ, block.getX() + 1 - Minecraft.getMinecraft().getRenderManager().viewerPosX, block.getY() + 1 - Minecraft.getMinecraft().getRenderManager().viewerPosY, block.getZ() + 1 - Minecraft.getMinecraft().getRenderManager().viewerPosZ));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void miniBlockBox(final Vec3 block, final Color color) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        setColor(color);
        Minecraft.getMinecraft().getRenderManager();
        RenderGlobal.func_181561_a(new AxisAlignedBB(block.x - 0.05 - Minecraft.getMinecraft().getRenderManager().viewerPosX, block.y - 0.05 - Minecraft.getMinecraft().getRenderManager().viewerPosY, block.z - 0.05 - Minecraft.getMinecraft().getRenderManager().viewerPosZ, block.x + 0.05 - Minecraft.getMinecraft().getRenderManager().viewerPosX, block.y + 0.05 - Minecraft.getMinecraft().getRenderManager().viewerPosY, block.z + 0.05 - Minecraft.getMinecraft().getRenderManager().viewerPosZ));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void drawBorderedRoundedRect(final float x, final float y, final float width, final float height, final float radius, final float linewidth, final int insideC, final int borderC) {
        drawRoundRect(x, y, x + width, y + height, radius, insideC);
        drawOutlinedRoundedRect(x, y, width, height, radius, linewidth, borderC);
    }
    
    public static void drawOutlinedRoundedRect(float x, float y, final float width, final float height, final float radius, final float linewidth, final int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double x2 = x + width;
        double y2 = y + height;
        final float f = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2.0f;
        y *= 2.0f;
        x2 *= 2.0;
        y2 *= 2.0;
        GL11.glLineWidth(linewidth);
        GL11.glDisable(3553);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glEnable(2848);
        GL11.glBegin(2);
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * (radius * -1.0f), y + radius + Math.cos(i * 3.141592653589793 / 180.0) * (radius * -1.0f));
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * (radius * -1.0f), y2 - radius + Math.cos(i * 3.141592653589793 / 180.0) * (radius * -1.0f));
        }
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x2 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y2 - radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x2 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y + radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glPopAttrib();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void enableChams() {
        GL11.glEnable(32823);
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1000000.0f);
    }
    
    public static void disableChams() {
        GL11.glDisable(32823);
        GlStateManager.doPolygonOffset(1.0f, 1000000.0f);
        GlStateManager.disablePolygonOffset();
    }
    
    public static void unForceColor() {
        MobRenderUtils.unsetColor();
    }
    
    public static void renderStarredNametag(final Entity entityIn, final String str, final double x, final double y, final double z, final int maxDistance) {
        final double d0 = entityIn.getDistanceSq(OringoClient.mc.getRenderManager().renderViewEntity);
        if (d0 <= maxDistance * maxDistance) {
            final FontRenderer fontrenderer = OringoClient.mc.getRenderManager().getFontRenderer();
            final float f = 1.6f;
            final float f2 = 0.016666668f * f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x + 0.0f, (float)y + entityIn.height + 0.5f, (float)z);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-OringoClient.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(OringoClient.mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(-f2, -f2, f2);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            final int i = 0;
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
    
    public static void renderLivingLabel(final Entity entityIn, final String str, final double x, final double y, final double z, final int maxDistance) {
        final double d0 = entityIn.getDistanceSq(OringoClient.mc.getRenderManager().renderViewEntity);
        if (d0 <= maxDistance * maxDistance) {
            final FontRenderer fontrenderer = OringoClient.mc.getRenderManager().getFontRenderer();
            final float f = 1.6f;
            final float f2 = 0.016666668f * f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x + 0.0f, (float)y + entityIn.height + 0.5f, (float)z);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-OringoClient.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(OringoClient.mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(-f2, -f2, f2);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getBuffer();
            int i = 0;
            if (str.equals("deadmau5")) {
                i = -10;
            }
            final int j = fontrenderer.getStringWidth(str) / 2;
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos((double)(-j - 1), (double)(-1 + i), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldrenderer.pos((double)(-j - 1), (double)(8 + i), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldrenderer.pos((double)(j + 1), (double)(8 + i), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldrenderer.pos((double)(j + 1), (double)(-1 + i), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
    
    public static Color glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 256.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }
}
