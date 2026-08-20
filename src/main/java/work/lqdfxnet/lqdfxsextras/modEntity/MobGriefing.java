package work.lqdfxnet.lqdfxsextras.modEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import work.lqdfxnet.lqdfxsextras.Config;

@EventBusSubscriber
public class MobGriefing {

    /* -----------------------------------------------------------
     * onFarmlandTrample - (because this has its own event call)
     *  Stop entities from trampling farmland
     *  Player is still able to
     * ----------------------------------------------------------- */
    @SubscribeEvent
    public static void onFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {

        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (!farmlandTrampleEnabled()) return;

        event.setCanceled(true);
    }

    /* -----------------------------------------------------------
     * onMobGriefing
     *  Per entity griefing settings
     * ----------------------------------------------------------- */
    @SubscribeEvent
    public static void onMobGriefing(final EntityMobGriefingEvent event) {

        if (event.getEntity().level().isClientSide()) return;
        Entity entity = event.getEntity();
        if (entity instanceof Player) { return; }
        if ((entity instanceof EnderMan) && !endermanGriefingEnabled()) { event.setCanGrief(false); return; }
        if ((entity instanceof Creeper) && !creeperGriefingEnabled()) { event.setCanGrief(false); return; }
        if ((entity instanceof Silverfish) && !silverfishGriefingEnabled()) { event.setCanGrief(false); return; }
        if ((entity instanceof Fireball) && !ghastGriefingEnabled()) { event.setCanGrief(false); }
        event.setCanGrief(true);

    }

    /* -----------------------------------------------------------
     * ModConfig Helper shortcuts
     * ----------------------------------------------------------- */
    private static boolean farmlandTrampleEnabled() {
        return Config.mrFarmLand.get();
    }
    private static boolean endermanGriefingEnabled() {
        return Config.mrEndermanGriefing.get();
    }
    private static boolean creeperGriefingEnabled() {
        return Config.mrCreeperGriefing.get();
    }
    private static boolean silverfishGriefingEnabled() {
        return Config.mrSilverfishGriefing.get();
    }
    private static boolean ghastGriefingEnabled() {
        return Config.mrGhastGriefing.get();
    }
}
