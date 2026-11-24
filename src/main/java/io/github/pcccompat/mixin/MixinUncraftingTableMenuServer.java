package io.github.pcccompat.mixin;

import com.coolerpromc.uncrafteverything.screen.custom.UncraftingTableMenu;
import io.github.pcccompat.FakeBlockEntityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(UncraftingTableMenu.class)
public class MixinUncraftingTableMenuServer {
    @Inject(method = "method_7595", at = @At("HEAD"))
    private void pcccompat$serverUnregisterOnClose(PlayerEntity player, CallbackInfo ci) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            FakeBlockEntityRegistry.unregister(serverPlayer.getUuid());
        }
    }
}