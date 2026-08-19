package work.lqdfxnet.lqdfxsextras.datagen.villager;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import work.lqdfxnet.lqdfxsextras.entity.villager.ModVillagers;
import work.lqdfxnet.lqdfxsextras.LqDFxsExtras;

import java.util.concurrent.CompletableFuture;

public class ModPOITags extends PoiTypeTagsProvider {

    public ModPOITags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider){
        super(output, lookupProvider, LqDFxsExtras.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        getOrCreateRawBuilder(PoiTypeTags.ACQUIRABLE_JOB_SITE)
                .add(TagEntry.element(PoiTypes.BEEHIVE.identifier()));
    }
}
