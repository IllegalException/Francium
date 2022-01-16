package me.gopro336.zenith.module.misc;

import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.Setting;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

public class SilentXP
extends Module {
    private final Setting lookPitch = new Setting("LookPitch", this, 90, 0, 100);

    public SilentXP(String name, String description, Category category) {
        super(name, description, category);
    }

    private int delay_count;
    int prvSlot;

    @Override
    public void onEnable() {
        delay_count = 0;
    }

    @Override
    public void onUpdate() {
        if (mc.currentScreen == null) {
            usedXp();
        }
    }

    private int findExpInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    private void usedXp() {
        int oldPitch = (int) mc.player.rotationPitch;
        prvSlot = mc.player.inventory.currentItem;
        mc.player.connection.sendPacket(new CPacketHeldItemChange(findExpInHotbar()));
        mc.player.rotationPitch = lookPitch.getIntegerValue();
        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, lookPitch.getIntegerValue(), true));
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        mc.player.rotationPitch = oldPitch;
        mc.player.inventory.currentItem = prvSlot;
        mc.player.connection.sendPacket(new CPacketHeldItemChange(prvSlot));
    }
}