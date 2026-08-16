package work.lqdfxnet.lqdfxsextras;

import com.mojang.logging.LogUtils;
import net.minecraft.util.Tuple;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;
import work.lqdfxnet.lqdfxsextras.Data.ModDataComponents;
import work.lqdfxnet.lqdfxsextras.item.ModItems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod(LqDFxsExtras.MODID)
public class LqDFxsExtras {

    public static final String MODID = "lqdfxsextras";
    public static final Logger LOGGER = LogUtils.getLogger();



    // Debug Logger Method
    public static void debugInfo(String msg, Object... args) {
        LOGGER.info(msg, args);
    }

    // Tick scheduler queue
    private static final Collection<Tuple<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public LqDFxsExtras(IEventBus modEventBus, ModContainer modContainer) {

        // Register setup events
        modEventBus.addListener(this::commonSetup);

        //Register Items
        ModItems.ITEMS.register(modEventBus);
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);

        // Register global event listeners
        NeoForge.EVENT_BUS.register(this);

        // Register config
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modContainer.registerConfig(ModConfig.Type.COMMON, ModConfigCommon.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Lqdfxextras common setup complete.");
    }

    public void onClientSetup(final FMLClientSetupEvent event) {
        //ModItems.ModItemProperties.registerItemProperties();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Server starting — Lqdfxextras active.");
    }

    // Tick Scheduler
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
