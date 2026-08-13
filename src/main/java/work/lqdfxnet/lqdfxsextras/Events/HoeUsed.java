package work.lqdfxnet.lqdfxsextras.Events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import work.lqdfxnet.lqdfxsextras.LqDFxsExtras;
import work.lqdfxnet.lqdfxsextras.ModConfigCommon;
import work.lqdfxnet.lqdfxsextras.Utilities;


@EventBusSubscriber(modid = "lqdfxsextras")
public class HoeUsed {

    @SubscribeEvent
    public static void unTilling(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        BlockHitResult hit = event.getHitVec();
        BlockPos pos = hit.getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof FarmlandBlock)) {
            return;
        }   // Must be farmland

        Player player = event.getEntity();
        if (!player.isShiftKeyDown()) return;   // Must be sneaking
        ItemStack hoe = player.getMainHandItem();
        boolean correctTool = Utilities.isConfiguredTool(hoe, ModConfigCommon.ihuTools.get());
        if (hoe.isEmpty() || !correctTool) return;

        // Cancel vanilla interaction
        event.setCanceled(true);

        // Convert farmland → dirt next tick
        LqDFxsExtras.queueServerWork(1, () -> {
            Utilities.unTill(level, pos);
            player.swing(InteractionHand.MAIN_HAND, true);
            hoe.hurtAndBreak(1, player, player.getUsedItemHand());
            level.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 0.8F);
        });
    }

    @SubscribeEvent
    public static void areaTilling(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        BlockHitResult hit = event.getHitVec();
        BlockPos pos = hit.getBlockPos();
        BlockState state = level.getBlockState(pos);

        Player player = event.getEntity();
        if (player.isShiftKeyDown()) return;   // Sneaking preserves Vanilla behavior
        ItemStack hoe = player.getMainHandItem();
        boolean correctTool = Utilities.isConfiguredTool(hoe, ModConfigCommon.ihuTools.get());
        if (hoe.isEmpty() || !correctTool) return;

        int radius = Utilities.getHoeRadius(hoe.toString());
        int efficiencyLevel = Utilities.checkEfficiency(hoe,event.getLevel());
        if ((efficiencyLevel >= 1) && (efficiencyLevel <= 2))  radius = efficiencyLevel + radius;

        boolean tillable = Utilities.canBeFarmland(player, state, hit);
        boolean waterLogged;
        if (state.getBlock() instanceof SimpleWaterloggedBlock)
            waterLogged = state.getValue(BlockStateProperties.WATERLOGGED);
        else waterLogged = false;


        if (!tillable && !waterLogged) return;
        //else if (!tillable) return;

        // Cancel vanilla interaction
        event.setCanceled(true);
        int tillCount = 0;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                BlockPos target = pos.offset(dx, 0, dz);
                BlockState targetState = level.getBlockState(target);
                BlockHitResult targetHit = new BlockHitResult(target.getCenter(), hit.getDirection(), target, hit.isInside());
                boolean tillBlock = Utilities.canBeFarmland(player, targetState, targetHit);
                if (tillBlock) {
                    tillCount++;
                    LqDFxsExtras.queueServerWork(1, () -> {
                        BlockState farmland = BuiltInRegistries.BLOCK.getValue(Identifier.parse("minecraft:farmland")).defaultBlockState();
                        level.setBlock(target, farmland, Block.UPDATE_ALL);
                    });
                }
            }
        }
        player.swing(InteractionHand.MAIN_HAND, true);
        if (tillCount >=1) {
            hoe.hurtAndBreak(tillCount, player, player.getUsedItemHand());
            level.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 0.8F);
        }
    }
}
