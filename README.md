<p align="center"><img src="publishing/icon-512.png" width="128" alt="Portable Crafting Compat icon"></p>

# Portable Crafting Compat

A general compatibility add-on for [Portable Crafting](https://modrinth.com/mod/portable-crafting) (Fabric, Minecraft 1.21.1): it lets you use **modded workstations** straight from your inventory, the same way Portable Crafting does for vanilla ones. Each compatibility turns on only when its mod is installed.

Currently supported:

| Mod | Workstation |
|---|---|
| Uncraft Everything | Uncrafting Table |
| Precise Enchanting | Arcane Disenchanter |

Hold the workstation item in your hand and right-click: its screen opens without placing the block. New compatibilities are welcome as pull requests.

> **A note from the author:** I'm not an experienced developer. This mod was built entirely with **Claude Code**, an AI coding assistant. That means **I can't implement new compatibilities on request**. If you want support for another mod or block, please open a pull request (see below). Bug reports are welcome, but fixes may take a while.

## Download

- Modrinth: *coming soon*
- CurseForge: *coming soon*
- GitHub: [Releases](https://github.com/LoreRous/portable-crafting-compat/releases)

## Requirements

| | |
|---|---|
| Minecraft | 1.21.1 |
| Loader | Fabric Loader ≥ 0.16.9 + Fabric API |
| Required | Portable Crafting |
| Optional | Any of the supported mods (each compat turns on only if its mod is installed) |
| Side | Client **and** server |

## How it works

Most workstation menus check that a real block exists at a position and that the player is close to it. The mod satisfies both checks:

1. **Temporary real block.** When you right-click the item, the real block is placed 50 blocks above the player, and the previous block state is saved in a per-player registry (`FakeBlockEntityRegistry` for Uncraft Everything, `FakeDisenchanterRegistry` for Precise Enchanting).
2. **Open the original menu.**
   - *Uncraft Everything*: `UncraftingTableScreenFactory` is registered in `PortableCrafting.ITEM_FACTORIES`, so Portable Crafting opens it like any vanilla workstation.
   - *Precise Enchanting*: `MixinItemStack` intercepts `ItemStack.use` and calls `EnchanterBlock.open(...)` through reflection.
3. **Skip the distance check.** Mixins make `canUse` return `true` for the registered position (`MixinUncraftingTableMenu`, `MixinEnchanterMenu`). `MixinUncraftingPacketHandler` makes `World.getBlockEntity` return the registered block entity so Uncraft Everything's packets work.
4. **Clean up.** When the menu closes or the player disconnects, the original block state is restored.

Some mods also rely on client-side setup that normally happens when the block is clicked. For example, Uncraft Everything requests its config from the server. `PortableCraftingCompatClient` replicates that setup when the portable screen opens.

`PcccMixinPlugin` only applies the mixins of a compat when its mod is installed. The Precise Enchanting mixin targets intermediary names (`method_7597`) with `remap = false`, because that mod is only reached through reflection and string targets.

## Building

Requires JDK 21.

```sh
./gradlew build        # jar in build/libs/
./gradlew runClient    # dev client
```

Portable Crafting and Uncraft Everything are downloaded at build time from the [Modrinth Maven](https://support.modrinth.com/en/articles/8801191-modrinth-maven) as compile-only dependencies, and their versions are set in `gradle.properties`. To test in the dev client, put the jars of the target mods in `run/mods/`.

## Contributing

Everyone is welcome to read the code, fork it and send pull requests: fixes, cleanups, and especially **compatibility with more mods and blocks**. See [CONTRIBUTING.md](CONTRIBUTING.md) for how a new compat fits in.

## License

[MIT](LICENSE). Portable Crafting, Uncraft Everything and Precise Enchanting belong to their respective authors and are **not** included in this repository or in the mod jar.
