package io.github.pcccompat.compat;

import folk.sisby.portable_crafting.PortableCrafting;
import io.github.pcccompat.FakeBlockEntityRegistry;
import io.github.pcccompat.wrapper.UncraftingTableScreenFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registra il supporto portatile per l'UncraftingTable di UncraftEverything.
 *
 * Approccio:
 * 1. Recuperiamo il BlockItem dell'UncraftingTable dal registro di Minecraft.
 * 2. Registriamo in PortableCrafting.ITEM_FACTORIES una factory custom
 *    (UncraftingTableScreenFactory) che piazza temporaneamente un'UncraftingTable
 *    50 blocchi sopra il player e apre il menu della sua BE.
 * 3. La posizione viene salvata in FakeBlockEntityRegistry; il blocco originale
 *    viene ripristinato alla chiusura del menu (MixinUncraftingTableMenuServer)
 *    o alla disconnessione del player (listener qui sotto).
 *
 * Il networking funziona perché il mixin MixinUncraftingPacketHandler intercetta
 * World.getBlockEntity() e restituisce la BE registrata quando la pos
 * richiesta corrisponde a quella registrata per quel player.
 */
public class UncraftEverythingCompat {

    private static final Logger LOGGER = LoggerFactory.getLogger("pcccompat/UncraftEverything");
    private static final Identifier UNCRAFTING_TABLE_ID =
            Identifier.of("uncrafteverything", "uncrafting_table");

    private static boolean registered = false;

    public static void register() {
        Item item = Registries.ITEM.get(UNCRAFTING_TABLE_ID);

        if (!(item instanceof BlockItem blockItem)) {
            LOGGER.warn("[Portable Crafting Compat] UncraftEverything: impossibile " +
                    "trovare il BlockItem '{}'. Compatibilità non attivata.", UNCRAFTING_TABLE_ID);
            return;
        }

        // PortableCrafting.ITEM_FACTORIES è una Map<Item, NamedScreenHandlerFactory>:
        // inserendo qui il nostro wrapper, quando il player clicca l'item nell'inventario
        // PC userà la nostra factory invece di chiamare block.createScreenHandlerFactory().
        PortableCrafting.ITEM_FACTORIES.put(blockItem, new UncraftingTableScreenFactory());

        // SERVER_STARTING scatta a ogni mondo aperto in singleplayer: il listener va registrato una volta sola.
        if (registered) return;
        registered = true;

        // Pulizia alla disconnessione del player.
        ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if (entity instanceof ServerPlayerEntity player) {
                FakeBlockEntityRegistry.unregister(player.getUuid());
            }
        });

        LOGGER.info("[Portable Crafting Compat] UncraftEverything: registrato con successo.");
    }
}
