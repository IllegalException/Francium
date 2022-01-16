package me.primooctopus33.octohack.client.modules.combat;

import java.util.ArrayList;
import me.primooctopus33.octohack.OctoHack;
import me.primooctopus33.octohack.client.modules.Module;
import me.primooctopus33.octohack.client.modules.combat.Surround;
import me.primooctopus33.octohack.client.setting.Setting;
import me.primooctopus33.octohack.util.BlockUtil;
import me.primooctopus33.octohack.util.EntityUtil;
import me.primooctopus33.octohack.util.InventoryUtil;
import me.primooctopus33.octohack.util.PlayerUtil;
import me.primooctopus33.octohack.util.Timer;
import me.primooctopus33.octohack.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer$Position;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Surround
        extends Module {
    public static Setting<Boolean> rotate;
    public static Setting<Boolean> packet;
    public static Setting<Boolean> ground;
    public static Setting<Integer> noFullExtend;
    public static Setting<CrystalClear> crystalClear;
    public static Setting<Integer> crystalClearRange;
    public static Setting<Float> maxSelfDamage;
    public static Setting<Boolean> swingCrystalClear;
    public static Setting<Boolean> packetCrystal;
    public static Setting<Boolean> extraPacket;
    public static Setting<Boolean> emptyCheck;
    public static Setting<Boolean> shift;
    public static Setting<Boolean> center;
    public static Setting<Sensitivity> sensitivity;
    public static Setting<AntiCity> antiCity;
    public static Setting<Integer> delay;
    public static Setting<Integer> bps;
    public static Setting<Boolean> yDisable;
    public static Setting<Boolean> disables;
    public static Setting<Boolean> toggleOnStep;
    public Timer timer;
    private final BlockPos[] pos = new BlockPos[]{new BlockPos(0, 0, 1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0)};

    @Override
    public void onLogout() {
        this.disable();
    }

    public SurroundRewrite() {
        super("Surround", "Surrounds you with obsidian at your feet to block crystal damage", Module.Category.COMBAT, true, false, false);
        rotate = this.register(new Setting<Boolean>("Rotate", true));
        packet = this.register(new Setting<Boolean>("Packet Place", true));
        ground = this.register(new Setting<Boolean>("Build Base", true));
        noFullExtend = this.register(new Setting<Integer>("No Full Extend", 1, -10, 10));
        crystalClear = this.register(new Setting<CrystalClear>("Crystal Clear", CrystalClear.Toggle));
        crystalClearRange = this.register(new Setting<Integer>("Crystal Clear Range", 2, 1, 5));
        maxSelfDamage = this.register(new Setting<Float>("Max Self Damage", Float.valueOf(10.0f), Float.valueOf(0.1f), Float.valueOf(20.0f)));
        swingCrystalClear = this.register(new Setting<Boolean>("Swing Crystal Clear", true));
        packetCrystal = this.register(new Setting<Boolean>("Packet Crystal", true));
        extraPacket = this.register(new Setting<Boolean>("Extra Packet", false));
        emptyCheck = this.register(new Setting<Boolean>("Empty Check", false));
        shift = this.register(new Setting<Boolean>("Place On Shift", false));
        center = this.register(new Setting<Boolean>("Center", false));
        sensitivity = this.register(new Setting<Sensitivity>("Sensitivity", Sensitivity.High));
        antiCity = this.register(new Setting<AntiCity>("Anti City", AntiCity.Smart));
        delay = this.register(new Setting<Integer>("Delay", 1, 0, 25));
        bps = this.register(new Setting<Integer>("Blocks Per Place", 16, 1, 20));
        yDisable = this.register(new Setting<Boolean>("Y Disable", true));
        disables = this.register(new Setting<Boolean>("Jump Disable", true));
        toggleOnStep = this.register(new Setting<Boolean>("Toggle On Step", true));
        this.timer = new Timer();
    }

    @Override
    public void onEnable() {
        BlockPos centerPos = new BlockPos((double)PlayerUtil.getPlayerPosFloored().func_177958_n() + 0.5, (double)PlayerUtil.getPlayerPosFloored().func_177956_o(), (double)PlayerUtil.getPlayerPosFloored().func_177952_p() + 0.5);
        if (center.getValue().booleanValue()) {
            SurroundRewrite.mc.field_71439_g.func_70107_b(centerPos.func_177958_n(), centerPos.func_177956_o(), centerPos.func_177952_p());
            SurroundRewrite.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayer$Position(centerPos.func_177958_n(), centerPos.func_177956_o(), centerPos.func_177952_p(), SurroundRewrite.mc.field_71439_g.field_70122_E));
        }
        if (crystalClear.getValue() == CrystalClear.Toggle) {
            for (Entity entities : SurroundRewrite.mc.field_71441_e.field_72996_f) {
                if (!(entities instanceof EntityEnderCrystal) || !(SurroundRewrite.mc.field_71439_g.func_70011_f(entities.field_70165_t, entities.field_70163_u, entities.field_70161_v) < (double)crystalClearRange.getValue().intValue()) || EntityUtil.isSafe(SurroundRewrite.mc.field_71439_g)) continue;
                EntityUtil.attackEntity(entities, packetCrystal.getValue(), swingCrystalClear.getValue());
            }
        }
    }

    @Override
    public void onUpdate() {
        BlockPos posB;
        BlockPos posA;
        BlockPos playerPos;
        if (SurroundRewrite.nullCheck()) {
            return;
        }
        if (yDisable.getValue().booleanValue() && SurroundRewrite.mc.field_71439_g.field_70163_u != (double)(playerPos = new BlockPos(SurroundRewrite.mc.field_71439_g.field_70165_t, SurroundRewrite.mc.field_71439_g.field_70163_u, SurroundRewrite.mc.field_71439_g.field_70161_v)).func_177956_o()) {
            this.disable();
        }
        if (disables.getValue().booleanValue() && SurroundRewrite.mc.field_71474_y.field_74314_A.func_151470_d()) {
            this.disable();
        }
        if (toggleOnStep.getValue().booleanValue() && OctoHack.moduleManager.isModuleEnabled("Step")) {
            this.disable();
        }
        if (shift.getValue().booleanValue() && !SurroundRewrite.mc.field_71474_y.field_74311_E.func_151470_d()) {
            return;
        }
        if (shift.getValue().booleanValue() && SurroundRewrite.mc.field_71474_y.field_74311_E.func_151470_d() && (SurroundRewrite.mc.field_71439_g.func_184613_cA() || SurroundRewrite.mc.field_71439_g.field_71075_bZ.field_75100_b)) {
            return;
        }
        playerPos = PlayerUtil.getPlayerPosFloored();
        int placed = 0;
        int oldslot = SurroundRewrite.mc.field_71439_g.field_71071_by.field_70461_c;
        ArrayList<BlockPos> blocks = EntityUtil.getPos(0.0, 0.0, 0.0, SurroundRewrite.mc.field_71439_g);
        if (blocks.size() == 2) {
            Block block1;
            Block block;
            BlockPos pos2 = blocks.get(1);
            BlockPos pos = blocks.get(0);
            BlockPos[] offsets = new BlockPos[]{pos.func_177978_c(), pos.func_177968_d(), pos.func_177974_f(), pos.func_177976_e()};
            BlockPos[] offsets2 = new BlockPos[]{pos2.func_177978_c(), pos2.func_177968_d(), pos2.func_177974_f(), pos2.func_177976_e()};
            if (WorldUtils.empty.contains(WorldUtils.getBlock(pos2.func_177977_b())) && !this.intersectsWithEntity(pos)) {
                this.placeBlock(pos2.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                ++placed;
            }
            if (placed > bps.getValue() - 1) {
                return;
            }
            if (WorldUtils.empty.contains(WorldUtils.getBlock(pos.func_177977_b())) && !this.intersectsWithEntity(pos)) {
                this.placeBlock(pos.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                ++placed;
            }
            for (BlockPos off : offsets) {
                if (placed > bps.getValue() - 1) continue;
                block = WorldUtils.getBlock(off);
                block1 = WorldUtils.getBlock(off.func_177977_b());
                if (WorldUtils.empty.contains(block1) && ground.getValue().booleanValue() && !this.intersectsWithEntity(off.func_177977_b())) {
                    this.placeBlock(off.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                    ++placed;
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(block) || this.intersectsWithEntity(off)) continue;
                if (SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150486_ae || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150447_bR || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150465_bP) {
                    this.placeBlock(new BlockPos(off.func_177958_n(), off.func_177956_o() + noFullExtend.getValue(), off.func_177952_p()), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                } else {
                    this.placeBlock(off, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                ++placed;
            }
            for (BlockPos off : offsets2) {
                if (placed > bps.getValue() - 1) continue;
                block = WorldUtils.getBlock(off);
                block1 = WorldUtils.getBlock(off.func_177977_b());
                if (WorldUtils.empty.contains(block1) && ground.getValue().booleanValue() && !this.intersectsWithEntity(off.func_177977_b())) {
                    this.placeBlock(off.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                    ++placed;
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(block) || this.intersectsWithEntity(off)) continue;
                if (SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150486_ae || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150447_bR || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150465_bP) {
                    this.placeBlock(new BlockPos(off.func_177958_n(), off.func_177956_o() + noFullExtend.getValue(), off.func_177952_p()), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                } else {
                    this.placeBlock(off, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                ++placed;
            }
        } else if (blocks.size() == 4) {
            Block block1;
            Block block;
            posA = blocks.get(1);
            posB = blocks.get(2);
            BlockPos posC = blocks.get(3);
            BlockPos posD = blocks.get(0);
            BlockPos[] offsetsA = new BlockPos[]{posA.func_177978_c(), posA.func_177968_d(), posA.func_177974_f(), posA.func_177976_e()};
            BlockPos[] offsetsB = new BlockPos[]{posB.func_177978_c(), posB.func_177968_d(), posB.func_177974_f(), posB.func_177976_e()};
            BlockPos[] offsetsC = new BlockPos[]{posC.func_177978_c(), posC.func_177968_d(), posC.func_177974_f(), posC.func_177976_e()};
            BlockPos[] offsetsD = new BlockPos[]{posD.func_177978_c(), posD.func_177968_d(), posD.func_177974_f(), posD.func_177976_e()};
            if (WorldUtils.empty.contains(WorldUtils.getBlock(posA.func_177977_b())) && !this.intersectsWithEntity(posA)) {
                this.placeBlock(posA.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                ++placed;
            }
            if (placed > bps.getValue() - 1) {
                return;
            }
            if (WorldUtils.empty.contains(WorldUtils.getBlock(posB.func_177977_b())) && !this.intersectsWithEntity(posB)) {
                this.placeBlock(posB.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                ++placed;
            }
            if (placed > bps.getValue() - 1) {
                return;
            }
            if (WorldUtils.empty.contains(WorldUtils.getBlock(posC.func_177977_b())) && !this.intersectsWithEntity(posC)) {
                this.placeBlock(posC.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                ++placed;
            }
            if (placed > bps.getValue() - 1) {
                return;
            }
            if (WorldUtils.empty.contains(WorldUtils.getBlock(posD.func_177977_b())) && !this.intersectsWithEntity(posD)) {
                this.placeBlock(posD.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                ++placed;
            }
            for (BlockPos off : offsetsA) {
                if (placed > bps.getValue() - 1) continue;
                block = WorldUtils.getBlock(off);
                block1 = WorldUtils.getBlock(off.func_177977_b());
                if (WorldUtils.empty.contains(block1) && ground.getValue().booleanValue() && !this.intersectsWithEntity(off.func_177977_b())) {
                    this.placeBlock(off.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                    ++placed;
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(block) || this.intersectsWithEntity(off)) continue;
                if (SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150486_ae || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150447_bR || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150465_bP) {
                    this.placeBlock(new BlockPos(off.func_177958_n(), off.func_177956_o() + noFullExtend.getValue(), off.func_177952_p()), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                } else {
                    this.placeBlock(off, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                ++placed;
            }
            for (BlockPos off : offsetsB) {
                if (placed > bps.getValue() - 1) continue;
                block = WorldUtils.getBlock(off);
                block1 = WorldUtils.getBlock(off.func_177977_b());
                if (WorldUtils.empty.contains(block1) && ground.getValue().booleanValue() && !this.intersectsWithEntity(off.func_177977_b())) {
                    this.placeBlock(off.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                    ++placed;
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(block) || this.intersectsWithEntity(off)) continue;
                if (SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150486_ae || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150447_bR || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150465_bP) {
                    this.placeBlock(new BlockPos(off.func_177958_n(), off.func_177956_o() + noFullExtend.getValue(), off.func_177952_p()), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                } else {
                    this.placeBlock(off, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                ++placed;
            }
            for (BlockPos off : offsetsC) {
                if (placed > bps.getValue() - 1) continue;
                block = WorldUtils.getBlock(off);
                block1 = WorldUtils.getBlock(off.func_177977_b());
                if (WorldUtils.empty.contains(block1) && ground.getValue().booleanValue() && !this.intersectsWithEntity(off.func_177977_b())) {
                    this.placeBlock(off.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                    ++placed;
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(block) || this.intersectsWithEntity(off)) continue;
                if (SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150486_ae || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150447_bR || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150465_bP) {
                    this.placeBlock(new BlockPos(off.func_177958_n(), off.func_177956_o() + noFullExtend.getValue(), off.func_177952_p()), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                } else {
                    this.placeBlock(off, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                ++placed;
            }
            for (BlockPos off : offsetsD) {
                if (placed > bps.getValue() - 1) continue;
                block = WorldUtils.getBlock(off);
                block1 = WorldUtils.getBlock(off.func_177977_b());
                if (WorldUtils.empty.contains(block1) && ground.getValue().booleanValue() && !this.intersectsWithEntity(off.func_177977_b())) {
                    this.placeBlock(off.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                    ++placed;
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(block) || this.intersectsWithEntity(off)) continue;
                if (SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150486_ae || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150447_bR || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150465_bP) {
                    this.placeBlock(new BlockPos(off.func_177958_n(), off.func_177956_o() + noFullExtend.getValue(), off.func_177952_p()), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                } else {
                    this.placeBlock(off, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                ++placed;
            }
        } else if (blocks.size() == 3) {
            Block block1;
            Block block;
            posA = blocks.get(1);
            posB = blocks.get(2);
            BlockPos posD = blocks.get(0);
            BlockPos[] offsetsA = new BlockPos[]{posA.func_177978_c(), posA.func_177968_d(), posA.func_177974_f(), posA.func_177976_e()};
            BlockPos[] offsetsB = new BlockPos[]{posB.func_177978_c(), posB.func_177968_d(), posB.func_177974_f(), posB.func_177976_e()};
            BlockPos[] offsetsD = new BlockPos[]{posD.func_177978_c(), posD.func_177968_d(), posD.func_177974_f(), posD.func_177976_e()};
            if (WorldUtils.empty.contains(WorldUtils.getBlock(posA.func_177977_b())) && !this.intersectsWithEntity(posA)) {
                this.placeBlock(posA.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                ++placed;
            }
            if (placed > bps.getValue() - 1) {
                return;
            }
            if (WorldUtils.empty.contains(WorldUtils.getBlock(posB.func_177977_b())) && !this.intersectsWithEntity(posB)) {
                this.placeBlock(posB.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                ++placed;
            }
            if (placed > bps.getValue() - 1) {
                return;
            }
            if (WorldUtils.empty.contains(WorldUtils.getBlock(posD.func_177977_b())) && !this.intersectsWithEntity(posD)) {
                this.placeBlock(posD.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                ++placed;
            }
            for (BlockPos off : offsetsA) {
                if (placed > bps.getValue() - 1) continue;
                block = WorldUtils.getBlock(off);
                block1 = WorldUtils.getBlock(off.func_177977_b());
                if (WorldUtils.empty.contains(block1) && ground.getValue().booleanValue() && !this.intersectsWithEntity(off.func_177977_b())) {
                    this.placeBlock(off.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                    ++placed;
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(block) || this.intersectsWithEntity(off)) continue;
                if (SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150486_ae || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150447_bR || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150465_bP) {
                    this.placeBlock(new BlockPos(off.func_177958_n(), off.func_177956_o() + noFullExtend.getValue(), off.func_177952_p()), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                } else {
                    this.placeBlock(off, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                ++placed;
            }
            for (BlockPos off : offsetsB) {
                if (placed > bps.getValue() - 1) continue;
                block = WorldUtils.getBlock(off);
                block1 = WorldUtils.getBlock(off.func_177977_b());
                if (WorldUtils.empty.contains(block1) && ground.getValue().booleanValue() && !this.intersectsWithEntity(off.func_177977_b())) {
                    this.placeBlock(off.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                    ++placed;
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(block) || this.intersectsWithEntity(off)) continue;
                if (SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150486_ae || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150447_bR || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150465_bP) {
                    this.placeBlock(new BlockPos(off.func_177958_n(), off.func_177956_o() + noFullExtend.getValue(), off.func_177952_p()), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                } else {
                    this.placeBlock(off, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                ++placed;
            }
            for (BlockPos off : offsetsD) {
                if (placed > bps.getValue() - 1) continue;
                block = WorldUtils.getBlock(off);
                block1 = WorldUtils.getBlock(off.func_177977_b());
                if (WorldUtils.empty.contains(block1) && ground.getValue().booleanValue() && !this.intersectsWithEntity(off.func_177977_b())) {
                    this.placeBlock(off.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                    ++placed;
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(block) || this.intersectsWithEntity(off)) continue;
                if (SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150486_ae || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150447_bR || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150465_bP) {
                    this.placeBlock(new BlockPos(off.func_177958_n(), off.func_177956_o() + noFullExtend.getValue(), off.func_177952_p()), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                } else {
                    this.placeBlock(off, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                ++placed;
            }
        } else if (blocks.size() == 6) {
            Block block1;
            Block block;
            posA = blocks.get(1);
            posB = blocks.get(2);
            BlockPos posC = blocks.get(3);
            BlockPos posD = blocks.get(0);
            BlockPos posE = blocks.get(4);
            BlockPos posF = blocks.get(5);
            BlockPos[] offsetsA = new BlockPos[]{posA.func_177978_c(), posA.func_177968_d(), posA.func_177974_f(), posA.func_177976_e()};
            BlockPos[] offsetsB = new BlockPos[]{posB.func_177978_c(), posB.func_177968_d(), posB.func_177974_f(), posB.func_177976_e()};
            BlockPos[] offsetsD = new BlockPos[]{posD.func_177978_c(), posD.func_177968_d(), posD.func_177974_f(), posD.func_177976_e()};
            BlockPos[] offsetsC = new BlockPos[]{posC.func_177978_c(), posC.func_177968_d(), posC.func_177974_f(), posC.func_177976_e()};
            BlockPos[] offsetsE = new BlockPos[]{posE.func_177978_c(), posE.func_177968_d(), posE.func_177974_f(), posE.func_177976_e()};
            BlockPos[] offsetsF = new BlockPos[]{posF.func_177978_c(), posF.func_177968_d(), posF.func_177974_f(), posF.func_177976_e()};
            if (WorldUtils.empty.contains(WorldUtils.getBlock(posA.func_177977_b())) && !this.intersectsWithEntity(posA)) {
                this.placeBlock(posA.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                ++placed;
            }
            if (placed > bps.getValue() - 1) {
                return;
            }
            if (WorldUtils.empty.contains(WorldUtils.getBlock(posB.func_177977_b())) && !this.intersectsWithEntity(posB)) {
                this.placeBlock(posB.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                ++placed;
            }
            if (placed > bps.getValue() - 1) {
                return;
            }
            if (WorldUtils.empty.contains(WorldUtils.getBlock(posD.func_177977_b())) && !this.intersectsWithEntity(posD)) {
                this.placeBlock(posD.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                ++placed;
            }
            if (placed > bps.getValue() - 1) {
                return;
            }
            if (WorldUtils.empty.contains(WorldUtils.getBlock(posC.func_177977_b())) && !this.intersectsWithEntity(posC)) {
                this.placeBlock(posC.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                ++placed;
            }
            if (placed > bps.getValue() - 1) {
                return;
            }
            if (WorldUtils.empty.contains(WorldUtils.getBlock(posE.func_177977_b())) && !this.intersectsWithEntity(posE)) {
                this.placeBlock(posE.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                ++placed;
            }
            if (placed > bps.getValue() - 1) {
                return;
            }
            if (WorldUtils.empty.contains(WorldUtils.getBlock(posF.func_177977_b())) && !this.intersectsWithEntity(posF)) {
                this.placeBlock(posF.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                ++placed;
            }
            for (BlockPos off : offsetsA) {
                if (placed > bps.getValue() - 1) continue;
                block = WorldUtils.getBlock(off);
                block1 = WorldUtils.getBlock(off.func_177977_b());
                if (WorldUtils.empty.contains(block1) && ground.getValue().booleanValue() && !this.intersectsWithEntity(off.func_177977_b())) {
                    this.placeBlock(off.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                    ++placed;
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(block) || this.intersectsWithEntity(off)) continue;
                if (SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150486_ae || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150447_bR || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150465_bP) {
                    this.placeBlock(new BlockPos(off.func_177958_n(), off.func_177956_o() + noFullExtend.getValue(), off.func_177952_p()), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                } else {
                    this.placeBlock(off, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                ++placed;
            }
            for (BlockPos off : offsetsB) {
                if (placed > bps.getValue() - 1) continue;
                block = WorldUtils.getBlock(off);
                block1 = WorldUtils.getBlock(off.func_177977_b());
                if (WorldUtils.empty.contains(block1) && ground.getValue().booleanValue() && !this.intersectsWithEntity(off.func_177977_b())) {
                    this.placeBlock(off.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                    ++placed;
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(block) || this.intersectsWithEntity(off)) continue;
                if (SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150486_ae || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150447_bR || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150465_bP) {
                    this.placeBlock(new BlockPos(off.func_177958_n(), off.func_177956_o() + noFullExtend.getValue(), off.func_177952_p()), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                } else {
                    this.placeBlock(off, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                ++placed;
            }
            for (BlockPos off : offsetsD) {
                if (placed > bps.getValue() - 1) continue;
                block = WorldUtils.getBlock(off);
                block1 = WorldUtils.getBlock(off.func_177977_b());
                if (WorldUtils.empty.contains(block1) && ground.getValue().booleanValue() && !this.intersectsWithEntity(off.func_177977_b())) {
                    this.placeBlock(off.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                    ++placed;
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(block) || this.intersectsWithEntity(off)) continue;
                if (SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150486_ae || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150447_bR || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150465_bP) {
                    this.placeBlock(new BlockPos(off.func_177958_n(), off.func_177956_o() + noFullExtend.getValue(), off.func_177952_p()), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                } else {
                    this.placeBlock(off, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                ++placed;
            }
            for (BlockPos off : offsetsC) {
                if (placed > bps.getValue() - 1) continue;
                block = WorldUtils.getBlock(off);
                block1 = WorldUtils.getBlock(off.func_177977_b());
                if (WorldUtils.empty.contains(block1) && ground.getValue().booleanValue() && !this.intersectsWithEntity(off.func_177977_b())) {
                    this.placeBlock(off.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                    ++placed;
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(block) || this.intersectsWithEntity(off)) continue;
                if (SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150486_ae || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150447_bR || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150465_bP) {
                    this.placeBlock(new BlockPos(off.func_177958_n(), off.func_177956_o() + noFullExtend.getValue(), off.func_177952_p()), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                } else {
                    this.placeBlock(off, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                ++placed;
            }
            for (BlockPos off : offsetsE) {
                if (placed > bps.getValue() - 1) continue;
                block = WorldUtils.getBlock(off);
                block1 = WorldUtils.getBlock(off.func_177977_b());
                if (WorldUtils.empty.contains(block1) && ground.getValue().booleanValue() && !this.intersectsWithEntity(off.func_177977_b())) {
                    this.placeBlock(off.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                    ++placed;
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(block) || this.intersectsWithEntity(off)) continue;
                if (SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150486_ae || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150447_bR || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150465_bP) {
                    this.placeBlock(new BlockPos(off.func_177958_n(), off.func_177956_o() + noFullExtend.getValue(), off.func_177952_p()), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                } else {
                    this.placeBlock(off, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                ++placed;
            }
            for (BlockPos off : offsetsF) {
                if (placed > bps.getValue() - 1) continue;
                block = WorldUtils.getBlock(off);
                block1 = WorldUtils.getBlock(off.func_177977_b());
                if (WorldUtils.empty.contains(block1) && ground.getValue().booleanValue() && !this.intersectsWithEntity(off.func_177977_b())) {
                    this.placeBlock(off.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                    ++placed;
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(block) || this.intersectsWithEntity(off)) continue;
                if (SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150486_ae || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150447_bR || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150465_bP) {
                    this.placeBlock(new BlockPos(off.func_177958_n(), off.func_177956_o() + noFullExtend.getValue(), off.func_177952_p()), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                } else {
                    this.placeBlock(off, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                ++placed;
            }
        } else if (blocks.size() == 1) {
            BlockPos pos = blocks.get(0);
            BlockPos[] offsets = new BlockPos[]{pos.func_177978_c(), pos.func_177968_d(), pos.func_177974_f(), pos.func_177976_e()};
            if (WorldUtils.empty.contains(WorldUtils.getBlock(pos.func_177977_b())) && !this.intersectsWithEntity(pos)) {
                this.placeBlock(pos.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                ++placed;
            }
            for (BlockPos off : offsets) {
                if (placed > bps.getValue() - 1) continue;
                Block block = WorldUtils.getBlock(off);
                Block block1 = WorldUtils.getBlock(off.func_177977_b());
                if (WorldUtils.empty.contains(block1) && ground.getValue().booleanValue() && !this.intersectsWithEntity(off.func_177977_b())) {
                    this.placeBlock(off.func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                    ++placed;
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(block) || this.intersectsWithEntity(off)) continue;
                if (SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150486_ae || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150447_bR || SurroundRewrite.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150465_bP) {
                    this.placeBlock(new BlockPos(off.func_177958_n(), off.func_177956_o() + noFullExtend.getValue(), off.func_177952_p()), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                } else {
                    this.placeBlock(off, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                ++placed;
            }
        } else {
            Vec3d Center = new Vec3d((double)PlayerUtil.getPlayerPosFloored().func_177958_n() + 0.5, PlayerUtil.getPlayerPosFloored().func_177956_o(), (double)PlayerUtil.getPlayerPosFloored().func_177952_p() + 0.5);
            for (BlockPos pos : this.pos) {
                if (placed > bps.getValue() - 1) continue;
                if (WorldUtils.empty.contains(WorldUtils.getBlock(playerPos.func_177971_a(pos).func_177977_b())) && !this.intersectsWithEntity(pos.func_177977_b())) {
                    ++placed;
                    this.placeBlock(playerPos.func_177971_a(pos).func_177977_b(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
                }
                if (placed > bps.getValue() - 1 || !WorldUtils.empty.contains(WorldUtils.getBlock(playerPos.func_177971_a(pos))) || this.intersectsWithEntity(pos)) continue;
                ++placed;
                this.placeBlock(playerPos.func_177971_a(pos), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
            }
        }
        if (placed == 0) {
            // empty if block
        }
        if (crystalClear.getValue() == CrystalClear.Always) {
            for (Entity entities : SurroundRewrite.mc.field_71441_e.field_72996_f) {
                if (!(entities instanceof EntityEnderCrystal) || !(SurroundRewrite.mc.field_71439_g.func_70011_f(entities.field_70165_t, entities.field_70163_u, entities.field_70161_v) < (double)crystalClearRange.getValue().intValue()) || EntityUtil.isSafe(SurroundRewrite.mc.field_71439_g)) continue;
                EntityUtil.attackEntity(entities, packetCrystal.getValue(), swingCrystalClear.getValue());
            }
        }
    }

    private void placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean sneaking) {
        int oldSlot = SurroundRewrite.mc.field_71439_g.field_71071_by.field_70461_c;
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        int old_slot = -1;
        BlockPos ecPos = new BlockPos(pos.func_177958_n(), pos.func_177956_o() + 1, pos.func_177952_p());
        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
            if (sensitivity.getValue() == Sensitivity.High && obbySlot != SurroundRewrite.mc.field_71439_g.field_71071_by.field_70461_c) {
                old_slot = SurroundRewrite.mc.field_71439_g.field_71071_by.field_70461_c;
                SurroundRewrite.mc.field_71439_g.field_71071_by.field_70461_c = obbySlot;
            }
            if (sensitivity.getValue() == Sensitivity.Low) {
                SurroundRewrite.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketHeldItemChange(obbySlot));
            }
            if (this.timer.passedMs(delay.getValue().intValue())) {
                BlockUtil.placeBlock(pos, hand, rotate, packet, extraPacket.getValue(), sneaking);
                this.timer.reset();
            }
            if (sensitivity.getValue() == Sensitivity.High && old_slot != -1) {
                SurroundRewrite.mc.field_71439_g.field_71071_by.field_70461_c = old_slot;
            }
            if (sensitivity.getValue() == Sensitivity.Low) {
                SurroundRewrite.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketHeldItemChange(oldSlot));
            }
        } else if (InventoryUtil.findHotbarBlock(BlockEnderChest.class) != -1) {
            if (sensitivity.getValue() == Sensitivity.High && echestSlot != SurroundRewrite.mc.field_71439_g.field_71071_by.field_70461_c) {
                old_slot = SurroundRewrite.mc.field_71439_g.field_71071_by.field_70461_c;
                SurroundRewrite.mc.field_71439_g.field_71071_by.field_70461_c = echestSlot;
            }
            if (sensitivity.getValue() == Sensitivity.Low) {
                SurroundRewrite.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketHeldItemChange(echestSlot));
            }
            if (this.timer.passedMs(delay.getValue().intValue())) {
                BlockUtil.placeBlock(pos, hand, rotate, packet, extraPacket.getValue(), sneaking);
                this.timer.reset();
            }
            if (sensitivity.getValue() == Sensitivity.High && old_slot != -1) {
                SurroundRewrite.mc.field_71439_g.field_71071_by.field_70461_c = old_slot;
            }
            if (sensitivity.getValue() == Sensitivity.Low) {
                SurroundRewrite.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketHeldItemChange(oldSlot));
            }
        }
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : SurroundRewrite.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem || entity instanceof EntityEnderCrystal || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    public static enum CrystalClear {
        Always,
        Toggle;

    }

    public static enum AntiCity {
        Smart,
        None;

    }

    public static enum Sensitivity {
        High,
        Low;

    }
}
