package work.lqdfxnet.lqdfxsextras.datagen.villager;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.item.trading.VillagerTrades;
import work.lqdfxnet.lqdfxsextras.LqDFxsExtras;

import java.util.List;
import java.util.Optional;

public class ModVillagerTrades {

    /* -----------------------------------------------------------
     * Bee Keeper Trades
     * ----------------------------------------------------------- */
    public static final ResourceKey<VillagerTrade> BEEKEEPER_1_STRING_EMERALDS = createKey("beekeeper/1/beekeeper_1_string_emeralds");
    public static final ResourceKey<VillagerTrade> BEEKEEPER_1_EMERALDS_CANDLES = createKey("beekeeper/1/beekeeper_1_emeralds_candles");

    public static final ResourceKey<VillagerTrade> BEEKEEPER_2_SMALLFLOWER_EMERALDS = createKey("beekeeper/2/beekeeper_2_smallflower_emeralds");
    public static final ResourceKey<VillagerTrade> BEEKEEPER_2_TALLFLOWER_EMERALDS = createKey("beekeeper/2/beekeeper_2_tallflower_emeralds");
    public static final ResourceKey<VillagerTrade> BEEKEEPER_2_PLANKS_EMERALDS = createKey("beekeeper/2/beekeeper_2_planks_emeralds");

    public static final ResourceKey<VillagerTrade> BEEKEEPER_3_EmptyBottles_EMERALDS = createKey("beekeeper/3/beekeeper_3_emptybottles_emeralds");
    public static final ResourceKey<VillagerTrade> BEEKEEPER_3_EMERALDS_HONEYBOTTLES = createKey("beekeeper/3/beekeeper_3_emeralds_honeybottles");
    public static final ResourceKey<VillagerTrade> BEEKEEPER_3_EMERALDS_HONEYCOMB = createKey("beekeeper/3/beekeeper_3_emeralds_honeycomb");

    public static final ResourceKey<VillagerTrade> BEEKEEPER_4_EMERALDS_HONEYCOMBBLOCK = createKey("beekeeper/4/beekeeper_4_emeralds_honeycombblock");
    public static final ResourceKey<VillagerTrade> BEEKEEPER_4_EMERALDS_HONEYBLOCK = createKey("beekeeper/4/beekeeper_4_emeralds_honeyblock");
    public static final ResourceKey<VillagerTrade> BEEKEEPER_4_EMERALDS_BEENEST = createKey("beekeeper/4/beekeeper_4_emeralds_beenest");
    public static final ResourceKey<VillagerTrade> BEEKEEPER_4_EMERALDS_BEEHIVE = createKey("beekeeper/4/beekeeper_4_emeralds_beehive");

    public static final ResourceKey<VillagerTrade> BEEKEEPER_5_Shears_AND_EMERALDS_SILKTOUCHSHEARS = createKey("beekeeper/5/beekeeper_5_shears_and_emeralds_silktouchshears");
    public static final ResourceKey<VillagerTrade> BEEKEEPER_5_EMERALDS_BEESPAWNEGG = createKey("beekeeper/5/beekeeper_5_emeralds_beespawnegg");

    public static void bootstrap(BootstrapContext<VillagerTrade> context) {
        var items = context.lookup(Registries.ITEM);
        var enchantments = context.lookup(Registries.ENCHANTMENT);

        register(context, BEEKEEPER_1_STRING_EMERALDS, new VillagerTrade(
                new TradeCost(Items.STRING, 2),
                new ItemStackTemplate(Items.EMERALD, 1),
                12,12,.05f, Optional.empty(), List.of()));

        register(context, BEEKEEPER_1_EMERALDS_CANDLES, new VillagerTrade(
                new TradeCost(Items.EMERALD, 1),
                new ItemStackTemplate(Items.CANDLE, 3),
                12,12,.05f, Optional.empty(), List.of()));

        register(context, BEEKEEPER_2_SMALLFLOWER_EMERALDS, new VillagerTrade(
                new TradeCost(Items.POPPY, 1),
                new ItemStackTemplate(Items.EMERALD, 1),
                12,12,.05f, Optional.empty(), List.of()));

        register(context, BEEKEEPER_2_TALLFLOWER_EMERALDS, new VillagerTrade(
                new TradeCost(Items.ROSE_BUSH, 1),
                new ItemStackTemplate(Items.EMERALD, 2),
                12,12,.05f, Optional.empty(), List.of()));

        register(context, BEEKEEPER_2_PLANKS_EMERALDS, new VillagerTrade(
                new TradeCost(Items.BIRCH_PLANKS, 1),
                new ItemStackTemplate(Items.EMERALD, 2),
                12,12,.05f, Optional.empty(), List.of()));

        register(context, BEEKEEPER_3_EmptyBottles_EMERALDS, new VillagerTrade(
                new TradeCost(Items.GLASS_BOTTLE, 1),
                new ItemStackTemplate(Items.EMERALD, 2),
                12,12,.05f, Optional.empty(), List.of()));

        register(context, BEEKEEPER_3_EMERALDS_HONEYBOTTLES, new VillagerTrade(
                new TradeCost(Items.EMERALD, 2),
                new ItemStackTemplate(Items.HONEY_BOTTLE, 2),
                12,12,.05f, Optional.empty(), List.of()));

        register(context, BEEKEEPER_3_EMERALDS_HONEYCOMB, new VillagerTrade(
                new TradeCost(Items.EMERALD, 2),
                new ItemStackTemplate(Items.HONEYCOMB, 2),
                12,12,.05f, Optional.empty(), List.of()));

        register(context, BEEKEEPER_4_EMERALDS_HONEYCOMBBLOCK, new VillagerTrade(
                new TradeCost(Items.EMERALD, 4),
                new ItemStackTemplate(Items.HONEYCOMB_BLOCK, 2),
                12,12,.05f, Optional.empty(), List.of()));

        register(context, BEEKEEPER_4_EMERALDS_HONEYBLOCK, new VillagerTrade(
                new TradeCost(Items.EMERALD, 4),
                new ItemStackTemplate(Items.HONEY_BLOCK, 2),
                12,12,.05f, Optional.empty(), List.of()));

        register(context, BEEKEEPER_4_EMERALDS_BEENEST, new VillagerTrade(
                new TradeCost(Items.EMERALD, 1),
                new ItemStackTemplate(Items.BEE_NEST, 1),
                12,12,.05f, Optional.empty(), List.of()));

        register(context, BEEKEEPER_4_EMERALDS_BEEHIVE, new VillagerTrade(
                new TradeCost(Items.EMERALD, 1),
                new ItemStackTemplate(Items.BEEHIVE, 1),
                12,12,.05f, Optional.empty(), List.of()));

        register(context, BEEKEEPER_5_EMERALDS_BEESPAWNEGG, new VillagerTrade(
                new TradeCost(Items.EMERALD, 3),
                new ItemStackTemplate(Items.BEE_SPAWN_EGG, 1),
                12,12,.05f, Optional.empty(), List.of()));

        register(context, BEEKEEPER_5_Shears_AND_EMERALDS_SILKTOUCHSHEARS, new VillagerTrade(
                new TradeCost(Items.SHEARS, 1),
                Optional.of(new TradeCost(Items.EMERALD,5)),
                new ItemStackTemplate(Items.EMERALD, 1),
                12,12,.05f, Optional.empty(),
                VillagerTrades.enchantedItem(items, enchantments.getOrThrow(Enchantments.SILK_TOUCH), 1, Items.SHEARS)
        ));
    }


    private static ResourceKey<VillagerTrade> createKey(String id) {
        return ResourceKey.create(Registries.VILLAGER_TRADE, Identifier.fromNamespaceAndPath(LqDFxsExtras.MODID, id));
    }

    private static void register(BootstrapContext<VillagerTrade> context, ResourceKey<VillagerTrade> key, VillagerTrade trade) {
        context.register(key, trade);
    }
}
