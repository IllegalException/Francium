package me.gopro336.zenith.module.combat;

import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.*;

public class Criticals
extends Module {
    private final Minecraft mc = Minecraft.getMinecraft();

    public Criticals(String name, String description, Category category) {
        super(name, description, category);
    }

    @SubscribeEvent
    public void onAttack(final AttackEntityEvent event) {
        if (this.mc.player.isInWater() || this.mc.player.isInLava()) {
            return;
        }
        if (this.mc.player.onGround) {
            this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 0.1625, this.mc.player.posZ, true));
            this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ, true));
        }
    }
}
