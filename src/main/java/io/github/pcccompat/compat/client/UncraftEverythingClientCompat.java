package io.github.pcccompat.compat.client;

import com.coolerpromc.uncrafteverything.networking.RequestConfigPayload;
import com.coolerpromc.uncrafteverything.screen.custom.UncraftingTableScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

/**
 * Quando l'Uncrafting Table viene usata da piazzata, UncraftingTableBlock.onUse invia
 * lato client un RequestConfigPayload: il server risponde con la config, che la schermata
 * delle impostazioni legge da UncraftEverythingClient.payloadFromServer.
 * In modalità portatile onUse non viene mai chiamato, quindi la config restava null
 * e il pulsante delle impostazioni mandava in crash il gioco. Qui inviamo noi la richiesta
 * ogni volta che si apre la schermata dell'Uncrafting Table.
 */
public class UncraftEverythingClientCompat {

    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof UncraftingTableScreen && ClientPlayNetworking.canSend(RequestConfigPayload.TYPE)) {
                ClientPlayNetworking.send(new RequestConfigPayload());
            }
        });
    }
}
