package work.lqdfxnet.lqdfxsextras.EntityRules;

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
import work.lqdfxnet.lqdfxsextras.ModConfigCommon;

@EventBusSubscriber
public class mobGriefing {

    @SubscribeEvent
    public static void trampleFarmland(BlockEvent.FarmlandTrampleEvent event) {

        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (!farmlandTrampleEnabled()) return;

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void MobEntityGriefing(final EntityMobGriefingEvent event) {

        if (event.getEntity().level().isClientSide()) return;
        Entity entity = event.getEntity();
        if (entity instanceof Player) { return; }
        if ((entity instanceof EnderMan) && !endermanGriefingEnabled()) { event.setCanGrief(false); return; }
        if ((entity instanceof Creeper) && !creeperGriefingEnabled()) { event.setCanGrief(false); return; }
        if ((entity instanceof Silverfish) && !silverfishGriefingEnabled()) { event.setCanGrief(false); return; }
        if ((entity instanceof Fireball) && !ghastGriefingEnabled()) { event.setCanGrief(false); }
        event.setCanGrief(true);

    }

    // Helpers
    private static boolean farmlandTrampleEnabled() {
        return ModConfigCommon.mrFarmLand.get();
    }

    private static boolean endermanGriefingEnabled() {
        return ModConfigCommon.mrEndermanGriefing.get();
    }

    private static boolean creeperGriefingEnabled() {
        return ModConfigCommon.mrCreeperGriefing.get();
    }

    private static boolean silverfishGriefingEnabled() {
        return ModConfigCommon.mrSilverfishGriefing.get();
    }

    private static boolean ghastGriefingEnabled() {
        return ModConfigCommon.mrGhastGriefing.get();
    }
}
