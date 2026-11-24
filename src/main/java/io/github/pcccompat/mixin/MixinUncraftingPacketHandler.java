package io.github.pcccompat.mixin;

import com.coolerpromc.uncrafteverything.blockentity.custom.UncraftingTableBlockEntity;
import io.github.pcccompat.FakeBlockEntityRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Lato server: World.getBlockEntity() restituisce la BE dell'Uncrafting Table portatile
 * quando la pos è quella registrata, così i pacchetti di UncraftEverything funzionano.
 */
@Mixin(World.class)
public class MixinUncraftingPacketHandler {

    @Inject(method = "getBlockEntity", at = @At("HEAD"), cancellable = true)
    private void pcccompat$interceptFakeBlockEntity(
            BlockPos pos, CallbackInfoReturnable<BlockEntity> cir) {
        Object self = this;
        if (!(self instanceof ServerWorld world)) return;

        UncraftingTableBlockEntity fakeEntity = FakeBlockEntityRegistry.findByPos(world, pos);
        if (fakeEntity != null) {
            cir.setReturnValue(fakeEntity);
        }
    }
}
