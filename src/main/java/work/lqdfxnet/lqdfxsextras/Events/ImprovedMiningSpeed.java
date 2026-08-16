package work.lqdfxnet.lqdfxsextras.Events;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import work.lqdfxnet.lqdfxsextras.ModConfigCommon;
import work.lqdfxnet.lqdfxsextras.Utilities;


@EventBusSubscriber
public class ImprovedMiningSpeed {

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!ModConfigCommon.imsEnable.get()) return;   // Bail if not enabled

        LevelAccessor world = event.getEntity().level();
        Player player = event.getEntity();

        ItemStack tool = player.getMainHandItem();
        if (tool.isEmpty()) return;     // Make sure there is a tool in hand
        boolean correctTool = Utilities.isConfiguredTool(tool, ModConfigCommon.ihuTools.get());
        if (!correctTool) return;

        int effLevel = Utilities.checkEfficiency(tool,world);   // Get Efficiency Level

        // Haste Check (Beacon Mining)
        MobEffectInstance haste = player.getEffect(MobEffects.HASTE);
        int hasHasteLevel = haste != null ? haste.getAmplifier() + 1 : 0;

        // Haste Beacon Mining
        if (effLevel >= 5 && hasHasteLevel >= 2) {
            event.setNewSpeed(9999f);   // Instant Mine!
            return;
        }

        // Efficiency but no Haste
        BlockState state = event.getState();
        BlockPos blockPos = event.getPosition().orElse(player.blockPosition());
        float baseSpeed = event.getOriginalSpeed();
        float hardness = state.getDestroySpeed(world, blockPos);
        float hardnessMultiplier = 1.0f + (hardness / 2);   // Hardness-scaled multiplier
        float bonus = Mth.clamp(effLevel * hardnessMultiplier,0,90);   // Efficiency-scaled bonus
        event.setNewSpeed(baseSpeed + bonus);   // Final speed
    }
}
