package work.lqdfxnet.lqdfxsextras;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;
import work.lqdfxnet.lqdfxsextras.item.ModItems;
import work.lqdfxnet.lqdfxsextras.ModData.ModDataComponents;
import work.lqdfxnet.lqdfxsextras.mixin.PoiTypeAccessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

//import work.lqdfxnet.lqdfxsextras.entity.villager.ModVillagers;

@Mod(LqDFxsExtras.MODID)
public class LqDFxsExtras {

    public static final String MODID = "lqdfxsextras";
    public static final Logger LOGGER = LogUtils.getLogger();
    /* -----------------------------------------------------------
     * Tick Scheduler Queue creation
     * ----------------------------------------------------------- */
    private static final Collection<Tuple<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public LqDFxsExtras(IEventBus modEventBus, ModContainer modContainer) {

        /* -----------------------------------------------------------
         * Register Setup Events
         * ----------------------------------------------------------- */
        modEventBus.addListener(this::commonSetup);

        /* -----------------------------------------------------------
         * Register global event listeners
         * ----------------------------------------------------------- */
        NeoForge.EVENT_BUS.register(this);

        /* -----------------------------------------------------------
         * Register Items
         * ----------------------------------------------------------- */
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        //ModVillagers.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);


        /* -----------------------------------------------------------
         * Register config and config screen
         * ----------------------------------------------------------- */
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModConfig.SPEC);
    }

    /* -----------------------------------------------------------
     * Register Debug Logger Method
     * ----------------------------------------------------------- */
    public static void debugInfo(String msg, Object... args) {
        LOGGER.info(msg, args);
    }

    /* -----------------------------------------------------------
     * Tick Scheduler
     *  - MCreator Code made to work with mod
     * ----------------------------------------------------------- */
    // Tick Scheduler
    public static void queueServerWork(int ticks, Runnable action) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            workQueue.add(new Tuple<>(action, ticks));
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        /* -----------------------------------------------------------
         * Bee Keeper POI
         *  - Bee Hive set max tickets (1)
         *  - Must be done so workstation will work
         * ----------------------------------------------------------- */

        event.enqueueWork(() -> {
            ResourceKey<PoiType> beehivePoi = PoiTypes.BEEHIVE;
            ((PoiTypeAccessor) beehivePoi).setMaxTickets(1);
        });
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Server starting — Lqdfxextras active.");
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
