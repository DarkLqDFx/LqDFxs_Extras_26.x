package work.lqdfxnet.lqdfxsextras.EntityRules;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import work.lqdfxnet.lqdfxsextras.ModConfigCommon;

import java.util.Comparator;

@EventBusSubscriber
public class EvokerDeath {

    @SubscribeEvent
    public static void onEvokerDied(LivingDeathEvent event) {

        // Server side event only
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Evoker)) return;
        if (!vexDespawnOnEvokerDeathEnabled()) return;

        BlockPos pos = event.getEntity().blockPosition();
        Vec3 center = new Vec3(pos.getX(), pos.getY(), pos.getZ());
        LevelAccessor world = event.getEntity().level();

        world.getEntitiesOfClass(
                Entity.class,
                new AABB(center, center).inflate(8), // 16/2d = 8 radius
                e -> true
        ).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList().stream().filter(nearby -> !nearby.level().isClientSide() && nearby instanceof Vex).forEach(Entity::discard);
    }

    private static boolean vexDespawnOnEvokerDeathEnabled() {
        return ModConfigCommon.mrEvokerDeath.get();
    }
}
