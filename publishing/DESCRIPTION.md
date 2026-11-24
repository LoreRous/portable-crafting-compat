# Portable Crafting Compat

**Use modded workstations straight from your inventory, just like Portable Crafting does for vanilla ones.**

[Portable Crafting](https://modrinth.com/mod/portable-crafting) lets you right-click a crafting table, smithing table, stonecutter and other vanilla workstations in your inventory and use them without placing them. Workstations added by other mods usually don't work this way, because their screens expect a real block in the world.

**Portable Crafting Compat** is a general compatibility add-on that fills that gap, one mod at a time. Each compatibility turns on automatically when its mod is installed, and you can use the mod with any combination of the supported mods.

## Supported mods

| Mod | Workstation | Status |
|---|---|---|
| [Uncraft Everything](https://modrinth.com/mod/uncraft-everything) | Uncrafting Table | ✅ Working |
| [Precise Enchanting](https://modrinth.com/mod/precise-enchanting) | Arcane Disenchanter | ✅ Working |

More mods can be added over time, mostly through community contributions (see below).

## How to use it

1. Install the mod together with Portable Crafting and any of the supported mods.
2. Hold a supported workstation **in your hand** and right-click, as you would use any item.
3. The usual screen of that workstation opens. Use it exactly as if the block were placed in front of you.

Everything the original workstation does still works: recipes, experience costs and the config options of the original mods all apply.

## How it works

Most workstation screens check that the block really exists in the world and that you are standing close to it. So when you open one from your inventory:

1. The mod briefly places the real block **50 blocks above your head**, where you won't see it or bump into it.
2. It opens that block's own screen, and tells the game that you are allowed to use it even though it's far away.
3. When you close the screen (or log out), whatever was at that position before (usually air) is put back.

This way the original mods run their own code, and the result is the same as using the placed block.

## Requirements

- Minecraft **1.21.1**, **Fabric** Loader 0.16.9+, Fabric API
- [Portable Crafting](https://modrinth.com/mod/portable-crafting) (required)
- At least one of the supported mods above (optional individually; each compatibility only turns on if its mod is installed)

The mod must be installed on **both client and server**.

## Known limitations

- Tested in **singleplayer**. Multiplayer on a dedicated server has not been tested yet: reports are welcome.
- The temporary block needs a free spot 50 blocks above you, inside the world height limit. Near the build limit it may not work.
- This mod only makes workstations portable. It doesn't change what they do. For example, getting enchanted books back from uncrafted items depends on Uncraft Everything's config, not on this mod.

## Open source: contributions welcome!

The source code is on [GitHub](https://github.com/LoreRous/portable-crafting-compat) under the MIT license. Feel free to look around, fork it, and open pull requests: bug fixes, improvements, and above all **compatibility with more mods and workstations** are very welcome. The repository includes a short guide on how to add a new one.

## A note from the author

I'm not an experienced developer: this mod was built entirely with the help of **Claude Code** (an AI coding assistant). Because of that, **I'm not able to take requests to add new compatibilities myself**. If you'd like support for another mod, the best way is to contribute it (or ask someone who can) through a pull request on GitHub. Bug reports are still appreciated, but I can't promise quick fixes.
