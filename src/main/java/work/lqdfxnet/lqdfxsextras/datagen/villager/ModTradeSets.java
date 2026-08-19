package work.lqdfxnet.lqdfxsextras.datagen.villager;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import work.lqdfxnet.lqdfxsextras.LqDFxsExtras;
import work.lqdfxnet.lqdfxsextras.tag.ModTags;

import java.util.Optional;

public class ModTradeSets {
    public static final ResourceKey<TradeSet> BEEKEEPER_LEVEL_1 = create("beekeeper/level_1");
    public static final ResourceKey<TradeSet> BEEKEEPER_LEVEL_2 = create("beekeeper/level_2");
    public static final ResourceKey<TradeSet> BEEKEEPER_LEVEL_3 = create("beekeeper/level_3");
    public static final ResourceKey<TradeSet> BEEKEEPER_LEVEL_4 = create("beekeeper/level_4");
    public static final ResourceKey<TradeSet> BEEKEEPER_LEVEL_5 = create("beekeeper/level_5");

    public static void bootstrap(BootstrapContext<TradeSet> context) {
        register(context, BEEKEEPER_LEVEL_1, ModTags.Trades.BEEKEEPER_LEVEL_1);
        register(context, BEEKEEPER_LEVEL_2, ModTags.Trades.BEEKEEPER_LEVEL_2);
        register(context, BEEKEEPER_LEVEL_3, ModTags.Trades.BEEKEEPER_LEVEL_3);
        register(context, BEEKEEPER_LEVEL_4, ModTags.Trades.BEEKEEPER_LEVEL_4);
        register(context, BEEKEEPER_LEVEL_5, ModTags.Trades.BEEKEEPER_LEVEL_5);
    }

    private static ResourceKey<TradeSet> create(final String id) {
        return ResourceKey.create(Registries.TRADE_SET, Identifier.fromNamespaceAndPath(LqDFxsExtras.MODID, id));
    }

    public static Holder.Reference<TradeSet> register(final BootstrapContext<TradeSet> context,
                                                      final ResourceKey<TradeSet> resourceKey, final TagKey<VillagerTrade> tradeTag) {
        return register(context, resourceKey, tradeTag, ConstantValue.exactly(2.0F));
    }

    public static Holder.Reference<TradeSet> register(final BootstrapContext<TradeSet> context, final ResourceKey<TradeSet> resourceKey,
                                                      final TagKey<VillagerTrade> tradeTag, final NumberProvider numberProvider) {
        return context.register(resourceKey, new TradeSet(context.lookup(Registries.VILLAGER_TRADE).getOrThrow(tradeTag),
                numberProvider, false, Optional.of(resourceKey.identifier().withPrefix("trade_set/"))));
    }
}
