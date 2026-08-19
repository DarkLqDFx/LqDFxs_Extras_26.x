package work.lqdfxnet.lqdfxsextras.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import work.lqdfxnet.lqdfxsextras.LqDFxsExtras;
import work.lqdfxnet.lqdfxsextras.datagen.villager.ModTradeSets;
import work.lqdfxnet.lqdfxsextras.datagen.villager.ModVillagerTrades;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackProvider  extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.VILLAGER_TRADE, ModVillagerTrades::bootstrap)
            .add(Registries.TRADE_SET, ModTradeSets::bootstrap);

    public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(LqDFxsExtras.MODID));

    }
}
