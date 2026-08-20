package work.lqdfxnet.lqdfxsextras.modules.VillagerBucket;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;

public class InaBucketItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("lqdfxsextras");

    // Filled variants — custom class since these need placement (useOn) behavior later, stack size 1
    public static final DeferredItem<Item> VILLAGER_BUCKET_PLAINS = ITEMS.registerItem(
            "villager_bucket_plains",
            InaBucketDropoff::new,
            props -> props.stacksTo(1)
    );
    public static final DeferredItem<Item> VILLAGER_BUCKET_DESERT = ITEMS.registerItem(
            "villager_bucket_desert", InaBucketDropoff::new, props -> props.stacksTo(1));
    public static final DeferredItem<Item> VILLAGER_BUCKET_SAVANNA = ITEMS.registerItem(
            "villager_bucket_savanna", InaBucketDropoff::new, props -> props.stacksTo(1));
    public static final DeferredItem<Item> VILLAGER_BUCKET_TAIGA = ITEMS.registerItem(
            "villager_bucket_taiga", InaBucketDropoff::new, props -> props.stacksTo(1));
    public static final DeferredItem<Item> VILLAGER_BUCKET_SNOW = ITEMS.registerItem(
            "villager_bucket_snow", InaBucketDropoff::new, props -> props.stacksTo(1));
    public static final DeferredItem<Item> VILLAGER_BUCKET_SWAMP = ITEMS.registerItem(
            "villager_bucket_swamp", InaBucketDropoff::new, props -> props.stacksTo(1));
    public static final DeferredItem<Item> VILLAGER_BUCKET_JUNGLE = ITEMS.registerItem(
            "villager_bucket_jungle", InaBucketDropoff::new, props -> props.stacksTo(1));

    // Vanilla VillagerType registry keys are all in the "minecraft" namespace
    public static final Map<Identifier, DeferredItem<Item>> VILLAGER_BUCKET_BY_TYPE = Map.of(
            Identifier.withDefaultNamespace("plains"),  VILLAGER_BUCKET_PLAINS,
            Identifier.withDefaultNamespace("desert"),  VILLAGER_BUCKET_DESERT,
            Identifier.withDefaultNamespace("savanna"), VILLAGER_BUCKET_SAVANNA,
            Identifier.withDefaultNamespace("taiga"),   VILLAGER_BUCKET_TAIGA,
            Identifier.withDefaultNamespace("snow"),    VILLAGER_BUCKET_SNOW,
            Identifier.withDefaultNamespace("swamp"),   VILLAGER_BUCKET_SWAMP,
            Identifier.withDefaultNamespace("jungle"),  VILLAGER_BUCKET_JUNGLE
    );

}
