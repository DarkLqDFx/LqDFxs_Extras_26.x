package work.lqdfxnet.lqdfxsextras.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.trading.VillagerTrade;
import work.lqdfxnet.lqdfxsextras.LqDFxsExtras;

public class ModTags {

    public static class Trades {
        public static final TagKey<VillagerTrade> BEEKEEPER_LEVEL_1 = createTag("beekeeper/level_1");
        public static final TagKey<VillagerTrade> BEEKEEPER_LEVEL_2 = createTag("beekeeper/level_2");
        public static final TagKey<VillagerTrade> BEEKEEPER_LEVEL_3 = createTag("beekeeper/level_2");
        public static final TagKey<VillagerTrade> BEEKEEPER_LEVEL_4 = createTag("beekeeper/level_2");
        public static final TagKey<VillagerTrade> BEEKEEPER_LEVEL_5 = createTag("beekeeper/level_2");

        private static TagKey<VillagerTrade> createTag(String name) {
            return TagKey.create(Registries.VILLAGER_TRADE, Identifier.fromNamespaceAndPath(LqDFxsExtras.MODID, name));
        }
    }
}
