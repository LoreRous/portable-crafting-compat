package io.github.pcccompat.mixin;

import io.github.pcccompat.FakeDisenchanterRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * EnchanterMenu.canUse (method_7597) chiama ScreenHandler.canUse(access, player, DIS_BLOCK),
 * che richiede il player entro 8 blocchi dal blocco. Il disenchanter portatile è piazzato
 * 50 blocchi sopra il player, quindi il server chiudeva la schermata al tick successivo.
 * Qui consideriamo il menu sempre valido se la pos è quella registrata per il player.
 *
 * Il jar di Precise Enchanting non è rimappato (compileOnly), quindi usiamo nomi
 * intermediary e remap = false. @Pseudo perché la mod è opzionale.
 */
@Pseudo
@Mixin(targets = "com.levls.enchanter.EnchanterMenu", remap = false)
public class MixinEnchanterMenu {

    @Shadow(remap = false) @Final public BlockPos pos;

    @Inject(method = "method_7597", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private void pcccompat$portableCanUse(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        FakeDisenchanterRegistry.Entry entry = FakeDisenchanterRegistry.getEntry(player.getUuid());
        if (entry != null && entry.pos().equals(pos)) {
            cir.setReturnValue(true);
        }
    }
}
