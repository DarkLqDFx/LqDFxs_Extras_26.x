package work.lqdfxnet.lqdfxsextras.modules.VillagerBucket;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class InaBucketData {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, "lqdfxsextras");

    public static final Supplier<DataComponentType<CompoundTag>> VILLAGER_DATA =
            DATA_COMPONENTS.registerComponentType(
                    "villager_data",
                    builder -> builder
                            .persistent(CompoundTag.CODEC)                      // how it's saved to disk (world save, item NBT on disk)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)    // how it's sent client<->server
            );
}

