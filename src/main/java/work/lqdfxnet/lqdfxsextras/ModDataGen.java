package work.lqdfxnet.lqdfxsextras;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
//import work.lqdfxnet.lqdfxsextras.datagen.villager.ModVillagerTradeTags;

@EventBusSubscriber(modid = LqDFxsExtras.MODID)
public class ModDataGen {

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();

        //generator.addProvider(true, new ModVillagerTradeTags(packOutput, lookupProvider));
        //generator.addProvider(true, new ModPOITags(packOutput, lookupProvider));
    }
}
