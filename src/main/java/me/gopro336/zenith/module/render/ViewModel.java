package me.gopro336.zenith.module.render;

import me.gopro336.zenith.event.RenderItemEvent;
import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ViewModel
extends Module {
    public static final Minecraft mc = Minecraft.getMinecraft();
    private final Setting noEatAnimation = new Setting("NoEatAnimation", this, true);
    private final Setting mainX = new Setting("offZ", this, 1, -2, 4);
    private final Setting mainY = new Setting("MainY", this, 1, -3, 3);
    private final Setting mainZ = new Setting("MainZ", this, 1, -5, 5);
    private final Setting offX = new Setting("OffX", this, 1, -2, 4);
    private final Setting offY = new Setting("OffY", this, 1, -3, 3);
    private final Setting offZ = new Setting("OffZ", this, 1, -5, 5);
    private final Setting offRotX = new Setting("RotZ", this, 0, -36, 36);
    private final Setting offRotY = new Setting("RotZ", this, 0, -36, 36);
    private final Setting offRotZ = new Setting("RotZ", this, 0, -36, 36);
    private final Setting mainRotX = new Setting("mainRotX", this, 0, -36, 36);
    private final Setting mainRotY = new Setting("RotY", this, 0, -36, 36);
    private final Setting mainRotZ = new Setting("RotZ", this, 0, -36, 36);
    private final Setting mainScaleX = new Setting("MainScaleZ", this, 1, 1, 5);
    private final Setting mainScaleY = new Setting("MainScaleZ", this, 1, 1, 5);
    private final Setting mainScaleZ = new Setting("MainScaleZ", this, 1, 1, 5);
    private final Setting offScaleX = new Setting("OffScaleX", this, 1, 1, 5);
    private final Setting offScaleY = new Setting("OffScaleY", this, 1, 1, 5);
    private final Setting offScaleZ = new Setting("OffScaleZ", this, 1, 1, 5);

    public ViewModel(String name, String description, Category category) {
        super(name, description, category);
        this.addSetting(this.mainX);
        this.addSetting(this.mainY);
        this.addSetting(this.mainZ);
        this.addSetting(this.offX);
        this.addSetting(this.offY);
        this.addSetting(this.offZ);
        this.addSetting(this.offRotX);
        this.addSetting(this.offRotY);
        this.addSetting(this.offRotZ);
        this.addSetting(this.mainRotX);
        this.addSetting(this.mainRotY);
        this.addSetting(this.mainRotZ);
        this.addSetting(this.mainScaleX);
        this.addSetting(this.mainScaleY);
        this.addSetting(this.mainScaleZ);
        this.addSetting(this.offScaleX);
        this.addSetting(this.offScaleY);
        this.addSetting(this.offScaleZ);
    }
    @SubscribeEvent
    public void onItemRender(final RenderItemEvent event) {
        event.setMainX((double)this.mainX.getIntegerValue());
        event.setMainY((double)this.mainY.getIntegerValue());
        event.setMainZ((double)this.mainZ.getIntegerValue());
        event.setOffX(-this.offX.getIntegerValue());
        event.setOffY((double)this.offY.getIntegerValue());
        event.setOffZ((double)this.offZ.getIntegerValue());
        event.setMainRotX((double)(this.mainRotX.getIntegerValue() * 5));
        event.setMainRotY((double)(this.mainRotY.getIntegerValue() * 5));
        event.setMainRotZ((double)(this.mainRotZ.getIntegerValue() * 5));
        event.setOffRotX((double)(this.offRotX.getIntegerValue() * 5));
        event.setOffRotY((double)(this.offRotY.getIntegerValue() * 5));
        event.setOffRotZ((double)(this.offRotZ.getIntegerValue() * 5));
        event.setOffHandScaleX((double)this.offScaleX.getIntegerValue());
        event.setOffHandScaleY((double)this.offScaleY.getIntegerValue());
        event.setOffHandScaleZ((double)this.offScaleZ.getIntegerValue());
        event.setMainHandScaleX((double)this.mainScaleX.getIntegerValue());
        event.setMainHandScaleY((double)this.mainScaleY.getIntegerValue());
        event.setMainHandScaleZ((double)this.mainScaleZ.getIntegerValue());
    }
}
