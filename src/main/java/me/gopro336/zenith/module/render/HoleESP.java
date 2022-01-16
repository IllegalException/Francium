package me.gopro336.zenith.module.render;

import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.util.RenderUtil;
import net.minecraft.client.Minecraft;

public class HoleESP
extends Module {
    private final Minecraft mc = Minecraft.getMinecraft();
    public HoleESP(String name, String description, Category category) {
        super(name, description, category);
    }
}