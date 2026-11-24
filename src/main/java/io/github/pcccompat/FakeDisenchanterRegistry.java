package io.github.pcccompat;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FakeDisenchanterRegistry {
    private static final Map<UUID, Entry> REGISTRY = new ConcurrentHashMap<>();
    public record Entry(BlockPos pos, BlockState oldState, ServerWorld world) {}

    public static void register(UUID uuid, BlockPos pos, BlockState oldState, ServerWorld world) {
        REGISTRY.put(uuid, new Entry(pos, oldState, world));
    }

    public static Entry getEntry(UUID uuid) {
        return REGISTRY.get(uuid);
    }

    public static void unregister(UUID uuid) {
        Entry entry = REGISTRY.remove(uuid);
        if (entry != null) entry.world().setBlockState(entry.pos(), entry.oldState());
    }
}