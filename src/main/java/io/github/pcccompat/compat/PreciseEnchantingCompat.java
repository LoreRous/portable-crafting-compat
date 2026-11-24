package io.github.pcccompat.compat;

import io.github.pcccompat.FakeDisenchanterRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class PreciseEnchantingCompat {

    private static final Logger LOGGER = LoggerFactory.getLogger("pcccompat/PreciseEnchanting");

    public static BlockItem disenchanterItem = null;
    private static Method openMethod = null;

    public static void register() {
        // SERVER_STARTING scatta a ogni mondo aperto in singleplayer: registriamo una volta sola.
        if (disenchanterItem != null) return;
        Item item = Registries.ITEM.get(Identifier.of("slider_enchanting", "arcane_disenchanter"));
        if (!(item instanceof BlockItem blockItem)) {
            LOGGER.warn("[Portable Crafting Compat] PreciseEnchanting: arcane_disenchanter non trovato. Saltato.");
            return;
        }

        try {
            Class<?> cls = Class.forName("com.levls.enchanter.EnchanterBlock");
            openMethod = cls.getMethod("open", ServerPlayerEntity.class, BlockPos.class, boolean.class);
        } catch (Exception e) {
            LOGGER.warn("[Portable Crafting Compat] PreciseEnchanting: EnchanterBlock.open() non trovato. Saltato.", e);
            return;
        }

        disenchanterItem = blockItem;

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (var player : server.getPlayerManager().getPlayerList()) {
                if (FakeDisenchanterRegistry.getEntry(player.getUuid()) != null) {
                    if (!player.currentScreenHandler.getClass().getName().equals("com.levls.enchanter.EnchanterMenu")) {
                        FakeDisenchanterRegistry.unregister(player.getUuid());
                    }
                }
            }
        });

        // Pulizia alla disconnessione del player.
        ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if (entity instanceof ServerPlayerEntity player) {
                FakeDisenchanterRegistry.unregister(player.getUuid());
            }
        });

        LOGGER.info("[Portable Crafting Compat] PreciseEnchanting: registrato con successo.");
    }

    public static boolean tryOpenDisenchanter(ServerPlayerEntity sp, ItemStack stack) {
        if (disenchanterItem == null || openMethod == null) return false;
        if (!stack.isOf(disenchanterItem)) return false;

        LOGGER.debug("[PCC] tryOpenDisenchanter: apertura per {}", sp.getName().getString());
        BlockPos pos = sp.getBlockPos().up(50);
        ServerWorld sw = sp.getServerWorld();
        Block block = Registries.BLOCK.get(Identifier.of("slider_enchanting", "arcane_disenchanter"));
        BlockState oldState = sw.getBlockState(pos);
        sw.setBlockState(pos, block.getDefaultState());
        FakeDisenchanterRegistry.register(sp.getUuid(), pos, oldState, sw);

        try {
            openMethod.invoke(null, sp, pos, true);
            LOGGER.debug("[PCC] dopo invoke: handler={}", sp.currentScreenHandler.getClass().getName());
        } catch (Exception e) {
            sw.setBlockState(pos, oldState);
            FakeDisenchanterRegistry.unregister(sp.getUuid());
            LOGGER.error("[PCC] Errore EnchanterBlock.open()", e);
            return false;
        }

        return true;
    }
}