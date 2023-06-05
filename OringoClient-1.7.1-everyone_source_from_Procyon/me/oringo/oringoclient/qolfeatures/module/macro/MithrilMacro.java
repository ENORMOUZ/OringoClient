//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.macro;

import net.minecraft.client.renderer.DestroyBlockProgress;
import java.util.Map;
import net.minecraft.block.BlockColored;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStone;
import java.util.Optional;
import java.util.Comparator;
import java.util.function.Predicate;
import me.oringo.oringoclient.utils.RotationUtils;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;
import java.util.Random;
import me.oringo.oringoclient.utils.SkyblockUtils;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import net.minecraft.item.ItemMap;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import net.minecraft.inventory.Slot;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.entity.Entity;
import net.minecraft.client.settings.KeyBinding;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.utils.RenderUtils;
import java.awt.Color;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.OringoClient;
import net.minecraftforge.event.world.WorldEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import net.minecraft.entity.item.EntityArmorStand;
import java.util.ArrayList;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.minecraft.client.Minecraft;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class MithrilMacro extends Module
{
    private Minecraft mc;
    private BlockPos target;
    private BlockPos test;
    private Vec3 targetRotation;
    private Vec3 targetRotation2;
    private ArrayList<Float> yaw;
    private ArrayList<Float> pitch;
    private boolean stopLoop;
    private int ticksTargeting;
    private int ticksMining;
    private int ticks;
    private int ticksSeen;
    private int shouldReconnect;
    public EntityArmorStand drillnpc;
    private int lastKey;
    private int timeLeft;
    private int pause;
    private BooleanSetting drillRefuel;
    private NumberSetting rotations;
    private NumberSetting accuracyChecks;
    private NumberSetting maxBreakTime;
    private NumberSetting quickBreak;
    private NumberSetting panic;
    private BooleanSetting titanium;
    private BooleanSetting sneak;
    private BooleanSetting under;
    private BooleanSetting autoAbility;
    private NumberSetting moreMovement;
    private NumberSetting walking;
    private NumberSetting walkingTime;
    private ModeSetting mode;
    
    public MithrilMacro() {
        super("Mithril Macro", 0, Category.MACRO);
        this.mc = Minecraft.getMinecraft();
        this.target = null;
        this.test = null;
        this.targetRotation = null;
        this.targetRotation2 = null;
        this.yaw = new ArrayList<Float>();
        this.pitch = new ArrayList<Float>();
        this.stopLoop = false;
        this.ticksTargeting = 0;
        this.ticksMining = 0;
        this.ticks = 0;
        this.ticksSeen = 0;
        this.shouldReconnect = -1;
        this.lastKey = -1;
        this.timeLeft = 0;
        this.pause = 0;
        this.drillRefuel = new BooleanSetting("Drill Refuel", false);
        this.rotations = new NumberSetting("Rotations", 10.0, 1.0, 20.0, 1.0);
        this.accuracyChecks = new NumberSetting("Accuracy", 5.0, 3.0, 10.0, 1.0);
        this.maxBreakTime = new NumberSetting("Max break time", 160.0, 40.0, 400.0, 1.0);
        this.quickBreak = new NumberSetting("Block skip progress", 0.9, 0.0, 1.0, 0.1);
        this.panic = new NumberSetting("Auto leave", 100.0, 0.0, 200.0, 1.0);
        this.titanium = new BooleanSetting("Prioritize titanium", true);
        this.sneak = new BooleanSetting("Sneak", false);
        this.under = new BooleanSetting("Mine under", false);
        this.autoAbility = new BooleanSetting("Auto ability", true);
        this.moreMovement = new NumberSetting("Head movements", 5.0, 0.0, 50.0, 1.0);
        this.walking = new NumberSetting("Walking %", 0.1, 0.0, 5.0, 0.1);
        this.walkingTime = new NumberSetting("Walking ticks", 5.0, 0.0, 60.0, 1.0);
        this.mode = new ModeSetting("Target", "Clay", new String[] { "Clay", "Prismarine", "Wool", "Blue", "Gold" });
        this.addSettings(this.rotations, this.drillRefuel, this.accuracyChecks, this.titanium, this.sneak, this.quickBreak, this.maxBreakTime, this.autoAbility, this.under, this.panic, this.moreMovement, this.walking, this.walkingTime, this.mode);
    }
    
    @SubscribeEvent
    public void onLoad(final WorldEvent.Load event) {
        this.drillnpc = null;
        if (this.isToggled()) {
            this.setToggled(false);
            if (OringoClient.aotvReturn.isToggled()) {
                OringoClient.aotvReturn.start(() -> this.setToggled(true), false);
            }
        }
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent event) {
        if (!this.isToggled()) {
            return;
        }
        if (this.target != null) {
            RenderUtils.blockBox(this.target, Color.CYAN);
        }
        if (this.targetRotation != null) {
            RenderUtils.miniBlockBox(this.targetRotation, Color.GREEN);
        }
        if (this.targetRotation2 != null) {
            RenderUtils.miniBlockBox(this.targetRotation2, Color.RED);
        }
    }
    
    @SubscribeEvent
    public void reconnect(final TickEvent.ClientTickEvent event) {
        if (this.mc.currentScreen instanceof GuiDisconnected && this.shouldReconnect < 0 && this.isToggled()) {
            this.shouldReconnect = 250;
            this.setToggled(false);
        }
        if (this.shouldReconnect-- == 0) {
            this.mc.displayGuiScreen((GuiScreen)new GuiConnecting((GuiScreen)new GuiMainMenu(), this.mc, new ServerData("Hypixel", "play.Hypixel.net", false)));
            new Thread(() -> {
                try {
                    Thread.sleep(15000L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (this.mc.player != null && OringoClient.aotvReturn.isToggled()) {
                    OringoClient.aotvReturn.start(() -> this.setToggled(true), false);
                }
            }).start();
        }
    }
    
    @Override
    public void onEnable() {
        this.ticksSeen = 0;
        this.ticksMining = 0;
        this.ticksTargeting = 0;
        if (this.autoAbility.isEnabled() && this.mc.player.func_70694_bm() != null) {
            this.mc.playerController.func_78769_a((EntityPlayer)this.mc.player, (World)this.mc.world, this.mc.player.func_70694_bm());
        }
    }
    
    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent event) {
        if (!this.isToggled()) {
            return;
        }
        final String message = event.message.getFormattedText();
        if (this.drillRefuel.isEnabled() && ChatFormatting.stripFormatting(message).startsWith("Your") && ChatFormatting.stripFormatting(message).endsWith("Refuel it by talking to a Drill Mechanic!") && this.drillnpc != null) {
            this.setToggled(false);
            int[] array;
            int length;
            int l = 0;
            int a;
            int i;
            Slot slot;
            int j;
            Slot slot2;
            new Thread(() -> {
                try {
                    array = new int[] { this.mc.gameSettings.keyBindForward.getKeyCode(), this.mc.gameSettings.keyBindLeft.getKeyCode(), this.mc.gameSettings.keyBindBack.getKeyCode(), this.mc.gameSettings.keyBindRight.getKeyCode(), this.mc.gameSettings.keyBindSneak.getKeyCode(), this.mc.gameSettings.keyBindAttack.getKeyCode() };
                    for (length = array.length; l < length; ++l) {
                        a = array[l];
                        KeyBinding.setKeyBindState(a, false);
                    }
                    Thread.sleep(500L);
                    this.mc.playerController.func_78768_b((EntityPlayer)this.mc.player, (Entity)this.drillnpc);
                    Thread.sleep(2500L);
                    if (this.mc.player.openContainer instanceof ContainerChest && ((ContainerChest)this.mc.player.openContainer).getLowerChestInventory().getDisplayName().getUnformattedText().contains("Drill Anvil")) {
                        i = 0;
                        while (i < this.mc.player.openContainer.inventorySlots.size()) {
                            slot = this.mc.player.openContainer.getSlot(i);
                            if (slot.getHasStack() && slot.getStack().getDisplayName().contains("Drill") && slot.getStack().getItem() == Items.PRISMARINE_SHARD) {
                                this.mc.playerController.func_78753_a(this.mc.player.openContainer.windowId, slot.slotNumber, 0, 1, (EntityPlayer)this.mc.player);
                                break;
                            }
                            else {
                                ++i;
                            }
                        }
                        Thread.sleep(500L);
                        j = 0;
                        while (j < this.mc.player.openContainer.inventorySlots.size()) {
                            slot2 = this.mc.player.openContainer.getSlot(j);
                            if (slot2.getHasStack() && (slot2.getStack().getDisplayName().contains("Volta") || slot2.getStack().getDisplayName().contains("Oil Barrel") || slot2.getStack().getDisplayName().contains("Biofuel"))) {
                                this.mc.playerController.func_78753_a(this.mc.player.openContainer.windowId, slot2.slotNumber, 0, 1, (EntityPlayer)this.mc.player);
                                break;
                            }
                            else {
                                ++j;
                            }
                        }
                        Thread.sleep(500L);
                        this.mc.playerController.func_78753_a(this.mc.player.openContainer.windowId, 22, 0, 0, (EntityPlayer)this.mc.player);
                        Thread.sleep(250L);
                        this.mc.playerController.func_78753_a(this.mc.player.openContainer.windowId, 13, 0, 1, (EntityPlayer)this.mc.player);
                        Thread.sleep(250L);
                        this.mc.player.closeScreen();
                    }
                    Thread.sleep(2500L);
                    this.setToggled(true);
                    KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindAttack.getKeyCode(), true);
                    this.mc.displayGuiScreen((GuiScreen)new GuiChat());
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return;
            }).start();
        }
        if (ChatFormatting.stripFormatting(event.message.getUnformattedText()).equals("Mining Speed Boost is now available!") && this.autoAbility.isEnabled() && this.mc.player.func_70694_bm() != null) {
            OringoClient.sendMessageWithPrefix("Auto ability");
            this.mc.playerController.func_78769_a((EntityPlayer)this.mc.player, (World)this.mc.world, this.mc.player.func_70694_bm());
        }
        if (ChatFormatting.stripFormatting(event.message.getUnformattedText()).equals("Oh no! Your Pickonimbus 2000 broke!")) {
            int k;
            new Thread(() -> {
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                k = 0;
                while (k < 9) {
                    if (this.mc.player.inventory.getStackInSlot(k) != null && this.mc.player.inventory.getStackInSlot(k).getDisplayName().contains("Pickonimbus")) {
                        this.mc.player.inventory.currentItem = k;
                        break;
                    }
                    else {
                        ++k;
                    }
                }
            }).start();
        }
    }
    
    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), false);
        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
    }
    
    @SubscribeEvent(receiveCanceled = true)
    public void onPacket(final PacketReceivedEvent event) {
        if (event.packet instanceof S08PacketPlayerPosLook && this.isToggled()) {
            this.pause = 200;
            this.target = null;
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), false);
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
            for (final int a : new int[] { this.mc.gameSettings.keyBindForward.getKeyCode(), this.mc.gameSettings.keyBindLeft.getKeyCode(), this.mc.gameSettings.keyBindBack.getKeyCode(), this.mc.gameSettings.keyBindRight.getKeyCode() }) {
                KeyBinding.setKeyBindState(a, false);
            }
        }
    }
    
    private boolean isPickaxe(final ItemStack itemStack) {
        return itemStack != null && (itemStack.getDisplayName().contains("Pickaxe") || itemStack.getItem() == Items.PRISMARINE_SHARD || itemStack.getDisplayName().contains("Gauntlet"));
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        --this.pause;
        if (this.isToggled() && !(this.mc.currentScreen instanceof GuiContainer) && !(this.mc.currentScreen instanceof GuiEditSign) && this.pause < 1) {
            ++this.ticks;
            if (this.mc.player != null && this.mc.player.func_70694_bm() != null && this.mc.player.func_70694_bm().getItem() instanceof ItemMap) {
                this.setToggled(false);
                this.mc.player.sendChatMessage("/l");
            }
            if (this.mc.world != null) {
                if (this.drillnpc == null && this.drillRefuel.isEnabled()) {
                    for (final Entity entityArmorStand : (List)this.mc.world.getLoadedEntityList().stream().filter(entity -> entity instanceof EntityArmorStand).collect(Collectors.toList())) {
                        if (entityArmorStand.getDisplayName().getFormattedText().contains("§e§lDRILL MECHANIC§r")) {
                            OringoClient.mithrilMacro.drillnpc = (EntityArmorStand)entityArmorStand;
                            OringoClient.sendMessageWithPrefix("Mechanic");
                            return;
                        }
                    }
                    this.setToggled(false);
                    OringoClient.aotvReturn.start(() -> this.setToggled(true), false);
                    return;
                }
                if (!this.isPickaxe(this.mc.player.func_70694_bm())) {
                    for (int i = 0; i < 9; ++i) {
                        if (this.isPickaxe(this.mc.player.inventory.getStackInSlot(i))) {
                            this.mc.player.inventory.currentItem = i;
                            SkyblockUtils.updateItemNoEvent();
                        }
                    }
                }
                if (this.timeLeft-- <= 0) {
                    final int[] keybinds = { this.mc.gameSettings.keyBindForward.getKeyCode(), this.mc.gameSettings.keyBindLeft.getKeyCode(), this.mc.gameSettings.keyBindBack.getKeyCode(), this.mc.gameSettings.keyBindRight.getKeyCode(), this.mc.gameSettings.keyBindLeft.getKeyCode(), this.mc.gameSettings.keyBindBack.getKeyCode(), this.mc.gameSettings.keyBindRight.getKeyCode(), this.mc.gameSettings.keyBindBack.getKeyCode(), this.mc.gameSettings.keyBindBack.getKeyCode() };
                    if (this.lastKey != -1) {
                        KeyBinding.setKeyBindState(this.lastKey, false);
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), this.sneak.isEnabled());
                    }
                    if (new Random().nextFloat() < this.walking.getValue() / 100.0) {
                        this.lastKey = keybinds[new Random().nextInt(keybinds.length)];
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), true);
                        KeyBinding.setKeyBindState(this.lastKey, true);
                        this.timeLeft = (int)this.walkingTime.getValue();
                    }
                }
                else {
                    KeyBinding.setKeyBindState(this.lastKey, true);
                    KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                }
                if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                    final Entity entity2 = this.mc.objectMouseOver.entityHit;
                    if (entity2 instanceof EntityPlayer && !SkyblockUtils.isTeam((EntityLivingBase)entity2, (EntityLivingBase)this.mc.player)) {
                        SkyblockUtils.click();
                        this.pause = 5;
                        return;
                    }
                }
                if (this.mc.world.playerEntities.stream().anyMatch(playerEntity -> !playerEntity.equals((Object)this.mc.player) && playerEntity.getDistance((Entity)this.mc.player) < 10.0f && SkyblockUtils.isTeam((EntityLivingBase)this.mc.player, (EntityLivingBase)playerEntity) && (!playerEntity.isInvisible() || playerEntity.posY - this.mc.player.posY <= 5.0))) {
                    ++this.ticksSeen;
                }
                else {
                    this.ticksSeen = 0;
                }
                final boolean inDwarven = SkyblockUtils.anyTab("Dwarven Mines");
                if ((this.panic.getValue() <= this.ticksSeen && this.panic.getValue() != 0.0) || !inDwarven) {
                    this.setToggled(false);
                    if (OringoClient.aotvReturn.isToggled()) {
                        OringoClient.aotvReturn.start(() -> this.setToggled(true), false);
                    }
                    this.ticksSeen = 0;
                    OringoClient.sendMessageWithPrefix(inDwarven ? ("You have been seen by " + ((EntityPlayer)this.mc.world.playerEntities.stream().filter(playerEntity -> !((EntityPlayer)playerEntity).equals((Object)this.mc.player) && ((EntityPlayer)playerEntity).getDistance((Entity)this.mc.player) < 10.0f && SkyblockUtils.isTeam((EntityLivingBase)this.mc.player, playerEntity)).findFirst().get()).getName()) : "Not in dwarven");
                    return;
                }
                if (this.target == null) {
                    if (!this.findTarget()) {
                        OringoClient.sendMessageWithPrefix("No possible target found");
                    }
                    return;
                }
                if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                    if (this.ticksTargeting++ == 40) {
                        this.setToggled(false);
                        if (OringoClient.aotvReturn.isToggled()) {
                            OringoClient.aotvReturn.start(() -> this.setToggled(true), false);
                        }
                        return;
                    }
                }
                else {
                    this.ticksTargeting = 0;
                }
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), true);
                if (this.sneak.isEnabled() || this.timeLeft != 0) {
                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), true);
                }
                if (this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiContainer) && this.ticks % 2 == 0) {
                    SkyblockUtils.click();
                }
                if (!this.yaw.isEmpty() && (this.stopLoop || !this.isTitanium(this.target))) {
                    this.mc.player.rotationYaw = this.yaw.get(0);
                    this.mc.player.rotationPitch = this.pitch.get(0);
                    this.yaw.remove(0);
                    this.pitch.remove(0);
                    if (this.yaw.isEmpty() && this.isBlockVisible(this.target) && this.moreMovement.getValue() != 0.0) {
                        this.stopLoop = false;
                        final Vec3 targetRotationTemp = this.targetRotation;
                        this.targetRotation = this.getRandomVisibilityLine(this.target);
                        this.targetRotation2 = this.targetRotation;
                        this.getRotations(false);
                        this.targetRotation = targetRotationTemp;
                        return;
                    }
                    if (this.moreMovement.getValue() == 0.0) {
                        this.targetRotation2 = null;
                    }
                    if (this.stopLoop) {
                        return;
                    }
                }
                if (this.mc.world.getBlockState(this.target).getBlock().equals(Blocks.BEDROCK)) {
                    if (!this.findTarget()) {}
                    return;
                }
                if (this.mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
                    if (!this.findTarget()) {}
                    return;
                }
                final BlockPos pos = this.mc.objectMouseOver.getBlockPos();
                if (!pos.equals((Object)this.target)) {
                    if (!this.findTarget()) {}
                    return;
                }
                if (this.quickBreak.getValue() != 0.0 && !this.isTitanium(this.target) && OringoClient.getBlockBreakProgress().values().stream().anyMatch(progress -> progress.getPosition().equals((Object)this.target)) && OringoClient.getBlockBreakProgress().values().stream().anyMatch(progress -> progress.getPosition().equals((Object)this.target) && progress.getPartialBlockDamage() == (int)(this.quickBreak.getValue() * 10.0))) {
                    this.findTarget();
                }
                if (this.ticksMining++ == this.maxBreakTime.getValue()) {
                    OringoClient.sendMessageWithPrefix("Mining one block took too long");
                    this.findTarget();
                }
            }
        }
    }
    
    private void getRotations(final boolean stop) {
        final Vec3 lookVec = this.mc.player.getLookVec().add(this.mc.player.getPositionEyes(0.0f));
        if (!this.yaw.isEmpty()) {
            this.yaw.clear();
            this.pitch.clear();
        }
        final double max = (this.rotations.getValue() + 1.0) * (stop ? 1.0 : this.moreMovement.getValue());
        for (int i = 0; i < max; ++i) {
            final Vec3 target = new Vec3(lookVec.x + (this.targetRotation.x - lookVec.x) / max * i, lookVec.y + (this.targetRotation.y - lookVec.y) / max * i, lookVec.z + (this.targetRotation.z - lookVec.z) / max * i);
            final float[] rotation = RotationUtils.getAngles(target);
            this.yaw.add(rotation[0]);
            this.pitch.add(rotation[1]);
        }
        this.stopLoop = stop;
    }
    
    private boolean findTarget() {
        final ArrayList<BlockPos> blocks = new ArrayList<BlockPos>();
        for (int x = -5; x < 6; ++x) {
            for (int y = -5; y < 6; ++y) {
                for (int z = -5; z < 6; ++z) {
                    blocks.add(new BlockPos(this.mc.player.posX + x, this.mc.player.posY + y, this.mc.player.posZ + z));
                }
            }
        }
        final BlockPos sortingCenter = (this.target != null) ? this.target : this.mc.player.getPosition();
        final BlockPos pos2;
        Optional<BlockPos> any = blocks.stream().filter(pos -> !pos.equals((Object)this.target)).filter(this::matchesMode).filter(pos -> this.mc.player.getDistance((double)pos.getX(), (double)(pos.getY() - this.mc.player.getEyeHeight()), (double)pos.getZ()) < 5.5).filter(this::isBlockVisible).min(Comparator.comparingDouble(pos -> (this.isTitanium(pos) && this.titanium.isEnabled()) ? 0.0 : this.getDistance(pos, pos2, 0.6)));
        if (any.isPresent()) {
            this.target = any.get();
            this.targetRotation2 = null;
            this.targetRotation = this.getRandomVisibilityLine(any.get());
            this.getRotations(true);
        }
        else {
            final BlockPos pos3;
            any = blocks.stream().filter(pos -> !pos.equals((Object)this.target)).filter(this::matchesAny).filter(pos -> this.mc.player.getDistance((double)pos.getX(), (double)(pos.getY() - this.mc.player.getEyeHeight()), (double)pos.getZ()) < 5.5).filter(this::isBlockVisible).min(Comparator.comparingDouble(pos -> (this.isTitanium(pos) && this.titanium.isEnabled()) ? 0.0 : this.getDistance(pos, pos3, 0.6)));
            if (any.isPresent()) {
                this.target = any.get();
                this.targetRotation2 = null;
                this.targetRotation = this.getRandomVisibilityLine(any.get());
                this.getRotations(true);
            }
        }
        this.ticksMining = 0;
        return any.isPresent();
    }
    
    private double getDistance(final BlockPos pos1, final BlockPos pos2, final double multiY) {
        final double deltaX = pos1.getX() - pos2.getX();
        final double deltaY = (pos1.getY() - pos2.getY()) * multiY;
        final double deltaZ = pos1.getZ() - pos2.getZ();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }
    
    private boolean isBlockVisible(final BlockPos pos) {
        return this.getRandomVisibilityLine(pos) != null;
    }
    
    private Vec3 getRandomVisibilityLine(final BlockPos pos) {
        final List<Vec3> lines = new ArrayList<Vec3>();
        for (int x = 0; x < this.accuracyChecks.getValue(); ++x) {
            for (int y = 0; y < this.accuracyChecks.getValue(); ++y) {
                for (int z = 0; z < this.accuracyChecks.getValue(); ++z) {
                    final Vec3 target = new Vec3(pos.getX() + x / this.accuracyChecks.getValue(), pos.getY() + y / this.accuracyChecks.getValue(), pos.getZ() + z / this.accuracyChecks.getValue());
                    this.test = new BlockPos(target.x, target.y, target.z);
                    final MovingObjectPosition movingObjectPosition = this.mc.world.rayTraceBlocks(this.mc.player.getPositionEyes(0.0f), target, true, false, true);
                    if (movingObjectPosition != null) {
                        final BlockPos obj = movingObjectPosition.getBlockPos();
                        if (obj.equals((Object)this.test) && this.mc.player.getDistance(target.x, target.y - this.mc.player.getEyeHeight(), target.z) < 4.5 && (this.under.isEnabled() || Math.abs(this.mc.player.posY - target.y) > 1.3)) {
                            lines.add(target);
                        }
                    }
                }
            }
        }
        return lines.isEmpty() ? null : lines.get(new Random().nextInt(lines.size()));
    }
    
    private boolean isTitanium(final BlockPos pos) {
        final IBlockState state = this.mc.world.getBlockState(pos);
        return state.getBlock() == Blocks.STONE && ((BlockStone.EnumType)state.getValue((IProperty)BlockStone.VARIANT)).equals((Object)BlockStone.EnumType.DIORITE_SMOOTH);
    }
    
    private boolean matchesMode(final BlockPos pos) {
        final IBlockState state = this.mc.world.getBlockState(pos);
        if (this.isTitanium(pos)) {
            return true;
        }
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Clay": {
                return state.getBlock().equals(Blocks.STAINED_HARDENED_CLAY) || (state.getBlock().equals(Blocks.WOOL) && ((EnumDyeColor)state.getValue((IProperty)BlockColored.COLOR)).equals((Object)EnumDyeColor.GRAY));
            }
            case "Prismarine": {
                return state.getBlock().equals(Blocks.PRISMARINE);
            }
            case "Wool": {
                return state.getBlock().equals(Blocks.WOOL) && ((EnumDyeColor)state.getValue((IProperty)BlockColored.COLOR)).equals((Object)EnumDyeColor.LIGHT_BLUE);
            }
            case "Blue": {
                return (state.getBlock().equals(Blocks.WOOL) && ((EnumDyeColor)state.getValue((IProperty)BlockColored.COLOR)).equals((Object)EnumDyeColor.LIGHT_BLUE)) || state.getBlock().equals(Blocks.PRISMARINE);
            }
            case "Gold": {
                return state.getBlock().equals(Blocks.GOLD_BLOCK);
            }
            default: {
                return false;
            }
        }
    }
    
    private boolean matchesAny(final BlockPos pos) {
        final IBlockState state = this.mc.world.getBlockState(pos);
        return (state.getBlock().equals(Blocks.WOOL) && state.getProperties().entrySet().stream().anyMatch(entry -> entry.toString().contains("lightBlue"))) || state.getBlock().equals(Blocks.PRISMARINE) || state.getBlock().equals(Blocks.STAINED_HARDENED_CLAY) || (state.getBlock().equals(Blocks.WOOL) && state.getProperties().entrySet().stream().anyMatch(entry -> entry.toString().contains("gray"))) || this.isTitanium(pos);
    }
}
