# Contributing

Pull requests are welcome! The maintainer is not an experienced developer (the mod was written with Claude Code), so please:

- keep each PR focused on one thing (one fix or one new compat);
- explain in the PR description what you changed and how you tested it in game;
- be patient with reviews.

## Adding compatibility for another mod

This mod aims to be a general compatibility layer between Portable Crafting and any mod that adds a workstation, so new compats are the most welcome kind of contribution. Every compat follows the same pattern. Look at `compat/UncraftEverythingCompat.java` or `compat/PreciseEnchantingCompat.java` as a template.

1. **Register only if the mod is installed.** Add an `isModLoaded("<modid>")` check in `PortableCraftingCompat.onInitialize` that calls your `YourModCompat.register()`.
2. **Open the menu.** Pick one of these:
   - if the block's menu can be built from a `NamedScreenHandlerFactory`, put a factory in `PortableCrafting.ITEM_FACTORIES` (see `UncraftingTableScreenFactory`);
   - otherwise, intercept the item use (see `MixinItemStack`) and call the block's own "open" method.
3. **Give the menu a real block.** Most menus check the block in the world. Place it temporarily above the player and remember the previous state in a per-player registry, so it can be restored.
4. **Skip the distance check.** Add a mixin on the menu's `canUse` (`method_7597`) that returns `true` for the registered position (see `MixinEnchanterMenu`).
5. **Clean up** on menu close and on `ServerEntityEvents.ENTITY_UNLOAD`.
   Also check whether the block's `onUse` does client-side setup (for example sending a packet). If it does, replicate it when the screen opens: see `compat/client/UncraftEverythingClientCompat`, registered from `PortableCraftingCompatClient`.
6. **Gate your mixins.** Add each new mixin to `PcccMixinPlugin.REQUIRED_MOD`, so the game doesn't crash when that mod is missing. Put client-only mixins in the `client` list of `pcccompat.mixins.json`.
7. Add the mod as `suggests` in `fabric.mod.json`, and add it to the table in `README.md` and `publishing/DESCRIPTION.md`.

If the target mod can be downloaded from Modrinth, add it as `modCompileOnly "maven.modrinth:<slug>:<version>"` in `build.gradle`. Never commit third-party jars.
