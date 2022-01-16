package me.gopro336.zenith.module.movement;

import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.Setting;
import net.minecraft.client.Minecraft;

public class Step
extends Module {
    public static final Minecraft mc = Minecraft.getMinecraft();
    private final Setting stepHeight = new Setting("StepHeight", this, 1, 1, 2);
    public Step(String name, String description, Category category) {
        super(name, description, category);
        this.addSetting(this.stepHeight);
    }
    public void onEnable() {
        this.mc.player.stepHeight = this.stepHeight.getIntegerValue();
    }
    public void onDisable() {
        this.mc.player.stepHeight = 0.5f;
    }
}
