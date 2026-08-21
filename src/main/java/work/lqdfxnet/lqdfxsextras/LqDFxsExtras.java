package work.lqdfxnet.lqdfxsextras;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import work.lqdfxnet.lqdfxsextras.modules.VillagerBucket.InaBucketData;
import work.lqdfxnet.lqdfxsextras.modules.VillagerBucket.InaBucketItems;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod(LqDFxsExtras.MODID)
public class LqDFxsExtras {

    public static final String MODID = "lqdfxsextras";

    /* -----------------------------------------------------------
     * Register Debug Logger Method
     * ----------------------------------------------------------- */
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void debugInfo(String msg, Object... args) {
        LOGGER.info(msg, args);
    }
    public static boolean debugDev() { return true; }

    /* -----------------------------------------------------------
     * Deferred Registers
     * ----------------------------------------------------------- */
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public LqDFxsExtras(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        /* -----------------------------------------------------------
         * Register Stuff and Things
         * ----------------------------------------------------------- */
        InaBucketData.DATA_COMPONENTS.register(modEventBus);
        InaBucketItems.ITEMS.register(modEventBus);

        CREATIVE_MODE_TABS.register(modEventBus);

        /* -----------------------------------------------------------
         * Register config and config screen
         * ----------------------------------------------------------- */
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, Config.SPEC);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    /* -----------------------------------------------------------
     * Tick Scheduler
     *  - MCreator Code made to work with mod
     * ----------------------------------------------------------- */
    private static final Collection<Tuple<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int ticks, Runnable action) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            workQueue.add(new Tuple<>(action, ticks));
        }
    }

    @SubscribeEvent
    public void tick(ServerTickEvent.Post event) {

        List<Tuple<Runnable, Integer>> ready = new ArrayList<>();

        workQueue.forEach(work -> {
            work.setB(work.getB() - 1);
            if (work.getB() <= 0) {
                ready.add(work);
            }
        });

        ready.forEach(e -> e.getA().run());
        workQueue.removeAll(ready);
    }
}