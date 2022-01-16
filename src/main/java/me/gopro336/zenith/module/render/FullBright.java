package me.gopro336.zenith.module.render;

import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import net.minecraft.client.Minecraft;

public class FullBright
extends Module {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public FullBright(String name, String description, Category category) {
        super(name, description, category);
    }
    public void onEnable() {
        Minecraft.getMinecraft().gameSettings.gammaSetting = 1000;
    }

    public void onDisable() {
        Minecraft.getMinecraft().gameSettings.gammaSetting = 1;
    }
}
