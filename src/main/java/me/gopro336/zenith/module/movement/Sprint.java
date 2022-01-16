package me.gopro336.zenith.module.movement;

import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Sprint
extends Module {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public Sprint(String name, String description, Category category) {
        super(name, description, category);
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(!mc.player.isSprinting()) {
            mc.player.setSprinting(true);
        }
    }
}
