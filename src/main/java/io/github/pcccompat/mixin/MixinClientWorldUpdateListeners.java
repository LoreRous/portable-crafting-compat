package io.github.pcccompat.mixin;

import io.github.pcccompat.FakeBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class MixinClientWorldUpdateListeners {
    @Inject(method = "updateListeners", at = @At("HEAD"), cancellable = true)
    private void pcccompat$suppressFakePosUpdate(
            BlockPos pos, BlockState oldState, BlockState newState, int flags, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null && FakeBlockEntityRegistry.get(mc.player.getUuid(), pos) != null) {
            ci.cancel();
        }
    }
}
