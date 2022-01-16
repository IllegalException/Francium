package me.gopro336.zenith.module.misc;

import com.mojang.authlib.GameProfile;
import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;

import java.util.UUID;

public class FakePlayer
extends Module {
    public EntityOtherPlayerMP fakePlayer;
    private final Setting totempops = new Setting("TotemPops", this, true);

    public FakePlayer(String name, String description, Category category) {
        super(name, description, category);
        this.addSetting(this.totempops);
    }
    public void onEnable() {
        if (mc.world == null || mc.player == null) {
            toggle();
        }else {
            UUID playerUUID = mc.player.getUniqueID();
            fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString(playerUUID.toString()), mc.player.getDisplayNameString()));
            fakePlayer.copyLocationAndAnglesFrom(mc.player);
            fakePlayer.inventory.copyInventory(mc.player.inventory);
            mc.world.addEntityToWorld(-7777, fakePlayer);

            if(fakePlayer != null) {
                fakePlayer.inventory.offHandInventory.set(0, new ItemStack(Items.TOTEM_OF_UNDYING));
                if(fakePlayer.getHealth() <= 0) {
                    fakePop(fakePlayer);
                    fakePlayer.setHealth(20f);
                }
            }
        }
    }
    @Override
    public void onDisable() {
        mc.world.removeEntityFromWorld(-7777);
    }
    private void fakePop(EntityOtherPlayerMP entity) {
        this.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.TOTEM, 30);
        this.mc.world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0F, 1.0F, false);
    }
}
