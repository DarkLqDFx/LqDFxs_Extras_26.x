package work.lqdfxnet.lqdfxsextras.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import work.lqdfxnet.lqdfxsextras.ModConfig;

import static work.lqdfxnet.lqdfxsextras.LqDFxsExtras.debugInfo;
import static work.lqdfxnet.lqdfxsextras.LqDFxsExtras.queueServerWork;

@EventBusSubscriber
public class Monsters {


    /* -----------------------------------------------------------
     * Creepers Burn
     *  They should burn in the daylight like the rest, right?
     * ----------------------------------------------------------- */
    @SubscribeEvent
    public static void burnMonster(EntityTickEvent.Pre event) {

        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Creeper)) return;
        if (!creepersBurnEnabled()) return;

        Entity creeper = event.getEntity();
        BlockPos pos = event.getEntity().blockPosition();
        LevelAccessor world = event.getEntity().level();

        if (!canSunBurn(world, pos, creeper)) return;
        queueServerWork(20, () -> creeper.igniteForSeconds(8F));

    }


    /* -----------------------------------------------------------
     * onEvokerDeath
     *  When an Evoker dies, the Vex in a 16 block radius
     *  will also be despawned
     * ----------------------------------------------------------- */
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

    /* -----------------------------------------------------------
     * canSunBurn - Helper Method
     *  Checks to make sure environment entity is in will allow
     *  them to burn in daylight
     * ----------------------------------------------------------- */
    private static boolean canSunBurn(LevelAccessor world, BlockPos pos, Entity entity) {

        boolean worldDim = world.getBiome(pos).is(TagKey.create(Registries.BIOME, Identifier.parse("minecraft:is_overworld")));
        boolean worldBio = world.getBiome(pos).is(TagKey.create(Registries.BIOME, Identifier.parse("c:is_dry")));
        boolean worldDay = world instanceof Level level && level.isBrightOutside();
        boolean blockSeeSky = world.canSeeSky(pos);
        boolean skyBrightness = world.getBrightness(LightLayer.SKY, pos) == 15;
        boolean waterLikeBlock = entity.isInWaterOrRain() || entity.isInPowderSnow || entity.wasInPowderSnow;
        debugInfo("Overworld: {}", worldDim);
        debugInfo("Biome: {}", worldBio);
        debugInfo("Day: {}", worldDay);
        debugInfo("BlockSeeSky: {}", blockSeeSky);
        debugInfo("Brightness: {}", skyBrightness);
        debugInfo("WaterLikeBlock: {}", waterLikeBlock);

        if (worldDim && worldDay && blockSeeSky && skyBrightness) {
            if (!entity.isOnFire() && (worldBio || !entity.isInWaterOrRain()))
                return true;
        }
        return false;
    }

    /* -----------------------------------------------------------
     * ModConfig Helper shortcuts
     * ----------------------------------------------------------- */
    private static boolean vexDespawnOnEvokerDeathEnabled() {
        return ModConfig.mrEvokerDeath.get();
    }
    private static boolean creepersBurnEnabled() {
        return ModConfig.mrCreepersBurn.get();
    }
}