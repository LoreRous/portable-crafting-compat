package io.github.pcccompat.mixin;

import com.coolerpromc.uncrafteverything.screen.custom.UncraftingTableMenu;
import io.github.pcccompat.FakeBlockEntityRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(UncraftingTableMenu.class)
public class MixinUncraftingTableMenu {
    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void pcccompat$alwaysCanUse(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null && FakeBlockEntityRegistry.get(mc.player.getUuid(),
                ((UncraftingTableMenu)(Object)this).blockEntity.getPos()) != null) {
            cir.setReturnValue(true);
        }
    }
    @Inject(method = "method_7595", at = @At("HEAD"), cancellable = true)
    private void pcccompat$cancelClientClose(PlayerEntity player, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null && FakeBlockEntityRegistry.get(mc.player.getUuid(),
                ((UncraftingTableMenu)(Object)this).blockEntity.getPos()) != null) {
            ci.cancel();
        }
    }
}