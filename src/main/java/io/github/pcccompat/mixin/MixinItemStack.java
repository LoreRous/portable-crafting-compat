package io.github.pcccompat.mixin;

import io.github.pcccompat.compat.PreciseEnchantingCompat;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class, priority = 500)
public class MixinItemStack {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void pcccompat$interceptDisenchanter(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!(player instanceof ServerPlayerEntity sp)) return;
        ItemStack stack = player.getStackInHand(hand);
        if (PreciseEnchantingCompat.tryOpenDisenchanter(sp, stack)) {
            cir.setReturnValue(TypedActionResult.success(stack));
        }
    }
}