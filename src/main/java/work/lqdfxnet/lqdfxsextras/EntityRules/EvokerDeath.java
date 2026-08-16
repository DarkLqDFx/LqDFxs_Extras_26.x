package work.lqdfxnet.lqdfxsextras.EntityRules;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import work.lqdfxnet.lqdfxsextras.ModConfigCommon;

@EventBusSubscriber
public class EvokerDeath {

    @SubscribeEvent
    public static void onEvokerDied(LivingDeathEvent event) {

        // Server side event only
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Evoker evoker)) return;
        if (!vexDespawnOnEvokerDeathEnabled()) return;

        LevelAccessor world = evoker.level();
        Vec3 center = evoker.position();

        // Search only for Vex entities within radius
        for (Vex vex : world.getEntitiesOfClass(
                Vex.class,
                new AABB(center, center).inflate(16),
                EntitySelector.NO_SPECTATORS
        )) {
            if (vex.getOwner() == evoker) {
                vex.discard();
            }
        }
    }

    private static boolean vexDespawnOnEvokerDeathEnabled() {
        return ModConfigCommon.mrEvokerDeath.get();
    }

}