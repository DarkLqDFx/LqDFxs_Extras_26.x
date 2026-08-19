package work.lqdfxnet.lqdfxsextras.entity.villager;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.TagValueOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import work.lqdfxnet.lqdfxsextras.ModData.ModDataComponents;

import static work.lqdfxnet.lqdfxsextras.LqDFxsExtras.debugInfo;
import static work.lqdfxnet.lqdfxsextras.item.ModItems.VILLAGER_BUCKET_BY_TYPE;

@EventBusSubscriber
public class VillagerPickup {

    @SubscribeEvent
    public static void onVillagerPickup(PlayerInteractEvent.EntityInteract event) {

        if (!(event.getTarget() instanceof Villager villager)) return;
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        ItemStack held = player.getMainHandItem();
        if (!held.is(Items.BUCKET)) return;
        if (!player.isShiftKeyDown()) return;
        debugInfo("Attempting to pick up Villager");

        // Don't snatch a villager out from under an active trade
        if (villager.getTradingPlayer() != null) return;

        // Find the correct biome item for this villager's type
        VillagerType type = villager.getVillagerData().type().value();
        Identifier typeId = BuiltInRegistries.VILLAGER_TYPE.getKey(type);
        DeferredItem<Item> filledItem = VILLAGER_BUCKET_BY_TYPE.get(typeId);
        if (filledItem == null) return; // unknown/modded villager type I haven't made a variant for

        // Snapshot the full entity (profession, trades, level, xp, age/baby state — all of it)
        TagValueOutput output = TagValueOutput.createWithContext(
                ProblemReporter.DISCARDING,
                villager.level().registryAccess()
        );
        villager.saveWithoutId(output);
        CompoundTag villagerData = output.buildResult();  // pull the actual CompoundTag back out

        // Strip anything we explicitly don't want to persist:
        // - Brain memories (job site / home / meeting point) so the villager re-searches on placement
        // - UUID, so the placed copy gets a fresh identity instead of risking a collision
        villagerData.remove("Brain");
        villagerData.remove("UUID");

        ItemStack filledBucket = new ItemStack(filledItem.get());
        filledBucket.set(ModDataComponents.VILLAGER_DATA.get(), villagerData);

        // Remove the original — one bucket, one villager, no duplication
        villager.discard();

        held.shrink(1);

        // Don't lose the item if the inventory's full
        if (!player.addItem(filledBucket)) {
            player.drop(filledBucket, false);
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }


}
