package work.lqdfxnet.lqdfxsextras.EntityRules;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import work.lqdfxnet.lqdfxsextras.ModConfigCommon;

import static work.lqdfxnet.lqdfxsextras.LqDFxsExtras.queueServerWork;

@EventBusSubscriber
public class CreepersBun {

    @SubscribeEvent
    public static void burnCreepers(EntityTickEvent.Pre event) {

        // Server side event only
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Creeper)) return;
        if (!creepersBurnEnabled()) return;

        // Set variables
        Entity creeper = event.getEntity();
        BlockPos pos = event.getEntity().blockPosition();
        LevelAccessor world = event.getEntity().level();

        if (!isInNonBurnableBlock(world, pos, creeper)) {
            queueServerWork(20, () -> creeper.igniteForSeconds(8F));
        }

    }

    private static boolean isInNonBurnableBlock(LevelAccessor world, BlockPos pos, Entity entity) {

        // world
        boolean worldDim = world.getBiome(pos).is(TagKey.create(Registries.BIOME, Identifier.parse("minecraft:is_overworld")));
        boolean worldBio = world.getBiome(pos).is(TagKey.create(Registries.BIOME, Identifier.parse("c:is_dry")));
        boolean worldDay = world instanceof Level level && level.isBrightOutside();
        boolean blockSeeSky = world.canSeeSky(pos);
        boolean skyBrightness = world.getBrightness(LightLayer.SKY, pos) == 15;

        // Entity
        boolean waterLikeBlock = entity.isInWaterOrRain() || entity.isInPowderSnow || entity.wasInPowderSnow;

        return worldDim && worldBio && worldDay && blockSeeSky && skyBrightness && waterLikeBlock;
    }

    private static boolean creepersBurnEnabled() {
        return ModConfigCommon.mrCreepersBurn.get();
    }

}