package me.gopro336.zenith.module.render;

import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.Setting;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRender
extends Module {
    public final Setting fire = new Setting("Fire", this, true);
    public final Setting totempop = new Setting("TotemPop", this, true);

    public NoRender(String name, String description, Category category) {
    super(name, description, category);
    this.addSetting(this.fire);
    this.addSetting(this.totempop);
    }

    @SubscribeEvent
    public void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
        if (event.getOverlayType().equals(RenderBlockOverlayEvent.OverlayType.FIRE))
            event.setCanceled(true);
        }

    }
