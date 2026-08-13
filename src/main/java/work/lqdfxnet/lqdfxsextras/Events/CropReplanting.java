package work.lqdfxnet.lqdfxsextras.Events;

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
import work.lqdfxnet.lqdfxsextras.LqDFxsExtras;
import work.lqdfxnet.lqdfxsextras.ModConfigCommon;
import work.lqdfxnet.lqdfxsextras.Utilities;

@EventBusSubscriber(modid = "lqdfxsextras")
public class CropReplanting {

    @SubscribeEvent
    public static void onCropHarvest(PlayerInteractEvent.RightClickBlock event) {

        if (!(event.getLevel().getBlockState(event.getPos()).getBlock() instanceof CropBlock)) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;   // Must be server-side
        if (!ModConfigCommon.ihuReplantEnabled.get()) return;           // Option Enabled?

        Player player = event.getEntity();                              // Get Player Entity
        ItemStack tool = player.getMainHandItem();                      // Tool Check
        if (tool.isEmpty()) return;                                     // Make sure there is a tool in hand
        boolean correctTool = Utilities.isConfiguredTool(tool, ModConfigCommon.ihuTools.get());
        if (!correctTool) return;

        BlockPos pos = event.getHitVec().getBlockPos();
        BlockState state = event.getLevel().getBlockState(pos);
        Block block = state.getBlock();

        IntegerProperty ageProp = Utilities.getAgeProperty(block);
        int maxAge = Utilities.getMaxAge(block);
        Item seedItem = Utilities.getSeedFromCrop(level, state, pos);
        if (seedItem == null) return;

        if (ageProp != null && state.getValue(ageProp) == maxAge) {

            Utilities.consumeOneSeed(event.getEntity(), seedItem);

            int xp = level.getRandom().nextInt(3) + 1;                  // 1–3 XP like vanilla
            level.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            player.swing(InteractionHand.MAIN_HAND, true);

            // Replant next tick (after drops)
            LqDFxsExtras.queueServerWork(5, () -> {
                level.destroyBlock(pos, true);                // true = drop loot
                tool.hurtAndBreak(1, player, player.getUsedItemHand());
                level.addFreshEntity(new ExperienceOrb(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, xp));
                Utilities.replantCrop(level, pos, state);
                level.playSound(null,pos,SoundEvents.CROP_PLANTED,SoundSource.BLOCKS,1.0F,1.0F);
            });
        }
    }
}