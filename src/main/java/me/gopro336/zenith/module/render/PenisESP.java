package me.gopro336.zenith.module.render;

import me.gopro336.zenith.event.Render3DEvent;
import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import java.util.*;
import org.lwjgl.util.glu.*;

public class PenisESP
extends Module {
    public static final Minecraft mc = Minecraft.getMinecraft();
    private final Setting penisSize = new Setting("PenisSize", this, 1, 1, 5);
    public PenisESP(String name, String description, Category category) {
        super(name, description, category);
        this.addSetting(this.penisSize);
    }
    public void onRender3D(final Render3DEvent event) {
        for (final Object o : PenisESP.mc.world.loadedEntityList) {
            if (o instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer)o;
                final double n = player.lastTickPosX + (player.posX - player.lastTickPosX) * PenisESP.mc.timer.renderPartialTicks;
                PenisESP.mc.getRenderManager();
                final double x = n - PenisESP.mc.getRenderManager().renderPosX;
                final double n2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * PenisESP.mc.timer.renderPartialTicks;
                PenisESP.mc.getRenderManager();
                final double y = n2 - PenisESP.mc.getRenderManager().renderPosY;
                final double n3 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * PenisESP.mc.timer.renderPartialTicks;
                PenisESP.mc.getRenderManager();
                final double z = n3 - PenisESP.mc.getRenderManager().renderPosZ;
                GL11.glPushMatrix();
                RenderHelper.disableStandardItemLighting();
                this.esp(player, x, y, z);
                RenderHelper.enableStandardItemLighting();
                GL11.glPopMatrix();
            }
        }
    }

    public void esp(final EntityPlayer player, final double x, final double y, final double z) {
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(true);
        GL11.glLineWidth(1.0f);
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-player.rotationYaw, 0.0f, player.height, 0.0f);
        GL11.glTranslated(-x, -y, -z);
        GL11.glTranslated(x, y + player.height / 2.0f - 0.22499999403953552, z);
        GL11.glColor4f(1.38f, 0.55f, 2.38f, 1.0f);
        GL11.glRotated((double)(player.isSneaking() ? 35 : 0), 1.0, 0.0, 0.0);
        GL11.glTranslated(0.0, 0.0, 0.07500000298023224);
        final Cylinder shaft = new Cylinder();
        shaft.setDrawStyle(100013);
        shaft.draw(0.1f * this.penisSize.getIntegerValue(), 0.11f, 0.4f, 25, 20);
        GL11.glTranslated(0.0, 0.0, -0.12500000298023223);
        GL11.glTranslated(-0.09000000074505805, 0.0, 0.0);
        final Sphere right = new Sphere();
        right.setDrawStyle(100013);
        right.draw(0.14f * this.penisSize.getIntegerValue(), 10, 20);
        GL11.glTranslated(0.16000000149011612, 0.0, 0.0);
        final Sphere left = new Sphere();
        left.setDrawStyle(100013);
        left.draw(0.14f * this.penisSize.getIntegerValue(), 10, 20);
        GL11.glColor4f(1.35f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslated(-0.07000000074505806, 0.0, 0.589999952316284);
        final Sphere tip = new Sphere();
        tip.setDrawStyle(100013);
        tip.draw(0.13f * this.penisSize.getIntegerValue(), 15, 20);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
    }
}

