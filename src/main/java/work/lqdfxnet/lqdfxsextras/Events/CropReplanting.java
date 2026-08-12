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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
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

        if (!(event.getLevel() instanceof ServerLevel level)) return;   // Must be server-side
        if (!ModConfigCommon.ihuReplantEnabled.get()) return;           // Option Enabled? Make this bhReplant = true/false

        Player player = event.getEntity();

        // Tool Check
        ItemStack tool = player.getMainHandItem();
        if (tool.isEmpty()) return;                                     // Make sure there is a tool in hand
        if (!Utilities.isConfiguredTool(tool, ModConfigCommon.ihuTools.get())) return;


        BlockHitResult hitResult = event.getHitVec();
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = event.getLevel().getBlockState(pos);
        Block block = state.getBlock();

        IntegerProperty ageProp = Utilities.getAgeProperty(block);
        int maxAge = Utilities.getMaxAge(block);

        if (ageProp != null && state.getValue(ageProp) == maxAge) {

            Item seedItem = Utilities.getSeedFromCrop(level, state, pos);
            if (seedItem == null) return;

            Utilities.consumeOneSeed(event.getEntity(), seedItem);

            int xp = level.getRandom().nextInt(3) + 1; // 1–3 XP like vanilla

            // Replant next tick (after drops)
            LqDFxsExtras.queueServerWork(1, () -> {
                level.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);

                player.swing(InteractionHand.MAIN_HAND, true);

                level.destroyBlock(pos, true); // true = drop loot

                tool.hurtAndBreak(1, player, player.getUsedItemHand());

                Utilities.replantCrop(level, pos, state);

                level.playSound(null,pos,SoundEvents.CROP_PLANTED,SoundSource.BLOCKS,1.0F,1.0F);

                level.addFreshEntity(new ExperienceOrb(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, xp));

            });

        }

    }
}