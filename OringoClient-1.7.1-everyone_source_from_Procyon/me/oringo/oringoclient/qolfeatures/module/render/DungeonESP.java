//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.render;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import me.oringo.oringoclient.utils.MobRenderUtils;
import me.oringo.oringoclient.utils.RenderUtils;
import net.minecraftforge.client.event.RenderLivingEvent;
import me.oringo.oringoclient.utils.OutlineUtils;
import me.oringo.oringoclient.events.RenderLayersEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.item.ItemBow;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityBat;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.EntityLivingBase;
import java.util.List;
import me.oringo.oringoclient.utils.SkyblockUtils;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import net.minecraft.entity.Entity;
import java.util.HashMap;
import java.awt.Color;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class DungeonESP extends Module
{
    public BooleanSetting bat;
    public BooleanSetting starred;
    public BooleanSetting enderman;
    public BooleanSetting miniboss;
    public BooleanSetting bowWarning;
    public ModeSetting mode;
    public NumberSetting opacity;
    private Color starredColor;
    private Color batColor;
    private Color SAcolor;
    private Color LAcolor;
    private Color AAcolor;
    private HashMap<Entity, Color> starredMobs;
    private Entity lastRendered;
    
    public DungeonESP() {
        super("Dungeon ESP", 0, Category.RENDER);
        this.bat = new BooleanSetting("Bat ESP", true);
        this.starred = new BooleanSetting("Starred ESP", true);
        this.enderman = new BooleanSetting("Show endermen", true);
        this.miniboss = new BooleanSetting("Miniboss ESP", true);
        this.bowWarning = new BooleanSetting("Bow warning", false);
        this.mode = new ModeSetting("Mode", "2D", new String[] { "Outline", "2D", "Chams", "Box", "Tracers" });
        this.opacity = new NumberSetting("Opacity", 255.0, 0.0, 255.0, 1.0) {
            @Override
            public boolean isHidden() {
                return !DungeonESP.this.mode.is("Chams");
            }
        };
        this.starredColor = new Color(245, 81, 66);
        this.batColor = new Color(139, 69, 19);
        this.SAcolor = new Color(75, 0, 130);
        this.LAcolor = new Color(34, 139, 34);
        this.AAcolor = new Color(97, 226, 255);
        this.starredMobs = new HashMap<Entity, Color>();
        this.addSettings(this.mode, this.opacity, this.bat, this.starred, this.enderman, this.miniboss);
    }
    
    @SubscribeEvent
    public void onUpdate(final MotionUpdateEvent.Pre event) {
        if (OringoClient.mc.player.ticksExisted % 20 == 0 && SkyblockUtils.inDungeon) {
            this.starredMobs.clear();
            for (final Entity entity2 : (List)OringoClient.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityLivingBase).collect(Collectors.toList())) {
                if (this.starredMobs.containsKey(entity2)) {
                    continue;
                }
                if (entity2 instanceof EntityBat && !entity2.isInvisible() && this.bat.isEnabled()) {
                    this.starredMobs.put(entity2, this.batColor);
                }
                else {
                    if (this.starred.isEnabled()) {
                        if (entity2 instanceof EntityEnderman && entity2.getName().equals("Dinnerbone")) {
                            entity2.setInvisible(false);
                            if (this.enderman.isEnabled()) {
                                this.starredMobs.put(entity2, this.starredColor);
                                continue;
                            }
                            continue;
                        }
                        else if (entity2 instanceof EntityArmorStand && entity2.getName().contains("\u272f")) {
                            final List<Entity> possibleMobs = (List<Entity>)OringoClient.mc.world.getEntitiesWithinAABBExcludingEntity(entity2, entity2.getEntityBoundingBox().grow(0.1, 3.0, 0.1));
                            if (!possibleMobs.isEmpty() && !SkyblockUtils.isMiniboss(possibleMobs.get(0)) && !this.starredMobs.containsKey(possibleMobs.get(0))) {
                                this.starredMobs.put(possibleMobs.get(0), this.starredColor);
                                continue;
                            }
                            continue;
                        }
                    }
                    if (!this.miniboss.isEnabled() || !(entity2 instanceof EntityOtherPlayerMP) || !SkyblockUtils.isMiniboss(entity2)) {
                        continue;
                    }
                    final String getName = entity2.getName();
                    switch (getName) {
                        case "Lost Adventurer": {
                            this.starredMobs.put(entity2, this.LAcolor);
                            break;
                        }
                        case "Shadow Assassin": {
                            entity2.setInvisible(false);
                            this.starredMobs.put(entity2, this.SAcolor);
                            break;
                        }
                        case "Diamond Guy": {
                            this.starredMobs.put(entity2, this.AAcolor);
                            break;
                        }
                    }
                    if (!this.bowWarning.isEnabled() || ((EntityOtherPlayerMP)entity2).func_70694_bm() == null || !(((EntityOtherPlayerMP)entity2).func_70694_bm().getItem() instanceof ItemBow)) {
                        continue;
                    }
                    this.drawBowWarning();
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onRenderWorld(final RenderWorldLastEvent event) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   me/oringo/oringoclient/qolfeatures/module/render/DungeonESP.isToggled:()Z
        //     4: ifeq            49
        //     7: getstatic       me/oringo/oringoclient/utils/SkyblockUtils.inDungeon:Z
        //    10: ifeq            49
        //    13: aload_0         /* this */
        //    14: getfield        me/oringo/oringoclient/qolfeatures/module/render/DungeonESP.mode:Lme/oringo/oringoclient/qolfeatures/module/settings/impl/ModeSetting;
        //    17: ldc             "2D"
        //    19: invokevirtual   me/oringo/oringoclient/qolfeatures/module/settings/impl/ModeSetting.is:(Ljava/lang/String;)Z
        //    22: ifne            50
        //    25: aload_0         /* this */
        //    26: getfield        me/oringo/oringoclient/qolfeatures/module/render/DungeonESP.mode:Lme/oringo/oringoclient/qolfeatures/module/settings/impl/ModeSetting;
        //    29: ldc             "Box"
        //    31: invokevirtual   me/oringo/oringoclient/qolfeatures/module/settings/impl/ModeSetting.is:(Ljava/lang/String;)Z
        //    34: ifne            50
        //    37: aload_0         /* this */
        //    38: getfield        me/oringo/oringoclient/qolfeatures/module/render/DungeonESP.mode:Lme/oringo/oringoclient/qolfeatures/module/settings/impl/ModeSetting;
        //    41: ldc             "Tracers"
        //    43: invokevirtual   me/oringo/oringoclient/qolfeatures/module/settings/impl/ModeSetting.is:(Ljava/lang/String;)Z
        //    46: ifne            50
        //    49: return         
        //    50: aload_0         /* this */
        //    51: getfield        me/oringo/oringoclient/qolfeatures/module/render/DungeonESP.starredMobs:Ljava/util/HashMap;
        //    54: aload_0         /* this */
        //    55: aload_1         /* event */
        //    56: invokedynamic   BootstrapMethod #1, accept:(Lme/oringo/oringoclient/qolfeatures/module/render/DungeonESP;Lnet/minecraftforge/client/event/RenderWorldLastEvent;)Ljava/util/function/BiConsumer;
        //    61: invokevirtual   java/util/HashMap.forEach:(Ljava/util/function/BiConsumer;)V
        //    64: return         
        //    StackMapTable: 00 02 31 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SubscribeEvent
    public void onEntityRender(final RenderLayersEvent event) {
        if (!this.isToggled() || !SkyblockUtils.inDungeon || !this.mode.is("Outline")) {
            return;
        }
        if (this.starredMobs.containsKey(event.entity)) {
            OutlineUtils.outlineESP(event, this.starredMobs.get(event.entity));
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderPre(final RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (!this.isToggled() || !SkyblockUtils.inDungeon || !this.mode.is("Chams")) {
            return;
        }
        if (this.starredMobs.containsKey(event.entity)) {
            MobRenderUtils.setColor(RenderUtils.applyOpacity(this.starredMobs.get(event.entity), (int)this.opacity.getValue()));
            RenderUtils.enableChams();
            this.lastRendered = (Entity)event.entity;
        }
    }
    
    @SubscribeEvent
    public void onRenderPost(final RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (this.lastRendered == event.entity) {
            this.lastRendered = null;
            RenderUtils.disableChams();
            MobRenderUtils.unsetColor();
        }
    }
    
    private void drawBowWarning() {
        final S45PacketTitle p1 = new S45PacketTitle(0, 20, 0);
        final S45PacketTitle p2 = new S45PacketTitle(S45PacketTitle.Type.SUBTITLE, (IChatComponent)new ChatComponentText(ChatFormatting.DARK_RED + "Bow"));
        OringoClient.mc.player.connection.handleTitle(p1);
        OringoClient.mc.player.connection.handleTitle(p2);
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load event) {
        this.starredMobs.clear();
    }
}
