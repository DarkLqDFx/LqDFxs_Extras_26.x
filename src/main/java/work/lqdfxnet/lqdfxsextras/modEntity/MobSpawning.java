package work.lqdfxnet.lqdfxsextras.modEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import work.lqdfxnet.lqdfxsextras.Config;

@EventBusSubscriber
public class MobSpawning {

    @SubscribeEvent
    public static void mobJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        /* -----------------------------------------------------------
         * Vex spawning - False here means not to spawn
         * ----------------------------------------------------------- */
        if (event.getEntity() instanceof Vex && !vexSpawnEnabled()) {
            event.setCanceled(true);
            return;
        }

        if (event.getEntity() instanceof Creeper creeper) { creeperSpawn(creeper); }

        /* -----------------------------------------------------------
         * Convert Skeletons in Nether - True means Convert
         * ----------------------------------------------------------- */
        if (event.getEntity() instanceof Skeleton skeleton && netherSkeletonReplace()) {

            BlockPos pos = skeleton.blockPosition();
            LevelAccessor world = skeleton.level();
            boolean isNether = world.getBiome(pos).is(TagKey.create(Registries.BIOME, Identifier.parse("minecraft:is_nether")));

            if (!isNether) return;      // Only operate in Nether biomes
            event.setCanceled(true);    // Cancel original spawning skeleton

            // Spawn a Wither Skeleton instead
            if (world instanceof ServerLevel serverLevel) {
                Entity witherSkeleton = EntityType.WITHER_SKELETON.spawn(serverLevel, pos, EntitySpawnReason.NATURAL);
                if (witherSkeleton != null) { witherSkeleton.setDeltaMovement(0, 0, 0); }
            }
        }

    }

    private static void creeperSpawn(Entity entity) {
        BlockPos pos = entity.blockPosition();
        LevelAccessor world = entity.level();
    }

    /* -----------------------------------------------------------
     * ModConfig Helper shortcuts
     * ----------------------------------------------------------- */
    private static boolean vexSpawnEnabled() { return Config.mrVexSpawn.get(); }
    private static boolean netherSkeletonReplace() { return Config.mrNetherSkeleton.get(); }

}