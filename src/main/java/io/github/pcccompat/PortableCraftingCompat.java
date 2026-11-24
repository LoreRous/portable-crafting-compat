package io.github.pcccompat;

import io.github.pcccompat.compat.PreciseEnchantingCompat;
import io.github.pcccompat.compat.UncraftEverythingCompat;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortableCraftingCompat implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("pcccompat");

    @Override
    public void onInitialize() {
        LOGGER.info("[Portable Crafting Compat] Inizializzazione...");

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            if (FabricLoader.getInstance().isModLoaded("uncrafteverything")) {
                UncraftEverythingCompat.register();
            } else {
                LOGGER.info("[Portable Crafting Compat] UncraftEverything non trovato, saltato.");
            }

            if (FabricLoader.getInstance().isModLoaded("slider_enchanting")) {
                PreciseEnchantingCompat.register();
            } else {
                LOGGER.info("[Portable Crafting Compat] Precise Enchanting non trovato, saltato.");
            }

            LOGGER.info("[Portable Crafting Compat] Pronto.");
        });
    }
}