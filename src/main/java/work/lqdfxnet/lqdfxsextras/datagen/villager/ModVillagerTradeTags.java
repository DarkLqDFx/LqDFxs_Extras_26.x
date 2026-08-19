package work.lqdfxnet.lqdfxsextras.datagen.villager;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.VillagerTradesTagsProvider;
import net.minecraft.tags.TagEntry;
import work.lqdfxnet.lqdfxsextras.tag.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModVillagerTradeTags extends VillagerTradesTagsProvider {
    public ModVillagerTradeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        getOrCreateRawBuilder(ModTags.Trades.BEEKEEPER_LEVEL_1)
                .add(TagEntry.element(ModVillagerTrades.BEEKEEPER_1_EMERALDS_CANDLES.identifier()))
                .add(TagEntry.element(ModVillagerTrades.BEEKEEPER_1_STRING_EMERALDS.identifier()))

                .add(TagEntry.element(ModVillagerTrades.BEEKEEPER_2_PLANKS_EMERALDS.identifier()))
                .add(TagEntry.element(ModVillagerTrades.BEEKEEPER_2_SMALLFLOWER_EMERALDS.identifier()))
                .add(TagEntry.element(ModVillagerTrades.BEEKEEPER_2_TALLFLOWER_EMERALDS.identifier()))


                .add(TagEntry.element(ModVillagerTrades.BEEKEEPER_3_EMERALDS_HONEYBOTTLES.identifier()))
                .add(TagEntry.element(ModVillagerTrades.BEEKEEPER_3_EMERALDS_HONEYCOMB.identifier()))
                .add(TagEntry.element(ModVillagerTrades.BEEKEEPER_3_EmptyBottles_EMERALDS.identifier()))

                .add(TagEntry.element(ModVillagerTrades.BEEKEEPER_4_EMERALDS_BEEHIVE.identifier()))
                .add(TagEntry.element(ModVillagerTrades.BEEKEEPER_4_EMERALDS_BEENEST.identifier()))
                .add(TagEntry.element(ModVillagerTrades.BEEKEEPER_4_EMERALDS_HONEYBLOCK.identifier()))
                .add(TagEntry.element(ModVillagerTrades.BEEKEEPER_4_EMERALDS_HONEYCOMBBLOCK.identifier()))


                .add(TagEntry.element(ModVillagerTrades.BEEKEEPER_5_EMERALDS_BEESPAWNEGG.identifier()))
                .add(TagEntry.element(ModVillagerTrades.BEEKEEPER_5_Shears_AND_EMERALDS_SILKTOUCHSHEARS.identifier()))

        ;

    }
}
