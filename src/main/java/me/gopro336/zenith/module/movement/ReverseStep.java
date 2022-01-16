package me.gopro336.zenith.module.movement;

import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ReverseStep
extends Module {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Setting stepSpeed = new Setting("StepSpeed", this, 2, 1, 100);
    private final Setting stepHeight = new Setting("StepHeight", this, 2, 1, 2);

    public ReverseStep(String name, String description, Category category) {
        super(name, description, category);
        this.addSetting(this.stepSpeed);
        this.addSetting(this.stepHeight);
    }

    @SubscribeEvent
    public void onUpdate(final TickEvent.PlayerTickEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (this.mc.player.onGround && !this.mc.player.isSneaking() && !this.mc.player.isInLava() && !this.mc.player.isInWater()) {
            this.mc.player.motionY = -this.stepSpeed.getIntegerValue();
        }
        if (this.mc.player.onGround && !this.mc.player.isSneaking() && !this.mc.player.isInLava() && !this.mc.player.isInWater()) {
            this.mc.player.motionY = -this.stepHeight.getIntegerValue();
        }
    }
}

