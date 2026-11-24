package io.github.pcccompat;

import com.coolerpromc.uncrafteverything.blockentity.custom.UncraftingTableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FakeBlockEntityRegistry {

    private static final Map<UUID, FakeEntry> REGISTRY = new ConcurrentHashMap<>();

    public record FakeEntry(BlockPos fakePos, UncraftingTableBlockEntity blockEntity, BlockState oldState, ServerWorld world) {}

    public static void register(UUID playerUuid, BlockPos pos, UncraftingTableBlockEntity be, BlockState oldState, ServerWorld world) {
        REGISTRY.put(playerUuid, new FakeEntry(pos, be, oldState, world));
    }

    public static UncraftingTableBlockEntity get(UUID playerUuid, BlockPos pos) {
        FakeEntry entry = REGISTRY.get(playerUuid);
        if (entry != null && entry.fakePos().equals(pos)) {
            return entry.blockEntity();
        }
        return null;
    }

    /** Usato su ogni World.getBlockEntity(): uscita immediata se nessun menu portatile è aperto. */
    public static UncraftingTableBlockEntity findByPos(ServerWorld world, BlockPos pos) {
        if (REGISTRY.isEmpty()) return null;
        for (FakeEntry entry : REGISTRY.values()) {
            if (entry.world() == world && entry.fakePos().equals(pos)) {
                return entry.blockEntity();
            }
        }
        return null;
    }

    public static void unregister(UUID playerUuid) {
        FakeEntry entry = REGISTRY.remove(playerUuid);
        if (entry != null && entry.world() != null && entry.oldState() != null) {
            entry.world().setBlockState(entry.fakePos(), entry.oldState());
        }
    }

    public static FakeEntry getEntry(UUID playerUuid) {
        return REGISTRY.get(playerUuid);
    }
}