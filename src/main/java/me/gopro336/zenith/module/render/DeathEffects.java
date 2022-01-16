package me.gopro336.zenith.module.render;

import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DeathEffects
extends Module {
    private final Minecraft mc = Minecraft.getMinecraft();
    public final Setting lightning = new Setting("Lightning", this, true);
    public final Setting totempop = new Setting("TotemPop", this, true);

    public DeathEffects(String name, String description, Category category) {
        super(name, description, category);
        this.addSetting(this.lightning);
    }

    @SubscribeEvent
    public void onDeath(final LivingDeathEvent event) {
        if (event.getEntity() == this.mc.player) {
            return;
        }
            this.mc.world.addEntityToWorld(-999, (Entity) new EntityLightningBolt((World) this.mc.world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, true));
        }
    }
