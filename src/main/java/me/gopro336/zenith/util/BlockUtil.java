package me.primooctopus33.octohack.util;

import com.google.common.util.concurrent.AtomicDouble;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import me.primooctopus33.octohack.OctoHack;
import me.primooctopus33.octohack.client.command.Command;
import me.primooctopus33.octohack.client.modules.combat.Surround;
import me.primooctopus33.octohack.util.CrystalUtil;
import me.primooctopus33.octohack.util.EntityUtil;
import me.primooctopus33.octohack.util.InventoryUtil;
import me.primooctopus33.octohack.util.MathUtil;
import me.primooctopus33.octohack.util.RotationUtil;
import me.primooctopus33.octohack.util.TestUtil;
import me.primooctopus33.octohack.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction$Action;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer$Rotation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class BlockUtil
        implements Util {
    public static List<Block> rightclickableBlocks = Arrays.asList(Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150477_bB, Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA, Blocks.field_150467_bQ, Blocks.field_150471_bO, Blocks.field_150430_aB, Blocks.field_150441_bU, Blocks.field_150413_aR, Blocks.field_150416_aS, Blocks.field_150455_bV, Blocks.field_180390_bo, Blocks.field_180391_bp, Blocks.field_180392_bq, Blocks.field_180386_br, Blocks.field_180385_bs, Blocks.field_180387_bt, Blocks.field_150382_bo, Blocks.field_150367_z, Blocks.field_150409_cd, Blocks.field_150442_at, Blocks.field_150323_B, Blocks.field_150421_aI, Blocks.field_150461_bJ, Blocks.field_150324_C, Blocks.field_150460_al, Blocks.field_180413_ao, Blocks.field_180414_ap, Blocks.field_180412_aq, Blocks.field_180411_ar, Blocks.field_180410_as, Blocks.field_180409_at, Blocks.field_150414_aQ, Blocks.field_150381_bn, Blocks.field_150380_bt, Blocks.field_150438_bZ, Blocks.field_185776_dc, Blocks.field_150483_bI, Blocks.field_185777_dd, Blocks.field_150462_ai);
    public static final List<Block> blackList = Arrays.asList(Blocks.field_150477_bB, Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150462_ai, Blocks.field_150467_bQ, Blocks.field_150382_bo, Blocks.field_150438_bZ, Blocks.field_150409_cd, Blocks.field_150367_z, Blocks.field_150415_aT, Blocks.field_150381_bn);
    public static final List<Block> shulkerList = Arrays.asList(Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA);
    public static final List<Block> unSafeBlocks = Arrays.asList(Blocks.field_150343_Z, Blocks.field_150357_h, Blocks.field_150477_bB, Blocks.field_150467_bQ);
    public static List<Block> unSolidBlocks = Arrays.asList(Blocks.field_150356_k, Blocks.field_150457_bL, Blocks.field_150433_aE, Blocks.field_150404_cg, Blocks.field_185764_cQ, Blocks.field_150465_bP, Blocks.field_150457_bL, Blocks.field_150473_bD, Blocks.field_150479_bC, Blocks.field_150471_bO, Blocks.field_150442_at, Blocks.field_150430_aB, Blocks.field_150468_ap, Blocks.field_150441_bU, Blocks.field_150455_bV, Blocks.field_150413_aR, Blocks.field_150416_aS, Blocks.field_150437_az, Blocks.field_150429_aA, Blocks.field_150488_af, Blocks.field_150350_a, Blocks.field_150427_aO, Blocks.field_150384_bq, Blocks.field_150355_j, Blocks.field_150358_i, Blocks.field_150353_l, Blocks.field_150356_k, Blocks.field_150345_g, Blocks.field_150328_O, Blocks.field_150327_N, Blocks.field_150338_P, Blocks.field_150337_Q, Blocks.field_150464_aj, Blocks.field_150459_bM, Blocks.field_150469_bN, Blocks.field_185773_cZ, Blocks.field_150436_aH, Blocks.field_150393_bb, Blocks.field_150394_bc, Blocks.field_150392_bi, Blocks.field_150388_bm, Blocks.field_150375_by, Blocks.field_185766_cS, Blocks.field_185765_cR, Blocks.field_150329_H, Blocks.field_150330_I, Blocks.field_150395_bd, Blocks.field_150480_ab, Blocks.field_150448_aq, Blocks.field_150408_cc, Blocks.field_150319_E, Blocks.field_150318_D, Blocks.field_150478_aa);
    public static List<Block> emptyBlocks = Arrays.asList(Blocks.field_150350_a, Blocks.field_150356_k, Blocks.field_150353_l, Blocks.field_150358_i, Blocks.field_150355_j, Blocks.field_150395_bd, Blocks.field_150431_aC, Blocks.field_150329_H, Blocks.field_150480_ab);
    public static List<Block> rightClickableBlocks;
    private static BlockPos _currentBlock;
    private static boolean _started;

    public static List<BlockPos> getBlockSphere(float breakRange, Class clazz) {
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)BlockUtil.getSphere(EntityUtil.getPlayerPos(BlockUtil.mc.field_71439_g), breakRange, (int)breakRange, false, true, 0).stream().filter(pos -> clazz.isInstance(BlockUtil.mc.field_71441_e.func_180495_p((BlockPos)pos).func_177230_c())).collect(Collectors.toList()));
        return positions;
    }

    public static List<EnumFacing> getPossibleSides(BlockPos pos) {
        ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        for (EnumFacing side : EnumFacing.values()) {
            IBlockState blockState;
            BlockPos neighbour = pos.func_177972_a(side);
            if (!BlockUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(BlockUtil.mc.field_71441_e.func_180495_p(neighbour), false) || (blockState = BlockUtil.mc.field_71441_e.func_180495_p(neighbour)).func_185904_a().func_76222_j()) continue;
            facings.add(side);
        }
        return facings;
    }

    public static EnumFacing getFirstFacing(BlockPos pos) {
        Iterator<EnumFacing> iterator = BlockUtil.getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            EnumFacing facing = iterator.next();
            return facing;
        }
        return null;
    }

    public static EnumFacing getRayTraceFacing(BlockPos pos) {
        RayTraceResult result = BlockUtil.mc.field_71441_e.func_72933_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n() + 0.5, (double)pos.func_177958_n() - 0.5, (double)pos.func_177958_n() + 0.5));
        if (result == null || result.field_178784_b == null) {
            return EnumFacing.UP;
        }
        return result.field_178784_b;
    }

    public static boolean isBlockNotEmpty(BlockPos pos) {
        if (emptyBlocks.contains(BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c())) {
            Entity entity;
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(pos);
            Iterator iterator = BlockUtil.mc.field_71441_e.field_72996_f.iterator();
            do {
                if (iterator.hasNext()) continue;
                return true;
            } while (!((entity = (Entity)iterator.next()) instanceof EntityLivingBase) || !axisAlignedBB.func_72326_a(entity.func_174813_aQ()));
        }
        return false;
    }

    public static void rotatePacket(double x, double y, double z) {
        double diffX = x - BlockUtil.mc.field_71439_g.field_70165_t;
        double diffY = y - (BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e());
        double diffZ = z - BlockUtil.mc.field_71439_g.field_70161_v;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayer$Rotation(yaw, pitch, BlockUtil.mc.field_71439_g.field_70122_E));
    }

    public static int isPositionPlaceable(BlockPos pos, boolean rayTrace) {
        return BlockUtil.isPositionPlaceable(pos, rayTrace, true);
    }

    public static int isPositionPlaceable(BlockPos pos, boolean rayTrace, boolean entityCheck) {
        Block block = BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        if (!(block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow)) {
            return 0;
        }
        if (!BlockUtil.rayTracePlaceCheck(pos, rayTrace, 0.0f)) {
            return -1;
        }
        if (entityCheck) {
            for (Entity entity : BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
                if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
                return 1;
            }
        }
        for (EnumFacing side : BlockUtil.getPossibleSides(pos)) {
            if (!BlockUtil.canBeClicked(pos.func_177972_a(side))) continue;
            return 3;
        }
        return 2;
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            float f = (float)(vec.field_72450_a - (double)pos.func_177958_n());
            float f1 = (float)(vec.field_72448_b - (double)pos.func_177956_o());
            float f2 = (float)(vec.field_72449_c - (double)pos.func_177952_p());
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        } else {
            BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, pos, direction, vec, hand);
        }
        BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        BlockUtil.mc.field_71467_ac = 4;
    }

    public static void rightClickBlockLegit(BlockPos pos, float range, boolean rotate, EnumHand hand, AtomicDouble Yaw, AtomicDouble Pitch, AtomicBoolean rotating) {
        Vec3d eyesPos = RotationUtil.getEyesPos();
        Vec3d posVec = new Vec3d(pos).func_72441_c(0.5, 0.5, 0.5);
        double distanceSqPosVec = eyesPos.func_72436_e(posVec);
        for (EnumFacing side : EnumFacing.values()) {
            Vec3d hitVec = posVec.func_178787_e(new Vec3d(side.func_176730_m()).func_186678_a(0.5));
            double distanceSqHitVec = eyesPos.func_72436_e(hitVec);
            if (!(distanceSqHitVec <= MathUtil.square(range)) || !(distanceSqHitVec < distanceSqPosVec) || BlockUtil.mc.field_71441_e.func_147447_a(eyesPos, hitVec, false, true, false) != null) continue;
            if (rotate) {
                float[] rotations = RotationUtil.getLegitRotations(hitVec);
                Yaw.set(rotations[0]);
                Pitch.set(rotations[1]);
                rotating.set(true);
            }
            BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, pos, side, hitVec, hand);
            BlockUtil.mc.field_71439_g.func_184609_a(hand);
            BlockUtil.mc.field_71467_ac = 4;
            break;
        }
    }

    public static boolean placeBlocks(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean extraPacket, boolean isSneaking) {
        Block block = BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        boolean sneaking = false;
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        EnumFacing side = BlockUtil.getPlaceableSide(pos);
        if (side == null) {
            return false;
        }
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d(neighbour).func_178787_e(new Vec3d(0.5, 0.5, 0.5)).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        Block neighbourBlock = BlockUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
        if (!BlockUtil.mc.field_71439_g.func_70093_af() && (blackList.contains(neighbourBlock) || shulkerList.contains(neighbourBlock))) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(BlockUtil.mc.field_71439_g, CPacketEntityAction$Action.START_SNEAKING));
            BlockUtil.mc.field_71439_g.func_70095_a(true);
            sneaking = true;
        }
        if (rotate) {
            RotationUtil.faceVectorPacketInstant(hitVec);
        }
        if (packet) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(pos.func_177972_a(opposite), opposite.func_176734_d(), EnumHand.MAIN_HAND, Float.intBitsToFloat(Float.floatToIntBits(2.7f)), Float.intBitsToFloat(Float.floatToIntBits(3.8f)), Float.intBitsToFloat(Float.floatToIntBits(30.0f))));
        }
        if (extraPacket) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(pos.func_177972_a(opposite), opposite.func_176734_d(), EnumHand.MAIN_HAND, Float.intBitsToFloat(Float.floatToIntBits(2.7f)), Float.intBitsToFloat(Float.floatToIntBits(3.8f)), Float.intBitsToFloat(Float.floatToIntBits(30.0f))));
        }
        BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, pos.func_177972_a(side), side.func_176734_d(), new Vec3d(pos), EnumHand.MAIN_HAND);
        BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, neighbour, opposite, hitVec, hand);
        BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(BlockUtil.mc.field_71439_g, CPacketEntityAction$Action.STOP_SNEAKING));
        return sneaking || isSneaking;
    }

    public static void clickBlock(BlockPos position, EnumFacing side, EnumHand hand, boolean packet) {
        if (packet) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(position.func_177972_a(side), side.func_176734_d(), hand, Float.intBitsToFloat(Float.floatToIntBits(17.0f)), Float.intBitsToFloat(Float.floatToIntBits(26.0f)), Float.intBitsToFloat(Float.floatToIntBits(3.0f))));
        } else {
            BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, position.func_177972_a(side), side.func_176734_d(), new Vec3d(position), hand);
        }
    }

    public static boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean extraPacket, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = BlockUtil.getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        }
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d(neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        Block neighbourBlock = BlockUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
        if (!BlockUtil.mc.field_71439_g.func_70093_af() && (blackList.contains(neighbourBlock) || shulkerList.contains(neighbourBlock))) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(BlockUtil.mc.field_71439_g, CPacketEntityAction$Action.START_SNEAKING));
            BlockUtil.mc.field_71439_g.func_70095_a(true);
            sneaking = true;
        }
        if (rotate) {
            RotationUtil.faceVector(hitVec, true);
        }
        if (extraPacket) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(pos.func_177972_a(side), side.func_176734_d(), EnumHand.MAIN_HAND, Float.intBitsToFloat(Float.floatToIntBits(2.7f)), Float.intBitsToFloat(Float.floatToIntBits(3.8f)), Float.intBitsToFloat(Float.floatToIntBits(30.0f))));
        }
        if (TestUtil.isBlockEmpty(pos) && SurroundRewrite.emptyCheck.getValue().booleanValue()) {
            BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
            BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, pos.func_177972_a(side), opposite.func_176734_d(), new Vec3d(pos), EnumHand.MAIN_HAND);
            BlockUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        } else {
            BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
            BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, pos.func_177972_a(side), opposite.func_176734_d(), new Vec3d(pos), EnumHand.MAIN_HAND);
            BlockUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        }
        BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        BlockUtil.mc.field_71467_ac = 4;
        return sneaking || isSneaking;
    }

    public static boolean placeBlockSmartRotate(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = BlockUtil.getFirstFacing(pos);
        Command.sendMessage(side.toString());
        if (side == null) {
            return isSneaking;
        }
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d(neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        Block neighbourBlock = BlockUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
        if (!BlockUtil.mc.field_71439_g.func_70093_af() && (blackList.contains(neighbourBlock) || shulkerList.contains(neighbourBlock))) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(BlockUtil.mc.field_71439_g, CPacketEntityAction$Action.START_SNEAKING));
            sneaking = true;
        }
        if (rotate) {
            OctoHack.rotationManager.lookAtVec3d(hitVec);
        }
        BlockUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        BlockUtil.mc.field_71467_ac = 4;
        return sneaking || isSneaking;
    }

    public static void placeBlockStopSneaking(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = BlockUtil.placeBlockSmartRotate(pos, hand, rotate, packet, isSneaking);
        if (!isSneaking && sneaking) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(BlockUtil.mc.field_71439_g, CPacketEntityAction$Action.STOP_SNEAKING));
        }
    }

    public static boolean isBothHole(BlockPos blockPos) {
        for (BlockPos pos : BlockUtil.getTouchingBlocks(blockPos)) {
            IBlockState touchingState = BlockUtil.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() != Blocks.field_150350_a && (touchingState.func_177230_c() == Blocks.field_150357_h || touchingState.func_177230_c() == Blocks.field_150343_Z)) continue;
            return false;
        }
        return true;
    }

    public static BlockPos[] getTouchingBlocks(BlockPos blockPos) {
        return new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b()};
    }

    public static List<BlockPos> getSphereRealth(float radius, boolean ignoreAir) {
        ArrayList<BlockPos> sphere = new ArrayList<BlockPos>();
        BlockPos pos = new BlockPos(BlockUtil.mc.field_71439_g.func_174791_d());
        int posX = pos.func_177958_n();
        int posY = pos.func_177956_o();
        int posZ = pos.func_177952_p();
        int radiuss = (int)radius;
        int x = posX - radiuss;
        while ((float)x <= (float)posX + radius) {
            int z = posZ - radiuss;
            while ((float)z <= (float)posZ + radius) {
                int y = posY - radiuss;
                while ((float)y < (float)posY + radius) {
                    if ((float)((posX - x) * (posX - x) + (posZ - z) * (posZ - z) + (posY - y) * (posY - y)) < radius * radius) {
                        BlockPos position = new BlockPos(x, y, z);
                        if (!ignoreAir || BlockUtil.mc.field_71441_e.func_180495_p(position).func_177230_c() != Blocks.field_150350_a) {
                            sphere.add(position);
                        }
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return sphere;
    }

    public static Vec3d[] getHelpingBlocks(Vec3d vec3d) {
        return new Vec3d[]{new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b - 1.0, vec3d.field_72449_c), new Vec3d(vec3d.field_72450_a != 0.0 ? vec3d.field_72450_a * 2.0 : vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72450_a != 0.0 ? vec3d.field_72449_c : vec3d.field_72449_c * 2.0), new Vec3d(vec3d.field_72450_a == 0.0 ? vec3d.field_72450_a + 1.0 : vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72450_a == 0.0 ? vec3d.field_72449_c : vec3d.field_72449_c + 1.0), new Vec3d(vec3d.field_72450_a == 0.0 ? vec3d.field_72450_a - 1.0 : vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72450_a == 0.0 ? vec3d.field_72449_c : vec3d.field_72449_c - 1.0), new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b + 1.0, vec3d.field_72449_c)};
    }

    public static List<BlockPos> possiblePlacePositions(float placeRange) {
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)BlockUtil.getSphere(EntityUtil.getPlayerPos(BlockUtil.mc.field_71439_g), placeRange, (int)placeRange, false, true, 0).stream().filter(BlockUtil::canPlaceCrystal).collect(Collectors.toList()));
        return positions;
    }

    public static List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = pos.func_177958_n();
        int cy = pos.func_177956_o();
        int cz = pos.func_177952_p();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                int y = sphere ? cy - (int)r : cy;
                while (true) {
                    float f2;
                    float f = y;
                    float f3 = f2 = sphere ? (float)cy + r : (float)(cy + h);
                    if (!(f < f2)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < (double)(r * r) && (!hollow || dist >= (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.func_177982_a(0, 1, 0);
        BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
        try {
            return (BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150357_h || BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z) && BlockUtil.mc.field_71441_e.func_180495_p(boost).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_180495_p(boost2).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty() && BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
        }
        catch (Exception e) {
            return false;
        }
    }

    public static List<BlockPos> possiblePlacePositions(float placeRange, boolean specialEntityCheck) {
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)BlockUtil.getSphere(EntityUtil.getPlayerPos(BlockUtil.mc.field_71439_g), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> BlockUtil.canPlaceCrystal(pos, specialEntityCheck)).collect(Collectors.toList()));
        return positions;
    }

    public static boolean placeBlock2(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = BlockUtil.getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        }
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d(neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        if (!BlockUtil.mc.field_71439_g.func_70093_af() && isSneaking) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(BlockUtil.mc.field_71439_g, CPacketEntityAction$Action.START_SNEAKING));
            BlockUtil.mc.field_71439_g.func_70095_a(true);
            sneaking = true;
        }
        BlockUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        BlockUtil.mc.field_71467_ac = 4;
        if (!BlockUtil.mc.field_71439_g.func_70093_af() && isSneaking) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(BlockUtil.mc.field_71439_g, CPacketEntityAction$Action.STOP_SNEAKING));
            BlockUtil.mc.field_71439_g.func_70095_a(false);
            sneaking = false;
        }
        return sneaking || isSneaking;
    }

    public static boolean isInterceptedByOther(BlockPos blockPos) {
        for (Entity entity : BlockUtil.mc.field_71441_e.field_72996_f) {
            if (entity.equals(BlockUtil.mc.field_71439_g) || !new AxisAlignedBB(blockPos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    public static List<BlockPos> possiblePlacePosition(float placeRange, boolean specialEntityCheck, boolean oneDot15) {
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)BlockUtil.getSphere(EntityUtil.getPlayerPos(BlockUtil.mc.field_71439_g), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> BlockUtil.canPlaceCrystal(pos, specialEntityCheck, oneDot15)).collect(Collectors.toList()));
        return positions;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos, boolean bl, boolean bl2, boolean bl3) {
        boolean multiPlace = bl;
        boolean placeUnderBlock = bl2;
        BlockPos position = blockPos;
        if (CrystalUtil.mc.field_71441_e.func_180495_p(position).func_177230_c() != Blocks.field_150357_h && CrystalUtil.mc.field_71441_e.func_180495_p(position).func_177230_c() != Blocks.field_150343_Z) {
            return false;
        }
        if (CrystalUtil.mc.field_71441_e.func_180495_p(position.func_177982_a(0, 1, 0)).func_177230_c() != Blocks.field_150350_a || !placeUnderBlock && CrystalUtil.mc.field_71441_e.func_180495_p(position.func_177982_a(0, 2, 0)).func_177230_c() != Blocks.field_150350_a) {
            return false;
        }
        if (multiPlace) {
            return CrystalUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(position.func_177982_a(0, 1, 0))).isEmpty() && !placeUnderBlock && CrystalUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(position.func_177982_a(0, 2, 0))).isEmpty();
        }
        for (Entity entity : CrystalUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(position.func_177982_a(0, 1, 0)))) {
            if (entity instanceof EntityEnderCrystal) continue;
            return false;
        }
        if (!placeUnderBlock) {
            for (Entity entity : CrystalUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(position.func_177982_a(0, 2, 0)))) {
                boolean holePlace = placeUnderBlock;
                if (entity instanceof EntityEnderCrystal || holePlace && entity instanceof EntityPlayer) continue;
                return false;
            }
        }
        return true;
    }

    public static void placeCrystalOnBlock(BlockPos pos, EnumHand hand, boolean swing, boolean exactHand, boolean silent) {
        RayTraceResult result = BlockUtil.mc.field_71441_e.func_72933_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() - 0.5, (double)pos.func_177952_p() + 0.5));
        EnumFacing facing = result == null || result.field_178784_b == null ? EnumFacing.UP : result.field_178784_b;
        int old = BlockUtil.mc.field_71439_g.field_71071_by.field_70461_c;
        int crystal = InventoryUtil.getItemHotbar(Items.field_185158_cP);
        if (hand == EnumHand.MAIN_HAND && silent && crystal != -1 && crystal != BlockUtil.mc.field_71439_g.field_71071_by.field_70461_c) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketHeldItemChange(crystal));
        }
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0f, 0.0f, 0.0f));
        if (hand == EnumHand.MAIN_HAND && silent && crystal != -1 && crystal != BlockUtil.mc.field_71439_g.field_71071_by.field_70461_c) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketHeldItemChange(old));
        }
        if (swing) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketAnimation(exactHand ? hand : EnumHand.MAIN_HAND));
        }
    }

    public static List<BlockPos> getSphere(float radius, boolean ignoreAir) {
        ArrayList<BlockPos> sphere = new ArrayList<BlockPos>();
        BlockPos pos = new BlockPos(BlockUtil.mc.field_71439_g.func_174791_d());
        int posX = pos.func_177958_n();
        int posY = pos.func_177956_o();
        int posZ = pos.func_177952_p();
        int radiuss = (int)radius;
        int x = posX - radiuss;
        while ((float)x <= (float)posX + radius) {
            int z = posZ - radiuss;
            while ((float)z <= (float)posZ + radius) {
                int y = posY - radiuss;
                while ((float)y < (float)posY + radius) {
                    if ((float)((posX - x) * (posX - x) + (posZ - z) * (posZ - z) + (posY - y) * (posY - y)) < radius * radius) {
                        BlockPos position = new BlockPos(x, y, z);
                        if (!ignoreAir || BlockUtil.mc.field_71441_e.func_180495_p(position).func_177230_c() != Blocks.field_150350_a) {
                            sphere.add(position);
                        }
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return sphere;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck, boolean oneDot15) {
        BlockPos boost = blockPos.func_177982_a(0, 1, 0);
        BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
        try {
            if (BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h && BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z) {
                return false;
            }
            if (!oneDot15 && BlockUtil.mc.field_71441_e.func_180495_p(boost2).func_177230_c() != Blocks.field_150350_a || BlockUtil.mc.field_71441_e.func_180495_p(boost).func_177230_c() != Blocks.field_150350_a) {
                return false;
            }
            for (Entity entity : BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost))) {
                if (entity.field_70128_L || specialEntityCheck && entity instanceof EntityEnderCrystal) continue;
                return false;
            }
            if (!oneDot15) {
                for (Entity entity : BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2))) {
                    if (entity.field_70128_L || specialEntityCheck && entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
            }
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck) {
        block7: {
            BlockPos boost = blockPos.func_177982_a(0, 1, 0);
            BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
            try {
                if (BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h && BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z) {
                    return false;
                }
                if (BlockUtil.mc.field_71441_e.func_180495_p(boost).func_177230_c() != Blocks.field_150350_a || BlockUtil.mc.field_71441_e.func_180495_p(boost2).func_177230_c() != Blocks.field_150350_a) {
                    return false;
                }
                if (specialEntityCheck) {
                    for (Entity entity : BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost))) {
                        if (entity instanceof EntityEnderCrystal) continue;
                        return false;
                    }
                    for (Entity entity : BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2))) {
                        if (entity instanceof EntityEnderCrystal) continue;
                        return false;
                    }
                    break block7;
                }
                return BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty() && BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
            }
            catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }

    public static boolean canBeClicked(BlockPos pos) {
        return BlockUtil.getBlock(pos).func_176209_a(BlockUtil.getState(pos), false);
    }

    private static Block getBlock(BlockPos pos) {
        return BlockUtil.getState(pos).func_177230_c();
    }

    private static IBlockState getState(BlockPos pos) {
        return BlockUtil.mc.field_71441_e.func_180495_p(pos);
    }

    public static boolean isBlockAboveEntitySolid(Entity entity) {
        if (entity != null) {
            BlockPos pos = new BlockPos(entity.field_70165_t, entity.field_70163_u + 2.0, entity.field_70161_v);
            return BlockUtil.isBlockSolid(pos);
        }
        return false;
    }

    public static void debugPos(String message, BlockPos pos) {
        Command.sendMessage(message + pos.func_177958_n() + "x, " + pos.func_177956_o() + "y, " + pos.func_177952_p() + "z");
    }

    public static void placeCrystalOnBlock(BlockPos pos, EnumHand hand) {
        RayTraceResult result = BlockUtil.mc.field_71441_e.func_72933_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() - 0.5, (double)pos.func_177952_p() + 0.5));
        EnumFacing facing = result == null || result.field_178784_b == null ? EnumFacing.UP : result.field_178784_b;
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0f, 0.0f, 0.0f));
    }

    public static BlockPos[] toBlockPos(Vec3d[] vec3ds) {
        BlockPos[] list = new BlockPos[vec3ds.length];
        for (int i = 0; i < vec3ds.length; ++i) {
            list[i] = new BlockPos(vec3ds[i]);
        }
        return list;
    }

    public static Vec3d posToVec3d(BlockPos pos) {
        return new Vec3d(pos);
    }

    public static BlockPos vec3dToPos(Vec3d vec3d) {
        return new BlockPos(vec3d);
    }

    public static void faceVectorPacketInstant(Vec3d vec) {
        float[] rotations = BlockUtil.getNeededRotations2(vec);
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayer$Rotation(rotations[0], rotations[1], BlockUtil.mc.field_71439_g.field_70122_E));
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v);
    }

    private static float[] getNeededRotations2(Vec3d vec) {
        Vec3d eyesPos = BlockUtil.getEyesPos();
        double diffX = vec.field_72450_a - eyesPos.field_72450_a;
        double diffY = vec.field_72448_b - eyesPos.field_72448_b;
        double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{BlockUtil.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g(yaw - BlockUtil.mc.field_71439_g.field_70177_z), BlockUtil.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g(pitch - BlockUtil.mc.field_71439_g.field_70125_A)};
    }

    public static EnumFacing getPlaceableSide(BlockPos pos) {
        for (EnumFacing side : EnumFacing.values()) {
            IBlockState blockState;
            BlockPos neighbour = pos.func_177972_a(side);
            if (!BlockUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(BlockUtil.mc.field_71441_e.func_180495_p(neighbour), false) || (blockState = BlockUtil.mc.field_71441_e.func_180495_p(neighbour)).func_185904_a().func_76222_j()) continue;
            return side;
        }
        return null;
    }

    public static Boolean isPosInFov(BlockPos pos) {
        int dirnumber = RotationUtil.getDirection4D();
        if (dirnumber == 0 && (double)pos.func_177952_p() - BlockUtil.mc.field_71439_g.func_174791_d().field_72449_c < 0.0) {
            return false;
        }
        if (dirnumber == 1 && (double)pos.func_177958_n() - BlockUtil.mc.field_71439_g.func_174791_d().field_72450_a > 0.0) {
            return false;
        }
        if (dirnumber == 2 && (double)pos.func_177952_p() - BlockUtil.mc.field_71439_g.func_174791_d().field_72449_c > 0.0) {
            return false;
        }
        return dirnumber != 3 || (double)pos.func_177958_n() - BlockUtil.mc.field_71439_g.func_174791_d().field_72450_a >= 0.0;
    }

    public static boolean isBlockBelowEntitySolid(Entity entity) {
        if (entity != null) {
            BlockPos pos = new BlockPos(entity.field_70165_t, entity.field_70163_u - 1.0, entity.field_70161_v);
            return BlockUtil.isBlockSolid(pos);
        }
        return false;
    }

    public static boolean isBlockSolid(BlockPos pos) {
        return !BlockUtil.isBlockUnSolid(pos);
    }

    public static boolean isBlockUnSolid(BlockPos pos) {
        return BlockUtil.isBlockUnSolid(BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c());
    }

    public static boolean isBlockUnSolid(Block block) {
        return unSolidBlocks.contains(block);
    }

    public static boolean isBlockUnSafe(Block block) {
        return unSafeBlocks.contains(block);
    }

    public static Vec3d[] convertVec3ds(Vec3d vec3d, Vec3d[] input) {
        Vec3d[] output = new Vec3d[input.length];
        for (int i = 0; i < input.length; ++i) {
            output[i] = vec3d.func_178787_e(input[i]);
        }
        return output;
    }

    public static Vec3d[] convertVec3ds(EntityPlayer entity, Vec3d[] input) {
        return BlockUtil.convertVec3ds(entity.func_174791_d(), input);
    }

    public static boolean canBreak(BlockPos pos) {
        IBlockState blockState = BlockUtil.mc.field_71441_e.func_180495_p(pos);
        Block block = blockState.func_177230_c();
        return block.func_176195_g(blockState, BlockUtil.mc.field_71441_e, pos) != -1.0f;
    }

    public static boolean isValidBlock(BlockPos pos) {
        Block block = BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        return !(block instanceof BlockLiquid) && block.func_149688_o(null) != Material.field_151579_a;
    }

    public static boolean isScaffoldPos(BlockPos pos) {
        return BlockUtil.mc.field_71441_e.func_175623_d(pos) || BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150431_aC || BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150329_H || BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockLiquid;
    }

    public static boolean rayTracePlaceCheck(BlockPos pos, boolean shouldCheck, float height) {
        return !shouldCheck || BlockUtil.mc.field_71441_e.func_147447_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n(), (float)pos.func_177956_o() + height, pos.func_177952_p()), false, true, false) == null;
    }

    public static boolean rayTracePlaceCheck(BlockPos pos, boolean shouldCheck) {
        return BlockUtil.rayTracePlaceCheck(pos, shouldCheck, 1.0f);
    }

    public static boolean rayTracePlaceCheck(BlockPos pos) {
        return BlockUtil.rayTracePlaceCheck(pos, true);
    }

    public static BlockPos GetLocalPlayerPosFloored() {
        return new BlockPos(Math.floor(BlockUtil.mc.field_71439_g.field_70165_t), Math.floor(BlockUtil.mc.field_71439_g.field_70163_u), Math.floor(BlockUtil.mc.field_71439_g.field_70161_v));
    }

    public static ValidResult valid(BlockPos pos) {
        if (!BlockUtil.mc.field_71441_e.func_72855_b(new AxisAlignedBB(pos))) {
            return ValidResult.NoEntityCollision;
        }
        if (!BlockUtil.checkForNeighbours(pos)) {
            return ValidResult.NoNeighbors;
        }
        IBlockState l_State = BlockUtil.mc.field_71441_e.func_180495_p(pos);
        if (l_State.func_177230_c() == Blocks.field_150350_a) {
            BlockPos[] l_Blocks;
            for (BlockPos l_Pos : l_Blocks = new BlockPos[]{pos.func_177978_c(), pos.func_177968_d(), pos.func_177974_f(), pos.func_177976_e(), pos.func_177984_a(), pos.func_177977_b()}) {
                IBlockState l_State2 = BlockUtil.mc.field_71441_e.func_180495_p(l_Pos);
                if (l_State2.func_177230_c() == Blocks.field_150350_a) continue;
                for (EnumFacing side : EnumFacing.values()) {
                    BlockPos neighbor = pos.func_177972_a(side);
                    if (!BlockUtil.mc.field_71441_e.func_180495_p(neighbor).func_177230_c().func_176209_a(BlockUtil.mc.field_71441_e.func_180495_p(neighbor), false)) continue;
                    return ValidResult.Ok;
                }
            }
            return ValidResult.NoNeighbors;
        }
        return ValidResult.AlreadyBlockThere;
    }

    public static List<BlockPos> getSphere(double range, BlockPos pos, boolean sphere, boolean hollow) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = pos.func_177958_n();
        int cy = pos.func_177956_o();
        int cz = pos.func_177952_p();
        int x = cx - (int)range;
        while ((double)x <= (double)cx + range) {
            int z = cz - (int)range;
            while ((double)z <= (double)cz + range) {
                int y = sphere ? cy - (int)range : cy;
                while (true) {
                    double d = y;
                    double d2 = sphere ? (double)cy + range : (double)cy + range;
                    if (!(d < d2)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < range * range) || hollow && dist < (range - 1.0) * (range - 1.0))) {
                        BlockPos l = new BlockPos(x, y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    public static boolean canPlaceInPosition(BlockPos position, boolean entityCheck, boolean sideCheck) {
        if (!BlockUtil.mc.field_71441_e.func_180495_p(position).func_177230_c().func_176200_f(BlockUtil.mc.field_71441_e, position)) {
            return false;
        }
        if (entityCheck) {
            for (Entity entity : BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(position))) {
                if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
                return false;
            }
        }
        return !sideCheck || BlockUtil.getPlaceableSide(position) != null;
    }

    public static boolean isPositionPlaceable(BlockPos position, boolean entityCheck, boolean sideCheck, boolean ignoreCrystals) {
        if (!BlockUtil.mc.field_71441_e.func_180495_p(position).func_177230_c().func_176200_f(BlockUtil.mc.field_71441_e, position)) {
            return false;
        }
        if (entityCheck) {
            for (Entity entity : BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(position))) {
                if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityEnderCrystal && ignoreCrystals) continue;
                return false;
            }
        }
        return !sideCheck || BlockUtil.getPlaceableSide(position) != null;
    }

    public static boolean isPositionPlaceable(BlockPos pos, boolean entityCheck, double distance) {
        Block block = BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        if (!(block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow)) {
            return false;
        }
        if (entityCheck) {
            for (Entity entity : BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
                if ((double)BlockUtil.mc.field_71439_g.func_70032_d(entity) > distance || entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
                return false;
            }
        }
        return true;
    }

    public static boolean checkForNeighbours(BlockPos blockPos) {
        if (!BlockUtil.hasNeighbour(blockPos)) {
            for (EnumFacing side : EnumFacing.values()) {
                BlockPos neighbour = blockPos.func_177972_a(side);
                if (!BlockUtil.hasNeighbour(neighbour)) continue;
                return true;
            }
            return false;
        }
        return true;
    }

    private static boolean hasNeighbour(BlockPos blockPos) {
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = blockPos.func_177972_a(side);
            if (BlockUtil.mc.field_71441_e.func_180495_p(neighbour).func_185904_a().func_76222_j()) continue;
            return true;
        }
        return false;
    }

    public static void placeBlockss(BlockPos blockPos, boolean bl, boolean bl2, boolean bl3) {
        for (EnumFacing enumFacing : EnumFacing.values()) {
            boolean swing = bl;
            boolean packet = bl2;
            boolean rotate = bl3;
            BlockPos blockPos2 = blockPos;
            if (BlockUtil.mc.field_71441_e.func_180495_p(blockPos2.func_177972_a(enumFacing)).func_177230_c().equals(Blocks.field_150350_a) || BlockUtil.isIntercepted(blockPos2)) continue;
            if (packet) {
                BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(blockPos2.func_177972_a(enumFacing), enumFacing.func_176734_d(), EnumHand.MAIN_HAND, Float.intBitsToFloat(Float.floatToIntBits(2.7f)), Float.intBitsToFloat(Float.floatToIntBits(3.8f)), Float.intBitsToFloat(Float.floatToIntBits(30.0f))));
            } else {
                BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, blockPos2.func_177972_a(enumFacing), enumFacing.func_176734_d(), new Vec3d(blockPos2), EnumHand.MAIN_HAND);
            }
            if (swing) {
                BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            }
            if (rotate) {
                RotationUtil.faceVector(new Vec3d(blockPos2), true);
            }
            return;
        }
    }

    public static boolean placeBlock(BlockPos pos, int slot, boolean rotate, boolean rotateBack, boolean swing) {
        if (TestUtil.isBlockEmpty(pos)) {
            EnumFacing[] facings;
            int old_slot = -1;
            if (slot != BlockUtil.mc.field_71439_g.field_71071_by.field_70461_c) {
                old_slot = BlockUtil.mc.field_71439_g.field_71071_by.field_70461_c;
                BlockUtil.mc.field_71439_g.field_71071_by.field_70461_c = slot;
            }
            for (EnumFacing f : facings = EnumFacing.values()) {
                Block neighborBlock = BlockUtil.mc.field_71441_e.func_180495_p(pos.func_177972_a(f)).func_177230_c();
                Vec3d vec = new Vec3d((double)pos.func_177958_n() + 0.5 + (double)f.func_82601_c() * 0.5, (double)pos.func_177956_o() + 0.5 + (double)f.func_96559_d() * 0.5, (double)pos.func_177952_p() + 0.5 + (double)f.func_82599_e() * 0.5);
                if (emptyBlocks.contains(neighborBlock) || !(BlockUtil.mc.field_71439_g.func_174824_e(mc.func_184121_ak()).func_72438_d(vec) <= 4.25)) continue;
                float[] rot = new float[]{BlockUtil.mc.field_71439_g.field_70177_z, BlockUtil.mc.field_71439_g.field_70125_A};
                if (rotate) {
                    BlockUtil.rotatePacket(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
                }
                if (rightclickableBlocks.contains(neighborBlock)) {
                    BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(BlockUtil.mc.field_71439_g, CPacketEntityAction$Action.START_SNEAKING));
                }
                BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, pos.func_177972_a(f), f.func_176734_d(), new Vec3d(pos), EnumHand.MAIN_HAND);
                if (rightclickableBlocks.contains(neighborBlock)) {
                    BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(BlockUtil.mc.field_71439_g, CPacketEntityAction$Action.STOP_SNEAKING));
                }
                if (rotateBack) {
                    BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayer$Rotation(rot[0], rot[1], BlockUtil.mc.field_71439_g.field_70122_E));
                }
                if (swing) {
                    BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                }
                if (old_slot != -1) {
                    BlockUtil.mc.field_71439_g.field_71071_by.field_70461_c = old_slot;
                }
                return true;
            }
            if (old_slot != -1) {
                BlockUtil.mc.field_71439_g.field_71071_by.field_70461_c = old_slot;
            }
        }
        return false;
    }

    public static void placeBlocksss(BlockPos blockPos, EnumHand enumHand, boolean bl) {
        boolean packet = bl;
        EnumHand hand = enumHand;
        BlockPos position = blockPos;
        if (!BlockUtil.mc.field_71441_e.func_180495_p(position).func_177230_c().func_176200_f(BlockUtil.mc.field_71441_e, position)) {
            return;
        }
        if (BlockUtil.getPlaceableSide(position) == null) {
            return;
        }
        BlockUtil.clickBlock(position, BlockUtil.getPlaceableSide(position), hand, packet);
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketAnimation(hand));
    }

    public static boolean isIntercepted(BlockPos blockPos) {
        for (Entity entity : BlockUtil.mc.field_71441_e.field_72996_f) {
            BlockPos blockPos2 = blockPos;
            if (entity instanceof EntityItem || entity instanceof EntityEnderCrystal || !new AxisAlignedBB(blockPos2).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    public static int findObiInHotbar() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = BlockUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof ItemBlock)) continue;
            Block block = ((ItemBlock)((Object)stack.func_77973_b())).func_179223_d();
            if (block instanceof BlockEnderChest) {
                return i;
            }
            if (!(block instanceof BlockObsidian)) continue;
            return i;
        }
        return -1;
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(BlockUtil.mc.field_71439_g.field_70165_t), Math.floor(BlockUtil.mc.field_71439_g.field_70163_u), Math.floor(BlockUtil.mc.field_71439_g.field_70161_v));
    }

    public static enum ValidResult {
        NoEntityCollision,
        AlreadyBlockThere,
        NoNeighbors,
        Ok;

    }
}
