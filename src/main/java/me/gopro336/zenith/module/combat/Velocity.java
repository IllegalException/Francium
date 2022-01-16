package me.gopro336.zenith.module.combat;

import me.gopro336.zenith.event.PacketEvent;
import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import net.minecraft.client.Minecraft;

public class Velocity
extends Module {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public Velocity(String name, String description, Category category) {
        super(name, description, category);
    }
}

