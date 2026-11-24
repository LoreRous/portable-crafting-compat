package io.github.pcccompat;

import io.github.pcccompat.compat.client.UncraftEverythingClientCompat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class PortableCraftingCompatClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        if (FabricLoader.getInstance().isModLoaded("uncrafteverything")) {
            UncraftEverythingClientCompat.register();
        }
    }
}
