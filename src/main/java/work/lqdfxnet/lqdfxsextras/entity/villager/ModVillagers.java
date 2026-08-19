package work.lqdfxnet.lqdfxsextras.entity.villager;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import work.lqdfxnet.lqdfxsextras.LqDFxsExtras;
import work.lqdfxnet.lqdfxsextras.datagen.villager.ModTradeSets;

public class ModVillagers {

    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, LqDFxsExtras.MODID);

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSION =
            DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, LqDFxsExtras.MODID);

    public static final Holder<VillagerProfession> BEEKEEPER = VILLAGER_PROFESSION.register("beekeeper",
            () -> new VillagerProfession(Component.literal("Bee Keeper"),
                    holder -> holder.is(PoiTypes.BEEHIVE),
                    holder -> holder.is(PoiTypes.BEEHIVE),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    SoundEvents.BEEHIVE_SHEAR,
                    Int2ObjectMap.ofEntries(
                            Int2ObjectMap.entry(1, ModTradeSets.BEEKEEPER_LEVEL_1),
                            Int2ObjectMap.entry(2, ModTradeSets.BEEKEEPER_LEVEL_2),
                            Int2ObjectMap.entry(3, ModTradeSets.BEEKEEPER_LEVEL_3),
                            Int2ObjectMap.entry(4, ModTradeSets.BEEKEEPER_LEVEL_4),
                            Int2ObjectMap.entry(5, ModTradeSets.BEEKEEPER_LEVEL_5)
                    )));

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSION.register(eventBus);
    }

}
