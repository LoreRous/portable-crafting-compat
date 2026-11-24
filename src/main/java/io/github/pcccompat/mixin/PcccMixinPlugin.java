package io.github.pcccompat.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Applica i mixin di una compat solo se la mod corrispondente è installata,
 * così la mod funziona anche con solo Uncraft Everything o solo Precise Enchanting.
 */
public class PcccMixinPlugin implements IMixinConfigPlugin {

    private static final Map<String, String> REQUIRED_MOD = Map.of(
            "MixinUncraftingPacketHandler", "uncrafteverything",
            "MixinUncraftingTableMenu", "uncrafteverything",
            "MixinUncraftingTableMenuServer", "uncrafteverything",
            "MixinClientWorldUpdateListeners", "uncrafteverything",
            "MixinClientWorldBlockEntity", "uncrafteverything",
            "MixinEnchanterMenu", "slider_enchanting",
            "MixinItemStack", "slider_enchanting"
    );

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String simpleName = mixinClassName.substring(mixinClassName.lastIndexOf('.') + 1);
        String modId = REQUIRED_MOD.get(simpleName);
        return modId == null || FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
