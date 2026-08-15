package work.lqdfxnet.lqdfxsextras.EntityRules;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;

import static work.lqdfxnet.lqdfxsextras.LqDFxsExtras.debugInfo;

@EventBusSubscriber
public class BreedingAnimals {

    @SubscribeEvent
    public static void pigLitters(BabyEntitySpawnEvent event) {
        if (event.getChild() instanceof Pig) {
            if (event.getChild().level().isClientSide()) return;
            if (!(event.getChild().level() instanceof ServerLevel serverLevel)) return;
            if (!(event.getParentA() instanceof Animal parentA)) return;
            if (!(event.getParentB() instanceof Animal parentB)) return;
            int litterSize = 1 + serverLevel.getRandom().nextInt(2);

            if (litterSize > 1) {
                for (int i = 0; i < litterSize; i++) {
                    AgeableMob newChild = parentA.getBreedOffspring(serverLevel, parentB);
                    if (newChild != null) {
                        newChild.setBaby(true);
                        newChild.setPos(parentA.getX(), parentA.getY(), parentA.getZ());
                        newChild.setYRot(parentA.getYRot());
                        serverLevel.addFreshEntityWithPassengers(newChild);
                    }
                }

            }
        }
    }

}

