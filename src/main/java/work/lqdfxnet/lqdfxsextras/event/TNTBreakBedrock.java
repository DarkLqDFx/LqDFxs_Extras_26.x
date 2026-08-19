package work.lqdfxnet.lqdfxsextras.event;


import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import work.lqdfxnet.lqdfxsextras.ModConfig;

import java.util.function.Consumer;

@EventBusSubscriber
public class TNTBreakBedrock {

    @SubscribeEvent
    public static void tntBreaksBedrock(ExplosionEvent.Detonate event) {
        if (!ModConfig.imsTNTBreaksEnable.get()) return;
        if (event.getLevel().isClientSide()) return;
        if (!(event.getExplosion().getDirectSourceEntity() instanceof PrimedTnt)) return;

        Level level = event.getLevel();
        Vec3 center = event.getExplosion().center();
        int radius = ModConfig.imsTNTBreakRadius.getAsInt(); // Configurable Radius!!!

        scanExplosionRadius(center, radius, pos -> {
            BlockState state = level.getBlockState(pos);
            if (state.is(Blocks.BEDROCK)) level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        });
    }

    public static void scanExplosionRadius( Vec3 center, int radius, Consumer<BlockPos> action) {

        BlockPos origin = BlockPos.containing(center);

        for (BlockPos pos : BlockPos.betweenClosed(
                origin.getX() - radius, origin.getY() - radius, origin.getZ() - radius,
                origin.getX() + radius, origin.getY() + radius, origin.getZ() + radius)) {

            if (pos.distSqr(origin) <= radius * radius) action.accept(pos);
        }
    }

}
