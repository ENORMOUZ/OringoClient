//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\zxlie\Downloads\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import java.lang.reflect.Field;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.network.Packet;
import java.util.ArrayList;

public class PacketUtils
{
    public static ArrayList<Packet<?>> noEvent;
    
    public static void sendPacketNoEvent(final Packet<?> packet) {
        PacketUtils.noEvent.add(packet);
        OringoClient.mc.getConnection().getNetworkManager().sendPacket((Packet)packet);
    }
    
    public static String packetToString(final Packet<?> packet) {
        final StringBuilder postfix = new StringBuilder();
        boolean first = true;
        for (final Field field : packet.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                postfix.append(first ? "" : ", ").append(field.get(packet));
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            first = false;
        }
        return packet.getClass().getSimpleName() + String.format("{%s}", postfix);
    }
    
    static {
        PacketUtils.noEvent = new ArrayList<Packet<?>>();
    }
}
