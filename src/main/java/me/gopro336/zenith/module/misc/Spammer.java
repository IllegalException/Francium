package me.gopro336.zenith.module.misc;

import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import net.minecraft.client.Minecraft;

public class Spammer
extends Module {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public Spammer(String name, String description, Category category) {
        super(name, description, category);
    }
}
