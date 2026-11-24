package io.github.pcccompat.wrapper;

import com.coolerpromc.uncrafteverything.blockentity.custom.UncraftingTableBlockEntity;
import io.github.pcccompat.FakeBlockEntityRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class UncraftingTableScreenFactory implements ExtendedScreenHandlerFactory<BlockPos> {

    public static final Text TITLE =
            Text.translatable("block.uncrafteverything.uncrafting_table");

    @Override
    public Text getDisplayName() {
        return TITLE;
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return player.getBlockPos().up(50);
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return null;

        BlockPos pos = serverPlayer.getBlockPos().up(50);
        ServerWorld world = serverPlayer.getServerWorld();
        Block uncraftingBlock = Registries.BLOCK.get(Identifier.of("uncrafteverything", "uncrafting_table"));

        BlockState oldState = world.getBlockState(pos);
        world.setBlockState(pos, uncraftingBlock.getDefaultState());

        UncraftingTableBlockEntity realBE = (UncraftingTableBlockEntity) world.getBlockEntity(pos);
        if (realBE == null) {
            world.setBlockState(pos, oldState);
            return null;
        }

        FakeBlockEntityRegistry.register(serverPlayer.getUuid(), pos, realBE, oldState, world);
        return realBE.createMenu(syncId, playerInventory, player);
    }
}
