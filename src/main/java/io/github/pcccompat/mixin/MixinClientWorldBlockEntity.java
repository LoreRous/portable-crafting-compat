package io.github.pcccompat.mixin;

import com.coolerpromc.uncrafteverything.blockentity.custom.UncraftingTableBlockEntity;
import io.github.pcccompat.FakeBlockEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Lato client: se il blocco dell'Uncrafting Table portatile non è ancora sincronizzato,
 * restituisce una BE temporanea così la schermata si apre comunque.
 */
@Mixin(World.class)
public class MixinClientWorldBlockEntity {

    private static final ThreadLocal<Boolean> IN_METHOD_8321 = ThreadLocal.withInitial(() -> false);

    @Inject(method = "method_8321", at = @At("HEAD"), cancellable = true)
    private void pcccompat$interceptClientBlockEntity(BlockPos pos, CallbackInfoReturnable<BlockEntity> cir) {
        if (IN_METHOD_8321.get()) return;
        Object self = this;
        if (!(self instanceof ClientWorld clientWorld)) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        FakeBlockEntityRegistry.FakeEntry entry = FakeBlockEntityRegistry.getEntry(mc.player.getUuid());
        if (entry == null || !entry.fakePos().equals(pos)) return;
        IN_METHOD_8321.set(true);
        try {
            BlockEntity be = ((World) self).getBlockEntity(pos);
            if (be instanceof UncraftingTableBlockEntity) {
                cir.setReturnValue(be);
                return;
            }
            Block uncraftingBlock = Registries.BLOCK.get(Identifier.of("uncrafteverything", "uncrafting_table"));
            UncraftingTableBlockEntity fakeBe = new UncraftingTableBlockEntity(pos, uncraftingBlock.getDefaultState());
            fakeBe.setWorld(clientWorld);
            cir.setReturnValue(fakeBe);
        } finally {
            IN_METHOD_8321.set(false);
        }
    }
}
