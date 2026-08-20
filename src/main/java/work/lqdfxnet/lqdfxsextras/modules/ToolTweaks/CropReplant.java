package work.lqdfxnet.lqdfxsextras.modules.ToolTweaks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import work.lqdfxnet.lqdfxsextras.Config;
import work.lqdfxnet.lqdfxsextras.LqDFxsExtras;

@EventBusSubscriber
public class CropReplant {

    @SubscribeEvent
    public static void onCropHarvest(PlayerInteractEvent.RightClickBlock event) {

        BlockState clickedState = event.getLevel().getBlockState(event.getPos());
        if (!(clickedState.getBlock() instanceof CropBlock)) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!Config.ihuReplantEnabled.get()) return;

        Player player = event.getEntity();
        ItemStack tool = player.getMainHandItem();
        boolean correctTool = ToolUtilities.isConfiguredTool(tool, Config.ihuTools.get());
        int radius = correctTool ? ToolUtilities.getHoeRadius(tool.toString()) : 0;

        BlockPos origin = event.getHitVec().getBlockPos();
        event.setCanceled(true);

        int replantedCount = 0;

        for (int dx = -radius; dx <= radius; ++dx) {
            for (int dz = -radius; dz <= radius; ++dz) {

                BlockPos targetPos = origin.offset(dx, 0, dz);
                BlockState targetState = level.getBlockState(targetPos);
                Block targetBlock = targetState.getBlock();

                IntegerProperty ageProp = ToolUtilities.getAgeProperty(targetBlock);
                if (ageProp == null) continue;

                int age = targetState.getValue(ageProp);
                int maxAge = ToolUtilities.getMaxAge(targetBlock);

                Item seedItem = ToolUtilities.getSeedFromCrop(level, targetState, targetPos);
                if (seedItem == null) continue;

                if (age == maxAge) {
                    ToolUtilities.consumeOneSeed(event.getEntity(), seedItem);
                    replantedCount++;

                    LqDFxsExtras.queueServerWork(1, () -> {
                        int xp = level.getRandom().nextInt(3) + 1;
                        level.destroyBlock(targetPos, true);
                        level.addFreshEntity(new ExperienceOrb(level, targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5, xp));
                        ToolUtilities.replantCrop(level, targetPos, targetState);
                    });
                }
            }
        }
        player.swing(InteractionHand.MAIN_HAND, true);
        if (replantedCount != 0) {
            level.playSound(null, origin, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!tool.isEmpty()) tool.hurtAndBreak(replantedCount, player, player.getUsedItemHand());
        }
    }
}